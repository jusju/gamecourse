package com.juslin.tictactoe.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.xml.sax.SAXException;

import com.juslin.tictactoe.helper.SimpleSound;
import com.juslin.tictactoe.helper.Sound;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

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
	private JButton btPelaaUusi = new JButton("New Game");
	private JButton btHighScores = new JButton("High Scores");
	private int numberOfChoicesMade = 0;
	private int[][] matrix = new int[3][3];
	private int debuggerHelper = 0;

	public TicTacToeGame() {
		// The size of the window in pixels
		// from left to right and up to down
		setSize(320, 450);
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
		btRightDownMost.setBounds(200, 200, 100, 100);
		btCenterDownMost.setBounds(100, 200, 100, 100);
		btLeftDownMost.setBounds(0, 200, 100, 100);
		btHighScores.setBounds(100, 380, 150, 30);
		btPelaaUusi.setBounds(0, 380, 100, 30);
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
		content.add(btPelaaUusi);
		content.add(btHighScores);
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
		
		btPelaaUusi.addActionListener(new AlsNewGame());
		btHighScores.addActionListener(new AlsHighScores());
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

	public void doComputerAction() {
		Random random = new Random();
		boolean currentXOkay = false;
		boolean currentYOkay = false;
		int randomNumberX = 0;
		int randomNumberY = 0;
		do {
			currentXOkay = false;
			currentYOkay = false;
			randomNumberX = random.nextInt(3);
			randomNumberY = random.nextInt(3);
			if (isFreeSlot(randomNumberX, randomNumberY)) {
				currentXOkay = true;
				currentYOkay = true;
			}
		} while (currentXOkay != true && currentYOkay != true);
		System.out.println("P‰‰stiin ulos do-whilesta!!!");
		boolean weAreReady = false;
		if (debuggerHelper > 0) {
			System.out.println("Tee t‰‰");
		}
		while (weAreReady != true) {
			if(numberOfChoicesMade == 2) {
				System.out.println("Now we debug");
			}
			for (int i = 0; i < 3; i++) {
				if (weAreReady != true) {
					for (int j = 0; j < 3; j++) {
						if (matrix[i][j] != 1 && matrix[i][j] != 2) {
							if (i == 0 && j == 0) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println("eka");
									btLeftUpmost.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							} else if (i == 1 && j == 0) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println("toka");
									btCenterUpmost.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							} else if (i == 2 && j == 0) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println("kolmas");
									btRightUpmost.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							} else if (i == 0 && j == 1) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println(matrix[0][1]);
									System.out.println("neljas");
									btLeftCenter.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							} else if (i == 1 && j == 1) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println("viides");
									btCenterCenter.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							} else if (i == 2 && j == 1) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println("kuudes");
									btRightCenter.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							} else if (i == 2 && j == 0) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println("seitsemas");
									btLeftDownMost.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							} else if (i == 2 && j == 1) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println("kahdeksas");
									btCenterDownMost.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							} else if (i == 2 && j == 2) {
								if (matrix[i][j] != 1 && matrix[i][j] != 2) {
									System.out.println("yhdeksas");
									btRightDownMost.setText("O");
									matrix[i][j] = 2;
									weAreReady = true;
								}
								break;
							}
						}
					}
				}
			}
		}
		debuggerHelper++;
		if (numberOfChoicesMade == 3) {
			if (detectIfYouWon()) {
				JOptionPane.showMessageDialog(null, "Voitit pelin - onneksi olkoon!",
						"Voitto",
						JOptionPane.PLAIN_MESSAGE);

				String wavFile = "sound/centuryfox.wav";
				Sound soundObj = new Sound(wavFile);
				soundObj.play();
			} else if(detectIfComputerWon()) {
				JOptionPane.showMessageDialog(null, "Tietokone voitti. H‰visit.",
						"H‰viˆ",
						JOptionPane.PLAIN_MESSAGE);			
			} else {
				JOptionPane.showMessageDialog(null, "Kukaan ei voittanut peli‰.",
						"Ei voittajaa",
						JOptionPane.PLAIN_MESSAGE);
			}

		}
	}

	public boolean detectIfYouWon() {
		System.out.println("noi voititko?");
		System.out.println(matrix[0][2]);
		System.out.println(matrix[1][1]);
		System.out.println(matrix[2][0]);
		if (matrix[0][0] == 1 && matrix[1][0] == 1 && matrix[2][0] == 1) {
			return true;
		} else if (matrix[0][1] == 1 && matrix[1][1] == 1 && matrix[2][1] == 1) {
			return true;
		} else if (matrix[0][2] == 1 && matrix[1][2] == 1 && matrix[2][2] == 1) {
			return true;
		} else if (matrix[0][0] == 1 && matrix[0][1] == 1 && matrix[0][2] == 1) {
			return true;
		} else if (matrix[1][0] == 1 && matrix[1][1] == 1 && matrix[1][2] == 1) {
			return true;
		} else if (matrix[2][0] == 1 && matrix[2][1] == 1 && matrix[2][2] == 1) {
			return true;
		} else if (matrix[0][0] == 1 && matrix[1][1] == 1 && matrix[2][2] == 1) {
			System.out.println("Ristiin 1");
			return true;
		} else if (matrix[0][2] == 1 && matrix[1][1] == 1 && matrix[2][0] == 1) {
			System.out.println("Ristiin 2");
			return true;
		}

		return false;
	}

	public boolean detectIfComputerWon() {

		if (matrix[0][0] == 2 && matrix[1][0] == 2 && matrix[2][0] == 2) {
			return true;
		} else if (matrix[0][1] == 2 && matrix[1][1] == 2 && matrix[2][1] == 2) {
			return true;
		} else if (matrix[0][2] == 2 && matrix[1][2] == 2 && matrix[2][2] == 2) {
			return true;
		} else if (matrix[0][0] == 2 && matrix[0][1] == 2 && matrix[0][2] == 2) {
			return true;
		} else if (matrix[1][0] == 2 && matrix[1][1] == 2 && matrix[1][2] == 2) {
			return true;
		} else if (matrix[2][0] == 2 && matrix[2][1] == 2 && matrix[2][2] == 2) {
			return true;
		} else if (matrix[0][0] == 2 && matrix[1][1] == 2 && matrix[2][2] == 2) {
			return true;
		} else if (matrix[0][2] == 2 && matrix[1][1] == 2 && matrix[2][0] == 2) {
			return true;
		}

		return false;
	}

	public boolean isFreeSlot(int x, int y) {
		if (matrix[x][y] != 1) {
			return false;
		} else {
			return true;
		}
	}

	class AlsNewGame implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Button element. Convention in this material to
			// use prefixes to hint the type of the object.
			btLeftUpmost.setText("");
			btRightUpmost.setText("");
			btCenterUpmost.setText("");
			btLeftCenter.setText("");
			btCenterCenter.setText("");
			btRightCenter.setText("");
			btRightDownMost.setText("");
			btCenterDownMost.setText("");
			btLeftDownMost.setText("");
			numberOfChoicesMade = 0;

			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix.length; j++) {
					matrix[i][j] = 0;
				}
			}
		}
	}

	class AlsUpperLeft implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				System.out
						.println("TicTacToeGame.AlsUpperLeft.actionPerformed()");
				btLeftUpmost.setText("X");
				numberOfChoicesMade++;
				matrix[0][0] = 1;
				doComputerAction();
			}
		}
	}

	class AlsUpperRight implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				System.out
						.println("TicTacToeGame.AlsUpperRight.actionPerformed()");
				btRightUpmost.setText("X");
				numberOfChoicesMade++;
				matrix[2][0] = 1;
				doComputerAction();
			}
		}
	}

	class AlsUpperCenter implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				System.out
						.println("TicTacToeGame.AlsUpperCenter.actionPerformed()");
				btCenterUpmost.setText("X");
				numberOfChoicesMade++;
				matrix[1][0] = 1;
				doComputerAction();
			}
		}
	}

	class AlsCenterLeft implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				System.out
						.println("TicTacToeGame.AlsCenterLeft.actionPerformed()");
				btLeftCenter.setText("X");
				numberOfChoicesMade++;
				matrix[0][1] = 1;
				doComputerAction();
			}
		}
	}

	class AlsCenterCenter implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				System.out
						.println("TicTacToeGame.AlsCenterCenter.actionPerformed()");
				btCenterCenter.setText("X");
				numberOfChoicesMade++;
				matrix[1][1] = 1;
				doComputerAction();
			}
		}
	}

	class AlsCenterRight implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				System.out
						.println("TicTacToeGame.AlsCenterRight.actionPerformed()");
				btRightCenter.setText("X");
				numberOfChoicesMade++;
				matrix[2][1] = 1;
				doComputerAction();
			}
		}
	}

	class AlsDownMostRight implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				System.out
						.println("TicTacToeGame.AlsDownMostRight.actionPerformed()");
				btRightDownMost.setText("X");
				numberOfChoicesMade++;
				matrix[2][2] = 1;
				doComputerAction();
			}
		}
	}

	class AlsDownMostCenter implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				System.out
						.println("TicTacToeGame.AlsDownMostCenter.actionPerformed()");
				btCenterDownMost.setText("X");
				numberOfChoicesMade++;
				matrix[1][2] = 1;
				doComputerAction();
			}
		}
	}

	class AlsDownMostLeft implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (numberOfChoicesMade < 3) {
				btLeftDownMost.setText("X");
				numberOfChoicesMade++;
				System.out
						.println("TicTacToeGame.AlsDownMostLeft.actionPerformed()");
				matrix[0][2] = 1;
				doComputerAction();
			} else {

			}
		}
	}
	class AlsHighScores implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
	
			System.out.println("TicTacToeGame.AlsHighScores.actionPerformed()");
			WebConversation wc = new WebConversation();
			 WebResponse resp = null;
			
				try {
					resp = wc.getResponse("http://osoitteet.palvelum.me");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
             // muunnetaan vastaus merkkijonoksi
             String huippuPisteet = "";
			try {
				huippuPisteet = resp.getText();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             JOptionPane.showMessageDialog (null, 
               		huippuPisteet, 
        	           		"Ilmoituksen otsikko", 
                     		JOptionPane.PLAIN_MESSAGE) ;
			

			
		}
		
	}
}
