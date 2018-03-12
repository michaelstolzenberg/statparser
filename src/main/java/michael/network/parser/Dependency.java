package michael.network.parser;

public class Dependency {//implements Comparable<Dependency>{
    private final int headIndex;

    private final String relation;

    private final int dependentIndex;

    public Dependency(int headIndex, String relation, int dependentIndex) {
        this.headIndex = headIndex;
        this.relation = relation;
        this.dependentIndex = dependentIndex;
    }

    public int headIndex() {
        return headIndex;
    }

    public String relation() {
        return relation;
    }

    public int dependentIndex() {
        return dependentIndex;
    }

    public String toString() {
        return String.format("(head: %d, relation: %s, dependent: %d)", headIndex, relation, dependentIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dependency that = (Dependency) o;

        if (dependentIndex != that.dependentIndex) return false;
        if (headIndex != that.headIndex) return false;
        if (relation != null ? !relation.equals(that.relation) : that.relation != null) return false;
    
        return true;
    }

    @Override
    public int hashCode() {
        int result = headIndex;
        result = 31 * result + (relation != null ? relation.hashCode() : 0);
        result = 31 * result + dependentIndex;
        return result;
    }
    
    //test
    //public int compareTo(Dependency d) {
    //    return Integer.compare(dependentIndex(),d.dependentIndex());
    //}
}
