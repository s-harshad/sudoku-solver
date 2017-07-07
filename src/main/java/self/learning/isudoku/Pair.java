package self.learning.isudoku;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author hshrishrimal
 * @param <L>
 * @param <R>
 */
public class Pair<L,R>  implements Serializable{
    
    private final L row;
    private final R col;

    public Pair(L row, R col) {
        this.row = row;
        this.col = col;
    }

    public L getRow() {
        return row;
    }

    public R getCol() {
        return col;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.row);
        hash = 23 * hash + Objects.hashCode(this.col);
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
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.row, other.row)) {
            return false;
        }
        if (!Objects.equals(this.col, other.col)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pair{" + "row=" + row + ", col=" + col + '}';
    }
    
    
}
