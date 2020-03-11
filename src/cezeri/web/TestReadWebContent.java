/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author BAP1
 */
public class TestReadWebContent {

    public static void main(String[] args) {
        try {
            org.jsoup.nodes.Document document = Jsoup.connect("http://localhost:8080/").get();
            
            Element elms = document.getElementById("prediction-container");
            System.out.println("elms = " + elms);
        } catch (IOException ex) {
            Logger.getLogger(TestReadWebContent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
