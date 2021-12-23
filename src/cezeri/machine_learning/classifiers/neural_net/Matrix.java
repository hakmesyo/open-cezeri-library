package cezeri.machine_learning.classifiers.neural_net;

import java.util.Random;

public class Matrix {

    public double[][] Multiplication(double[][] A, double[][] B){
        int m = A.length;
        int n = A[0].length;
        int p = B[0].length;
        double temp;
        double[][] M = new double[m][p];

        for(int i=0; i<m; i++){
            for(int j=0; j<p; j++){
                temp=0.0;

                for(int k=0; k<n; k++){
                    temp+=(A[i][k] * B[k][j]);
                }
                M[i][j] = temp;

            }
        }

        return M;
    }

    public double[][] Transpose(double[][] M){
        double[][] T = new double[M[0].length][M.length];
        for(int i=0; i<M.length; i++){
            for(int j=0; j<M[0].length; j++){
                T[j][i] = M[i][j];
            }
        }

        return T;
    }

    public double[][] correspondMultiplication(double[][] A, double[][] B){
        double[][] resultMatrix = new double[A.length][A[0].length];
        for(int i=0; i<A.length; i++){
            for(int j=0; j<A[0].length; j++){
                resultMatrix[i][j] = A[i][j] * B[i][j];
            }
        }

        return resultMatrix;
    }

    public double[][] logMatrix(double[][] A){
        double[][] temp_A = A;
        for(int i=0; i<temp_A.length; i++){
            for(int j=0; j<temp_A[0].length; j++){
                temp_A[i][j] = Math.log(temp_A[i][j]);
            }
        }
        return temp_A;
    }

    public double[][] scalarMultiply(double[][] A, double scalar){
        double[][] tempA = A;
        for(int i=0; i<tempA.length; i++){
            for(int j=0; j<tempA[0].length; j++){
                tempA[i][j]*=scalar;
            }
        }

        return tempA;
    }

    public double[][] correspondAddition(double[][] A, double[][] B){
        double[][] resultMatrix = new double[A.length][A[0].length];
        for(int i=0; i<A.length; i++){
            for(int j=0; j<A[0].length; j++){
                resultMatrix[i][j] = A[i][j] + B[i][j];
            }
        }

        return resultMatrix;
    }

    public double sumMatrix(double[][] A){
        double sum = 0.0;
        for(int i=0; i<A.length; i++){
            for(int j=0; j<A[0].length; j++){
                sum+=A[i][j];
            }
        }
        return sum;
    }

    public double[][] copyMatrix(double[][] A){
        double[][] temp = new double[A.length][A[0].length];
        for(int i=0; i<A.length; i++){
            for(int j=0; j<A[0].length; j++){
                temp[i][j] = A[i][j];
            }
        }
        return temp;
    }

}
