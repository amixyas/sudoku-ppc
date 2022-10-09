import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;

import static org.chocosolver.solver.search.strategy.Search.minDomLBSearch;
import static org.chocosolver.util.tools.ArrayUtils.append;

public class SudokuPPCLevel4 {

    static int n;
    static int s;
    private int instance;
    private long timeout = 3600000; // one hour

    IntVar[][] rows, cols, shapes;
    Model model;

    private String rowPath;
    private String colPath;

    private String[] args;

    public SudokuPPCLevel4 (String rowPath, String colPath) throws ParseException {

        final Options options = configParameters();
        final CommandLineParser parser = new DefaultParser();
        final CommandLine line = parser.parse(options, args);

        boolean helpMode = line.hasOption("help");
        if (helpMode) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("sudoku", options, true);
            System.exit(0);
        }

        // Check arguments and options
        for (Option opt : line.getOptions()) {
            checkOption(line, opt.getLongOpt());
        }

        this.rowPath = rowPath;
        this.colPath = colPath;
        instance = 9;
        n = 9;
        s = (int) Math.sqrt(n);
    }


    public void solve() {
        buildModel();
        model.getSolver().solve();
        printGrid();
        //model.getSolver().printStatistics();
    }

    public void printGrid() {

        String a;
        a = "┌───";
        String b;
        b = "├───";
        String c;
        c = "─┬────┐";
        String d;
        d = "─┼────┤";
        String e;
        e = "─┬───";
        String f;
        f = "─┼───";
        String g;
        g = "└────┴─";
        String h;
        h = "───┘";
        String k;
        k = "───┴─";


        for (int i = 0; i < n; i++) {

            for (int line = 0; line < n; line++) {
                if (line == 0) {
                    System.out.print(i == 0 ? a : b);
                } else if (line == n - 1) {
                    System.out.print(i == 0 ? c : d);
                } else {
                    System.out.print(i == 0 ? e : f);
                }
            }
            System.out.println("");
            System.out.print("│ ");
            for (int j = 0; j < n; j++) {
                if (rows[i][j].getValue() > 9) {
                    String Alpha = new String(Character.toChars(55 + rows[i][j].getValue()));
                    System.out.print(Alpha + "  │ ");
                }
                else
                    System.out.print(rows[i][j].getValue() + "  │ ");

            }

            if (i == n - 1) {
                System.out.println("");
                for (int line = 0; line < n; line++) {
                    System.out.print(line == 0 ? g : (line == n - 1 ? h : k));
                }
            }
            System.out.println("");

        }
    }

    public void buildModel() {
        model = new Model();

        rows = new IntVar[n][n];
        cols = new IntVar[n][n];
        shapes = new IntVar[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                rows[i][j] = model.intVar("[" + i + "]" + "[" + j + "]", 1, n, false);
                cols[j][i] = rows[i][j];
            }
        }

        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                for (int k = 0; k < s; k++) {
                    for (int l = 0; l < s; l++) {
                        shapes[j + k * s][i + (l * s)] = rows[l + k * s][i + j * s];
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            model.allDifferent(rows[i]).post();
            model.allDifferent(cols[i]).post();
            model.allDifferent(shapes[i]).post();
        }



        // Reading sudoku instance
        MatrixReader matrixReader = new MatrixReader();
        String[][] matrix = new String[n][n];


        matrix = matrixReader.reader(matrix, this.rowPath, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (matrix[i][j].equals("|")) continue;
                else if (matrix[i][j].equals("<")) rows[i][j].lt(rows[i][j + 1]).post();
                else rows[i][j].gt(rows[i][j + 1]).post();
            }
        }


        matrix = new String[n][n];
        matrix = matrixReader.reader(matrix, this.colPath, n);

        for (int i = 0; i < n-1; i++) {
            if (matrix[i][0].equals("_")) continue;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j].equals("A")) {
                    rows[i][j].gt(rows[i+1][j]).post();
                }
                else rows[i][j].lt(rows[i+1][j]).post();
            }
        }


    }

    // Check all parameters values
    public void checkOption(CommandLine line, String option) {

        switch (option) {
            case "inst":
                instance = Integer.parseInt(line.getOptionValue(option));
                break;
            case "timeout":
                timeout = Long.parseLong(line.getOptionValue(option));
                break;
            default: {
                System.err.println("Bad parameter: " + option);
                System.exit(2);
            }

        }

    }

    // Add options here
    private Options configParameters() {

        final Option helpFileOption = Option.builder("h").longOpt("help").desc("Display help message").build();

        final Option instOption = Option.builder("i").longOpt("instance").hasArg(true).argName("sudoku instance")
                .desc("sudoku instance size").required(false).build();

        final Option limitOption = Option.builder("t").longOpt("timeout").hasArg(true).argName("timeout in ms")
                .desc("Set the timeout limit to the specified time").required(false).build();

        // Create the options list
        final Options options = new Options();
        options.addOption(instOption);
        options.addOption(limitOption);
        options.addOption(helpFileOption);

        return options;
    }

    public void configureSearch() {
        model.getSolver().setSearch(minDomLBSearch(append(rows)));
    }

    public void findAllSolutions(){
        buildModel();
        model.getSolver().solve();
        List<Solution> Solutions = model.getSolver().findAllSolutions();
        System.out.println("Total Number of Solution:" + Solutions.stream().count());
        System.out.println("Solutions >>");
        Solutions.stream().forEach(e -> System.out.println(e.toString()));
    }

}
