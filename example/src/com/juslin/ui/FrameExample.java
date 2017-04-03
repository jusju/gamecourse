package com.juslin.ui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class FrameExample extends JFrame {

	public FrameExample() {
		JFrame testWindow = new JFrame();
		testWindow.setSize(300, 300);
		testWindow.setLocation(800, 400);
		testWindow.setTitle("Clash of Clans");
		testWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon("images/icon.png");
		testWindow.setIconImage(img.getImage());
		testWindow.setVisible(true);
	}
	
	public void setComponents() {
	   
	}
	
	public static void main(String[] args) {
		FrameExample frame = new FrameExample();
	}
}

