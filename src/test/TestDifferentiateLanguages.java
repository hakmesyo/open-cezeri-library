/*
This program is written for being the solution of the researchgate question
How to separate images based on language text using computer vision algorithms?
You have urdu and english text in images dataset and to classify it in their categories (urdu or english).

to do this first we download english-urdu translation table image from net
then we simply call CMatrix method

 */
package test;

import cezeri.matrix.CMatrix;

/**
 *
 * @author DELL LAB
 */
public class TestDifferentiateLanguages {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance()
                .imread("c:\\logo.png")
//                .imresize(2800, 640)
                .imshow()
                ;
//                .rgb2gray()
//                .binarizeOtsu()
//                .imnegative()
//                .imresize(128, 128)
//                .imshow("original urdu word list as image")
//                ;
    }
}
