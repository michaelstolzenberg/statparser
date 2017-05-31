package michael.network.reader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import michael.network.parser.Dependency;

/**
 *
 * @author michael
 */
public class Util {

    public static Set<Dependency> sentenceToDependencies(Sentence sentence) {
        Set<Dependency> goldDependencies = new HashSet<>();
        for (int i = 0; i < sentence.tokens.size(); ++i) {
            Token token = sentence.tokens.get(i);
            goldDependencies.add(new Dependency(token.head, token.headRel, token.tokenId));
        }
        return goldDependencies;
    }

    public static List<String> sentenceToTokens(Sentence sentence) {
        List<String> rawTokens = new ArrayList<>();
        for (Token token : sentence.tokens){
            rawTokens.add(token.form);
        }
        return rawTokens;
    }

    public static List<String> sentenceToTags(Sentence sentence) {
        List<String> rawTokens = new ArrayList<>();
        for (Token token : sentence.tokens){
            rawTokens.add(token.uTag); 
        }
        return rawTokens;
    }

}


