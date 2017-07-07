package self.learning.isudoku;

import javax.swing.*;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author hshrishrimal
 */
public class CombinationUtils {

    private CombinationUtils() {
    }

    private static void generateCombinationsUtil(List<SudokuCell> arr, int r, int n, SudokuCell[] data, int idx, int i,List<List<SudokuCell>> allCombinations ) {
        
        if(idx == r) {
            SudokuCell[] aCombination = new SudokuCell[r];
            for(int j=0;j<r;j++) {
                aCombination[j] = data[j];
            }
            allCombinations.add(Arrays.asList(aCombination));
            return;
        }
        
        if(i >= n) {
            return;
        }
        
        data[idx] = arr.get(i);
        generateCombinationsUtil(arr, r, n, data, idx+1, i+1,allCombinations);
        generateCombinationsUtil(arr, r, n, data, idx, i+1,allCombinations);
    }
    
    private static List<List<SudokuCell>> generateCombinations(List<SudokuCell> arr, int r, int n) {
        
        List<List<SudokuCell>> allCombinations = new ArrayList<>();
        SudokuCell[] data = new SudokuCell[r];
        generateCombinationsUtil(arr, r, n, data, 0, 0,allCombinations);
        return allCombinations;
    }
    
    private static List<SudokuCell> filterBy(List<SudokuCell> allZeroCells, int size) {
        List<SudokuCell> allFilteredCells = new ArrayList<>();
        for (SudokuCell cell : allZeroCells) {
            if(cell.getPossibleCellValues().size() <= size) {
                allFilteredCells.add(cell);
            }
        }
        return allFilteredCells;
    }

