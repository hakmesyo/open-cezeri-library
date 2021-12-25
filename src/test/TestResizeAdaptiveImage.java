/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author cezerilab
 */
public class TestResizeAdaptiveImage {

    public static void main(String[] args) {
//        String sourcePath = "C:\\Users\\cezerilab\\Downloads\\d_female";
//        String destPath = "C:\\Users\\cezerilab\\Downloads\\ds\\dfemale";
//        resizeWithAspectRatio(sourcePath, destPath);
//        resize(sourcePath,destPath);
        
        String path = "C:\\Users\\cezerilab\\Downloads\\ds";
        FactoryUtils.splitTrainTestFolder(path, 0.8, 0.2);
    }

    private static void resizeWithAspectRatio(String sourcePath, String destPath) {
        FactoryUtils.makeDirectory(destPath);
        File[] imgs = FactoryUtils.getFileListInFolderForImages(sourcePath);
        Collections.shuffle(Arrays.asList(imgs));
        for (File img : imgs) {
            CMatrix cm = CMatrix.getInstance()
                    .imread(img)
                    .imresizeWithAspectRatio(224)
                    //.imshow()
                    .imsave(destPath, img.getName());
        }
    }

    private static void resize(String sourcePath, String destPath) {
        FactoryUtils.makeDirectory(destPath);
        File[] imgs = FactoryUtils.getFileListInFolderForImages(sourcePath);
        Collections.shuffle(Arrays.asList(imgs));
        for (File img : imgs) {
            CMatrix cm = CMatrix.getInstance()
                    .imread(img)
                    .imresize(224,224)
                    //.imshow()
                    .imsave(destPath, img.getName());
        }
    }
}
