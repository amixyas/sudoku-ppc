import org.apache.commons.cli.ParseException;

import java.util.Scanner;

public class Main {

    public static void main(String args[]) throws ParseException {
        Scanner sc= new Scanner(System.in);
        System.out.println("Menu :");
        System.out.println("1 : Make comparison for one n*n solution between the Sudoku PPC model and the Sudoku BackTrack, in term of time.");
        System.out.println("2 : Make comparison for all n*n solutions between the Sudoku PPC model and the Sudoku BackTrack, in term of time.");
        System.out.println("3 : Solving Sudoku(9*9) with a provided pre-incomplete-instance.");
        System.out.println("4 : Solving Sudoku(16*16) with a provided pre-incomplete-instance.");
        System.out.println("5 : Solving 'Greater Than' Sudoku(16*16) with a provided pre-incomplete-instance.");
        System.out.println("Notes for 3 and 4 : - The provided pre-incomplete-instance, it's must be written in txt file please.");
        System.out.println("                    - To get a hint , take a look at resources folder.");
        System.out.println("                    - If you get an unwanted results, know that's due to the content of your file, please review it.");


        System.out.print("\nSo, what do you choose : ");
        int input = sc.nextInt();
        if  (input==1 || input==2) {
            System.out.print("Please, provide to system the sudoku 'n' number, to get sudoku of n*n, for example 4 or 9 : ");
            int n = sc.nextInt();
            while (n!=4 && n!=9){
                System.out.println("\nPlease, choose 4 or 9 for option 1 and just 4 for option 2");
                System.out.println("We don't want you to burn down your computer processor :)");
                System.out.print("ReInput : ");
                n = sc.nextInt();
            }
            if(input==1){
                SudokuBT sudokuBT = new SudokuBT(n);
                long start = System.nanoTime();
                sudokuBT.findSolution(0,0);
                long end = System.nanoTime();
                long duration = (end - start)/1000000;
                System.out.println("Start : " + start + " -- End : "+ end + " -- So, Duration is: " + duration + " milliseconds");
                SudokuPPC sudokuPPC = new SudokuPPC(1, n, null);
                start = System.nanoTime();
                sudokuPPC.solve();
                end = System.nanoTime();
                duration = (end - start)/1000000;
                System.out.println("Start : " + start + " -- End : "+ end + " -- So, Duration is: " + duration + " milliseconds");

                System.out.println("\n >>> As we see PPC is mush more faster then BackTrack");

            }
            if  (input==2) {
                SudokuBT sudokuBT = new SudokuBT(n);
                long start = System.nanoTime();
                sudokuBT.findSolutionAll(0,0);
                long end = System.nanoTime();
                long duration = (end - start)/1000000;
                System.out.println("Start : " + start + " -- End : "+ end + " -- So, Duration is: " + duration + " milliseconds");
                SudokuPPC sudokuPPC = new SudokuPPC(1, n, null);
                start = System.nanoTime();
                sudokuPPC.findAllSolutions();
                end = System.nanoTime();
                duration = (end - start)/1000000;
                System.out.println("Start : " + start + " -- End : "+ end + " -- So, Duration is: " + duration + " milliseconds");
            }
        }
        else if  (input==3 || input==4) {
            System.out.println("Please, provide to system the pre-incomplete-instance file path : ");
            String path = sc.next();
            if(input==3) {
                SudokuPPC sudokuPPC = new SudokuPPC(2,9, path);
                sudokuPPC.solve();
            }
            if  (input==4) {
                SudokuPPC sudokuPPC = new SudokuPPC(3, 16, path);
                sudokuPPC.solve();
            }
        }
        else if  (input==5) {
            System.out.print("Please, provide to system the pre-incomplete-instance file that contain row constraints : ");
            String rowPath = sc.next();
            System.out.print("Please, provide to system the pre-incomplete-instance file that contain col constraints : ");
            String colPath = sc.next();
            SudokuPPCLevel4 sudokuPPCLevel4 = new SudokuPPCLevel4(rowPath, colPath);
            sudokuPPCLevel4.solve();
        } else System.out.println("Wrong input");
    }
}
