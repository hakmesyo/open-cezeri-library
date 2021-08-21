/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.interfaces;

import cezeri.enums.EnumBackEnd;
import cezeri.enums.EnumDataSource;
import cezeri.enums.EnumDatasetType;
import cezeri.enums.EnumDevelopmentPlatform;
import cezeri.enums.EnumInferenceType;
import cezeri.enums.EnumLearningMode;
import cezeri.enums.EnumLearningType;
import cezeri.factory.FactoryUtils;


/**
 *
 * @author DELL LAB
 */
public abstract class Configuration implements InterfaceConfiguration{
    private EnumBackEnd backEnd=EnumBackEnd.CPU;
    private EnumDataSource dataSource=EnumDataSource.CAMERA;
    private EnumLearningMode learningMode=EnumLearningMode.TEST;
    private EnumLearningType learningType=EnumLearningType.CLASSIFICATION;
    private EnumDevelopmentPlatform language=EnumDevelopmentPlatform.PYTHON;
    private EnumDatasetType dataSetType=EnumDatasetType.IMAGE;
    private EnumInferenceType responseType=EnumInferenceType.REAL_TIME;
    private String taskName="";
    private String modelPath="";
    private InterfaceCallBack call_back=null;
    private String[] classLabels=null;
    private String testFolderPath="";
    
    public Configuration setCallBackFunction(InterfaceCallBack cb) {
        call_back=cb;
        return this;
    }
    
    public Configuration setProgrammingLanguage(EnumDevelopmentPlatform pl){
        language=pl;
        return this;
    }
    
    public Configuration setClassLabels(String[] cl){
        classLabels=FactoryUtils.clone(cl);
        return this;
    }
    
    public Configuration setTestFolderPath(String str){
        testFolderPath=str;
        return this;
    }
    
    public Configuration setTaskName(String str){
        taskName=str;
        return this;
    }
    
    public Configuration setBackEnd(EnumBackEnd be){
        backEnd=be;
        return this;
    }

    public Configuration setDataSource(EnumDataSource ds){
        dataSource=ds;
        return this;
    }

    public Configuration setDatasetType(EnumDatasetType dst){
        dataSetType=dst;
        return this;
    }
    
    public Configuration setLearningMode(EnumLearningMode lm){
        learningMode=lm;
        return this;
    }
    
    public Configuration setModelPath(String path){
        modelPath=path;
        return this;
    }
    
    public Configuration setLearningType(EnumLearningType lt){
        learningType=lt;
        return this;
    }

    public EnumBackEnd getBackEnd() {
        return backEnd;
    }

    public EnumDataSource getDataSource() {
        return dataSource;
    }

    public EnumLearningMode getLearningMode() {
        return learningMode;
    }

    public EnumLearningType getLearningType() {
        return learningType;
    }

    public EnumDevelopmentPlatform getLanguage() {
        return language;
    }

    public EnumDatasetType getDataSetType() {
        return dataSetType;
    }

    public EnumInferenceType getResponseType() {
        return responseType;
    }

    public String getModelPath() {
        return modelPath;
    }
    
    public String getTaskName(){
        return taskName;
    }

    public InterfaceCallBack getCall_back() {
        return call_back;
    }

    public String[] getClassLabels() {
        return classLabels;
    }

    public String getTestFolderPath() {
        return testFolderPath;
    }
    
    
    
}
