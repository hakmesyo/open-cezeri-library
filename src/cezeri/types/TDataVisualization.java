/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.types;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public abstract class TDataVisualization {    
    protected CMatrix jm = null;
    protected String type = "";
    protected String main_title="";
    protected String x_axis_title="";
    protected String y_axis_title="";
    protected double opacity=1.0;
    protected boolean isMainTitleVisible=false;
    protected boolean isXAxisTitleVisible=false;
    protected boolean isYAxisTitleVisible=false;
    protected boolean isGrid=false;
    protected boolean isGroupNameVisible=false;
    protected boolean isHeatColorRulerVisible=false;
    
    public TDataVisualization addMainTitle(String title){
        isMainTitleVisible=true;
        main_title=title;
        return this;
    }
    
    public TDataVisualization addXTitle(String title){
        isXAxisTitleVisible=true;
        x_axis_title=title;
        return this;
    }
    
    public TDataVisualization addYTitle(String title){
        isYAxisTitleVisible=true;
        y_axis_title=title;
        return this;
    }
    
    public abstract CMatrix buildAndShow();
    
}

