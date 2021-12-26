/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author BAP1
 */
public class TestAssert {
/**
 * For regular Apps and Swing Apps:

Right click on the project in the Project Explorer
Choose Properties (at the bottom of pop up menu)
Choose Run (under Categories)
Set the VM Options field to include -enableassertions
Click [OK]
 * @param args 
 */
    public static void main(String[] args) {
        int b = 110;
        int c = 0;
        //int a = divide(b, c);
        //System.out.println("a:" + a);
        doSomething(null);
    }

    private static int divide(int b, int c) {
        assert (c == 0);
        return b / c;
    }

    /**
     * Assertions are a development-phase tool to catch bugs in your code. 
     * They're designed to be easily removed, so they won't exist in production code. 
     * So assertions are not part of the "solution" that you deliver to the customer. 
     * They're internal checks to make sure that the assumptions you're making are correct. 
     * The most common example is to test for null. Many methods are written like this:
     * 
     * Very often in a method like this, the widget should simply never be null. 
     * So if it's null, there's a bug in your code somewhere that you need to track down. 
     * But the code above will never tell you this. So in a well-intentioned effort to write 
     * "safe" code, you're also hiding a bug. It's much better to write code like this:
     * @param Widget widget Should never be null
     */
    static void doSomething(String widget) {
        assert widget != null;
        //widget.toCharArray(); // ...
        // do more stuff with this widget
    }
}
