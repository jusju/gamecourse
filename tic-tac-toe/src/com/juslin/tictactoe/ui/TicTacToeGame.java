package com.juslin.tictactoe.ui;

import java.awt.Container;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class TicTacToeGame extends JFrame {

   private JButton btLeftUpmost = new JButton("");
   
   public TicTacToeGame() {
      setSize(300, 300);
      setLocation(300, 300);
      setTitle("Tic Tac Toe");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      ImageIcon img = new ImageIcon("images/icon.png");
      setIconImage(img.getImage());
      setComponents();
   }
   
   public void setComponents() {
      // Container, which is needed in Swing to draw this way
      Container content = this.getContentPane();
      // Easiest to manage layout system
      content.setLayout(null);
      btLeftUpmost.setBounds(0, 0, 100, 100);
      content.add(btLeftUpmost);
   }
   
   public static void main(String[] args) {
      TicTacToeGame frame = new TicTacToeGame();
      frame.setVisible(true);
   }
}
