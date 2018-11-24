/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.classifiers;

import cezeri.classifiers.C_NaiveBayes.AttributeType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author BAP1
 */
public class NaiveBayesTest {

    static String train_p = "src\\cezeri\\classifiers\\tic-tac-toe_train.txt";
    static String test_p = "src\\cezeri\\classifiers\\tic-tac-toe_test.txt";
    static String p = "src\\cezeri\\classifiers\\tic-tac-toe_all.txt";

    public NaiveBayesTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class NaiveBayes.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        C_NaiveBayes.main(args);
    }

    /**
     * Test of evaluateModelTrainTest method, of class NaiveBayes.
     */
    @Test
    public void testEvaluateModelTrainTest() {
        System.out.println("evaluateModelTrainTest");
        String trainPath = train_p;
        String testPath = test_p;
        C_NaiveBayes.evaluateModelTrainTest(AttributeType.Categorical, trainPath, testPath);
        C_NaiveBayes.evaluateModelTrainTest(AttributeType.Real, trainPath, testPath);
    }

    /**
     * Test of evaluateModelCrossValidation method, of class NaiveBayes.
     */
    @Test
    public void testEvaluateModelCrossValidation() {
        System.out.println("evaluateModelCrossValidation");
        String path = p;
        int nFolds = 10;
        C_NaiveBayes.evaluateModelCrossValidation(AttributeType.Categorical, path, nFolds);
        C_NaiveBayes.evaluateModelCrossValidation(AttributeType.Real, path, nFolds);
    }

}
