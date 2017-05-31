package michael.network.guide;



import michael.network.parser.system.Transition;
import java.util.Set;
import michael.network.parser.Configuration;

/**
 * Interface for parser guides. A guide (an oracle or classifier) informs a parser which
 * transition is considered to be the best, given the set of possible transitions and the
 * current parser state.
 */
public interface Guide {
    public Transition nextTransition(Set<Transition> transitions, Configuration configuration);
}
