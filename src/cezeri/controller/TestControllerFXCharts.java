/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.controller;

import cezeri.matrix.CMatrix;

/**
 *
 * @author BAP1
 */
public class TestControllerFXCharts {

    public static void main(String[] args) {
        ControllerFXCharts obj = new ControllerFXCharts(CMatrix.getInstance().rand(20, 5, -50, 100));
        obj.show();
        System.out.println("merhaba");
    }
}
