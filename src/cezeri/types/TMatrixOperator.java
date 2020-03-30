/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.types;

import cezeri.matrix.CMatrix;
import cezeri.factory.FactoryUtils;
import java.util.ArrayList;

/**
 *
 * @author BAP1
 */
public enum TMatrixOperator {

    BETWEEN() {
        @Override
        public CMatrix apply(CMatrix d, double t1, double t2) {
            double[][] m = d.toDoubleArray2D();
            int k = 0;
            ArrayList lst = new ArrayList();
            for (int j = 0; j < m[0].length; j++) {
                for (int i = 0; i < m.length; i++) {
                    if (m[i][j] > t1 && m[i][j] < t2) {
                        lst.add(k);
                    }
                    k++;
                }
            }
            int[] d1 = FactoryUtils.toIntArray1D(lst);
            return d.setArray(d1);
        }

        @Override
        public CMatrix apply(CMatrix p, double x) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CMatrix apply(CMatrix d, double val, String p1, String p2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }
    },
    BETWEEN_EQUALS() {
        @Override
        public CMatrix apply(CMatrix d, double t1, double t2) {
            double[][] m = d.toDoubleArray2D();
            int k = 0;
            ArrayList lst = new ArrayList();
            for (int j = 0; j < m[0].length; j++) {
                for (int i = 0; i < m.length; i++) {
                    if (m[i][j] >= t1 && m[i][j] <= t2) {
                        lst.add(k);
                    }
                    k++;
                }
            }
            int[] d1 = FactoryUtils.toIntArray1D(lst);
            return d.setArray(d1);
        }

        @Override
        public CMatrix apply(CMatrix p, double x) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CMatrix apply(CMatrix d, double val, String p1, String p2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }
    },
    EQUALS() {
        @Override
        public CMatrix apply(CMatrix d, double x) {
            double[][] m = d.toDoubleArray2D();
            int k = 0;
            ArrayList lst = new ArrayList();
            for (int row = 0; row < m.length; row++) {
                for (int col = 0; col < m[0].length; col++) {
                    if (m[row][col] == x) {
                        lst.add(k);
                    }
                    k++;
                }
            }
            int[] d1 = FactoryUtils.toIntArray1D(lst);
            return d.setArray(d1);
        }

        @Override
        public CMatrix apply(CMatrix d, double t1, double t2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CMatrix apply(CMatrix d, double val, String p1, String p2) {
            double[][] m = d.toDoubleArray2D();
            int column = 0;
            ArrayList lst = new ArrayList();
            if (p1.equals(":")) {
                if (p2.contains(":")) {
                    return apply(d, val);
                } else {
                    try {
                        column = Integer.parseInt(p2);
                        for (int row = 0; row < m.length; row++) {
                            if (m[row][column] == val) {
                                lst.add(row);
                            }
                        }
                        int[] ret_array = FactoryUtils.toIntArray1D(lst);
                        return d.setArray(ret_array);
                    } catch (Exception e) {
                    }

                }
            } else if (p1.contains(":")) {
                if (p2.contains(":")) {
                    return apply(d, val);
                } else {
                    String[] r = p1.split(":");
                    int row1 = Integer.parseInt(r[0]);
                    int row2=0;
                    if (r[1].equals("end")) {
                        row2=d.getRowNumber()-1;
                    }else{
                        row2 = Integer.parseInt(r[1]);
                    }
                    try {
                        column = Integer.parseInt(p2);
                        for (int row = row1; row < row2 + 1; row++) {
                            if (m[row][column] == val) {
                                lst.add(row);
                            }
                        }
                        if (lst.size()<1) {
                            return d;
                        }
                        int[] ret_array = FactoryUtils.toIntArray1D(lst);
                        return d.setArray(ret_array);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (p1.contains("[")) {
                String pp = p1.substring(1, p1.length() - 1);
                String[] items;
                if (pp.contains(",")) {
                    items = pp.split(",");
                } else if (pp.contains(" ")) {
                    items = pp.split(" ");
                } else if (pp.contains(";")) {
                    items = pp.split(";");
                } else {
                    return apply(d, val, ":", p2);
                }

                for (int i = 0; i < items.length; i++) {
                    try {
                        column = Integer.parseInt(p2);
                        int r=Integer.parseInt(items[i]);
                        if (m[r][column]==val) {
                            lst.add(r);
                        }
                        if (lst.size()<1) {
                            return d;
                        }
                        int[] ret_array = FactoryUtils.toIntArray1D(lst);
                        return d.setArray(ret_array);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                return apply(d, val);
            }
            return d;
        }
    },
    NOT_EQUALS() {
        @Override
        public CMatrix apply(CMatrix d, double x) {
            double[][] m = d.toDoubleArray2D();
            int k = 0;
            ArrayList lst = new ArrayList();
            for (int j = 0; j < m[0].length; j++) {
                for (int i = 0; i < m.length; i++) {
                    if (m[i][j] != x) {
                        lst.add(k);
                    }
                    k++;
                }
            }
            int[] d1 = FactoryUtils.toIntArray1D(lst);
            return d.setArray(d1);
        }

        @Override
        public CMatrix apply(CMatrix d, double t1, double t2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CMatrix apply(CMatrix d, double val, String p1, String p2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

    },
    GREATER() {
        @Override
        public CMatrix apply(CMatrix d, double x) {
            double[][] m = d.toDoubleArray2D();
            int k = 0;
            ArrayList lst = new ArrayList();
            for (int j = 0; j < m[0].length; j++) {
                for (int i = 0; i < m.length; i++) {
                    if (m[i][j] > x) {
                        lst.add(k);
                    }
                    k++;
                }
            }
            int[] d1 = FactoryUtils.toIntArray1D(lst);
            return d.setArray(d1);
        }

        @Override
        public CMatrix apply(CMatrix d, double t1, double t2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CMatrix apply(CMatrix d, double val, String p1, String p2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }
    },
    GREATER_EQUALS() {
        @Override
        public CMatrix apply(CMatrix d, double x) {
            double[][] m = d.toDoubleArray2D();
            int k = 0;
            ArrayList lst = new ArrayList();
            for (int j = 0; j < m[0].length; j++) {
                for (int i = 0; i < m.length; i++) {
                    if (m[i][j] >= x) {
                        lst.add(k);
                    }
                    k++;
                }
            }
            int[] d1 = FactoryUtils.toIntArray1D(lst);
            return d.setArray(d1);
        }

        @Override
        public CMatrix apply(CMatrix d, double t1, double t2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CMatrix apply(CMatrix d, double val, String p1, String p2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }
    },
    SMALLER() {
        @Override
        public CMatrix apply(CMatrix d, double x) {
            double[][] m = d.toDoubleArray2D();
            int k = 0;
            ArrayList lst = new ArrayList();
            for (int j = 0; j < m[0].length; j++) {
                for (int i = 0; i < m.length; i++) {
                    if (m[i][j] < x) {
                        lst.add(k);
                    }
                    k++;
                }
            }
            int[] d1 = FactoryUtils.toIntArray1D(lst);
            return d.setArray(d1);
        }

        @Override
        public CMatrix apply(CMatrix d, double t1, double t2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CMatrix apply(CMatrix d, double val, String p1, String p2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }
    },
    SMALLER_EQUALS() {
        @Override
        public CMatrix apply(CMatrix d, double x) {
            double[][] m = d.toDoubleArray2D();
            int k = 0;
            ArrayList lst = new ArrayList();
            for (int j = 0; j < m[0].length; j++) {
                for (int i = 0; i < m.length; i++) {
                    if (m[i][j] <= x) {
                        lst.add(k);
                    }
                    k++;
                }
            }
            int[] d1 = FactoryUtils.toIntArray1D(lst);
            return d.setArray(d1);
        }

        @Override
        public CMatrix apply(CMatrix d, double t1, double t2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public CMatrix apply(CMatrix d, double val, String p1, String p2) {
            throw new UnsupportedOperationException("Only BETWEEN can be run"); //To change body of generated methods, choose Tools | Templates.
        }
    };

    // Yes, enums *can* have abstract methods. This code compiles...
    public abstract CMatrix apply(CMatrix d, double x);
    
    public abstract CMatrix apply(CMatrix d, double t1, double t2);

    public abstract CMatrix apply(CMatrix d, double val, String p1, String p2);
}

//public class TOperator {
//    public static final int EQUALS=0;
//    public static final int NOT_EQUALS=1;
//    public static final int GREATER=2;
//    public static final int GREATER_EQUALS=3;
//    public static final int SMALLER=4;
//    public static final int SMALLER_EQUALS=5;
//    public static final int AND=6;
//    public static final int OR=7;
//    public static final int XOR=8;
//    
//}
