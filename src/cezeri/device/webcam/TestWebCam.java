/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.webcam;

import cezeri.factory.FactoryWebCam;

/**
 *
 * @author cezerilab
 */
public class TestWebCam {
    public static void main(String[] args) {
        FactoryWebCam.openWebCam(0).startWebCAM();
        
    }
}
