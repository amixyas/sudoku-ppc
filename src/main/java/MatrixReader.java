import java.io.File;
import java.util.Scanner;

public class MatrixReader {

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
                        }
                    }
                }
            }
        } catch (Exception e) {
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
