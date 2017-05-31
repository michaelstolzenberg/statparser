package michael.network.features;

import java.util.Set;
import michael.network.parser.Configuration;

public interface FeatureGenerator {
    public Set<FeatureValue> generate(Configuration configuration);
}
