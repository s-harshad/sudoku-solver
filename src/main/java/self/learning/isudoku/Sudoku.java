package self.learning.isudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author hshrishrimal
 */
public class Sudoku {
    
    private int NUM_OF_ROWS = 9;
    private int NUM_OF_COLUMNS = 9;
    private int SUB_MATRIX_SIZE = 3;

    private SudokuCell[][] sudoku;

    public SudokuCell[][] getSudoku() {
        return sudoku;
    }
    
    public Sudoku(SudokuCell[][] initialSudoko) {
        this.sudoku = initialSudoko;
        this.NUM_OF_ROWS = initialSudoko.length;
        this.NUM_OF_COLUMNS = this.NUM_OF_ROWS;
        this.SUB_MATRIX_SIZE = (int) Math.sqrt(NUM_OF_ROWS);   
    }
    
    public Sudoku(int[][] initialSudoko) {
        
        SudokuCell[][] sudoku = new SudokuCell[initialSudoko.length][initialSudoko[0].length];
        
        for (int row = 0; row < initialSudoko.length; row++) {
            for (int col = 0; col < initialSudoko[0].length; col++) {
                if(initialSudoko[row][col] != 0) {
                    sudoku[row][col] = new SudokuCell(SudokuCellColor.BLACK, new Pair<Integer,Integer>(row,col), initialSudoko[row][col]);
                } else {
                    sudoku[row][col] = new SudokuCell(SudokuCellColor.GREEN, new Pair<Integer,Integer>(row,col), initialSudoko[row][col]);
                }  
            }
        }

        this.NUM_OF_ROWS = initialSudoko.length;
        this.NUM_OF_COLUMNS = this.NUM_OF_ROWS;
        this.SUB_MATRIX_SIZE = (int) Math.sqrt(NUM_OF_ROWS);
        this.sudoku = sudoku;
        
    }

    
    
    public int getNUM_OF_ROWS() {
        return NUM_OF_ROWS;
    }

    public int getNUM_OF_COLUMNS() {
        return NUM_OF_COLUMNS;
    }

    public int getSUB_MATRIX_SIZE() {
        return SUB_MATRIX_SIZE;
    }

    
    
    
}
