package michael.network.features;

import java.util.List;

public class Example {
    public final double label;
    public final List embeddings;
    public final List featureVector;

    public Example(double label, List featureVector) {
        this.label = label;
        this.featureVector = featureVector;
        this.embeddings = null;
    }
    
    public Example(double label, List featureVector,List embeddings) {
        this.label = label;
        this.featureVector = featureVector;
        this.embeddings = embeddings;
    }
}