package michael.network.parser.system.stackprojective;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import michael.network.parser.Configuration;
import michael.network.parser.system.Transition;
import michael.network.parser.system.TransitionSystem;

/**
 * A simple transition system.
 */
public class StackProjectiveTransitionSystem implements TransitionSystem {
    /**
     * Instance of the Shift transition.
     */
    public static final Transition ARCHETYPE_SHIFT = new Shift();

    /**
     * Instance of the Left-Arc transition, with a null relation.
     */
    public static final Transition ARCHETYPE_LEFT_ARC = new LeftArc(null);

    /**
     * Instance of the Right-Arc transition, with a null relation.
     */
    public static final Transition ARCHETYPE_RIGHT_ARC = new RightArc(null);

    private final Set<Transition> TRANSITION_ARCHETYPES = new HashSet<>(Arrays.asList(
            ARCHETYPE_SHIFT,
            ARCHETYPE_LEFT_ARC,
            ARCHETYPE_RIGHT_ARC
    ));

    @Override
    public boolean isTerminal(Configuration configuration) {
        return configuration.buffer().isEmpty() && configuration.stack().isEmpty();
    }

    public Set<Transition> possibleOperations(Configuration configuration) {
        Set<Transition> possibleTransitions = new HashSet<Transition>();

        for (Transition transition : TRANSITION_ARCHETYPES) {
            if (transition.isPossible(configuration)) {
                possibleTransitions.add(transition);
            }
        }

        return possibleTransitions;
    }
}
