package michael.network.features;

import java.util.HashSet;
import java.util.Set;
import michael.network.parser.Configuration;

/**
 * This class should contain your set of feature generators.
 */
public class DefaultGenerator implements FeatureGenerator {
    private final AddressedValue STACK0_TAG = new AddressedValue(AddressedValue.Source.STACK, 0, AddressedValue.Layer.TAG, null);
    private final AddressedValue STACK1_TAG = new AddressedValue(AddressedValue.Source.STACK, 1, AddressedValue.Layer.TAG, null);
    private final AddressedValue STACK2_TAG = new AddressedValue(AddressedValue.Source.STACK, 2, AddressedValue.Layer.TAG, null);
    private final AddressedValue STACK3_TAG = new AddressedValue(AddressedValue.Source.STACK, 3, AddressedValue.Layer.TAG, null);

    private final AddressedValue BUFFER0_TAG = new AddressedValue(AddressedValue.Source.BUFFER, 0, AddressedValue.Layer.TAG, null);
    private final AddressedValue BUFFER1_TAG = new AddressedValue(AddressedValue.Source.BUFFER, 1, AddressedValue.Layer.TAG, null);
    private final AddressedValue BUFFER2_TAG = new AddressedValue(AddressedValue.Source.BUFFER, 2, AddressedValue.Layer.TAG, null);
    private final AddressedValue BUFFER3_TAG = new AddressedValue(AddressedValue.Source.BUFFER, 3, AddressedValue.Layer.TAG, null);
    
    //dep
    private final AddressedValue RDEPSTACK0_DEP = new AddressedValue(AddressedValue.Source.RDEPSTACK, 0, AddressedValue.Layer.DEPENDENCY, null);
    private final AddressedValue RDEPSTACK1_DEP = new AddressedValue(AddressedValue.Source.RDEPSTACK, 1, AddressedValue.Layer.DEPENDENCY, null);
    private final AddressedValue RDEPSTACK2_DEP = new AddressedValue(AddressedValue.Source.RDEPSTACK, 2, AddressedValue.Layer.DEPENDENCY, null);
    private final AddressedValue LDEPSTACK0_DEP = new AddressedValue(AddressedValue.Source.LDEPSTACK, 0, AddressedValue.Layer.DEPENDENCY, null);
    private final AddressedValue LDEPSTACK1_DEP = new AddressedValue(AddressedValue.Source.LDEPSTACK, 1, AddressedValue.Layer.DEPENDENCY, null);
    private final AddressedValue LDEPSTACK2_DEP = new AddressedValue(AddressedValue.Source.LDEPSTACK, 2, AddressedValue.Layer.DEPENDENCY, null);
    
    private final AddressedValue BUFFER0_TOKEN = new AddressedValue(AddressedValue.Source.BUFFER, 0, AddressedValue.Layer.TOKEN, null);
    private final AddressedValue BUFFER1_TOKEN = new AddressedValue(AddressedValue.Source.BUFFER, 1, AddressedValue.Layer.TOKEN, null);
    private final AddressedValue BUFFER2_TOKEN = new AddressedValue(AddressedValue.Source.BUFFER, 2, AddressedValue.Layer.TOKEN, null);

    private final AddressedValue STACK0_TOKEN = new AddressedValue(AddressedValue.Source.STACK, 0, AddressedValue.Layer.TOKEN, null);
    private final AddressedValue STACK1_TOKEN = new AddressedValue(AddressedValue.Source.STACK, 1, AddressedValue.Layer.TOKEN, null);
    private final AddressedValue STACK2_TOKEN = new AddressedValue(AddressedValue.Source.STACK, 2, AddressedValue.Layer.TOKEN, null);

    private final FeatureGenerator wrappedGenerators;

    public DefaultGenerator() {
        Set<FeatureGenerator> set = new HashSet<>();
        set.add(new AddressedValueFeatureGenerator(STACK1_TAG));
        set.add(new AddressedValueFeatureGenerator(STACK2_TAG));
        
        this.wrappedGenerators = new AggregateGenerator(set);
    }

    @Override
    public Set<FeatureValue> generate(Configuration configuration) {
        return wrappedGenerators.generate(configuration);
    }
}
