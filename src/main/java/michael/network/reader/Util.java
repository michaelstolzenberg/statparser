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
            goldDependencies.add(new Dependency(token.head, token.depRel, token.id));
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
            rawTokens.add(token.uPosTag); 
        }
        return rawTokens;
    }
    
    public static int UAS(Set<Dependency> s1, Set<Dependency> s2) {
        int c = 0;
        for(Dependency d1:s1){
            for(Dependency d2:s2){
                if(d1.dependentIndex()==d2.dependentIndex()&&
                        d1.headIndex()==d2.headIndex()){
                    c++;
                }
            }
        }
        return c;
    }
    public static int LAS(Set<Dependency> s1, Set<Dependency> s2) {
        int c = 0;
        for(Dependency d1:s1){
            for(Dependency d2:s2){
                if(d1.dependentIndex()==d2.dependentIndex()&&
                        d1.headIndex()==d2.headIndex()&&
                        d1.relation().equals(d2.relation())){
                    c++;
                }
            }
        }
        return c;
    }

}


