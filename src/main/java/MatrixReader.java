import java.io.File;
import java.util.Scanner;


public class MatrixReader {

    /*
    public static void main(String[] args) {
        String[][] matrix = new String[n][n];
        reader(matrix, "/home/ilyas/Documents/#Others/Univ-montpellier/Tps-um/IAGL/tp1-sudoku/src/main/resources/SudokuPPCLevel4Row.txt", 6);
    }
    */

    public static String[][] reader(String[][] matrix, String path, int n) {

        try {
            Scanner input = new Scanner(new File(path));

            while (input.hasNextLine()) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        try {
                            String temp = input.next();
                            if (temp!=null) matrix[i][j] = temp;
                        } catch (java.util.NoSuchElementException e) {
                            //System.out.println("error reading document v1");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error reading document v2");
        }
        //print the input matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //System.out.print(matrix[i][j]);
            }
            //System.out.println("");
        }
        return matrix;
    }

}
