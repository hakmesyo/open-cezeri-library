package cezeri.machine_learning.classifiers.deeplearning_ocl;

import cezeri.matrix.CMatrix;
import cezeri.matrix.CPoint;
import cezeri.matrix.FactoryMatrix;
import java.io.IOException;
import java.io.Serializable;


public class Matrix implements Serializable {

    public CMatrix data;

    public Matrix(int numRows, int numColumns) {
        data = CMatrix.getInstance(numRows, numColumns);
    }

    public Matrix(double[][] values) {
        data = CMatrix.getInstance(values);
    }

    public Matrix(CMatrix m) {
        data = m;
    }

    public int numRows() {
        return data.getRowNumber();
    }

    public int numColumns() {
        return data.getColumnNumber();
    }

    public Matrix copy() {
        return new Matrix(data.toDoubleArray2D());
    }

    public double get(int row, int col) {
        return data.getValue(row, col);
    }

    public double[] getRow(int row) {
        return data.getRow(row);
    }

    public double[] getCol(int col) {
        return data.getColumn(col);
    }

    public void set(int row, int col, double v) {
        data.setValue(new CPoint(row, col), v);
    }

    public void fill(double value) {
        data = CMatrix.getInstance(FactoryMatrix.matrixDoubleValue(numRows(), numColumns(), value));
    }

    private void checkSize(Matrix m2) {
        if (numRows() != m2.numRows()) {
            throw new IndexOutOfBoundsException("A.numRows != B.numRows ("
                    + numRows() + " != " + m2.numRows() + ")");
        }
        if (numColumns() != m2.numColumns()) {
            throw new IndexOutOfBoundsException(
                    "A.numColumns != B.numColumns (" + numColumns() + " != "
                    + m2.numColumns() + ")");
        }
    }

    public Matrix add(double n, Matrix m2) {
        checkSize(m2);
        double[][] d = getData();
        double[][] d2 = m2.getData();
        int r = d.length;
        int c = d[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                d[i][j] =d[i][j]+ n * d2[i][j];
            }
        }
        setData(d);
        return this;
    }

    public Matrix add(double n) {
        double[][] d = getData();
        int r = d.length;
        int c = d[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                d[i][j] += n;
            }
        }
        setData(d);
        return this;
    }

    public void add(int row, int col, double v) {
        set(row, col, get(row, col) + v);
    }

//    public Matrix mult(Matrix m2) {
//        return mult(m2, new Matrix(numRows(), m2.numColumns()));
//    }
    
    public Matrix multiply(Matrix m2) {
        data = data.times(m2.data);
        return new Matrix(data);
    }

//    public Matrix mult(Matrix m2, Matrix res) {
//        myMatrix = m2.myMatrix.times(res.myMatrix);
//        return this;
//    }

    public Matrix multElements(Matrix m2) {
        data = m2.data.multiplyElement(data);
        return this;
    }

    public Matrix multElements(Matrix m2, Matrix res) {
        checkSize(m2);
        checkSize(res);
        double[][] d = getData();
        double[][] d2 = m2.getData();
        double[][] dres = res.getData();
        int r = d.length;
        int c = d[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                dres[i][j] = d[i][j] * d2[i][j];
            }
        }
        res.setData(d);
        return res;
    }

    public Matrix trans1mult(Matrix m2) {
        data = data.transpose().times(m2.data);
        return this;
    }

    public Matrix trans1mult(Matrix m2, Matrix res) {
        data = m2.data.transpose().times(res.data);
        return this;
    }

    public Matrix trans2mult(Matrix m2) {
        data = data.times(m2.data.transpose());
        return this;
    }

    public Matrix trans2mult(Matrix m2, Matrix res) {
        data = m2.data.times(res.data.transpose());
        return this;
    }

    public Matrix scale(double val) {
        double[][] d = getData();
        int r = d.length;
        int c = d[0].length;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                d[i][j] = d[i][j] * val;
            }
        }
        setData(d);
        return this;
    }

    public double[][] getData() {
        return data.toDoubleArray2D();
    }

    public Matrix addColumns(Matrix m2) {
        if (numRows() != m2.numRows()) {
            throw new IndexOutOfBoundsException("A.numRows != B.numRows ("
                    + numRows() + " != " + m2.numRows() + ")");
        }
        CMatrix cm = data.cat(1, m2.data);
        Matrix ret = new Matrix(cm);
        return ret;
    }

    public Matrix getColumns(int startCol, int endCol) {
        endCol = endCol == -1 ? numColumns() - 1 : endCol;
        CMatrix ret = data.cmd(":", startCol + ":" + endCol);
        Matrix m2 = new Matrix(ret);
        return m2;
    }

    public Matrix getRows(int startRow, int endRow) {
        endRow = endRow == -1 ? numRows() - 1 : endRow;
        CMatrix ret = data.cmd(startRow + ":" + endRow, ":");
        Matrix m2 = new Matrix(ret);
        return m2;
    }

    @Override
    public String toString() {
        if (data==null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < numRows(); row++) {
            for (int col = 0; col < numColumns(); col++) {
                sb.append(get(row, col)).append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void println() {
        System.out.println(toString());
    }

    /**
     * Needed because MTJs Matrix is not serializable
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(numRows());
        out.writeInt(numColumns());
        out.writeObject(getData());
    }

    /**
     * Needed because MTJs Matrix is not serializable
     */
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        data = CMatrix.getInstance(in.readInt(), in.readInt());
        double[] data = (double[]) in.readObject();
        System.arraycopy(data, 0, this.data.toDoubleArray1D(), 0, data.length);
    }

    public void setData(double[][] d) {
        data = data.setArray(d);
    }

    public Matrix transpose() {
        data=data.transpose();
        return new Matrix(data);
    }
}
