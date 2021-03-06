package com.juslin.ui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Demonstrates fine-tuned window
 * @author Jukka Juslin
 *
 */
public class ExampleFrame2 extends JFrame {

	public ExampleFrame2() {
		setSize(300, 300);
		setLocation(800, 400);
		setTitle("Clash of Clans");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon("images/icon.png");
		setIconImage(img.getImage());

	}

	public static void main(String[] args) {
		ExampleFrame2 frame = new ExampleFrame2();
		frame.setVisible(true);
	}
}

