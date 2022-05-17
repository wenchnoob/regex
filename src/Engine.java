import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine {

    State startState;
    List<State> currentStates;

    public Engine(State startState) {
        this.startState = startState;
        reset();
    }

    public void reset() {
        this.currentStates = Arrays.asList(startState);
    }

    public boolean accepts(String s) {
        reset();

        while(true) {
            List<State> nextStates = new ArrayList<>();
            for (State s: currentStates) {

            }
        }
    }
}
