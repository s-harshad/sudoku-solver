
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author hshrishrimal
 */
public class CombinationTest {

    /*Driver function to check for above function*/
    public static void main(String[] args) {
        for (int boxNo = 0; boxNo < 9; boxNo++ ) {
            System.out.println("boxNo = " + boxNo);
            for (int rowIdx = 0; rowIdx < 3; rowIdx++ ) {
                for (int colIdx = 0; colIdx < 3; colIdx++ ) {
                    System.out.printf("[%d][%d] ",rowIdx+3*(boxNo/3),colIdx+3*(boxNo%3));
                }
                System.out.println();
            }
            System.out.println();
        }


    }

}
