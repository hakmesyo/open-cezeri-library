package cezeri.factory;

public class FactoryPermutation {

    public static void main(String[] args) {
        String str = "AB21";
        int n = str.length();
        FactoryPermutation.permute(str, 0, n - 1);
//        FactoryPermutation.permutePair(str, 0,3);
    }

    public static String[] getPermutation(char[] lst) {
        permutationStr = "";
        String str=String.valueOf(lst);
        FactoryPermutation.permutePair(str, 0,str.length()-1);
        return permutationStr.split(",");
    }

    /**
     * permutation function
     *
     * @param str string to calculate permutation for
     * @param l starting index
     * @param r end index
     */
    static String permutationStr = "";

    private static void permutePair(String str, int l, int r) {
        if (l == r) {
//            System.out.println(str);
            permutationStr += str + ",";
        } else {
            for (int i = l; i <= r; i++) {
                str = swap(str, l, i);
                permutePair(str, l + 1, r);
                str = swap(str, l, i);
            }
        }
    }

    /**
     * permutation function
     *
     * @param str string to calculate permutation for
     * @param l starting index
     * @param r end index
     */
    private static void permute(String str, int l, int r) {
        if (l == r) {
            System.out.println(str);
        } else {
            for (int i = l; i <= r; i++) {
                str = swap(str, l, i);
                permute(str, l + 1, r);
                str = swap(str, l, i);
            }
        }
    }

    /**
     * Swap Characters at position
     *
     * @param a string value
     * @param i position 1
     * @param j position 2
     * @return swapped string
     */
    private static String swap(String a, int i, int j) {
        char temp;
        char[] charArray = a.toCharArray();
        temp = charArray[i];
        charArray[i] = charArray[j];
        charArray[j] = temp;
        return String.valueOf(charArray);
    }

}
