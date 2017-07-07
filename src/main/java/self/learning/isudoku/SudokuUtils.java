package self.learning.isudoku;


import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class SudokuUtils {

    private SudokuUtils() {
    }

    private static List<SudokuCell> randomlySelectedCellCopy = new ArrayList<>();
    
    public static int[][] readFile(InputStream in) throws Exception {
        int[][] intSudoku = new int[9][9];
        if(in!=null) {
            try {
                String str;
                int row = 0;
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while((str = reader.readLine()) != null) {
                    String[] rowElements = str.split(",");
                    for (int col = 0; col < rowElements.length; col++) {
                        intSudoku[row][col] = Integer.parseInt(rowElements[col]);
                    }
                    row++;
                }
            } finally {
                in.close();
            }
        }
        return intSudoku;
    }
 
    
    public static void fillAllNullCellsWithZero(SudokuCell[][] sudoku) {
        for (int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {
                if(sudoku[row][col] == null) {
                    sudoku[row][col] = new SudokuCell(SudokuCellColor.GREEN, new Pair<Integer,Integer>(row,col), 0);
                } 
            }
        }    
    }
    
    public static void fillAllZeroCellsWith1to9PossibleValues(SudokuCell[][] sudoku) {
        
        //For all cells which are 0, fill in the possibleCellValues that can go. 
        for (int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {
                if(sudoku[row][col].getCellValue() == 0) {
                    sudoku[row][col].setPossibleCellValues(generateListOfNumbers(1,sudoku.length));
                } 
            }
        }
        
    }
    
    public static void removeNonApplicableCellValuesFromPossibleCellValues(SudokuCell[][] sudoku) {
        //From the possible cell values, remove the ones which are not applicable 
        int boxSize = (int) Math.sqrt(sudoku.length);
        for (int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {    
                usedInRow(row, col, sudoku[row][col], sudoku);
                usedInColumn(row, col, sudoku[row][col], sudoku);
                usedInBox(row-row%boxSize,col-col%boxSize,sudoku[row][col],sudoku);
            }
        }
        
        
    }
    
    public static Set<SudokuCell> findCellWithOnePossibeValue(SudokuCell[][] sudoku) {
        Set<SudokuCell> results = new HashSet<>();
        for(int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {    
                if(sudoku[row][col].getCellValue()== 0) {
                    Set<Integer> possilbeCellValues = sudoku[row][col].getPossibleCellValues();
                    if(possilbeCellValues.size() == 1) {
                        results.add(sudoku[row][col]);
                    }
                }
            }        
        }
        return results;
    }
    
    public static void fillAllCellsWithOnePossibleValues(SudokuCell[][] sudoku) {
        
        //Initialize the cells with one possible cell values.        
        Set<SudokuCell> cellWithOnePossibleValues = findCellWithOnePossibeValue(sudoku);
        while(cellWithOnePossibleValues.size() > 0) {
        
            for (SudokuCell cellWithOnePossibleValue : cellWithOnePossibleValues) {
                cellWithOnePossibleValue.setCellColor(SudokuCellColor.GRAY);
                cellWithOnePossibleValue.setCellValue(cellWithOnePossibleValue.getPossibleCellValues().iterator().next());
                cellWithOnePossibleValue.clearPossibleCellvalues();
            }
            int boxSize = (int) Math.sqrt(sudoku.length);
            //From the possible cell values, remove the ones which are not applicable 
            for(int row = 0; row < sudoku.length; row++) {
                for (int col = 0; col < sudoku[0].length; col++) {    
                    usedInRow(row, col, sudoku[row][col], sudoku);
                    usedInColumn(row, col, sudoku[row][col], sudoku);
                    usedInBox(row-row%boxSize,col-col%boxSize,sudoku[row][col],sudoku);
                }
            }
            
            cellWithOnePossibleValues = findCellWithOnePossibeValue(sudoku);
        }
        
    }
    
    
    
    private static void usedInRow(int row, int column, SudokuCell sudokoCell, SudokuCell[][] suduko) {
        
        if(sudokoCell.getCellValue() == 0) {
            Set<Integer> possilbeCellValues = sudokoCell.getPossibleCellValues();
            
            for(int i=0; i<suduko[0].length; i++) {
                if(suduko[row][i].getCellValue()> 0 ) {
                    Iterator<Integer> iter = possilbeCellValues.iterator();
                    while (iter.hasNext()) {
                        if (iter.next() == suduko[row][i].getCellValue()) {
                            iter.remove();
                            break;
                        }
                    }
                }
            }
            
            sudokoCell.setPossibleCellValues(possilbeCellValues);

        }
        
    }
    
    private static void usedInColumn(int row, int column, SudokuCell sudokoCell, SudokuCell[][] suduko) {
        
        if(sudokoCell.getCellValue() == 0) {
            Set<Integer> possilbeCellValues = sudokoCell.getPossibleCellValues();
            
            for(int i=0; i<suduko.length; i++) {
                if(suduko[i][column].getCellValue() > 0 ) {
                    Iterator<Integer> iter = possilbeCellValues.iterator();
                    while (iter.hasNext()) {
                        if (iter.next() == suduko[i][column].getCellValue()) {
                            iter.remove();
                            break;
                        }
                    }
                }
            }
            
            sudokoCell.setPossibleCellValues(possilbeCellValues);
            
        }
        
    }

    private static void usedInBox(int boxStartRow, int boxStartColumn, SudokuCell sudokoCell, SudokuCell[][] suduko) {
        if(sudokoCell.getCellValue() == 0) {
            int boxSize = (int) Math.sqrt(suduko.length);
            Set<Integer> possilbeCellValues = sudokoCell.getPossibleCellValues();
            for(int row=0; row<boxSize; row++ ) {
                for(int col=0; col<boxSize; col++ ) {
                    if(suduko[row + boxStartRow][col+boxStartColumn].getCellValue() > 0) {
                        Iterator<Integer> iter = possilbeCellValues.iterator();
                        while (iter.hasNext()) {
                            if (iter.next() == suduko[row + boxStartRow][col+boxStartColumn].getCellValue()) {
                                iter.remove();
                                break;
                            }
                        }
                    }
                }
            }
            
            sudokoCell.setPossibleCellValues(possilbeCellValues);
            
        }
    }
  
    private static Set<Integer> generateListOfNumbers(int lower, int upper) {
        Set<Integer> result = new HashSet<>();
        for(int i=lower; i<= upper; i++) {
            result.add(i);
        }
        return result;
    }

    private static SudokuCell[][] originalPartiallySolvedProblem;
    private static List<Sudoku> partiallySolvedSudoku = new ArrayList<>();

    public static void solve(SudokuCell[][] sudoku) {

        if(isPartialSudokuValid(sudoku)) {

            while(!doWeHaveACompleteSolution(sudoku)) {

                //fill all null cells with 0
                fillAllNullCellsWithZero(sudoku);

                //find all possible values that a 0 cell can take
                fillAllZeroCellsWith1to9PossibleValues(sudoku);
                removeNonApplicableCellValuesFromPossibleCellValues(sudoku);

                //for all cells that have 1 possible value
                fillAllCellsWithOnePossibleValues(sudoku);

                //fill all the forced value cells
                computePSet(sudoku);

                //preemptive set algo.
                CombinationUtils.removePossibleCellValuesByPreemptiveSetAlgo(sudoku);

                //find all cells with one possible value and keep populating those cells.
                while(!findCellWithOnePossibeValue(sudoku).isEmpty()) {
                    fillAllCellsWithOnePossibleValues(sudoku);
                    computePSet(sudoku);
                    CombinationUtils.removePossibleCellValuesByPreemptiveSetAlgo(sudoku);
                }

                //if no cells are 0 and we don't have a complete solution
                //then discard this possibility
                if(!isPresentACellWithZero(sudoku)) {
                    if(!doWeHaveACompleteSolution(sudoku)) {
                        System.out.println("--Not a valid solution----");
                        printResults(sudoku);
                        System.out.println("------");
                        if(partiallySolvedSudoku.isEmpty()) {
                            System.out.println("Given Sudoku doesn't have a solution");
                        } else {
                            //remove the last element from list
                            Sudoku solveForThisSudoko = partiallySolvedSudoku.remove(partiallySolvedSudoku.size() - 1);
                            solve(solveForThisSudoko.getSudoku());
                        }
                        return;
                    }
                }

                //check after forced values if we have a complete solution
                if(doWeHaveACompleteSolution(sudoku)) {
                    break;
                }

                //at this point we have solved and filled all possible values
                //that we can without any randomization

                //find a cell with least number of possible values
                SudokuCell chosenCell = findACellWithLeastPossibleValues(sudoku);

                Integer chosenRow = chosenCell.getLocation().getRow();
                Integer chosenCol = chosenCell.getLocation().getCol();

                //make copies of sudoku
                Iterator<Integer> iterator = chosenCell.getPossibleCellValues().iterator();
                while (iterator.hasNext()) {
                    Integer val = iterator.next();
                    Sudoku sudokuCopy = cloneSudoku(sudoku);
                    SudokuCell[][] copiedCell = sudokuCopy.getSudoku();
                    copiedCell[chosenRow][chosenCol].setCellValue(val);
                    copiedCell[chosenRow][chosenCol].setCellColor(SudokuCellColor.Blue);
                    copiedCell[chosenRow][chosenCol].clearPossibleCellvalues();
                    //System.out.println("setting cell : " + chosenCell + " with value" + val);
                    partiallySolvedSudoku.add(sudokuCopy);
                }

                break;

            }

            //check if you have a complete solution
            if(doWeHaveACompleteSolution(sudoku)) {
                //System.out.println("We have a complete valid solution");
                printResults(sudoku);
                return;
            } else {
                //remove the last element from list
                Sudoku solveForThisSudoko = partiallySolvedSudoku.remove(partiallySolvedSudoku.size() - 1);
                solve(solveForThisSudoko.getSudoku());
            }


        } else {
            //sudoku not valid do something.
            System.out.println("Not a valid solution");
            printResults(sudoku);
            if(partiallySolvedSudoku.isEmpty()) {
                System.out.println("Given Sudoku doesn't have a solution");
            } else {
                //remove the last element from list
                Sudoku solveForThisSudoko = partiallySolvedSudoku.remove(partiallySolvedSudoku.size() - 1);
                solve(solveForThisSudoko.getSudoku());
            }

        }

    }



    private static SudokuCell findACellWithLeastPossibleValues(SudokuCell[][] sudoku) {

        SudokuCell chosenCell = null;
        Set<SudokuCell> results = new HashSet<>();

        for(int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {
                if(sudoku[row][col].getCellValue()== 0) {
                    if(chosenCell == null) {
                        chosenCell = sudoku[row][col];
                    }
                    results.add(sudoku[row][col]);
                }
            }
        }

        for (SudokuCell cell :
                results) {
            if(chosenCell.getPossibleCellValues().size() > cell.getPossibleCellValues().size()) {
                chosenCell = cell;
            }
        }

        randomlySelectedCellCopy.add(chosenCell);

        return chosenCell;
    }


    private static Sudoku cloneSudoku(SudokuCell[][] sudoku) {

        SudokuCell[][] clonedSudoku = new SudokuCell[sudoku.length][sudoku[0].length];

        for(int row=0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {
                clonedSudoku[row][col] = SudokuCell.newInstanceSudokuCell(sudoku[row][col]);
            }
        }

        return new Sudoku(clonedSudoku);
    }

    private static boolean isPresentACellWithZero(SudokuCell[][] sudoku) {

        //check for a zero cell. if present return false
        for(int row=0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {
                if(sudoku[row][col].getCellValue() ==0) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean doWeHaveACompleteSolution(SudokuCell[][] sudoku) {

        if(!isPresentACellWithZero(sudoku)) {
            
            try {
               boolean rowResults = checkEachSudokuRow(sudoku);
               boolean colResults = checkEachSudokuCol(sudoku);
               boolean boxResults = checkEachSudokuBox(sudoku);
               
               if(rowResults && colResults && boxResults) {
                   return true;
               }
               
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                return false;
            }
            

        } else {
            return false;
        }

        return false;
    }

    
    private static boolean checkEachSudokuBox(SudokuCell[][] sudoku) {
        
        for (int boxNo = 0; boxNo < sudoku.length; boxNo++ ) {
            Map<Integer,Integer> mapNumberTimes = new HashMap<>(9);
            for (int rowIdx = 0; rowIdx < 3; rowIdx++ ) {
                int rIdx = rowIdx+3*(boxNo/3);
                for (int colIdx = 0; colIdx < 3; colIdx++ ) {
                    int cIdx = colIdx+3*(boxNo%3);
                    SudokuCell cell = sudoku[rIdx][cIdx];
                    if(mapNumberTimes.get(cell.getCellValue()) == null) {
                        mapNumberTimes.put(cell.getCellValue(), 1);
                    } else {
                        mapNumberTimes.put(cell.getCellValue(), mapNumberTimes.get(cell.getCellValue()) + 1);
                    }
                }
                //System.out.println();
            }
            for (Map.Entry<Integer, Integer> entry : mapNumberTimes.entrySet()) {
                Integer key = entry.getKey();
                Integer count = entry.getValue();
                if(count != 1) {
                    throw new RuntimeException("Invalid Configuration on Box : " + boxNo + " number : " + key + " appears " + count + " times");
                }
            }
        }
        return true;
    }

    private static boolean checkEachSudokuRow(SudokuCell[][] sudoku) {

        //check each row
        for (int row = 0; row < sudoku.length; row++) {
            Map<Integer,Integer> mapNumberTimes = new HashMap<>(9);
            
            for (int col = 0; col < sudoku[0].length; col++) {
                SudokuCell cell = sudoku[row][col];
                if(mapNumberTimes.get(cell.getCellValue()) == null) {
                    mapNumberTimes.put(cell.getCellValue(), 1);
                } else {
                    mapNumberTimes.put(cell.getCellValue(), mapNumberTimes.get(cell.getCellValue()) + 1);
                }
            }

            for (Map.Entry<Integer, Integer> entry : mapNumberTimes.entrySet()) {
                Integer key = entry.getKey();
                Integer count = entry.getValue();
                if(count != 1) {
                    throw new RuntimeException("Invalid Configuration on row : " + row + " number : " + key + " appears " + count + " times");
                }
            }

        }
        return true;
        
    }

    private static boolean checkEachSudokuCol(SudokuCell[][] sudoku) {

        //check each row
        for (int row = 0; row < sudoku.length; row++) {
            Map<Integer,Integer> mapNumberTimes = new HashMap<>(9);
            
            for (int col = 0; col < sudoku[0].length; col++) {
                SudokuCell cell = sudoku[col][row];
                if(mapNumberTimes.get(cell.getCellValue()) == null) {
                    mapNumberTimes.put(cell.getCellValue(), 1);
                } else {
                    mapNumberTimes.put(cell.getCellValue(), mapNumberTimes.get(cell.getCellValue()) + 1);
                }
            }

            for (Map.Entry<Integer, Integer> entry : mapNumberTimes.entrySet()) {
                Integer key = entry.getKey();
                Integer count = entry.getValue();
                if(count != 1) {
                    throw new RuntimeException("Invalid Configuration on column : "  + " number : " + key + " appears " + count + " times");
                }
            }

        }
        return true;
        
    }

    private static boolean checkPartialSudokuBox(SudokuCell[][] sudoku) {

        for (int boxNo = 0; boxNo < sudoku.length; boxNo++ ) {
            Map<Integer,Integer> mapNumberTimes = new HashMap<>(10);
            for (int rowIdx = 0; rowIdx < 3; rowIdx++ ) {
                int rIdx = rowIdx+3*(boxNo/3);
                for (int colIdx = 0; colIdx < 3; colIdx++ ) {
                    int cIdx = colIdx+3*(boxNo%3);
                    SudokuCell cell = sudoku[rIdx][cIdx];
                    if(mapNumberTimes.get(cell.getCellValue()) == null) {
                        mapNumberTimes.put(cell.getCellValue(), 1);
                    } else {
                        mapNumberTimes.put(cell.getCellValue(), mapNumberTimes.get(cell.getCellValue()) + 1);
                    }
                }
                //System.out.println();
            }
            for (Map.Entry<Integer, Integer> entry : mapNumberTimes.entrySet()) {
                Integer key = entry.getKey();
                Integer count = entry.getValue();
                if(count != 1) {
                    if(key != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private static boolean checkParitalSudokuRow(SudokuCell[][] sudoku) {
        //check each row
        for (int row = 0; row < sudoku.length; row++) {
            Map<Integer,Integer> mapNumberTimes = new HashMap<>(9);

            for (int col = 0; col < sudoku[0].length; col++) {
                SudokuCell cell = sudoku[row][col];
                if(mapNumberTimes.get(cell.getCellValue()) == null) {
                    mapNumberTimes.put(cell.getCellValue(), 1);
                } else {
                    mapNumberTimes.put(cell.getCellValue(), mapNumberTimes.get(cell.getCellValue()) + 1);
                }
            }

            for (Map.Entry<Integer, Integer> entry : mapNumberTimes.entrySet()) {
                Integer key = entry.getKey();
                Integer count = entry.getValue();
                if(count != 1) {
                    if(key != 0 ) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    private static boolean checkParitalSudokuCol(SudokuCell[][] sudoku) {
        //check each row
        for (int row = 0; row < sudoku.length; row++) {
            Map<Integer,Integer> mapNumberTimes = new HashMap<>(9);

            for (int col = 0; col < sudoku[0].length; col++) {
                SudokuCell cell = sudoku[col][row];
                if(mapNumberTimes.get(cell.getCellValue()) == null) {
                    mapNumberTimes.put(cell.getCellValue(), 1);
                } else {
                    mapNumberTimes.put(cell.getCellValue(), mapNumberTimes.get(cell.getCellValue()) + 1);
                }
            }

            for (Map.Entry<Integer, Integer> entry : mapNumberTimes.entrySet()) {
                Integer key = entry.getKey();
                Integer count = entry.getValue();
                if(count != 1) {
                    if(key != 0) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    private static boolean isPartialSudokuValid(SudokuCell[][] sudoku) {
        //check for each row
        //check for each col
        //check for each box
        boolean rowResults = checkParitalSudokuRow(sudoku);
        boolean colResults = checkParitalSudokuCol(sudoku);
        boolean boxResults = checkPartialSudokuBox(sudoku);

        if(rowResults && colResults && boxResults) {
            return true;
        }

        return false;
    }

    private static void computePSet(SudokuCell[][] sudoku) {
        List<PreemptiveSet> rowWiseSet = numberPossibleCellLocationsRowWise(sudoku);
        List<PreemptiveSet> colWiseSet = numberPossibleCellLocationsColWise(sudoku);
        List<PreemptiveSet> boxWiseSet = numberPossibleCellLocationsBoxWise(sudoku);
        
        if(pSetTerminationCondition(rowWiseSet) && 
                pSetTerminationCondition(colWiseSet) && 
                pSetTerminationCondition(boxWiseSet)) {
        
            fillAllNullCellsWithZero(sudoku);
            fillAllZeroCellsWith1to9PossibleValues(sudoku);
            removeNonApplicableCellValuesFromPossibleCellValues(sudoku);
            fillAllCellsWithOnePossibleValues(sudoku);
            return;
        }
        
        setForcedValues(rowWiseSet);
        setForcedValues(colWiseSet);
        setForcedValues(boxWiseSet);
        
        
        fillAllNullCellsWithZero(sudoku);
        fillAllZeroCellsWith1to9PossibleValues(sudoku);
        removeNonApplicableCellValuesFromPossibleCellValues(sudoku);
        fillAllCellsWithOnePossibleValues(sudoku);
        computePSet(sudoku);
        
    }
    
    private static boolean pSetTerminationCondition(List<PreemptiveSet> pSet) {
        for (PreemptiveSet p : pSet) {
            if(p.getNumbersThatBelongToOnlyOneCell().size() != 0) {
                return false;
            }
        }
        return true;
    }
    
    private static void setForcedValues(List<PreemptiveSet> pSet) {
        for (PreemptiveSet p : pSet) {
            Map<Integer, SudokuCell> m = p.getNumbersThatBelongToOnlyOneCell();
            for (Map.Entry<Integer, SudokuCell> entry : m.entrySet()) {
                Integer key = entry.getKey();
                SudokuCell value = entry.getValue();
                setCellValue(key, value);
            }
        }
    }
    
    public static void setCellValue(int cellValue, SudokuCell cell) {
        //System.out.println("Setting " + cellValue + " for " + cell.getLocation());
        cell.setCellColor(SudokuCellColor.GRAY);
        cell.setCellValue(cellValue);
        cell.clearPossibleCellvalues();
    }
    
    public static void printSudokuCell(SudokuCell[][] sudoku) {
        //print the numbers in each cell
        for (int row = 0; row < sudoku.length; row++) {
            for (int col = 0; col < sudoku[0].length; col++) {
                System.out.println(sudoku[row][col]);
            }
            System.out.println("--");
        }
    }
    
    public static void printResults(SudokuCell[][] sudoku) {
        //print the numbers in each cell
        int boxSize = (int) Math.sqrt(sudoku.length);
        for (int row = 0; row < sudoku.length; row++) {
            
            if(row%boxSize == 0 ) {
                System.out.printf("\n");
            }
            
            for (int col = 0; col < sudoku[0].length; col++) {
                if(col%boxSize == 0 ) {
                    System.out.printf("     ");
                }
                System.out.printf("%-5d",sudoku[row][col].getCellValue());
            }
            
            System.out.printf("\n");
        }
    }
    
    public static List<PreemptiveSet> numberPossibleCellLocationsRowWise(SudokuCell[][] sudoku) {
        
        List<PreemptiveSet> p = new ArrayList<>();
        
        for (int row = 0; row < sudoku.length; row++) {
            Map<Integer,List<SudokuCell>> ds = new HashMap<>();
            for (int col = 0; col < sudoku[0].length; col++) {
                if(sudoku[row][col].getCellValue() > 0 ) {
                    ds.put(sudoku[row][col].getCellValue(), new ArrayList<>());
                } else {
                    Set<Integer> possibleCellValues = sudoku[row][col].getPossibleCellValues();
                    for (Integer possibleCellValue : possibleCellValues) {
                        List<SudokuCell> v = ds.get(possibleCellValue);
                        if(v!=null) {
                            v.add(sudoku[row][col]);
                        } else {
                            List<SudokuCell> c = new ArrayList<>();
                            c.add(sudoku[row][col]);
                            ds.put(possibleCellValue,c);
                        }
                    }
                }
            }
            PreemptiveSet s = new PreemptiveSet(OperationType.ROW, row, ds);
            p.add(s);
        }
        return p;
        
    }
    

    public static List<PreemptiveSet> numberPossibleCellLocationsBoxWise(SudokuCell[][] sudoku) {
        List<PreemptiveSet> p = new ArrayList<>();
       
       
        int boxSize = (int) Math.sqrt(sudoku.length);
        for (int i = 0; i < sudoku.length; i = i+3) {
            for (int j = 0; j < sudoku[0].length; j = j+3) {
                Map<Integer,List<SudokuCell>> ds = new HashMap<>();
                for (int row = 0; row < boxSize; row++) {
                    for (int col = 0; col < boxSize; col++) {
                        SudokuCell cell = sudoku[row+i][col+j];
                        if(cell.getCellValue() > 0 ) {
                            ds.put(cell.getCellValue(), new ArrayList<>());
                        } else {
                            Set<Integer> possibleCellValues = cell.getPossibleCellValues();
                            for (Integer possibleCellValue : possibleCellValues) {
                                List<SudokuCell> v = ds.get(possibleCellValue);
                                if(v!=null) {
                                    v.add(cell);
                                } else {
                                    List<SudokuCell> c = new ArrayList<>();
                                    c.add(cell);
                                    ds.put(possibleCellValue,c);
                                }
                            }
                        }
                    }
                }
                PreemptiveSet s = new PreemptiveSet(OperationType.BOX, (i+(j/boxSize)), ds);
                p.add(s);
                
            }
        }
        
        return p;
    }
    
    public static List<PreemptiveSet> numberPossibleCellLocationsColWise(SudokuCell[][] sudoku) {
        
        List<PreemptiveSet> p = new ArrayList<>();
        
        for (int row = 0; row < 1; row++) {
            Map<Integer,List<SudokuCell>> ds = new HashMap<>();
            for (int col = 0; col < sudoku[0].length; col++) {
                SudokuCell cell = sudoku[col][row];
                if(cell.getCellValue() > 0 ) {
                    ds.put(cell.getCellValue(), new ArrayList<>());
                } else {
                    Set<Integer> possibleCellValues = cell.getPossibleCellValues();
                    for (Integer possibleCellValue : possibleCellValues) {
                        List<SudokuCell> v = ds.get(possibleCellValue);
                        if(v!=null) {
                            v.add(sudoku[col][row]);
                        } else {
                            List<SudokuCell> c = new ArrayList<>();
                            c.add(cell);
                            ds.put(possibleCellValue,c);
                        }
                    }
                }
            }
            PreemptiveSet s = new PreemptiveSet(OperationType.COL, row, ds);
            p.add(s);
        }
        
        return p;
        
    }
}
