package michael.network.parser.system;

import java.util.Set;
import michael.network.parser.Configuration;

public interface TransitionSystem {
    public Set<Transition> possibleOperations(Configuration configuration);

    public boolean isTerminal(Configuration configuration);
}