    private static boolean computeColPrimitiveCells(SudokuCell[][] sudoku,int col) {

        boolean anyPossibleValuesRemovedFromCell = false;

        //list contains all zero suduko cells dependening on row
        List<SudokuCell> allZeroCells = new ArrayList<>();
        for (int row = 0; row < sudoku.length; row++) {
            if(sudoku[row][col].getCellValue() == 0) {
                allZeroCells.add(sudoku[row][col]);
            }
        }

        //get all combinations of 2, 3, 4, .... 9
        for(int i = 2; i<9; i++) {
            List<SudokuCell> relevantCells = filterBy(allZeroCells, i);
            List<List<SudokuCell>> allSets = generateCombinations(relevantCells, i, relevantCells.size());

            //check for primitive set and print it for now.
            for (List<SudokuCell> aPossiblePrimitiveSet : allSets) {
                Set<Integer> allPossibleValues = new HashSet<>();
                for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                    allPossibleValues.addAll(sudokuCell.getPossibleCellValues());
                }
                if(allPossibleValues.size() == aPossiblePrimitiveSet.size()) {
                    //System.out.println("we have a column set " );
                    //remove the numbers present in this set from other cells in this column
                    for (int row = 0; row < sudoku.length; row++) {
                        SudokuCell cell = sudoku[row][col];
                        if(cell.getCellValue() == 0) {
                            Set<Integer> possibleValuesToRemove = new HashSet<>();
                            boolean thisCellPresentInSet = false;

                            for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                                Pair<Integer, Integer> cellLocation = sudokuCell.getLocation();
                                if(cellLocation.getRow() == row && cellLocation.getCol() == col) {
                                    thisCellPresentInSet = true;
                                    break;
                                }
                            }

                            if(!thisCellPresentInSet) {

                                for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                                    possibleValuesToRemove.addAll(sudokuCell.getPossibleCellValues());
                                }
                                Set<Integer> cellPossibleValues = cell.getPossibleCellValues();

                                if(Collections.disjoint(cellPossibleValues,possibleValuesToRemove)){
                                    //no elements in common do nothing.
                                } else {
                                    //System.out.println("Removing " + possibleValuesToRemove + " from " + cell.getLocation());
                                    cellPossibleValues.removeAll(possibleValuesToRemove);
                                    cell.setPossibleCellValues(cellPossibleValues);
                                    anyPossibleValuesRemovedFromCell = true;
                                }


                            }
                        }
                    }
                }
            }
        }

        return anyPossibleValuesRemovedFromCell;
    }

    private static boolean computeRowPrimitiveCells(SudokuCell[][] sudoku,int row) {
     
     boolean anyPossibleValuesRemovedFromCell = false;
        
     //list contains all zero suduko cells dependening on row
     List<SudokuCell> allZeroCells = new ArrayList<>();
     for (int col = 0; col < sudoku[0].length; col++) {
         if(sudoku[row][col].getCellValue() == 0) {
             allZeroCells.add(sudoku[row][col]);
         }
     }
     
     //get all combinations of 2, 3, 4, .... 9
     for(int i = 2; i<9; i++) {
         List<SudokuCell> relevantCells = filterBy(allZeroCells, i);
         List<List<SudokuCell>> allSets = generateCombinations(relevantCells, i, relevantCells.size());
         
         //check for primitive set and print it for now.
         for (List<SudokuCell> aPossiblePrimitiveSet : allSets) {
             Set<Integer> allPossibleValues = new HashSet<>();
             for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                 allPossibleValues.addAll(sudokuCell.getPossibleCellValues());
             }
             if(allPossibleValues.size() == aPossiblePrimitiveSet.size()) {
                 //System.out.println("we have a row set " );
                 
                 //remove the numbers present in this set from other cells in this row
                 for (int col = 0; col < sudoku[0].length; col++) {
                     SudokuCell cell = sudoku[row][col];
                    if(cell.getCellValue() == 0) {
                         
                         Set<Integer> possibleValuesToRemove = new HashSet<>();
                         boolean thisCellPresentInSet = false;
                         for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                             Pair<Integer, Integer> cellLocation = sudokuCell.getLocation();
                             if(cellLocation.getRow() == row && cellLocation.getCol() == col) {
                                 thisCellPresentInSet = true;
                                 break;
                             }
                         }
                         if(!thisCellPresentInSet) {
                            for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                                possibleValuesToRemove.addAll(sudokuCell.getPossibleCellValues());
                            }
                            Set<Integer> cellPossibleValues = cell.getPossibleCellValues();

                            if(Collections.disjoint(cellPossibleValues,possibleValuesToRemove)){
                                //no elements in common do nothing.
                            } else {
                                //System.out.println("Removing " + possibleValuesToRemove + " from " + cell.getLocation());
                                cellPossibleValues.removeAll(possibleValuesToRemove);
                                cell.setPossibleCellValues(cellPossibleValues);
                                anyPossibleValuesRemovedFromCell = true;
                            }


                         }
                    }
                }

//                 System.out.println("Set of Size : " + allSet.size());
//                 for (SudokuCell sudokuCell : allSet) {
//                    System.err.println(sudokuCell.getLocation() + "--" + sudokuCell.getPossibleCellValues() );
//                 }
             }
         }
         
     }
     return anyPossibleValuesRemovedFromCell;
    }

    private static boolean computeBoxPrimitiveCells(SudokuCell[][] sudoku,int boxNumber) {
        boolean anyPossibleValuesRemovedFromCell = false;

        //list contains all zero suduko cells dependening on the box
        List<SudokuCell> allZeroCells = new ArrayList<>();
        final int boxSize = (int) Math.sqrt(sudoku.length);
        for (int rowIdx = 0; rowIdx < boxSize; rowIdx++ ) {
            int rIdx = rowIdx+3*(boxNumber/3);
            for (int colIdx = 0; colIdx < boxSize; colIdx++ ) {
                int cIdx = colIdx+3*(boxNumber%3);
                if(sudoku[rIdx][cIdx].getCellValue() == 0) {
                    allZeroCells.add(sudoku[rIdx][cIdx]);
                }
            }
        }

        //get all combinations of 2,3,4,...9
        for(int i = 2; i<9; i++) {
            List<SudokuCell> relevantCells = filterBy(allZeroCells, i);
            List<List<SudokuCell>> allSets = generateCombinations(relevantCells, i, relevantCells.size());
            //check for primitive set and print it for now.
            for (List<SudokuCell> aPossiblePrimitiveSet : allSets) {
                Set<Integer> allPossibleValues = new HashSet<>();
                for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                    allPossibleValues.addAll(sudokuCell.getPossibleCellValues());
                }

                if(allPossibleValues.size() == aPossiblePrimitiveSet.size()) {
                    //System.out.println("we have a box set" );

                    //remove the numbers present in this set from other cells in this box
                    for (int rowIdx = 0; rowIdx < boxSize; rowIdx++ ) {
                        int rIdx = rowIdx+3*(boxNumber/3);
                        for (int colIdx = 0; colIdx < boxSize; colIdx++ ) {
                            int cIdx = colIdx+3*(boxNumber%3);
                            SudokuCell cell = sudoku[rIdx][cIdx];

                            if(cell.getCellValue() == 0) {

                                Set<Integer> possibleValuesToRemove = new HashSet<>();
                                boolean thisCellPresentInSet = false;
                                for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                                    Pair<Integer, Integer> cellLocation = sudokuCell.getLocation();
                                    if(cellLocation.getRow() == rIdx && cellLocation.getCol() == cIdx) {
                                        thisCellPresentInSet = true;
                                        break;
                                    }
                                }
                                if(!thisCellPresentInSet) {
                                    for (SudokuCell sudokuCell : aPossiblePrimitiveSet) {
                                        possibleValuesToRemove.addAll(sudokuCell.getPossibleCellValues());
                                    }
                                    Set<Integer> cellPossibleValues = cell.getPossibleCellValues();

                                    if(Collections.disjoint(cellPossibleValues,possibleValuesToRemove)){
                                        //no elements in common do nothing.
                                    } else {
                                        //System.out.println("Removing " + possibleValuesToRemove + " from " + cell.getLocation());
                                        cellPossibleValues.removeAll(possibleValuesToRemove);
                                        cell.setPossibleCellValues(cellPossibleValues);
                                        anyPossibleValuesRemovedFromCell = true;
                                    }


                                }
                            }

                        }
                    }
                }


                
            }
        }

        return anyPossibleValuesRemovedFromCell;
    }

    public static void removePossibleCellValuesByPreemptiveSetAlgo(SudokuCell[][] sudoku) {

        for (int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {
                //System.out.printf("sudoku[%d][%d]=%d, (%s)",row,col,sudoku[row][col].getCellValue(),sudoku[row][col].getPossibleCellValues());
                //System.out.println();
            }
            //System.out.println();
        }

        //compute primitive cells for each row
        for (int row = 0; row < sudoku.length; row++) {
            boolean anyPossibleValRemoved = computeRowPrimitiveCells(sudoku, row);
            while(anyPossibleValRemoved) {
                anyPossibleValRemoved = computeRowPrimitiveCells(sudoku, row);
            }
        }

        //compute primitive cells for each col
        for (int col = 0; col < sudoku[0].length; col++) {
            boolean anyPossibleValRemoved = computeColPrimitiveCells(sudoku, col);
            while(anyPossibleValRemoved) {
                anyPossibleValRemoved = computeColPrimitiveCells(sudoku, col);
            }
        }

        //compute primitive cells for each box
        for (int boxNumber = 0; boxNumber < sudoku[0].length; boxNumber++) {
            boolean anyPossibleValRemoved = computeBoxPrimitiveCells(sudoku, boxNumber);
            while(anyPossibleValRemoved) {
                anyPossibleValRemoved = computeBoxPrimitiveCells(sudoku, boxNumber);
            }
        }

        //System.out.println("After my pSet algo impl");

        for (int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {
                //System.out.printf("sudoku[%d][%d]=%d, (%s)",row,col,sudoku[row][col].getCellValue(),sudoku[row][col].getPossibleCellValues());
                //System.out.println();
            }
            //System.out.println();
        }

    }

    
}
