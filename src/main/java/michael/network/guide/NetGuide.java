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
import michael.network.network.Net;
import michael.network.parser.Configuration;
import michael.network.parser.system.Transition;
import org.jblas.DoubleMatrix;

public class NetGuide implements Guide{
    private final Net net;
    private final FeatureGenerator featureGenerator;
    private final FeatureVectorizer featureVectorizer;
    private final LabelNumberer labelNumberer;

    public NetGuide(Net net, FeatureGenerator featureGenerator, FeatureVectorizer featureVectorizer, LabelNumberer labelNumberer) {
        this.net = net;
        this.featureGenerator = featureGenerator;
        this.featureVectorizer = featureVectorizer;
        this.labelNumberer = labelNumberer;
    }

    public Transition nextTransition(Set<Transition> transitions, Configuration configuration) {
        
        Set<Class> allowedTransitions = new HashSet<Class>();
        for (Transition transition : transitions) {
            allowedTransitions.add(transition.getClass());
        }

        // Generate feature vector.
        List<Integer> v = featureVectorizer.vectorize(featureGenerator.generate(configuration), false);
        int n = featureVectorizer.nFeatures();
        double[][] temp = new double[1][n];
        for(int i=0;i<v.size();i++){
            temp[0][v.get(i)-1]=1d;
        }
        
        // Find the transition with the highest activation.
        double bestActivation = Double.NEGATIVE_INFINITY;
        Transition bestTransition = null;
        DoubleMatrix result = net.predict(new DoubleMatrix(temp));
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
}