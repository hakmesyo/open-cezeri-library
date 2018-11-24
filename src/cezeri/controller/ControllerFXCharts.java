/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.controller;

import cezeri.gui.FXCharts;
import cezeri.matrix.CMatrix;
import javafx.application.Application;

/**
 *
 * @author BAP1
 */
public class ControllerFXCharts {

    private final CMatrix cmatrix;

    public ControllerFXCharts(CMatrix cm) {        
        cmatrix = cm;
        cmatrix.runOnce();
    }

    public void show() {
        FXCharts.setCMatrix(cmatrix);
    }
    
    public void show(String title) {
        FXCharts.setCMatrix(cmatrix,title);
    }
    
    public void show(String title,String xAxisLabel,String yAxisLabel) {
        FXCharts.setCMatrix(cmatrix,title,xAxisLabel,yAxisLabel);
    }
}
