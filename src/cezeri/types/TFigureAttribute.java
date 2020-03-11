/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cezeri.types;

import cezeri.factory.FactoryMatrix;
import cezeri.factory.FactoryUtils;
import java.awt.Stroke;
import java.util.ArrayList;

/**
 *
 * @author BAP1
 */
public class TFigureAttribute {
    public String figureCaption="";
    public String title="";
    public String[] axis=new String[]{"Indexes","Values"};
//    public String[] items=new String[]{"Observed","Simulated"};;
    public String[] items=new String[]{"",""};;
//    public TPointType pointType=TPointType.LINE;
    public String pointType="-";
    public ArrayList<String> perfMetricStr=new ArrayList();
    public ArrayList<Double> perfMetricVal=new ArrayList();
    public boolean isStroke=false;
    public ArrayList<Stroke> stroke=new ArrayList<>();
    
    public TFigureAttribute clone(){
        TFigureAttribute ret=new TFigureAttribute();
        ret.figureCaption=this.figureCaption;
        ret.title=this.title;
        ret.isStroke=this.isStroke;
        ret.stroke=this.stroke;
        ret.axis=FactoryMatrix.clone(this.axis);
        ret.items=FactoryMatrix.clone(this.items);
        ret.perfMetricStr=(ArrayList<String>)this.perfMetricStr.clone();
        ret.perfMetricVal=(ArrayList<Double>)this.perfMetricVal.clone();
        return ret;
    }
}
