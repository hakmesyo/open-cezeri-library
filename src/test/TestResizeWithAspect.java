/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.io.File;

/**
 *
 * @author DELL LAB
 */
public class TestResizeWithAspect {

    public static void main(String[] args) {
        resizeWithAspectRatio();
    }

    private static void resizeWithAspectRatio() {
//        String source="C:\\python_data\\dataset\\gatenet\\datasets\\cats_and_dogs\\test\\cats";
//        String dest="C:\\python_data\\dataset\\gatenet\\datasets\\cats_and_dogs_224\\test\\cats";
//        String source="D:\\Dropbox\\PythonProjects\\GateNET\\simple_images\\ters_lale";
//        String dest="D:\\Dropbox\\PythonProjects\\GateNET\\simple_images\\ters_lale_224";
        String source = "C:\\Users\\cezerilab\\Desktop\\pollens";
        String dest = "C:\\Users\\cezerilab\\Desktop\\pollens_224";
        CMatrix cm = CMatrix.getInstance();
        File[] dirs = FactoryUtils.getDirectories(source);
        for (File dir : dirs) {
            FactoryUtils.makeDirectory(dest+"/"+dir.getName());
            File[] files = FactoryUtils.getFileListInFolder(dir.getAbsolutePath());            
            for (File file : files) {
                try {
                   cm.imread(file).imresizeWithAspectRatio(224).imsave(dest+'/'+dir.getName(), file.getName()); 
                } catch (Exception e) {
                    System.out.println("");
                }
                
            }

        }
    }
}
