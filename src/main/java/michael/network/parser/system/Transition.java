package michael.network.parser.system;
import michael.network.parser.Configuration;


public interface Transition {
    /**
     * Check whether the transition can be made in the from the current configuration.
     *
     * @param configuration The configuration.
     * @return {@code true} if the transition is possible in the current configuration.
     */
    public boolean isPossible(Configuration configuration);

    /**
     * 'Apply' the transition to the configuration.
     *
     * @param configuration The configuration.
     */
    public void apply(Configuration configuration);
}
