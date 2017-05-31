package michael.network.features;

import java.util.List;

public class Example {
    public final double label;

    public final List featureVector;

    public Example(double label, List featureVector) {
        this.label = label;
        this.featureVector = featureVector;
    }
}