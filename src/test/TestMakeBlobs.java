/*
 * The MIT License
 *
 * Copyright 2019 DELL LAB.
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
import cezeri.types.TFigureAttribute;

/**
 *
 * @author DELL LAB
 */
public class TestMakeBlobs {

    public static void main(String[] args) {

        CMatrix jm = CMatrix.getInstance()
                //                .make_blobs(300, 3, 5)
                .make_blobs(300, 7, 5);

        jm.scatterBlob();

        jm.scatterBlob("3,5", new TFigureAttribute("Python Bloblarından",
                "Different Blobs having various means and variances",
                "F-3,F-5",
                "Class-1,Class-2,Class-3,Class-4,Class-5"))
                .savePlot("images/blob.png")
                .saveImageAs("images/blob.png","images/blob.svg")
                ;
//        jm.scatterBlob("0,2", new TFigureAttribute("Python Bloblarından",
//                "Different Blobs having various means and variances",
//                "F-1,F-2",
//                "Class-1,Class-2,Class-3,Class-4,Class-5"));

    }
}
