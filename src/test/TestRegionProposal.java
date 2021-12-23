/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.matrix.CMatrix;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DELL LAB
 */
public class TestRegionProposal {

    public static void main(String[] args) {
        
        CMatrix cm2 = CMatrix.getInstance().imread("images/dog_1.png").imshow().rgb2gray()
//                .cmd("0:60","75:148")
                .edgeDetectionCanny()
                .imshow();
        System.out.println("total:"+cm2.sumTotal()/(cm2.getRowNumber()*cm2.getColumnNumber()*255)*100);
        
//        CMatrix cm = CMatrix.getInstance()
//                .rand(300, 300)
//                .map(170, 190)
//                .drawLine(10, 10, 100, 50, 3, Color.WHITE)
//                .imshow()
//                .edgeDetectionCanny()
//                .imshow();
//        List<CMatrix> lst = new ArrayList();
//        lst.add(cm.cmd("0:150", "0:150").imshow());
//        lst.add(cm.cmd("150:299", "0:150").imshow());
//        lst.add(cm.cmd("0:150", "150:299").imshow());
//        lst.add(cm.cmd("150:299", "150:299").imshow());
//        double thr = 10;
//        List<String> dizi = new ArrayList();
//        for (int i = 0; i < lst.size(); i++) {
//            if (lst.get(i).sumTotal() > thr) {
//                dizi.add(i+":" + lst.get(i).sumTotal());
//            }
//        }
//
//        int a=3;
    }
}
