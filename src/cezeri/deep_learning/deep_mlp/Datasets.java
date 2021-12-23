package cezeri.deep_learning.deep_mlp;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Datasets {
    public static double[] convertOneHotEncoding(int n,String class_number){
        double[] ret=new double[n];
        double cln=Double.parseDouble(class_number);
        if (cln==0) {
            ret[0]=1;
        }else{
            ret[(int)cln]=1;
        }
        return ret;
    }

    public static LinkedHashMap<String, List<String>> loadData(String path, int w, int h, int ch) {
        LinkedHashMap<String, List<String>> dataset = new LinkedHashMap<>();
        File[] dirs = FactoryUtils.getDirectories(path);
        List<String> lst_input = new ArrayList();
        List<String> lst_output = new ArrayList();
        for (File dir : dirs) {
            File[] files = FactoryUtils.getFileListInFolderForImages(dir.getAbsolutePath());
            for (File file : files) {
                lst_input.add(file.getAbsolutePath());
                lst_output.add(Arrays.toString(convertOneHotEncoding(dirs.length, dir.getName())));
            }
        }
        long seed = System.nanoTime();
        Collections.shuffle(lst_input, new Random(seed));
        Collections.shuffle(lst_output, new Random(seed));
        dataset.put("X", lst_input);
        dataset.put("Y", lst_output);
        return dataset;
    }

}
