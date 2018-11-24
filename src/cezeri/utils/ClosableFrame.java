/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.utils;

 

import java.awt.*;
import java.awt.event.*;

/**
 * <code>ClosableFrame</code> is a base class that obeys the 
 * <code>windowClosing </code> event. It is intended that the
 * student extend this class instead of <code>Frame</code>.
 *
 * @see   java.awt.Frame
 * @see   java.awt.event.WindowListener
 *
 * @author M. Dennis Mickunas
 */
public class ClosableFrame extends Frame implements WindowListener {

  /**
   * Class constructor, creating a frame that can respond to the 
   * "window closing" event; the frame will have a blank title.
   */
  public ClosableFrame () {
    this("");
  }
  
  /**
   * Class constructor, creating a frame that can respond to the 
   * "window closing" event; the frame will have a specific title.
   *
   * @param  title    the specific title for this frame.
   */
  public ClosableFrame (String title) {
    super(title);
    addWindowListener(this);
  }
 
  /**
   * Cleans up the window, and terminates the program. 
   *
   * @param  e    the specific <code>WindowEvent</code> that occurred.
   */
  public void windowClosing (WindowEvent e) {
    dispose();
//    System.exit(0);
  }
  /**
   * Vacuous implementation.
   */
  public void windowActivated (WindowEvent e) {}
  /**
   * Vacuous implementation.
   */
  public void windowClosed (WindowEvent e) {}
  /**
   * Vacuous implementation.
   */
  public void windowOpened (WindowEvent e) {}
  /**
   * Vacuous implementation.
   */
  public void windowDeactivated (WindowEvent e) {}
  /**
   * Vacuous implementation.
   */
  public void windowDeiconified (WindowEvent e) {}
  /**
   * Vacuous implementation.
   */
  public void windowIconified (WindowEvent e) {}

}