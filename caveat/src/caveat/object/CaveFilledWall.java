package caveat.object;

import java.awt.Graphics2D;

import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveFilledWall extends CaveWall {

	public CaveFilledWall(CavePosition cavePosition, CaveMove caveMove, int size) {
		super(cavePosition, caveMove, size);
	}

	@Override
	public void draw(Graphics2D g2d, Cave cave, long delta) {
		CaveWall[] walls = createWallsForCollisionChecking(this);
		for (CaveWall wall : walls) {
//			wall.draw(g2d, cave, delta);
		}
		CavePosition cavePosition1 = getCavePosition().add(new CavePosition(-getSize() / 2+1, -getSize() / 2+1));
		CavePosition cavePosition2 = new CavePosition(getSize()+1, getSize()+1);
		drawFilledRect(cavePosition1, cavePosition2, g2d, cave);
		drawRect(cavePosition1, cavePosition2, g2d, cave);
	}

	@Override
	public void createDestructionParticles(double amount, Cave cave) {
//		CaveWall[] walls = createWallsForCollisionChecking(this);
//		for (CaveWall wall : walls) {
//			wall.createDestructionParticles(amount/4, cave);
//		}
//		for (int i = 0; i < amount; i++) {
//			CaveObject wallParticle = createParticle(cave, getCavePosition().add(getRelativeDisplacementForSizeWithAngle(Math.random() * Math.PI * 2)));
//			cave.addCaveObject(wallParticle);
//		}
	}

	@Override
	public void destroy(Cave cave) {
		if (getSize() > 10) {
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					CaveObject wall = createFilledWall(x, y, 3);
					wall.setHealth(getSize());
					cave.addCaveObject(wall);
				}
			}
		}
	}

	private CaveObject createFilledWall(int x, int y, int particleMultiplier) {
		int wallSize = getSize() / particleMultiplier;
		double wallX = getCavePosition().getX() + x * wallSize;
		double wallY = getCavePosition().getY() + y * wallSize;
		CaveFilledWall caveFilledWall = new CaveFilledWall(new CavePosition(wallX, wallY), new CaveMove(), wallSize);
		return caveFilledWall;
	}

	public CaveWall[] createWallsForCollisionChecking(CaveFilledWall filledWall) {
		int size = filledWall.getSize();
		int halfSize = size / 2;
		CaveWall[] walls = new CaveWall[4];
		CavePosition[] cavePositions = new CavePosition[4];
		cavePositions[0] = new CavePosition(filledWall.getCavePosition().getX() - halfSize, filledWall.getCavePosition().getY());
		cavePositions[1] = new CavePosition(filledWall.getCavePosition().getX(), filledWall.getCavePosition().getY() - halfSize);
		cavePositions[2] = new CavePosition(filledWall.getCavePosition().getX() + halfSize, filledWall.getCavePosition().getY());
		cavePositions[3] = new CavePosition(filledWall.getCavePosition().getX(), filledWall.getCavePosition().getY() + halfSize);
		CaveMove[] caveMoves = new CaveMove[4];
		caveMoves[1] = new CaveMove(0, 0);
		caveMoves[2] = new CaveMove(0, Math.PI * 0.5);
		caveMoves[3] = new CaveMove(0, Math.PI);
		caveMoves[0] = new CaveMove(0, Math.PI * 1.5);
		walls[0] = new CaveWall(cavePositions[0], caveMoves[0], size);
		walls[1] = new CaveWall(cavePositions[1], caveMoves[1], size);
		walls[2] = new CaveWall(cavePositions[2], caveMoves[2], size);
		walls[3] = new CaveWall(cavePositions[3], caveMoves[3], size);
		for (CaveWall wall : walls) {
			wall.setVisible(false);
		}
		return walls;
	}

}
