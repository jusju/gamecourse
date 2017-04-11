package caveat.engine.util;

import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveCharacter;
import caveat.object.CaveGround;
import caveat.object.CaveItem;
import caveat.object.CaveLight;
import caveat.object.CaveObject;
import caveat.object.CaveParticle;
import caveat.object.CaveWall;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveMover {

	public void move(Cave cave, long delta) {
		if (delta == 0) {
			return;
		}
		ArrayList<CaveObject> caveObjects = cave.getCaveObjects();
		for (CaveObject caveObject : caveObjects) {
			if (!(caveObject instanceof CaveGround) || !(caveObject instanceof CaveWall)) {
				caveObject.setCourse(cave, delta);
				moveObject(caveObject, cave, delta);
			}
		}
		handlePlayerSpecialities(cave);
	}

	private void handlePlayerSpecialities(Cave cave) {
		CaveMove playerMove = cave.getPlayerCharacter().getCaveMove();
		if (playerMove.getSpeed()>=0) {
			playerMove.setSpeed(playerMove.getSpeed()-1);
			if (playerMove.getSpeed() < 0) {
				playerMove.setSpeed(0);
			}
		} else {
			playerMove.setSpeed(playerMove.getSpeed()+1);
			if (playerMove.getSpeed() > 0) {
				playerMove.setSpeed(0);
			}
		}
		handleTorch(cave);
	}

	private void moveObject(CaveObject caveObject, Cave cave, long delta) {
		CavePosition cavePosition = caveObject.getCavePosition();
		CaveMove caveMove = caveObject.getCaveMove();
		if (cavePosition != null && caveMove != null) {
			applySpeedsToPosition(delta, caveObject.getCavePosition(), caveObject.getCaveMove());
			// caveObject.setCavePosition(moveBackToCollisions(caveObject, cave, delta, cavePosition, caveMove));
			reduceFrictionFromSpeed(caveObject, delta, caveMove);
		}
	}

	private CavePosition moveBackToCollisions(CaveObject caveObject, Cave cave, long delta, CavePosition cavePosition, CaveMove caveMove) {
		CavePosition endPosition = CavePosition.createPosition(cavePosition);
		double movedDistance = CavePosition.accurateDistance(endPosition, cavePosition);
		if (movedDistance > .9) {
			// CaveCollider.checkWallCollisions(new ArrayList<CaveObject>(),
			// wall, new CaveParticle(caveObject.getCavePosition()), 0);
			for (int i = 1; i < movedDistance; i++) {
				CaveParticle tempObject = new CaveParticle(endPosition, caveObject.getSize());
				// face towards goal
				tempObject.getCaveMove().setDirection(tempObject.getDirectionTo(cavePosition));
				// set position (i x steps) towards goal
				tempObject.setCavePosition(tempObject.getCavePosition().add(caveObject.getRelativeDisplacementWithDistance(i)));
				// check for collisions in new position
				CavePosition positionAfterCollision = getPositionAfterCollision(cave, tempObject);
				// if distance between moved and collided position < moved steps + 0.5 (for rounding errors)
				if (CavePosition.distance(endPosition, positionAfterCollision) < i + 0.5) {
					endPosition = tempObject.getCavePosition();
				}
			}
		}
		return endPosition;
	}

	private void applySpeedsToPosition(long delta, CavePosition cavePosition, CaveMove caveMove) {
		cavePosition.setX(cavePosition.getX() + Math.cos(caveMove.getDirection()) * caveMove.getSpeed() * delta / 1000);
		cavePosition.setY(cavePosition.getY() + Math.sin(caveMove.getDirection()) * caveMove.getSpeed() * delta / 1000);
		cavePosition.setX(cavePosition.getX() + caveMove.getXSpeed() * delta / 1000);
		cavePosition.setY(cavePosition.getY() + caveMove.getYSpeed() * delta / 1000);
	}

	private CavePosition getPositionAfterCollision(Cave cave, CaveParticle tempObject) {
		tempObject.setCavePosition(CavePosition.createPosition(tempObject.getCavePosition()));
		for (CaveObject wall : cave.getCaveObjects(CaveObjectType.WALL)) {
			// CaveCollider.handleWallCollisions(new ArrayList<CaveObject>(), wall, tempObject, 0);
		}
		return tempObject.getCavePosition();
	}

	private void reduceFrictionFromSpeed(CaveObject caveObject, long delta, CaveMove caveMove) {
		caveMove.setXSpeed(caveMove.getXSpeed() * CaveDefaults.FRICTION * 1 - (delta / 1000));
		caveMove.setYSpeed(caveMove.getYSpeed() * CaveDefaults.FRICTION * 1 - (delta / 1000));
		if (caveObject instanceof CaveParticle || caveObject instanceof CaveLight) {
			caveMove.setSpeed(caveMove.getSpeed() * CaveDefaults.PARTICLE_FLYING_FRICTION * 1 - (delta / 1000));
		} else {
			//caveMove.setSpeed(caveMove.getSpeed() * CaveDefaults.FRICTION * 1 - (delta / 1000)); // TODO fixme
		}
	}

	private void handleTorch(Cave cave) {
		CaveItem torch = cave.getTorch();
		CaveCharacter player = cave.getPlayerCharacter();
		CavePosition cavePosition = player.getCavePosition();
		double pito2 = Math.PI / 2;
		int rightX = (int) (player.getRelativeDisplacementForSizeWithAngle(pito2).getX());
		int rightY = (int) (player.getRelativeDisplacementForSizeWithAngle(pito2).getY());
		CavePosition rightPosition = cavePosition.add(new CavePosition(rightX, rightY));
		torch.setCavePosition(rightPosition);
		if (torch.getCounter() < 100) {
			torch.setCounter(100);
		}
	}

	public static void moveObjectToDirection(CaveObject object2, double direction, long delta) {
		CavePosition cavePosition = object2.getCavePosition();
		cavePosition.setX(cavePosition.getX() + Math.cos(direction) * 20 * delta / 1000);
		cavePosition.setY(cavePosition.getY() + Math.sin(direction) * 20 * delta / 1000);
	}

}
