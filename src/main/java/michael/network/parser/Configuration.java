package michael.network.parser;


import java.util.*;

public class Configuration {
    private final List<String> tokens;

    private final List<String> tags;

    private final Stack<Integer> stack;

    private final List<Integer> buffer;

    private final Set<Dependency> dependencies;

    public Configuration(List<String> tokens, List<String> tags) {
        this.tokens = tokens;
        tokens.add(0, "ROOT");

        this.tags = tags;
        tags.add(0, "ROOT");

        // Initialize the stack with the ROOT.
        this.stack = new Stack<Integer>();
        stack.add(0);

        // Initialize the buffer with indices of the remaining tokens
        buffer = new LinkedList<Integer>();
        for (int i = 1; i < tokens.size(); ++i) {
            buffer.add(i);
        }

        dependencies = new HashSet<Dependency>();
    }

    public List<Integer> buffer() {
        return buffer;
    }

    public Set<Dependency> dependencies() {
        return dependencies;
    }

    public Stack<Integer> stack() {
        return stack;
    }

    public List<String> tags() {
        return tags;
    }

    public List<String> tokens() {
        return tokens;
    }


    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

    @Override
    public String toString() {
        return "ParserState{" +
                "tokens=" + tokens +
                ", stack=" + stack +
                ", buffer=" + buffer +
                ", dependencies=" + dependencies +
                '}';
    }
}
