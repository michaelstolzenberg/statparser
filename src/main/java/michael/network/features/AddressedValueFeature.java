package michael.network.features;

/**
 * Feature based consisting of one or more addressed values.
 */
public class AddressedValueFeature implements Feature {
    public final AddressedValue value;

    /**
     * The values in this feature.
     *
     * @param value The addressed value.
     */
    public AddressedValueFeature(AddressedValue value) {
        this.value = value;//copy?
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressedValueFeature that = (AddressedValueFeature) o;

        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
