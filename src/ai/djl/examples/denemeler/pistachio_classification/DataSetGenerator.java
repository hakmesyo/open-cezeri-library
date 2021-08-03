/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.djl.examples.denemeler.pistachio_classification;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.io.File;

/**
 *
 * @author cezerilab
 */
public class DataSetGenerator {

    public static void main(String[] args) {
        String path="C:\\ai\\djl\\pistachio_test\\close\\";
        File[] files=FactoryUtils.getFileListInFolderForImages(path);
        for (File file : files) {
            String f=path+System.nanoTime()+".jpg";
            FactoryUtils.renameFile(file.getAbsolutePath(),f);
        }
//        CMatrix cm = CMatrix.getInstance()
//                .tic()
//                .imread("")
//                .rgb2gray()
//                .getWeightCenteredROI(30, 23, 1)
//                .imshow()
//                ;

    }
}
