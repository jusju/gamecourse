package caveat.object;

import java.awt.Graphics2D;

import caveat.engine.CaveDefaults;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveParticle extends CaveItem {

	public CaveParticle(CavePosition position, CaveMove move, int size, int weight, int counter) {
		super(position, move, size, weight, counter);
		setIndestructible(true);
	}

	/**
	 * Only for checking collisions
	 * @param size 
	 */
	public CaveParticle(CavePosition position, int size) {
		super(position, new CaveMove(0,0), size, CaveDefaults.ITEM_WEIGHT, 0);
	}

	@Override
	public void draw(Graphics2D g2d, Cave cave, long delta) {
		drawSizeLine(g2d, cave);
//		drawRandomTriangle(g2d, cave, getSize());
	}
}
