/* 
Variational Mode Decomposition
--------------------------------------------------------
Authors: Konstantin Dragomiretskiy and Dominique Zosso
zosso@math.ucla.edu --- http://www.math.ucla.edu/~zosso
Initial release 2013-12-12 (c) 2013
---------------------------------------------------------
porting matlab to the java and ocl was implemented by 
Dr.Musa Ataş El-Cezeri Cybernetics and Robotics Lab 2017
 */
package cezeri.transform;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class VMD {

    public static void main(String[] args) {
        // Time Domain 0 to T
        double T = 1000;
        double fs = 1 / T;
        CMatrix t = CMatrix.getInstance().vector(1, T).divideScalar(T);
        CMatrix freqs = t.minusScalar(0.5 + 1 / T).timesScalar(2 * Math.PI).divideScalar(fs);
        //% center frequencies of components
        double f_1 = 2;
        double f_2 = 24;
        double f_3 = 288;

        //% modes
        CMatrix v_1 = t.timesScalar(2 * Math.PI * f_1).cos();
        CMatrix v_2 = t.timesScalar(2 * Math.PI * f_2).cos().timesScalar(1 / 4.0);
        CMatrix v_3 = t.timesScalar(2 * Math.PI * f_3).cos().timesScalar(1 / 16.0);

        //% composite signal, including noise
        CMatrix f = v_1.add(v_2).add(v_3).add(CMatrix.getInstance().randn(v_1.maxsize()).timesScalar(0.1)).plot();
        //f_hat = fftshift((fft(f)));

        //% some sample parameters for VMD
        double alpha = 2000;       // moderate bandwidth constraint
        double tau = 0;            // noise-tolerance (no strict fidelity enforcement)
        double K = 3;              // 3 modes
        double DC = 0;             // no DC part imposed
        double init = 1;           // initialize omegas uniformly
        double tol = 1e-7;

//figure;plot(f);
//%--------------- Run actual VMD code
//VMD(f, alpha, tau, K, DC, init, tol);
    }

}
