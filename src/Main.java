import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter("stats.txt");
        for (double p1 = 0.2; p1 < 0.8; p1 += 0.1) {
            for (double p2 = 0.1; p2 < 0.9; p2 += 0.1) {
                Vector<Problem> problems = generateProblems(p1, p2);
                String bmcbjStr = solveBMCBJ(problems);
                printWriter.append(bmcbjStr);

                for (Problem problem : problems) {
                    problem.initialize();
                }

                String macStr = solveMAC(problems);
                printWriter.append(macStr);
            }
        }
        printWriter.close();
    }

    public static Vector<Problem> generateProblems(double p1, double p2) {
        Vector<Problem> problems = new Vector<Problem>(50);
        for (int i = 0; i < 50; i++) {
            problems.add(new Problem(15, 10, p1, p2));
        }
        return problems;
    }

    // TODO: need to add String building along the function to return the report
    public static String solveBMCBJ(Vector<Problem> problems) {
        BMCBJ bmcbj = new BMCBJ();
        for (Problem problem : problems) {
            bmcbj.solve(problem);
        }
        return "";
    }

    // TODO: implement MAC algorithm
    public static String solveMAC(Vector<Problem> problems) {
        return "";
    }
}
