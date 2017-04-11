package caveat.object;

import java.awt.Graphics2D;

import caveat.engine.CaveDefaults;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveWall extends CaveObject {

	public CaveWall(CavePosition cavePosition, CaveMove caveMove, int size) {
		super(cavePosition,caveMove);
		setSize(size);
		setParticleColor(CaveDefaults.AMBIENT_LIGHT_COLOR);
		setColor(CaveDefaults.AMBIENT_LIGHT_COLOR);
		setHealth(CaveDefaults.WALL_STRENGTH);
	}
	
	@Override
	public void draw(Graphics2D g2d, Cave cave, long delta) {
		drawSizeLine(g2d, cave);
//		drawLine(getCavePosition(),new CavePosition(getCavePosition().getX(), getCavePosition().getY()-10-getLight()), g2d, cave);
	}

	@Override
	public void createDestructionParticles(double amount, Cave cave) {
		for (int i=0; i<amount; i+=2) {
			setSize(getSize()-2);
			CaveObject wallParticle = createParticle(cave, getCavePosition().add(getRelativeDisplacementForSize()));
//			cave.addCaveObject(wallParticle);
			CaveObject wallParticle2 = createParticle(cave, getCavePosition().add(getRelativeDisplacementForSizeWithAngle(Math.PI)));
//			cave.addCaveObject(wallParticle2);
		}
		setSize(CaveDefaults.WALL_SIZE);
	}

	public CaveObject createParticle(Cave cave, CavePosition position) {
		CaveObject wallParticle = new CaveParticle(CavePosition.createPosition(position), new CaveMove(Math.random()*CaveDefaults.WALL_PARTICLE_RANDOM_SPEED+CaveDefaults.WALL_PARTICLE_MINIMUM_SPEED,Math.random()*Math.PI*2), 1, 100, (int)(CaveDefaults.PARTICLE_LIFETIME+Math.random()*CaveDefaults.PARTICLE_LIFETIME_RANDOM));
		wallParticle.setColor(getParticleColor());
		wallParticle.setCollidable(false);
		return wallParticle;
	}
	
}
