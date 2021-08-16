/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.ai.djl.examples.denemeler.gender_text_classification;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author cezerilab
 */
public class Helper {

//    private static String path = "C:\\ai\\djl\\dataset_pdr\\female";
    private static String path = "C:\\ai\\djl\\dataset_pdr\\male";

    public static void main(String[] args) {
        //renameFiles();
        //extract_tc_numbers();
        //extract_gender_char();

        String modelName=extractModelName("models/gender/genders-0030.params");
        System.out.println("modelName = " + modelName);

    }

    private static CMatrix[] extractNumbers(CMatrix cm, int p) {
        int w = 303;
        int h = 1;
        //int p = 60;
        CMatrix[] ret = new CMatrix[11];
        for (int i = 0; i < 11; i++) {
            String str = (p + i * w) + ":" + (i * w + w + p);
            ret[i] = cm.cmd("100:540", str).imshow().imresize(224, 224).imsave_atFolder("C:\\ai\\djl\\dataset_pdr\\dataset");
        }
        return ret;
    }

    private static void renameFiles() {
        String path = "C:\\ai\\djl\\tc_numbers\\tc_images";
        File[] files = FactoryUtils.getFileListInFolderForImages(path);
        String[] names = FactoryUtils.readFromFileAsString1D("C:\\ai\\djl\\tc_numbers\\tc.txt");
        int i = 0;
        for (File file : files) {
            System.out.println("file_name:" + file.getAbsolutePath());
            System.out.println("names = " + names[i]);
            FactoryUtils.renameFile(file.getAbsolutePath(), path + "\\" + names[i] + ".jpeg");
            i++;
        }
    }

    private static void extract_tc_numbers() {
        File[] files = FactoryUtils.getFileListInFolderForImages(path);
        System.out.println("files = " + files.length);
        for (int i = 5; i < files.length; i++) {
            //System.out.println("name:"+files[i].getName());
            if (path.contains("female")) {
                CMatrix cm = CMatrix.getInstance()
                        .imread(files[i].getAbsolutePath())
                        //.imresize(2658, 1480)
                        .cmd("0:30", "217:387")
                        .imshow(files[i].getName())
                        .imresize(20) //.imshow()
                        ;

                CMatrix[] numbers = extractNumbers(cm, 0);
            } else {
                CMatrix cm = CMatrix.getInstance()
                        .imread(files[i].getAbsolutePath())
                        //.imresize(2658, 1480)
                        .cmd("0:30", "220:390")
                        .imshow(files[i].getName())
                        .imresize(20) //.imshow()
                        ;

                CMatrix[] numbers = extractNumbers(cm, 60);
            }

        }
    }

    private static void extract_gender_char() {
        File[] files = FactoryUtils.getFileListInFolderForImages(path);
        System.out.println("files = " + files.length);
        for (int i = 0; i < files.length; i++) {
            CMatrix cm = CMatrix.getInstance()
                    .imread(files[i].getAbsolutePath())
                    .cmd("0:30", "130:160")
                    .imshow(files[i].getName())
                    .imresize(20)
                    .imresize(224, 224).imsave("C:\\ai\\djl\\tc_numbers\\tc_images\\dataset\\gender\\female\\" + i + ".jpg");
            ;

            //CMatrix[] numbers = extractNumbers(cm, 0);
        }
    }

    private static String extractModelName(String path) {
        String modelName = "";
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            char current;
            int k = 0;
            int val = 0;
            while (fis.available() > 0 && k < 30) {
                val = fis.read();//System.out.print(val);
                if (k++ < 10) {
                    continue;
                }
                if (val == 0) {
                    break;
                }
                current = (char) val;
                modelName += current;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return modelName;
    }
}
