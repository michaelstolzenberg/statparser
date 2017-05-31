package michael.network.parser.system.stackprojective;

import java.util.Stack;
import michael.network.parser.Configuration;
import michael.network.parser.Dependency;
import michael.network.parser.system.Transition;

/**
 * Left-arc transition.
 */
public class LeftArc implements Transition {
    private final String relation;

    public LeftArc(String relation) {
        this.relation = relation;
    }

    @Override
    public boolean isPossible(Configuration configuration) {
        Stack<Integer> stack = configuration.stack();
        return stack.size() > 1 && stack.get(stack.size() - 2) != 0;
    }

    @Override
    public void apply(Configuration configuration) {
       
        Stack<Integer> stack = configuration.stack();

        Integer head = stack.pop();
        Integer dependent = stack.pop();
        configuration.addDependency(new Dependency(head, relation, dependent));
        stack.push(head);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeftArc leftArc = (LeftArc) o;

        if (relation != null ? !relation.equals(leftArc.relation) : leftArc.relation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return relation != null ? relation.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LeftArc{" +
                "relation='" + relation + '\'' +
                '}';
    }
}
