package michael.network.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CONLLUReader {
    private final BufferedReader reader;
    public CharacterMap characterMap;

    public CONLLUReader(String file) throws IOException{
        FileReader fr = new FileReader(file);
        this.reader = new BufferedReader(fr);
        this.characterMap = new CharacterMap();
    }

    public void close() throws IOException {
        reader.close();
    }

    public Sentence readSentence() throws IOException {
        List<Token> tokens = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            String parts[] = line.split("\t");

            // We are done with these tokens.
            if (line.isEmpty()) {
                if (tokens.isEmpty()){
                    continue;
                }
                return constructSentence(tokens);
            }

            if (parts.length < 2)
                continue;
            
            if (parts[0].contains("-"))
                continue;
            
            int id = intValueForColumn(parts, 0);
            String form = valueForColumn(parts, 1);
            String lemma = valueForColumn(parts, 2);
            String uPosTag = valueForColumn(parts, 3);
            String xPosTag = valueForColumn(parts, 4);
            List<String> feats = Arrays.asList(valueForColumn(parts, 5).split("\\|"));
            int head = intValueForColumn(parts, 6);
            String depRel = valueForColumn(parts, 7);
            String deps = valueForColumn(parts, 8);
            String misc = valueForColumn(parts, 9);
            
            characterMap.put(form);
            
            Token token = new Token(id, form, lemma, uPosTag, xPosTag, feats, head, depRel, deps, misc);
            tokens.add(token);
        }

        // If the the file does not end with a blank line, we have left-overs.
        if (!tokens.isEmpty()) {
            return constructSentence(tokens);
        }

        return null;
    }

    /**
     * Construct a sentence. If strictness is used and invariants do not hold, convert
     * the exception to an IOException.
     */
    
    private Sentence constructSentence(List<Token> tokens) throws IOException {
        Sentence sentence;
        try {
            sentence = new Sentence(tokens);
        } catch (IllegalArgumentException e) {
            throw new IOException(e.getMessage());
        }
        return sentence;
    }

    private String valueForColumn(String[] columns, int column) {
        if (column >= columns.length)
            return "";

        if (columns[column].equals("_"))
            return "";

        return columns[column];
    }

    private int intValueForColumn(String[] columns, int column) {
        if (column >= columns.length)
            return -1;

        if (columns[column].equals("_"))
            return -1;

        return Integer.parseInt(columns[column]);
    }
}