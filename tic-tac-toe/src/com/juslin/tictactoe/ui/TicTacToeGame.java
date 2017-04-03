package com.juslin.tictactoe.ui;

import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class TicTacToeGame extends JFrame {

   private JButton btLeftUpmost = new JButton();
   
   public TicTacToeGame() {
      JFrame testWindow = new JFrame();
      testWindow.setSize(300, 300);
      testWindow.setLocation(800, 400);
      testWindow.setTitle("Tic Tac Toe");
      testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      ImageIcon img = new ImageIcon("images/icon.png");
      testWindow.setIconImage(img.getImage());
      setComponents();
      testWindow.setVisible(true);
   }
   
   public void setComponents() {
      // Container, which is needed in Swing to draw this way
      Container content = this.getContentPane();
      // Easiest to manage layout system
      content.setLayout(null);
      
   }
   
   public static void main(String[] args) {
      TicTacToeGame frame = new TicTacToeGame();
   }
}
