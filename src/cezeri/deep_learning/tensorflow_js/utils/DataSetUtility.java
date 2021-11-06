/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.utils;

import cezeri.factory.FactoryUtils;
import cezeri.image_processing.ImageProcess;
import cezeri.matrix.CMatrix;
import java.io.File;

/**
 *
 * @author DELL LAB
 */
public class DataSetUtility {

    public static void main(String[] args) {
        String[] folder_list = {"animals", "flowers", "fruits", "planes", "tools"};
        String path = "C:\\Users\\DELL LAB\\Desktop\\gate-net-ds";

        //prepareDataSet(folder_list,path);
        resizeImages(folder_list, path, 224);
    }

    private static void prepareDataSet(String[] folder_list, String path) {
        for (int i = 0; i < folder_list.length; i++) {
            String p = path + "\\" + folder_list[i];
            File[] sub_folders = FactoryUtils.getFolderListInFolder(p);
            FactoryUtils.makeDirectory(p + "\\train");
            FactoryUtils.makeDirectory(p + "\\test");
            for (File sf : sub_folders) {
                File[] f = FactoryUtils.getFileListInFolderForImages(sf.getAbsolutePath());
                FactoryUtils.makeDirectory(p + "\\train\\" + sf.getName());
                FactoryUtils.makeDirectory(p + "\\test\\" + sf.getName());
                int n = f.length;
                int n_train = (int) (n * 0.8);
                for (int j = 0; j < n_train; j++) {
                    FactoryUtils.copyFile(f[j], new File(p + "\\train\\" + sf.getName() + "\\" + j + "." + FactoryUtils.getFileExtension(f[j])));
                }
                int n_test = n - n_train;
                for (int j = 0; j < n_test; j++) {
                    FactoryUtils.copyFile(f[n_train + j], new File(p + "\\test\\" + sf.getName() + "\\" + j + "." + FactoryUtils.getFileExtension(f[n_train + j])));
                }
            }
            System.out.println("ok");
        }

    }

    private static void resizeImages(String[] folder_list, String path, int size) {
        for (int i = 0; i < folder_list.length; i++) {
            String p = path + "\\" + folder_list[i];
            File[] fld = FactoryUtils.getFolderListInFolder(p + "\\test");
            for (File d : fld) {
//                if (d.getName().equals("rocket")) {
                    File[] fs = FactoryUtils.getFileListInFolderForImages(d.getAbsolutePath());
                    for (File f : fs) {
                        CMatrix cm = CMatrix.getInstance()
                                .imread(f)
                                //.imshow()
                                .imresizeWithAspectRatio(size)
                                //.imshow()
                                .imsave(f.getAbsolutePath());
                        System.out.println(d.getName() + " folder " + f.getName());
                    }

//                }

            }

        }

    }
}
