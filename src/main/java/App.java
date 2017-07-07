
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import self.learning.isudoku.Sudoku;
import self.learning.isudoku.SudokuUtils;

/**
 *
 * @author hshrishrimal
 */
public class App {

    

    public static void main(String[] args) throws Exception {

        if(args[0] == null) {
            System.err.println("No filename specified.");
            System.exit(-1);
        } else {
            File file = new File(args[0]);

            //InputStream in = App.class.getResourceAsStream(args[0]);
            //int[][] intSudoku = SudokuUtils.readFile(in);

            int[][] intSudoku = SudokuUtils.readFile(new FileInputStream(file));
            Sudoku sudoku = new Sudoku(intSudoku);

            SudokuUtils.printResults(sudoku.getSudoku());
            System.out.println("--------------------------------------");
            SudokuUtils.solve(sudoku.getSudoku());

        }

    }
    
}
