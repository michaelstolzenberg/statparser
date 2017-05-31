package michael.network.features;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import michael.network.parser.system.Transition;

public class LabelNumberer {
    private final Map<Transition, Integer> transitionToLabels;

    private final List<Transition> transitions;

    public LabelNumberer() {
        transitionToLabels = new HashMap<Transition, Integer>();
        transitions = new ArrayList<Transition>();
    }

    public int number(Transition label) {
        
        Integer labelNumber = transitionToLabels.get(label);

        if (labelNumber == null) {
            labelNumber = transitions.size();
            transitions.add(label);
            transitionToLabels.put(label, labelNumber);

        }
        return labelNumber;
    }

    public Transition transition(int labelNumber) {
        return transitions.get(labelNumber);
    }
    public int nLabels() {
        return transitionToLabels.size();
    }
}
