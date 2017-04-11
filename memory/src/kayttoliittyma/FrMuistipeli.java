package kayttoliittyma;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

import persistointi.TalletaTulokset;

import liiketoiminta.Pelaaja;

/**
 * Muistipeli. Sis‰lt‰‰ 18 erilaista korttiparia, jotka luovat 36:n ruudun 6x6
 * -ruudukon. Peli laskee pisteit‰ pelin edetess‰, pisteytys l‰htee nollasta.
 * Oikea pari lis‰‰ yhden pisteen ja v‰‰r‰ pari v‰hent‰‰ yhden pisteen. Pelin
 * p‰‰tytty‰ kysyt‰‰n pelaajan nimi ja lis‰t‰‰n pelaaja pisteineen tiedostoon.
 * 
 * @author Veli-Matti Luoto
 * 
 */

public class FrMuistipeli extends JFrame implements ActionListener {

	private int valinnat = 0;
	private int pisteet = 0;
	private int oikeatValinnat = 0;

	private ArrayList<JButton> kortit = new ArrayList<JButton>();
	private ArrayList<ImageIcon> hedelmat = new ArrayList<ImageIcon>();

	private JButton btKortti = null;
	private JButton btUusiPeli = new JButton("Uusi peli");
	private JButton btPoistuPelista = new JButton("Poistu pelist‰");
	private JButton btEkaValittuKortti = null;
	private JButton btTokaValittuKortti = null;
	private JButton btNaytaTulokset = new JButton("N‰yt‰ tulokset");

	private JLabel lbPisteet = new JLabel("Pisteet: ");
	private JLabel lbPisteidenLasku = new JLabel("" + pisteet);

	private JPanel paPisteet = new JPanel(new FlowLayout());
	private JPanel paKortit = new JPanel(new GridLayout(6, 6));
	private JPanel paToiminnot = new JPanel(new FlowLayout());

	private Viive uusiViive = null;

