package cezeri.machine_learning.classifiers.neural_net;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

public class Datasets {
    
    public static LinkedHashMap<String, double[][][]> loadMnistData(String path){
        LinkedHashMap<String, double[][][]> dataset = new LinkedHashMap<>();

        File[] dirs = FactoryUtils.getDirectories(path);
        List<double[]> lst_input = new ArrayList();
        List<double[]> lst_output = new ArrayList();
        
        int num_features=0;
        CMatrix cm = CMatrix.getInstance();
        for (File dir : dirs) {
            File[] files = FactoryUtils.getFileListInFolderForImages(dir.getAbsolutePath());
            for (File file : files) {
                cm=cm.imread(file).rgb2gray();
                double[] d = cm.normalizeZScore().toDoubleArray1D();
                num_features=d.length;
                lst_input.add(d);
                lst_output.add(convertOneHotEncoding(dirs.length,dir.getName()));
            }
            int a=1;
        }
        double[][][] X = new double[lst_input.size()][1][num_features];
        double[][][] Y = new double[lst_input.size()][1][dirs.length];
        for (int i = 0; i < X.length; i++) {
            X[i][0]=lst_input.get(i);
            Y[i][0]=lst_output.get(i);
            
        }
        dataset.put("X", X);
        dataset.put("Y", Y);
        return dataset;
    }
    
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

    public static LinkedHashMap<String, double[][][]> loadIrisData(){

        LinkedHashMap<String, double[][][]> dataset = new LinkedHashMap<>();
        double[][][] X = new double[150][1][4];
        double[][][] Y = new double[150][1][3];
        try {
            Scanner scanner = new Scanner(new File("data/iris_1.txt"));
            int i = 0;
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] fields = line.split("\t");
                if(fields[0].equals("virginica")){
                    Y[i] = new double[][]{{0.0, 0.0, 1.0}};
                }
                else if(fields[0].equals("versicolor")){
                    Y[i] = new double[][]{{0.0, 1.0, 0.0}};
                }
                else if(fields[0].equals("setosa")){
                    Y[i] = new double[][]{{1.0, 0.0, 0.0}};
                }

                    X[i] = new double[][]{{Double.parseDouble(fields[1]),
                    Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4])}};

                i++;
                if(i == 150)
                    break;
                //System.out.println(i);
            }
            dataset.put("X", X);
            dataset.put("Y", Y);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

       /* for(int j=0; j<150; j++)
        {
            for(int k=0; k<Y[j][0].length; k++){
                System.out.print(Y[j][0][k]+" ");

            }
            System.out.println();

            //System.out.print(Y[j][0][0]+" ");
            //System.out.println(X[j][0]);
        }*/
        return dataset;
    }

    public static LinkedHashMap<String, double[][][]> loadCharacterData(int no_instances, int no_features){

        LinkedHashMap<String, double[][][]> dataset = new LinkedHashMap<>();
        double[][][] X = new double[no_instances][1][no_features];
        double[][][] Y;
        String[] labels = new String[no_instances];
        LinkedHashSet<String> labelSet = new LinkedHashSet<>();
        try {
            Scanner scanner = new Scanner(new File("data/data.txt"));
            int i = 0;
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] fields = line.split(",");
                labelSet.add(fields[0]);
                labels[i] = fields[0];

                double[][] zeros_ones = new double[1][100];
                for(int k=1; k<fields.length; k++){
                    zeros_ones[0][k-1] = Double.parseDouble(fields[k]);
                }

                X[i] = zeros_ones;

                i++;
                if(i == 56)
                    break;
                //System.out.println(i);
            }



            dataset.put("X", X);
            Y = Preprocessing.oneHotEncoding(labels, labelSet);
            dataset.put("Y", Y);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return dataset;
    }



    public static void main(String[] args) {
        double[][][] X = loadIrisData().get("X");
        double[][][] Y = loadIrisData().get("Y");

        double[][][] X1 = loadCharacterData(56, 100).get("X");
        double[][][] Y1 = loadCharacterData(56, 100).get("Y");

        double[] Alif = X1[29][0];

        for(int i=0; i<Alif.length; i++){
            if((i+1)%10==0 && i!=0)
                System.out.println((int)Alif[i]);
            else
                System.out.print((int)Alif[i]+" ");
        }



        System.out.println("");

    }

}
