/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.arduino;

/**
 *
 * @author BAP1
 */
public class TestArduinoCode {
    
    //Aşağıdaki arduino kodunu arduino idesinede yeni açacağınız bir projeye kopyalayın ve kartınıza yükleyin
    /*
    String msg = "";
    int portIndex = 0;

    void setup() {
        //pinMode(2,OUTPUT);
        pinMode(3, OUTPUT);//class 1
        pinMode(4, OUTPUT);//class 2
        pinMode(5, OUTPUT);//class 3
        pinMode(6, OUTPUT);//class 4
        pinMode(7, OUTPUT);//class 5
        pinMode(8, OUTPUT);//class 6
        pinMode(9, OUTPUT);//biip
        pinMode(10, OUTPUT);
        digitalWrite(9, LOW);
        digitalWrite(10, HIGH);
        Serial.begin(9600);
    }

    void loop() {
        msg = readSerialInFromPC();
        if (!msg.equals("")) {
            doSomething();
        }
    }

    String readSerialInFromPC() {
        String str = "";
        if (Serial.available() > 0) {
            char letter = Serial.read();
            str += letter;
        }
        return str;
    }

    void doSomething() {
        int port = msg.toInt();
        //Serial.println(port); 
        if (port == 9) {
            digitalWrite(9, HIGH);
            delay(100);
            digitalWrite(9, LOW);
            Serial.print("merhaba from arduino:");
            Serial.println(port);
            delay(100);
        }
        msg = "";
    }
    */

}
