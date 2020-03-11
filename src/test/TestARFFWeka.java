
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author elcezerilab
 */
public class TestARFFWeka {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.fromARFF("data\\digit.arff").showDataGrid();
        
        
    }
}
