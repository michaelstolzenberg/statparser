package michael.network.parser.system.stackprojective;

import michael.network.parser.Configuration;
import michael.network.parser.system.Transition;


/**
 * Shift transition.
 */
public class Shift implements Transition {
    @Override
    public boolean isPossible(Configuration configuration) {
        return !configuration.buffer().isEmpty() || !configuration.stack().isEmpty();
    }

    @Override
    public void apply(Configuration configuration) {
        
        // Note: maybe a separate reduce transition?
        if (configuration.buffer().isEmpty())
            configuration.stack().pop();
        else
            configuration.stack().push(configuration.buffer().remove(0));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Shift;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Shift";
    }
}
