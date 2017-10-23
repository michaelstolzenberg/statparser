package michael.network.guide;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import michael.network.features.FeatureGenerator;
import michael.network.features.FeatureVectorizer;
import michael.network.features.LabelNumberer;
import michael.network.network.Net;
import michael.network.parser.Configuration;
import michael.network.parser.system.Transition;
import michael.network.reader.GloVeReader;
import org.jblas.DoubleMatrix;

public class NetGuide implements Guide{
    private final Net net;
    private final FeatureGenerator featureGenerator;
    private final FeatureVectorizer featureVectorizer;
    private final LabelNumberer labelNumberer;
    private final GloVeReader gr;

    public NetGuide(Net net, FeatureGenerator featureGenerator, FeatureVectorizer featureVectorizer, LabelNumberer labelNumberer, GloVeReader gr) {
        this.net = net;
        this.featureGenerator = featureGenerator;
        this.featureVectorizer = featureVectorizer;
        this.labelNumberer = labelNumberer;
        this.gr = gr;
    }

    public Transition nextTransition(Set<Transition> transitions, Configuration configuration) {
        
        Set<Class> allowedTransitions = new HashSet<Class>();
        for (Transition transition : transitions) {
            allowedTransitions.add(transition.getClass());
        }

        // Generate feature vector.
        //List<Integer> v = featureVectorizer.vectorize(featureGenerator.generate(configuration), false);
        int n = featureVectorizer.nFeatures();
        //double[][] temp = new double[1][n];
        //for(int i=0;i<v.size();i++){
        //    temp[0][v.get(i)-1]=1d;
        //}
        int m = featureVectorizer.embeddings.size();
        DoubleMatrix ret = DoubleMatrix.zeros(1,n+(m*50));
        
        List<Integer> featureVector = featureVectorizer.vectorize(featureGenerator.generate(configuration), false);
        List<String> embeddings = featureVectorizer.embeddings;
            for(int i=0;i<featureVector.size();i++){
                ret.put(0,featureVector.get(i)-1,1d);
            }
            for(int i=0;i<embeddings.size();i++){
                DoubleMatrix d = DoubleMatrix.zeros(1,50);
                if(gr.embeddings.get(embeddings.get(i).toLowerCase())!=null){
                    d = gr.embeddings.get(embeddings.get(i).toLowerCase()).transpose();
                }
                ret.put(0, makeRange(n+(i*50),50) ,d);
            }
        
        
        // Find the transition with the highest activation.
        double bestActivation = Double.NEGATIVE_INFINITY;
        Transition bestTransition = null;
        DoubleMatrix result = net.predict(ret);
        //System.out.println(Arrays.toString(result));
        for (int i=0;i<result.length;i++) {
            Transition transition = labelNumberer.transition(i);

            // Is the transition permitted?
            if (!allowedTransitions.contains(transition.getClass())) {
                continue;
            }

            if (result.get(i,0) > bestActivation) {
                bestActivation = result.get(i,0);
                bestTransition = transition;
            }
        }
        //System.out.println(bestTransition.getClass().toString());
        return bestTransition;
    }
    private int[] makeRange(int start,int length){
        int[] ret = new int[length];
        for(int i = 0;i<length;i++){
            ret[i] = start +i;
        }
        return ret;
    }
}