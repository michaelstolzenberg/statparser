package michael.network.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jblas.DoubleMatrix;

public class GloVeReader {
    public Map embeddings;
    private final BufferedReader reader;

    public GloVeReader(String file) throws IOException{
        this.embeddings = new HashMap<String,DoubleMatrix>();
        FileReader fr = new FileReader(file);
        this.reader = new BufferedReader(fr);   
    }

    public void close() throws IOException {
        reader.close();
    }

    public void read() throws IOException {
        String line;
        String parts[];
        DoubleMatrix v;
        String k;
        while ((line = reader.readLine()) != null) {
            parts = line.split(" ");
            k = parts[0];
            v = new DoubleMatrix(Arrays.stream(Arrays.copyOfRange(parts,1,parts.length)).mapToDouble(Double::parseDouble).toArray());
            embeddings.put(k, v);
        }
    }
}