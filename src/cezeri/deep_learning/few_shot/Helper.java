/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.few_shot;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import java.io.File;

/**
 *
 * @author cezerilab
 */
public class Helper {

    public static void main(String[] args) {
//        String path="C:\\Users\\cezerilab\\Desktop\\GateNET\\ds\\planes\\";
//        File[] dirs=FactoryUtils.getDirectories(path+"\\train");
//        for (File d : dirs) {
//            FactoryUtils.makeDirectory(path+"\\all\\"+d.getName());
//            File[] images=FactoryUtils.getFileListInFolderForImages(d.getAbsolutePath());
//            for (File f : images) {
//                FactoryUtils.copyFile(f, new File(path+"\\all\\"+d.getName()+"\\"+System.nanoTime()+"."+FactoryUtils.getFileExtension(f)));
//            }
//        }

//        String path="C:\\Users\\cezerilab\\Desktop\\GateNET\\dogs";
//        File[] images=FactoryUtils.getFileListInFolderForImages(path);
//        for (File f : images) {
//            CMatrix cm = CMatrix.getInstance().imread(f).imresizeWithAspectRatio(224).imsave(f.getAbsolutePath());
//        }


//        String path = "C:\\python_data\\dataset\\gatenet\\datasets\\plants\\valid";
//        String path2 = "C:\\python_data\\dataset\\gatenet\\datasets\\plants\\valid\\transformed_images";
//        File[] dirs = FactoryUtils.getDirectories(path);
//        int k=0;
//        for (File dir : dirs) {
//            File[] images = FactoryUtils.getFileListInFolderForImages(path+"\\"+dir.getName());
//            FactoryUtils.makeDirectory(path2+"\\"+dir.getName());
//            for (File f : images) {                
//                CMatrix cm = CMatrix.getInstance().imread(f).imresizeWithAspectRatio(224).imsave(path2+"\\"+dir.getName()+"\\"+f.getName());
////                if (FactoryUtils.getFileExtension(f).equals("jpeg")||FactoryUtils.getFileExtension(f).equals("JPEG")) {
////                    //CMatrix cm = CMatrix.getInstance().imread(f).imshow().imsave(f.getParent()+"\\"+FactoryUtils.getFileName(f.getName())+".jpg");
////                    FactoryUtils.deleteFile(f);
////                    int a=3;
////                }
//            }
//            System.out.println((k++)+".dir name:"+dir.getName());
//
//        }

        String path="C:\\python_data\\dataset\\gatenet\\datasets\\plants_224\\fat_model\\test";
        String path2="C:\\python_data\\dataset\\gatenet\\datasets\\plants_224\\temp3\\test";
        File[] dirs = FactoryUtils.getDirectories(path);
        for (File dir : dirs) {
            FactoryUtils.copyDirectory(dir, new File(path2+"\\"+dir.getName()));
//            FactoryUtils.makeDirectory(path2+"\\"+dir.getName());
//            File[] images = FactoryUtils.getFileListInFolderForImages(path+"\\"+dir.getName());
//            for (File img : images) {
//                FactoryUtils.copyFile(img.getAbsoluteFile(),new File(path2+"\\"+dir.getName()+"\\"+img.getName()));
//            }
            
        }
        
        
        
        
    }
}
