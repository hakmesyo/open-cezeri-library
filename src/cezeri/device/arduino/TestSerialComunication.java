package cezeri.device.arduino;

import cezeri.utils.SerialLib;
import cezeri.utils.SerialType;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestSerialComunication implements SerialPortEventListener {

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

    public static void main(String[] args) {
        TestSerialComunication obj = new TestSerialComunication();
        obj.start("COM14");
//        try {
//            for (int i = 0; i < 2; i++) {
//                obj.sendDataToSerialPort("3:500-5:500-70:50");
//                Thread.sleep(500);
//            }
//        } catch (InterruptedException ex) {
//        }
//        
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(TestSerialComunication.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        obj.stop();
//        System.exit(0);
    }

    public void start(String comPort) {
        st = slib.serialInitialize(serialPort, comPort,115200);
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String inputLine = st.input.readLine();
                System.out.println("from arduino = " + inputLine);
//                if (inputLine.equals("9")) {
//                    System.out.println("received msg:" + inputLine);
//                }
            } catch (Exception e) {
                System.err.println("err:" + e.toString());
            }
        }
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
