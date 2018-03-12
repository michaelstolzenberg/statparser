package michael.network.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import michael.network.features.Example;
import michael.network.features.FeatureGenerator;
import michael.network.features.FeatureVectorizer;
import michael.network.features.LabelNumberer;
import michael.network.guide.Guide;
import michael.network.parser.system.Transition;
import michael.network.parser.system.TransitionSystem;
import michael.network.reader.GloVeReader;
import org.jblas.DoubleMatrix;


public class GreedyTrainer {
    private final TransitionSystem transitionSystem;

    public final FeatureVectorizer featureVectorizer;

    public final LabelNumberer labelNumberer;

    public final ArrayList<Example> examples;
    public final GloVeReader gr;
    private final FeatureGenerator featureGenerator;

    public GreedyTrainer(TransitionSystem transitionSystem, FeatureGenerator featureGenerator,GloVeReader gr) {
        this.transitionSystem = transitionSystem;
        this.featureGenerator = featureGenerator;
        this.gr = gr;
        labelNumberer = new LabelNumberer();
        examples = new ArrayList<>();
        featureVectorizer = new FeatureVectorizer();
    }

    public Set<Dependency> parse(List<String> tokens, List<String> tags, Guide guide) {
        Configuration configuration = new Configuration(tokens, tags);
        parse(configuration, guide);
        return configuration.dependencies();
    }

    private void parse(Configuration configuration, Guide guide) {
        while (!transitionSystem.isTerminal(configuration)) {
// test print
            //System.out.println(configuration.toString());
            Transition nextTransition = guide.nextTransition(transitionSystem.possibleOperations(configuration), configuration);
            int x = labelNumberer.number(nextTransition);
            List y = featureVectorizer.vectorize(featureGenerator.generate(configuration), true);
            List z = featureVectorizer.embeddings;
   //test         
            //if(configuration.stack().size()+configuration.buffer().size()<10){
            //    y.add(1);
            //}
            //else{
            //    y.add(0);
            //}
     
            examples.add(new Example(x, y, z));
// test print
            //System.out.println(nextTransition.toString());
            //System.out.println(x);
            //System.out.println(y);
            
            nextTransition.apply(configuration);
        }
    }
    public DoubleMatrix sparseToDense(){
        int n = featureVectorizer.nFeatures();
        int m = examples.get(0).embeddings.size();
        DoubleMatrix ret = DoubleMatrix.zeros(examples.size(),n+(m*50));
        for(int x=0;x<ret.rows;x++){
            List<Integer> featureVector=examples.get(x).featureVector;
            List<String> embeddings = examples.get(x).embeddings;
            for(int i=0;i<featureVector.size();i++){
                ret.put(x,featureVector.get(i)-1,1d);
            }

            for(int i=0;i<embeddings.size();i++){
                DoubleMatrix d = DoubleMatrix.zeros(1,50);
                if(gr.embeddings.get(embeddings.get(i).toLowerCase())!=null){
                    d = gr.embeddings.get(embeddings.get(i).toLowerCase()).transpose();
                }
                ret.put(x, makeRange(n+(i*50),50) ,d);
            }
        }
        return ret;
    }
    public DoubleMatrix sparseToDenseLabel(){
        int n = labelNumberer.nLabels();
        DoubleMatrix ret = DoubleMatrix.zeros(examples.size(),n);
        for(int x=0;x<ret.rows;x++){
            ret.put(x,(int)examples.get(x).label,1d);
        }
        return ret;
    }
    private int[] makeRange(int start,int length){
        int[] ret = new int[length];
        for(int i = 0;i<length;i++){
            ret[i] = start +i;
        }
        return ret;
    }
}
