/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.types.TFigureAttribute;
import cezeri.matrix.CMatrix;
import cezeri.types.TPointType;
import cezeri.factory.FactoryStroke;
import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 *
 * @author BAP1
 */
public class TestPlotFigureAttribute {

    public static void main(String[] args) {
//        CMatrix cm1=CMatrix.getInstance().linspace(-30,30,60).gaussmf(1.2,10);
        TFigureAttribute a = new TFigureAttribute();
        a.axis = new String[]{"Frequency", "Return Losss"};
        a.figureCaption = "Optimum Resonans Figure";
        a.items = new String[]{"patch10", "patch20", "patch30"};
        a.title = "Resonans";
//        a.pointType = TPointType.LINE;
        a.pointType = "-*";
        a.isStroke = true;
        Stroke s = new BasicStroke(2.0f, // Width
                BasicStroke.CAP_ROUND, // End cap
                BasicStroke.JOIN_ROUND, // Join style
                10.0f, // Miter limit
                new float[]{5,3,1,3}, // Dash pattern
//                new float[]{16.0f, 20.0f}, // Dash pattern
                0.0f);                     // Dash phase

        a.stroke.add(FactoryStroke.getDashedWithDotStroke());
        BasicStroke bs2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        a.stroke.add(bs2);
        CMatrix.getInstance().rand(100, 2).plot(a);
//        CMatrix cm2=cm1.sin();
//        CMatrix cm3=cm1.cat(2, cm2);
//        cm3.println().transpose().plot();
    }

}
