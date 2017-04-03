package com.juslin.ui;

import javax.swing.JFrame;

/**
 * Demonstrates graphical window
 * @author Jukka Juslin
 */
public class ExampleFrame1 extends JFrame {

   public ExampleFrame1() {
      setSize(300, 300);
   }

   public static void main(String[] args) {
      ExampleFrame1 frame = new ExampleFrame1();
      frame.setVisible(true);
   }
}
