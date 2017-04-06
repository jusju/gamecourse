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
   private JButton btRightUpmost = new JButton("");
   private JButton btCenterUpmost = new JButton("");
   private JButton btLeftCenter = new JButton("");
   private JButton btCenterCenter = new JButton("");
   private JButton btRightCenter = new JButton("");
   private JButton btRightDownMost = new JButton("");
   private JButton btCenterDownMost = new JButton("");
   private JButton btLeftDownMost = new JButton("");
   private int numberOfChoicesMade = 0;

   public TicTacToeGame() {
      // The size of the window in pixels
      // from left to right and up to down
      setSize(320, 340);
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
      btRightUpmost.setBounds(200, 0, 100, 100);
      btCenterUpmost.setBounds(100, 0, 100, 100);
      btLeftCenter.setBounds(0, 100, 100, 100);
      btCenterCenter.setBounds(100, 100, 100, 100);
      btRightCenter.setBounds(200, 100, 100, 100);
      btRightDownMost.setBounds(0, 200, 100, 100);
      btCenterDownMost.setBounds(100, 200, 100, 100);
      btLeftDownMost.setBounds(200, 200, 100, 100);
      // Added to the content
      content.add(btLeftUpmost);
      content.add(btRightUpmost);
      content.add(btCenterUpmost);
      content.add(btLeftCenter);
      content.add(btCenterCenter);
      content.add(btRightCenter);
      content.add(btRightDownMost);
      content.add(btCenterDownMost);
      content.add(btLeftDownMost);
      // Connecting action listener to button
      btLeftUpmost.addActionListener(new AlsUpperLeft());
      btRightUpmost.addActionListener(new AlsUpperRight());
      btCenterUpmost.addActionListener(new AlsUpperCenter());
      btLeftCenter.addActionListener(new AlsCenterLeft());
      btCenterCenter.addActionListener(new AlsCenterCenter());
      btRightCenter.addActionListener(new AlsCenterRight());
      btRightDownMost.addActionListener(new AlsDownMostRight());
      btCenterDownMost.addActionListener(new AlsDownMostCenter());
      btLeftDownMost.addActionListener(new AlsDownMostLeft());
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
         if (numberOfChoicesMade < 3) {
            System.out.println("TicTacToeGame.AlsUpperLeft.actionPerformed()");
            btLeftUpmost.setText("X");
            numberOfChoicesMade++;
         }
      }
   }

   class AlsUpperRight implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (numberOfChoicesMade < 3) {
            btRightUpmost.setText("X");
            numberOfChoicesMade++;
         }
      }
   }

   class AlsUpperCenter implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (numberOfChoicesMade < 3) {
            btCenterUpmost.setText("X");
            numberOfChoicesMade++;
         }
      }
   }

   class AlsCenterLeft implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (numberOfChoicesMade < 3) {
            btLeftCenter.setText("X");
            numberOfChoicesMade++;
         }
      }
   }

   class AlsCenterCenter implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (numberOfChoicesMade < 3) {
            btCenterCenter.setText("X");
            numberOfChoicesMade++;
         }
      }
   }

   class AlsCenterRight implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (numberOfChoicesMade < 3) {
            btRightCenter.setText("X");
            numberOfChoicesMade++;
         }
      }
   }

   class AlsDownMostRight implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (numberOfChoicesMade < 3) {
            btRightDownMost.setText("X");
            numberOfChoicesMade++;
         }
      }
   }

   class AlsDownMostCenter implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (numberOfChoicesMade < 3) {
            btCenterDownMost.setText("X");
            numberOfChoicesMade++;
         }
      }
   }

   class AlsDownMostLeft implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         if (numberOfChoicesMade < 3) {
            btLeftDownMost.setText("X");
            numberOfChoicesMade++;
         }
      }
   }
}
