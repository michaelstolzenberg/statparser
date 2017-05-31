package michael.network.parser.system.stackprojective;


import java.util.Stack;
import michael.network.parser.Configuration;
import michael.network.parser.Dependency;
import michael.network.parser.system.Transition;

/**
 * Right-arc transition.
 */
public class RightArc implements Transition {
    private final String relation;

    public RightArc(String relation) {
        this.relation = relation;
    }

    @Override
    public boolean isPossible(Configuration configuration) {
        return configuration.stack().size() > 1;
    }

    @Override
    public void apply(Configuration configuration) {
        
        Stack<Integer> stack = configuration.stack();

        int dependant = stack.pop();
        int head = stack.peek();
        configuration.addDependency(new Dependency(head, relation, dependant));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RightArc rightArc = (RightArc) o;

        if (relation != null ? !relation.equals(rightArc.relation) : rightArc.relation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return relation != null ? relation.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RightArc{" +
                "relation='" + relation + '\'' +
                '}';
    }
}
