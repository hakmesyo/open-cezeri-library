/*
•	Generate a 640 x 480 random matrix values between 0-255.
•	Show histogram
•	Show image
•	Convert matrix to one dimensional vector or column matrix
•	Add some jitter or noise on the matrix
•	Plot original and noised matrixes
•	Plot scatter diagram and show some similarity measures

 */
package cezeri.test;
import cezeri.matrix.CMatrix;

public class UseCaseMatrixOperations {
    public static void main(String[] args) {
        CMatrix cm = CMatrix.getInstance().rand(3840,2160,0,255).transpose().round()
                .tic()
//                .m_imhist()
                .imshow() 
                .imhist()
//                .println()
                .toc()
                ;
        
    }
}
