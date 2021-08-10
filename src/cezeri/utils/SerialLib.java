/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 *
 * @author DELL LAB
 */
public class SerialLib {
    private SerialPortEventListener obj=null;
    
    public SerialLib(SerialPortEventListener obj){
        this.obj=obj;
    }
    
    public SerialType serialInitialize(SerialPort sp, String portName,int rate) {
        BufferedReader input=null;
        OutputStream output=null;
        int TIME_OUT = 2000;
        int DATA_RATE = rate;
        // the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
//                System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            if (currPortId.getName().equals(portName)) {
                portId = currPortId;
                break;
            }
        }
        if (portId == null) {
            System.out.println("Could not find " + portName + " COM port.");
            return null;
        }

        try {
            // open serial port, and use class name for the appName.
            sp = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

            // set port parameters
            sp.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            //sonradan eklendi
//            sp.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
//                    | SerialPort.FLOWCONTROL_RTSCTS_OUT);
//            sp.setRTS(true);
            /////////////////////////////////////////////////////
            // open the streams
            input = new BufferedReader(new InputStreamReader(sp.getInputStream()));
            output = sp.getOutputStream();
            output.write("A".getBytes());
            // add event listeners
            sp.addEventListener(obj);
            sp.notifyOnDataAvailable(true);

            System.out.println(portName + " Serial port was initialized");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        SerialType ret=new SerialType();
        ret.input=input;
        ret.output=output;
        ret.serialPort=sp;
        return ret;
    }
    
    
}
