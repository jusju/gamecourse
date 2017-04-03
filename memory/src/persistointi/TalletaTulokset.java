package persistointi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.lang.model.type.PrimitiveType;

import liiketoiminta.Pelaaja;

public class TalletaTulokset {

		public ArrayList listaaTulokset() {
			ArrayList<Pelaaja> tulokset = new ArrayList<Pelaaja>();
			try { 
				Scanner scan = new Scanner(new File("db/tulokset.csv"));
				while(scan.hasNext()) {
					Pelaaja pelaaja = new Pelaaja();
					String rivi = scan.nextLine();
					String[] solut = new String[2];
					solut = rivi.split(",");
					pelaaja.setNimi(solut[0]);
					pelaaja.setPisteet(Integer.parseInt(solut[1]));
					tulokset.add(pelaaja);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return tulokset;
		}
	
		public void talletaTulos(Pelaaja pelaaja) {
			try {
				PrintWriter out = new PrintWriter(new FileWriter("db/tulokset.csv", true));
				out.print(pelaaja.getNimi() + ";" + pelaaja.getPisteet()+ "\n");
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
