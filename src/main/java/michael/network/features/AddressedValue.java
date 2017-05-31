package michael.network.features;

/**
 * This class represents an addressing of the configuration and the value of one
 * of the available layers for that address. For instance:
 *
 * <ul>
 * <li>Source: stack.</li>
 * <li>Depth: 0 (stack tip).</li>
 * <li>Layer: part-of speech tag.</li>
 * <li>Value: VVFIN</li>
 * </ul>
 */
public class AddressedValue {
    public enum Source {
        STACK,
        BUFFER,
        RDEPSTACK,
        LDEPSTACK
    }

    public enum Layer {
        TOKEN,
        TAG,
        DEPENDENCY
    }

    private final Source source;
    private final Layer layer;
    private final int depth;
    private final String value;

    /**
     * Construct an addressed value.
     * @param source The source (stack/buffer).
     * @param depth The depth on the stack/buffer (0 is the tip/head).
     * @param layer The layer.
     * @param value The value.
     */
    public AddressedValue(Source source, int depth, Layer layer, String value) {
        this.source = source;
        this.layer = layer;
        this.depth = depth;
        this.value = value;
    }

    public Source source() {
        return source;
    }

    public Layer layer() {
        return layer;
    }

    public int depth() {
        return depth;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressedValue that = (AddressedValue) o;

        if (depth != that.depth) return false;
        if (source != that.source) return false;
        if (layer != that.layer) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (layer != null ? layer.hashCode() : 0);
        result = 31 * result + depth;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AddressedValue{" +
                "address=" + source +
                ", layer=" + layer +
                ", depth=" + depth +
                ", value='" + value + '\'' +
                '}';
    }
}
