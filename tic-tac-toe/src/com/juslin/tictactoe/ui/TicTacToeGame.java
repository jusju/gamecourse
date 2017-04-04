package com.juslin.tictactoe.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * First version of the game, which is third program of the material.
 * Demonstrates addition of a button element and programming convention.
 * 
 * @author Jukka Juslin
 *
 */
public class TicTacToeGame extends JFrame {

   // Button element. Convention in this material to
   // use prefixes to hint the type of the object.
   private JButton btLeftUpmost = new JButton("");

   public TicTacToeGame() {
      // The size of the window in pixels 
      // from left to right and up to down
      setSize(300, 300);
      // The location of the window on the 
      // computer screen of upper left corner
      // to righ and then down
      setLocation(300, 300);
      // Title to be displayed
      setTitle("Tic Tac Toe");
      // Closing X on right upper corner does 
      // not really work without this
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Reading image starting from path 
      // where project home is
      ImageIcon img = new ImageIcon("images/icon.png");
      setIconImage(img.getImage());
      // Convention used in this material to 
      // separate graphical creation and
      // layout to separate method
      setComponents();
   }

   public void setComponents() {
      // Container, which is needed 
      // in Swing to draw this way
      Container content = this.getContentPane();
      // Easiest to manage layout system
      content.setLayout(null);
      // A way to setup location and size 
      // of the item with one line in pixels
      btLeftUpmost.setBounds(0, 0, 100, 100);
      // Added to the content
      content.add(btLeftUpmost);
      // Connecting action listener to button
      btLeftUpmost.addActionListener(new AlsUpperLeft());
   }

   public static void main(String[] args) {
      // Create object of the same class - 
      // needed to get out from static context
      // in which Java programs should/could 
      // never be by default developer since
      // this is object oriented programming
      TicTacToeGame frame = new TicTacToeGame();
      // This method call is needed to really
      // show the frame
      frame.setVisible(true);
   }
   
   class AlsUpperLeft implements ActionListener {

      @Override
      public void actionPerformed(ActionEvent e) {
         System.out.println("TicTacToeGame.AlsUpperLeft.actionPerformed()");
         
      }
   }
}
