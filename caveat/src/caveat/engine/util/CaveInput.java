package caveat.engine.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class CaveInput implements KeyListener {

		private HashMap<Integer,Boolean> keys;

		public CaveInput() {
			super();
			keys = new HashMap<Integer,Boolean>();
			for (int i = 0; i<200; i++) {
				keys.put(i, false);
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			keys.put(e.getKeyCode(), true);
		}

		public boolean isKeyPressed(int keyCode) {
			return keys.get(keyCode);
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			keys.put(e.getKeyCode(), false);
		}

		public int validAmount(int amount) {
			if (amount < 0) {
				amount = 0;
			}
			return amount;
		}

}
