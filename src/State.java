import java.util.*;

public class State {

    private Hashtable<Character, List<State>> edges ;
    private boolean isAccepting;

    public State (boolean isAccepting) {
        this.isAccepting = isAccepting;
        edges = new Hashtable<Character, List<State>>();
    }

    public State (boolean isAccepting, Hashtable<Character, List<State>> edges) {
        this.isAccepting = isAccepting;
        this.edges = edges;
    }

    public State (boolean isAccepting, char c) {
        this(isAccepting);
        edges.put(c, Arrays.asList(new State(false)));
    }

    public void addEdges(Hashtable<Character, List<State>> edges) {
        for (Character c: edges.keySet()) {
            if (this.edges.containsKey(c)) {
                this.edges.get(c).addAll(edges.get(c));
            } else {
                this.edges.put(c, edges.get(c));
            }
        }
    }

    public void addEdge(char in, State next) {
        if (edges.containsKey(in)) edges.get(in).add(next);
        else edges.put(in, Arrays.asList(next));
    }

    public void appendEdge(char in, State next) {
        if (edges.size() != 0) {
            for (List<State> states: edges.values()) {
                for (State state: states) state.appendEdge(in, next);
            }
        } else {
            edges.put(in, Arrays.asList(next));
        }
    }

    public List<State> accept(char in) {
        List<State> cur = edges.get(in);
        List<State> result = new ArrayList<>();
        if (Objects.nonNull(cur)) result.addAll(cur);
        if (edges.containsKey('\0')) {
            for (State s: edges.get('\0')) {
                List<State> t = s.accept(in);
                if (Objects.nonNull(t)) result.addAll(t);
            }
        }
        return result;
    }

    public List<State> epsilonMove() {

        return null;
    }

    public boolean isAccepting() {
        return isAccepting;
    }

    public void setAccepting(boolean isAccepting) {
        this.isAccepting = isAccepting;
    }

    public static final String toString(State s, int dept) {
        StringBuilder tabs = new StringBuilder();
        for (int i = 0; i < dept; i++) tabs.append('\t');
        StringBuilder sb = new StringBuilder();
        if (Objects.isNull(s) || s.edges.size() == 0) return "";
        for (Character c: s.edges.keySet()) {
            sb.append(tabs).append(c + " -> ").append('\n');
            for (State st: s.edges.get(c))
                sb.append(State.toString(st, dept + 1));
        }
        return sb.toString();
    }

}
