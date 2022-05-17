import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String line;
        while (true) {
            System.out.print("=> ");
            line = in.nextLine();
            if (line.equals("")) break;

            Builder b = new Builder(line);
            State s = b.build();
            System.out.print(State.toString(s, 0));

            String line2;
            Engine engine = new Engine(s);
            while(true) {
                System.out.print("Test a string against your regex => ");
                line2 = in.nextLine();
                if (line2.equals("")) break;
                System.out.println(engine.accepts(line2));
            }
        }
    }

}
