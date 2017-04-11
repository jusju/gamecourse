package caveat.object;

import java.awt.Graphics2D;

import caveat.engine.CaveDefaults;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveGround extends CaveObject {

	public CaveGround(CavePosition cavePosition, CaveMove caveMove) {
		super(cavePosition, caveMove);
		setSize(CaveDefaults.GROUND_SIZE);
		setIndestructible(true);
		setCollidable(false);
	}
	
	@Override
	public void draw(Graphics2D g2d, Cave cave, long delta) {
		drawSizeLine(g2d, cave);
//		drawRandomTriangle(g2d, cave, 4);
//		drawLine(getCavePosition(),new CavePosition(getCavePosition().getX(), getCavePosition().getY()-10-getLight()), g2d, cave);
	}

}
