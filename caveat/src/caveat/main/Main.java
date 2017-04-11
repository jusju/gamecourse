package caveat.main;

import caveat.engine.CaveEngine;

public class Main {

	/**
	 * 	Cave Adventure 2 
	 *
	 *	Keys
	 *	Arrows 	Move
	 *  Ctrl	Bomb
	 *  ESC/Q	Quit
	 *  H		Help
	 *  
	 */
	public static void main (String[] args) {
		try {
			CaveEngine caveEngine = new CaveEngine();
			caveEngine.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
