/*
 * The MIT License
 *
 * Copyright 2018 BAP1.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package test;

import cezeri.matrix.CMatrix;


/**
 *
 * @author BAP1
 */
public class TestMeshGrid {

    public static void include(){
        
    }
    
    public static void main(String[] args) {
//        CMatrix mesh = CMatrix.getInstance()
//                //                .zeros(9)
//                //                .meshGridX(-3, 3)
//                //                .dump()
//                //                .meshGridY(-4, 4)
//                //                .dump()
//                //                .meshGridX(-4,4,11)
//                //                .dump()
//                //                .meshGrid(-5, 5, 111)
//                .meshGrid(0, 720, 180);
        int max=180;
        double beta=0.01;
        int size=150;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < max; j++) {
                CMatrix m1 = CMatrix.getInstance().meshGrid(-max - j, max + j, size);
                m1.pow(2).add(m1.transpose().pow(2)).timesScalar(beta).toRadians().cos().map(0, 255).imshowRefresh().delay(10);
            }
            for (int j = max; j > 0; j--) {
                CMatrix m1 = CMatrix.getInstance().meshGrid(-max - j, max + j, size);
                m1.pow(2).add(m1.transpose().pow(2)).timesScalar(beta).toRadians().cos().map(0, 255).imshowRefresh().delay(10);
            }
        }
    }
}
