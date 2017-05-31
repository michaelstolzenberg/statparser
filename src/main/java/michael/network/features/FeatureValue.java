package michael.network.features;

public class FeatureValue {
    public final AddressedValueFeature feature;
    public final double value;

    public FeatureValue(AddressedValueFeature feature, double value) {
        this.feature = feature;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeatureValue that = (FeatureValue) o;

        if (Double.compare(that.value, value) != 0) return false;
        if (!feature.equals(that.feature)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = feature.hashCode();
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
