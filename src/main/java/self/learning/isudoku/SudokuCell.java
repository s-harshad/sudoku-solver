package self.learning.isudoku;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hshrishrimal
 */
public class SudokuCell implements Serializable {

    private SudokuCellColor cellColor;
    private Set<Integer> possibleCellValues = new HashSet<>();
    private final Pair<Integer,Integer> location;
    private int cellValue;

    public static SudokuCell newInstanceSudokuCell(SudokuCell cell)  {

        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(cell);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();

            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            SudokuCell removePossibleCellValuesByPreemptiveSetAlgo = (SudokuCell) new ObjectInputStream(bais).readObject();
            return removePossibleCellValuesByPreemptiveSetAlgo;
        } catch (IOException ex) {
            Logger.getLogger(SudokuCell.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SudokuCell.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(SudokuCell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

    
    
    
    public SudokuCell(SudokuCellColor cellColor, Pair<Integer, Integer> location, int cellValue) {
        this.cellColor = cellColor;
        this.location = location;
        this.cellValue = cellValue;
    }

    public SudokuCellColor getCellColor() {
        return cellColor;
    }

    public Set<Integer> getPossibleCellValues() {
        Set<Integer> cellValues = new HashSet<>();
        for (Integer possibleCellValue : possibleCellValues) {
            cellValues.add(possibleCellValue);
        }
        return cellValues;
    }

    public void setPossibleCellValues(Set<Integer> possibleCellValues) {
        clearPossibleCellvalues();
        this.possibleCellValues.addAll(possibleCellValues);
    }

    public void clearPossibleCellvalues() {
        this.possibleCellValues.clear();
    }
    
    public void setCellValue(int cellValue) {
        this.cellValue = cellValue;
    }

    public Pair<Integer, Integer> getLocation() {
        return location;
    }

    public int getCellValue() {
        return cellValue;
    }

    public void setCellColor(SudokuCellColor cellColor) {
        this.cellColor = cellColor;
    }

    @Override
    public String toString() {
        return "SudokuCell{" + "cellColor=" + cellColor + 
               ", possibleCellValues=" + possibleCellValues + 
               ", location=" + location + 
               ", cellValue=" + cellValue + "}\n";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.cellColor);
        hash = 23 * hash + Objects.hashCode(this.possibleCellValues);
        hash = 23 * hash + Objects.hashCode(this.location);
        hash = 23 * hash + this.cellValue;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SudokuCell other = (SudokuCell) obj;
        if (this.cellValue != other.cellValue) {
            return false;
        }
        if (this.cellColor != other.cellColor) {
            return false;
        }
        if (!Objects.equals(this.possibleCellValues, other.possibleCellValues)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        return true;
    }
    
    
}
