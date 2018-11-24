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
public class TestArduinoCode_2 {
    
    //Aşağıdaki arduino kodunu arduino idesinede yeni açacağınız bir projeye kopyalayın ve kartınıza yükleyin
    /*
    String msg = "";
    int portIndex = 0;
    const int ledPin=13;
    int bekle=1000;
    int defa=1;

    void setup() {
        pinMode(13, OUTPUT);
        Serial.begin(9600);
    }

    void loop() {
//        msg = readSerialInFromPC();
//        if (!msg.equals("")) {
//            doSomething();
//        }
       String data = Serial.readStringUntil(':');
       if(data != ""){
         String s1=Serial.readStringUntil('-');
         defa=data.toInt();
         bekle=s1.toInt();
         blinkLed(defa,bekle);
         Serial.println(micros());
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
        int data = msg.toInt();
        Serial.println(data); 
        blinkLed(data,100);
        msg = "";
    }

    void blinkLed(int n,int d){
        for(int i=0;i<n;i++){
          digitalWrite(ledPin,1);
          delay(d);
          digitalWrite(ledPin,0);
          delay(d);
        }
    }    */

}
