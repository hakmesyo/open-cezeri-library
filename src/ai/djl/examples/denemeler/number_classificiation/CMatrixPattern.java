/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.djl.examples.denemeler.number_classificiation;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import cezeri.types.TBlockType;
import java.io.File;
import java.util.Arrays;

/**
 *
 * @author cezerilab
 */
public class CMatrixPattern {

    private static String path = "C:\\ai\\djl\\dataset_pdr\\mixed\\";

    public static void main(String[] args) {
        doTraining();
        //doPrediction();
    }

    private static CMatrix[] extractNumbers(CMatrix cm, int p) {
        int w = 303;
        int h = 1;
        CMatrix[] ret = new CMatrix[11];
        for (int i = 0; i < 11; i++) {
            String str = (p + i * w) + ":" + (i * w + w + p);
            ret[i] = cm.cmd("100:540", str).imresize(224, 224);
        }
        return ret;
    }

    private static void doPrediction() {
        File[] files = FactoryUtils.getFileListInFolder(path);
        System.out.println("size:" + files.length);
        String[] tc = new String[files.length];
        String[] gender = new String[files.length];
        CMatrix cm_gender = CMatrix.getInstance().setModelForInference("models/gender/genders-0030.params", 3, 224, 224, TBlockType.MLP);
        CMatrix cm_tc = CMatrix.getInstance().setModelForInference("models/tc_numbers/tc_numbers-0030.params", 3, 224, 224, TBlockType.MLP);
        for (int i = 0; i < files.length; i++) {
            String str = cm_gender
                    .imread(files[i].getAbsolutePath())
                    .cmd("0:30", "130:160")
                    .imresize(20)
                    .imresize(224, 224)
                    .predictWithLabel();
            System.out.println(str);
            gender[i] = str;
            if (str.equals("female")) {
                cm_tc = cm_tc
                        .imread(files[i].getAbsolutePath())
                        .cmd("0:30", "217:387")
                        .imresize(20) //.imshow()
                        ;

                CMatrix[] numbers = extractNumbers(cm_tc, 0);
                String s = "";
                for (CMatrix number : numbers) {
                    s += number.predictWithLabel();
                }
                tc[i] = s;

            } else {
                cm_tc = cm_tc
                        .imread(files[i].getAbsolutePath())
                        .cmd("0:30", "220:390")
                        .imresize(20) //.imshow()
                        ;

                CMatrix[] numbers = extractNumbers(cm_tc, 60);
                String s = "";
                for (CMatrix number : numbers) {
                    s += number.predictWithLabel();
                }
                tc[i] = s;
            }
            String fileName = FactoryUtils.getFileName(files[i].getName());
            if (!tc[i].equals(fileName)) {
                System.out.println(i + ".index error");
                System.out.println(tc[i]);
            } else {
                System.out.println(i + ".index hit");
            }

        }
        //Arrays.asList(tc).forEach(System.out::println);
        String[] rows = new String[tc.length];
        for (int i = 0; i < tc.length; i++) {
            rows[i] = gender[i] + ";" + tc[i];
        }
        FactoryUtils.writeToFile("models/tc_numbers/tcno.csv", rows);
    }

    private static void doTraining() {
        CMatrix cm = CMatrix.getInstance()
                .setModelForTrain("numnum", 3, 224, 224,10, TBlockType.MLP)
                .trainDataSet("C:\\ai\\djl\\dataset_pdr\\dataset", 32, 9, 1, 10)
                ;
    }
}
