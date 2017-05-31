package michael.network.guide;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import michael.network.parser.Configuration;
import michael.network.parser.Dependency;
import michael.network.parser.system.Transition;
import michael.network.parser.system.stackprojective.LeftArc;
import michael.network.parser.system.stackprojective.RightArc;
import michael.network.parser.system.stackprojective.Shift;
import michael.network.parser.system.stackprojective.StackProjectiveTransitionSystem;

public class NoLabelOracle implements Guide {
    private final Map<Integer, Dependency> dependentHeadMapping;

    public NoLabelOracle(Set<Dependency> goldStandardDependencies) {
        dependentHeadMapping = createDependentHeadMapping(goldStandardDependencies);
    }

    @Override
    public Transition nextTransition(Set<Transition> transitions, Configuration configuration) {
        
        Stack<Integer> stack = configuration.stack();
        
        if (stack.size() > 1) {
            Integer stack0 = stack.peek();
            Integer stack1 = stack.get(stack.size() - 2);

            // LEFT_ARC or RIGHT_ARC is the next transition if:
            //
            // - The operation is possible operation according to the parser.
            // - Applying the operation would introduce a dependency that corresponds to the gold standard.
            // - The dependent is not needed as the head of another token (if so, we postpone the attachment).
            //!neededForAttachment(configuration, stack1))

            if (transitions.contains(StackProjectiveTransitionSystem.ARCHETYPE_LEFT_ARC) &&
                    stack0.equals(dependentHeadMapping.get(stack1).headIndex()))
                return new LeftArc(null);

            if (transitions.contains(StackProjectiveTransitionSystem.ARCHETYPE_RIGHT_ARC) &&
                    stack1 == dependentHeadMapping.get(stack0).headIndex() &&
                    !neededForAttachment(configuration, stack0))
                return new RightArc(null);
        }

        // If none of the reductions should be carried out, shift!
        return new Shift();
    }

    /**
     * Check whether a token is still needed for a future attachment. This is the case
     * if the token is a head of another token that is not yet processed.
     *
     * @param configuration The parser state.
     * @param token         The token index.
     * @return {@code true} if the token is still needed, {@code false} otherwise.
     */
    private boolean neededForAttachment(Configuration configuration, int token) {
        // Note: potential candidates for attachment to 'token' can only reside in the buffer. If there
        // is a dependent on the stack for 'token' it is also 'waiting' for a dependent, to which it could
        // never attach because 'token' is blocking it. In other words, the tree would be non-projective.

        for (Integer bufferToken : configuration.buffer()) {
            if (dependentHeadMapping.get(bufferToken).headIndex() == token) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a mapping from tokens to the dependency relation that attaches that token to
     * its head.
     *
     * @param dependencies The set of gold-standard dependencies.
     * @return The mapping.
     */
    private Map<Integer, Dependency> createDependentHeadMapping(Set<Dependency> dependencies) {
        Map<Integer,Dependency> map = new HashMap<>();
        for (Dependency dependency : dependencies) {
            map.put(dependency.dependentIndex(), dependency);
        }
        return map;
    }

}
