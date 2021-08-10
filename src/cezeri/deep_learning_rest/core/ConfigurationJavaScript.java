/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning_rest.core;

import cezeri.deep_learning_rest.enums.EnumDevelopmentPlatform;
import cezeri.deep_learning_rest.interfaces.Configuration;


/**
 *
 * @author DELL LAB
 */
public class ConfigurationJavaScript extends Configuration{
    public ConfigurationJavaScript(){
        setProgrammingLanguage(EnumDevelopmentPlatform.JAVA_SCRIPT);
    }
}
