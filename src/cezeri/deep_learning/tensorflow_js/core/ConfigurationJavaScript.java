/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.core;

import cezeri.enums.EnumDevelopmentPlatform;
import cezeri.interfaces.Configuration;


/**
 *
 * @author DELL LAB
 */
public class ConfigurationJavaScript extends Configuration{
    public ConfigurationJavaScript(){
        setProgrammingLanguage(EnumDevelopmentPlatform.JAVA_SCRIPT);
    }
}
