/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.factory;

import cezeri.utils.SerialLib;
import cezeri.utils.SerialType;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;

/**
 *
 * @author cezerilab
 */
public class FactorySerialLib implements SerialPortEventListener {

    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {
        "/dev/tty.usbserial-A9007UX1", // Mac OS X
        "/dev/ttyACM0", // Raspberry Pi
        "/dev/ttyUSB0", // Linux
        "COM6", // Windows for solenoid valve
        "COM7" // 
    };
    private SerialPort serialPort;
    private SerialLib slib = new SerialLib(this);
    private SerialType st = new SerialType();

    @Override
    public void serialEvent(SerialPortEvent spe) {
        if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = st.input.readLine();
                System.out.println("received from serial socket = " + inputLine);
            } catch (Exception e) {
                System.err.println("err:" + e.toString());
            }
        }
    }

    public void start(String comPort) {
        st = slib.serialInitialize(serialPort, comPort,115200);
    }

    public void sendDataToSerialPort(String s1) {
        String s = s1 + "\n";
        try {
            st.output.write(s.getBytes());
            st.output.flush();
            System.out.println("sent message:"+s1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stop() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
            System.out.println("B.Serial Port was closed successfully");
        }
    }
    
}
