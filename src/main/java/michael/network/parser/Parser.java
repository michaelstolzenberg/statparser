package michael.network.parser;


import java.util.List;
import java.util.Set;

public interface Parser {
    public Set<Dependency> parse(List<String> tokens, List<String> tags);
}
