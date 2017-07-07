package michael.network.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import michael.network.features.Example;
import michael.network.features.FeatureGenerator;
import michael.network.features.FeatureValue;
import michael.network.features.FeatureVectorizer;
import michael.network.features.LabelNumberer;
import michael.network.guide.Guide;
import michael.network.parser.system.Transition;
import michael.network.parser.system.TransitionSystem;

public class GreedyTrainer {
    private final TransitionSystem transitionSystem;

    public final FeatureVectorizer featureVectorizer;

    public final LabelNumberer labelNumberer;

    public final ArrayList<Example> examples;

    private final FeatureGenerator featureGenerator;

    public GreedyTrainer(TransitionSystem transitionSystem, FeatureGenerator featureGenerator) {
        this.transitionSystem = transitionSystem;
        this.featureGenerator = featureGenerator;
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
            Transition nextTransition = guide.nextTransition(transitionSystem.possibleOperations(configuration), configuration);
            double y = labelNumberer.number(nextTransition);
            List x = featureVectorizer.vectorize(featureGenerator.generate(configuration), true);
            System.out.println(x.toString());
            examples.add(new Example(y, x));
            nextTransition.apply(configuration);
        }
    }
    public double[][] sparseToDense(){
        int n = featureVectorizer.nFeatures();
        double[][] ret = new double[examples.size()][n];
        for(int x=0;x<ret.length;x++){
            List<Integer> featureVector=examples.get(x).featureVector;
            for(int i=0;i<featureVector.size();i++){
                ret[x][featureVector.get(i)-1]=1d;
            }
        }
        return ret;
    }
    public double[][] sparseToDenseLabel(){
        int n = labelNumberer.nLabels();
        double[][] ret = new double[examples.size()][n];
        for(int x=0;x<ret.length;x++){
            ret[x][(int)examples.get(x).label]=1d;
            
        }
        return ret;
    }
}
