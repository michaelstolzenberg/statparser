package michael.network.features;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import michael.network.parser.Configuration;
import michael.network.parser.Dependency;

/**
 * Generate features based on addressed values.
 */
public class AddressedValueFeatureGenerator implements FeatureGenerator {
    private final AddressedValue value;
    public AddressedValueFeatureGenerator(AddressedValue value) {
        this.value = value;
    }

    @Override
    public Set<FeatureValue> generate(Configuration configuration) {
        Stack<Integer> stack = configuration.stack();
        List<Integer> buffer = configuration.buffer();

        ArrayList<AddressedValue> builder = new ArrayList<>();

            int idx=0;
            Boolean addressable=true;
            if (value.source() == AddressedValue.Source.STACK) {
                if (value.depth() >= stack.size()) {
                    addressable=false;
                }
                else{
                    idx = stack.get(stack.size() - value.depth() - 1);
                }
            } else if (value.source() == AddressedValue.Source.RDEPSTACK){
                if (value.depth() >= stack.size()) {
                    addressable=false;
                }
                else{
                    idx = findRightMostDependency(configuration, stack.get(stack.size() - value.depth() - 1));
                }
            }
            else if (value.source() == AddressedValue.Source.LDEPSTACK){
                if (value.depth() >= stack.size()) {
                    addressable=false;
                }
                else{
                    idx = findLeftMostDependency(configuration, stack.get(stack.size() - value.depth() - 1));
                }
            }
            else {
                if (value.depth() >= buffer.size()) {
                    addressable=false;
                }
                else{
                    idx = buffer.get(value.depth());
                }
            }

            // Obtain the value from the given layer.
            String thisValue="NULL";
            if(addressable){
                thisValue = getValue(configuration, value.layer(), idx);
                if (thisValue == null){
                    thisValue="NULL";
                }
            }
            
            //System.out.println(value.source()+"\t"+value.depth()+"\t"+value.layer()+"\t"+thisValue);
            builder.add(new AddressedValue(value.source(), value.depth(), value.layer(),
                    thisValue));
        
        Set<FeatureValue> set = new HashSet<>();
        set.add(new FeatureValue(new AddressedValueFeature(builder), 1d));
        
        return set;
    }

    /**
     * Obtain the value of the given layer/token index.
     *
     * @param configuration The configuration.
     * @param layer The layer to retrieve.
     * @param idx The token index.
     * @return
     */
    private String getValue(Configuration configuration, AddressedValue.Layer layer, int idx) {
        String value;
        loop:
        switch (layer) {
            case TOKEN:
                value = configuration.tokens().get(idx);
                break;
            case TAG:
                value = configuration.tags().get(idx);
                break;
            case DEPENDENCY:
                for (Dependency d : configuration.dependencies()) {
                    if (idx==d.dependentIndex()) {
                        value = d.relation();
                        break loop;
                    }                   
                }
                value = null;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown layer: %s", layer));
        }
        return value;
    }

    /**
     * Find the rightmost dependency (found so far).
     * @param configuration The configuration.
     * @param idx The token index.
     * @return The rightmost dependent token index.
     */
    private int findRightMostDependency(Configuration configuration, int idx) {
        assert configuration != null;
        assert idx >= 0;

        int rightMostIdx = Integer.MIN_VALUE;

        for (Dependency dependency : configuration.dependencies()) {
            if (dependency.headIndex() == idx && dependency.dependentIndex() > rightMostIdx) {
                rightMostIdx = dependency.dependentIndex();
            }
        }

        return rightMostIdx;
    }
    private int findLeftMostDependency(Configuration configuration, int idx) {
        assert configuration != null;
        assert idx >= 0;

        int leftMostIdx = Integer.MAX_VALUE;

        for (Dependency dependency : configuration.dependencies()) {
            if (dependency.headIndex() == idx && dependency.dependentIndex() < leftMostIdx) {
                leftMostIdx = dependency.dependentIndex();
            }
        }

        return leftMostIdx;
    }
}
