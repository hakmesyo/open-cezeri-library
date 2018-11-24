package cezeri.utils;

import java.math.BigInteger;
import java.util.ArrayList;

public class FactoryCombination {

    private int[] a;
    private int n;
    private int r;
    private BigInteger numLeft;
    private BigInteger total;

    //------------
    // Constructor
    //------------
    public FactoryCombination(int n, int r) {
        if (r > n) {
            throw new IllegalArgumentException();
        }
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.r = r;
        a = new int[r];
        BigInteger nFact = getFactorial(n);
        BigInteger rFact = getFactorial(r);
        BigInteger nminusrFact = getFactorial(n - r);
        total = nFact.divide(rFact.multiply(nminusrFact));
        reset();
    }

    //------
    // Reset
    //------
    public void reset() {
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }
        numLeft = new BigInteger(total.toString());
    }

    //------------------------------------------------
    // Return number of combinations not yet generated
    //------------------------------------------------
    public BigInteger getNumLeft() {
        return numLeft;
    }

    //-----------------------------
    // Are there more combinations?
    //-----------------------------
    public boolean hasMore() {
        return numLeft.compareTo(BigInteger.ZERO) == 1;
    }

    //------------------------------------
    // Return total number of combinations
    //------------------------------------
    public BigInteger getTotal() {
        return total;
    }

    //------------------
    // Compute factorial
    //------------------
    private static BigInteger getFactorial(int n) {
        BigInteger fact = BigInteger.ONE;
        for (int i = n; i > 1; i--) {
            fact = fact.multiply(new BigInteger(Integer.toString(i)));
        }
        return fact;
    }

    //--------------------------------------------------------
    // Generate next combination (algorithm from Rosen p. 286)
    //--------------------------------------------------------
    public int[] getNext() {

        if (numLeft.equals(total)) {
            numLeft = numLeft.subtract(BigInteger.ONE);
            return a;
        }

        int i = r - 1;
        while (a[i] == n - r + i) {
            i--;
        }
        a[i] = a[i] + 1;
        for (int j = i + 1; j < r; j++) {
            a[j] = a[i] + j - i;
        }

        numLeft = numLeft.subtract(BigInteger.ONE);
        return a;

    }

    public static String[] getCombination(String[] elements, int subSet) {
        int[] indices;
        FactoryCombination x = new FactoryCombination(elements.length, subSet);
//        System.out.println("Comb:" + x.getTotal());
        StringBuffer combination;
        int k = 0;
        String[] ret = new String[x.getTotal().intValue()];
        while (x.hasMore()) {
            combination = new StringBuffer();
            indices = x.getNext();
            for (int i = 0; i < indices.length; i++) {
                combination.append(elements[indices[i]]+",");
            }
            combination.deleteCharAt(combination.length()-1);
            ret[k++] = combination.toString();
        }
        return ret;
    }
    
    public static String[] getAllCombinations(String[] elements){
        int n=elements.length;
        String[][] lst=new String[n][];
        for (int i = 0; i < n; i++) {
            lst[i]=getCombination(elements, i+1);
        }
        ArrayList<String> iret=new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < lst[i].length; j++) {
                iret.add(lst[i][j]);
            }
        }
        String[] ret=iret.toArray(new String[iret.size()]);
        return ret;
    }
    
    public static String[] getAllCombinations(int num){
        String[] elements=new String[num];
        for (int i = 0; i < num; i++) {
            elements[i]=i+"";
        }
        int n=elements.length;
        String[][] lst=new String[n][];
        for (int i = 0; i < n; i++) {
            lst[i]=getCombination(elements, i+1);
        }
        ArrayList<String> iret=new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < lst[i].length; j++) {
                iret.add(lst[i][j]);
            }
        }
        String[] ret=iret.toArray(new String[iret.size()]);
        return ret;
    }
    

    public static void main(String[] args) {
        String[] elements = {"0","1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20","21","22","23"
        ,"24","25","26"};
        String[] lst=getCombination(elements,5);
        System.out.println("length:"+lst.length);
//        String[] lst=getAllCombinations(elements);
        String[] lst2=getAllCombinations(3);
        toString(lst2);
    }
    
    public static void toString(String[] lst){
        for (int i = 0; i < lst.length; i++) {
            System.out.println(lst[i]);
        }
        System.out.println("===============================");
        System.out.println("combination size:"+lst.length);
    }

}