	public FrMuistipeli() {

		setTitle("Muistipeli");
		setSize(600, 600);
		setLocation(100, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		lbPisteet.setFont(new Font("sansserif", Font.BOLD, 22));
		lbPisteidenLasku.setFont(new Font("sansserif", Font.BOLD, 22));
		
		int i = 0;
		int j = 0;

		// luodaan hedelm‰oliot
		for (i = 0; i < 18; i++) {
			// 2 x 18 erilaista ikonia
			for (j = 0; j < 2; j++) {
				ImageIcon hedelma = createImageIcon("images/rsz_kuva" + i
						+ ".gif");
				// lis‰t‰‰n olio arraylistiin
				hedelmat.add(hedelma);
			}
		}

		// sekoitetaan hedelm‰ikonit
		Collections.shuffle(hedelmat);

		// luodaan korttioliot
		for (i = 0; i < 36; i++) {
			btKortti = new JButton();
			btKortti.addActionListener(this);
			// lis‰t‰‰n olio arraylistiin
			kortit.add(btKortti);
		}

		setVisible(true);
		asetteleKomponentit();
	}

	private void asetteleKomponentit() {
		Container sisalto = this.getContentPane();
		sisalto.setLayout(new BorderLayout());
		
		paPisteet.add(lbPisteet);
		paPisteet.add(lbPisteidenLasku);
		sisalto.add(paPisteet, BorderLayout.NORTH);

		for (int i = 0; i < kortit.size(); i++) {
			paKortit.add(kortit.get(i));
		}
		sisalto.add(paKortit);

		paToiminnot.add(btUusiPeli);
		paToiminnot.add(btPoistuPelista);
		// TOISTAISEKSI POIS KƒYT÷STƒ
		// paToiminnot.add(btNaytaTulokset);
		sisalto.add(paToiminnot, BorderLayout.SOUTH);

		btUusiPeli.addActionListener(this);
		btPoistuPelista.addActionListener(this);
	}

	// painettaessa btUusiPeli, nollataan tilanne
	// eli aloitetaan uusi peli tyhj‰lt‰ pˆyd‰lt‰
	public void resetoiPeli() {
		for (int i = 0; i < kortit.size(); i++) {
			kortit.get(i).setIcon(null);
		}

		// sekoitetaan hedelmat uudemman kerran
		Collections.shuffle(hedelmat);
		
		// nollaukset
		piilotaKortit();
		valinnat = 0;
		pisteet = 0;
		oikeatValinnat = 0;
		lbPisteidenLasku.setText("" + pisteet);
	}

	public void actionPerformed(ActionEvent e) {

		int i;

		for (i = 0; i < kortit.size(); i++) {
			if (e.getSource().equals(kortit.get(i))) {
				if (btEkaValittuKortti != null && btTokaValittuKortti != null) {
					uusiViive.cancelTimer();
				}

				if (btEkaValittuKortti == null) {
					if (kortit.get(i).getIcon() != null) {
						return;
					} else {
						// otetaan muistiin ensimm‰inen valittu kortti
						btEkaValittuKortti = kortit.get(i);
						// tuodaan valitun kortin ikoni esiin
						kortit.get(i).setIcon(hedelmat.get(i));
						valinnat++;
					}
				} else if (btTokaValittuKortti == null) {
					if (btEkaValittuKortti.equals(kortit.get(i))) {
						return;
					} else {
						// otetaan muistiin toinen valittu kortti
						if (kortit.get(i).getIcon() != null) {
							return;
						} else {
							btTokaValittuKortti = kortit.get(i);
							// tuodaan valitun kortin ikoni esiin
							kortit.get(i).setIcon(hedelmat.get(i));
							valinnat++;
						}
					}
				}
			} else if (e.getSource().equals(btUusiPeli)) {
				resetoiPeli();
			} else if (e.getSource().equals(btPoistuPelista)) {
				System.exit(0);
			} else if (e.getSource().equals(btNaytaTulokset)) {
				// TODO: n‰yt‰ tulokset tulokset.csv -tiedostosta
			}
		}

		if (valinnat == 2) {

			// vertaillaan muistiin otettuja kortteja
			if (!btEkaValittuKortti.getIcon().toString().equals(
					btTokaValittuKortti.getIcon().toString())) {
				// pidet‰‰n kortit n‰kyvill‰ yhden sekunnin ajan
				uusiViive = new Viive(1);
				// jos kortit eiv‰t t‰sm‰‰, v‰hennet‰‰n pisteit‰
				lbPisteidenLasku.setText("" + pisteet--);
				// p‰ivitet‰‰n kentt‰, muuten laahaa yhden j‰ljess‰
				lbPisteidenLasku.setText("" + pisteet);
			} else {
				// pidet‰‰n lukua oikeista valinnoista
				oikeatValinnat++;
				// jos korttien ikonit t‰sm‰‰v‰t, lis‰t‰‰n pisteit‰
				lbPisteidenLasku.setText("" + pisteet++);
				// p‰ivitet‰‰n kentt‰, muuten laahaa yhden j‰ljess‰
				lbPisteidenLasku.setText("" + pisteet);
				// ja nollataan oliot seuraavaa valintaa varten
				btEkaValittuKortti = null;
				btTokaValittuKortti = null;
			}

			// nollataan valinnat, jotta prosessi voidaan aloittaa alusta
			valinnat = 0;
		}

		if (oikeatValinnat == 18) {

			// kysymysdialogi pelaajan nime‰ varten
			String nimi = (String) JOptionPane.showInputDialog(null, "Sait "
					+ pisteet + " pistett‰.\nAnna nimesi", "Peli p‰‰ttyi",
					JOptionPane.QUESTION_MESSAGE, null, null, "");

			Pelaaja pelaaja = new Pelaaja();
			pelaaja.setNimi(nimi);
			pelaaja.setPisteet(pisteet);
			TalletaTulokset tulokset = new TalletaTulokset();
			tulokset.talletaTulos(pelaaja);
			// TODO: lis‰ys tulokset.csv -tiedostoon
		}
	}

	// luokka sekunnin viivett‰ varten
	public class Viive {
		Timer timer;

		public Viive(int seconds) {
			timer = new Timer();
			timer.schedule(new RemindTask(), seconds * 1000);
		}

		// kutsutaan jos valittiin kortti ennen kuin sekunti oli kulunut
		public void cancelTimer() {
			timer.cancel();
			piilotaKortit();
		}

		class RemindTask extends TimerTask {
			public void run() {
				timer.cancel();
				// kun sekunti on kulunut, nollataan arvot
				piilotaKortit();
			}
		}
	}

	// arvojen nollaus
	private void piilotaKortit() {
		if (btEkaValittuKortti != null) {
			btEkaValittuKortti.setIcon(null);
			btEkaValittuKortti = null;
		}

		if (btTokaValittuKortti != null) {
			btTokaValittuKortti.setIcon(null);
			btTokaValittuKortti = null;
		}
	}

	// auttaa luomaan imageiconit
	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Kuvaa ei lˆytynyt: " + path);
			return null;
		}
	}

	public static void main(String[] args) {
		new FrMuistipeli();
	}
}
