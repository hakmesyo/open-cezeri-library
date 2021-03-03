/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Gpu;
import java.util.List;

/**
 *
 * @author cezerilab
 */
public class TestSensorsPC {
    public static void main(String[] args) {
        Components comp = JSensors.get.components();
        List<Gpu> lst=comp.gpus;
        for (Gpu gpu : lst) {
            System.out.println("gpu.name = " + gpu.name);
            System.out.println("gpu fans count= " + gpu.sensors.fans.size());
            //System.out.println("fan speed:"+gpu.sensors.fans.get(0).value);
        }
    }
}
