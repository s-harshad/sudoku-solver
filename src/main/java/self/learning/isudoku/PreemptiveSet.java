package self.learning.isudoku;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hshrishrimal
 */
public class PreemptiveSet {

    private final OperationType type;
    private final Map<Integer,List<SudokuCell>> mapIntegerToPossibleCells = new HashMap<>();
    private final int index;
    
    
    public PreemptiveSet(OperationType type, int index, Map<Integer, List<SudokuCell>> mapIntegerToPossibleCells) {
        this.type = type;
        this.mapIntegerToPossibleCells.putAll(mapIntegerToPossibleCells);
        this.index = index;
    }
    
    public Map<Integer,SudokuCell> getNumbersThatBelongToOnlyOneCell() {
        Map<Integer,SudokuCell> mapValAndPossibleCell = new HashMap<>();
        for (Map.Entry<Integer, List<SudokuCell>> entry : mapIntegerToPossibleCells.entrySet()) {
            Integer key = entry.getKey();
            List<SudokuCell> value = entry.getValue();
            if(value.size() == 1) {
                mapValAndPossibleCell.put(key, value.iterator().next());
            }
        }
        return mapValAndPossibleCell;
    }

    public OperationType getType() {
        return type;
    }

    public Map<Integer, List<SudokuCell>> getMapIntegerToPossibleCells() {
        return mapIntegerToPossibleCells;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "PreemptiveSet{" + "type=" + type + ", mapIntegerToPossibleCells=" + mapIntegerToPossibleCells + ", index=" + index + '}';
    }
    
    
    
}
