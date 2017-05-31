package michael.network.features;

import java.util.HashSet;
import java.util.Set;
import michael.network.parser.Configuration;

/**
 * A meta-generator that calls its embedded feature generators, combining
 * their results.
 */
public class AggregateGenerator implements FeatureGenerator {
    private final Set<FeatureGenerator> generators;

    /**
     * Construct an aggregate generator from a set of feature generators.
     * @param generators The generators.
     */
    public AggregateGenerator(Set<FeatureGenerator> generators) {
        this.generators = generators;
    }

    @Override
    public Set<FeatureValue> generate(Configuration configuration) {
        Set<FeatureValue> features = new HashSet<FeatureValue>();

        for (FeatureGenerator generator : generators) {
            features.addAll(generator.generate(configuration));
        }

        return features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggregateGenerator that = (AggregateGenerator) o;

        if (!generators.equals(that.generators)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return generators.hashCode();
    }
}
