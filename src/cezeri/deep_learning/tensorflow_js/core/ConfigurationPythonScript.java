/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.deep_learning.tensorflow_js.core;

import cezeri.deep_learning.tensorflow_js.enums.EnumDevelopmentPlatform;
import cezeri.deep_learning.tensorflow_js.interfaces.Configuration;


/**
 *
 * @author DELL LAB
 */
public class ConfigurationPythonScript extends Configuration {

    public ConfigurationPythonScript() {
        setProgrammingLanguage(EnumDevelopmentPlatform.PYTHON);
    }
}
