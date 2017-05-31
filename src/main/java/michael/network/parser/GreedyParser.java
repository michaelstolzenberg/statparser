package michael.network.parser;

import java.util.List;
import java.util.Set;
import michael.network.guide.Guide;
import michael.network.parser.system.Transition;
import michael.network.parser.system.TransitionSystem;

public class GreedyParser implements Parser {
    private final TransitionSystem transitionSystem;

    private final Guide guide;

    public GreedyParser(TransitionSystem transitionSystem, Guide guide) {
        this.transitionSystem = transitionSystem;
        this.guide = guide;
    }

    @Override
    public Set<Dependency> parse(List<String> tokens, List<String> tags) {
        Configuration configuration = new Configuration(tokens, tags);
        parse(configuration);

        // Note: it can be the case that some tokens are not attached. In a robust parser,
        // we want to check this and attach them before returning the dependency relations
        // from the final configuration.
        return configuration.dependencies();
    }

    /**
     * Parse the given (normally initial) configuration.
     *
     * @param configuration The configuration to parse.
     */
    private void parse(Configuration configuration) {
        while (!transitionSystem.isTerminal(configuration)) {
            Transition nextTransition = guide.nextTransition(transitionSystem.possibleOperations(configuration), configuration);
            nextTransition.apply(configuration);
        }
    }
}
