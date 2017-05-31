package michael.network.features;

import java.util.List;

/**
 * Feature based consisting of one or more addressed values.
 */
public class AddressedValueFeature implements Feature {
    public final List<AddressedValue> values;

    /**
     * The values in this feature.
     *
     * @param values The addressed values.
     */
    public AddressedValueFeature(List<AddressedValue> values) {
        this.values = values;//copy?
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressedValueFeature that = (AddressedValueFeature) o;

        if (values != null ? !values.equals(that.values) : that.values != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return values != null ? values.hashCode() : 0;
    }
}
