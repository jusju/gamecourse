package caveat.engine.util;

import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveCharacter;
import caveat.object.CaveFilledWall;
import caveat.object.CaveLight;
import caveat.object.CaveObject;
import caveat.object.CaveParticle;
import caveat.object.CaveWall;
import caveat.object.type.CaveItemFeature;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveCollider {

	public ArrayList<CavePosition> collisionPositions = new ArrayList<CavePosition>();

	public void checkCollisions(Cave cave, long delta) {
		ArrayList<CaveObject> damagedObjects = new ArrayList<CaveObject>();
		for (CaveObject object1 : cave.getCaveObjects()) {
			for (CaveObject object2 : cave.getCaveObjects()) {
				if (object1.isCollidable() && object2.isCollidable()) {
					checkCollisions(cave, delta, damagedObjects, object1, object2);
				}
			}
		}
		for (CaveObject object : damagedObjects) {
			object.createDestructionParticles(CaveDefaults.OBJECT_HIT_PARTICLE_COUNT, cave);
		}
	}

	private void checkCollisions(Cave cave, long delta, ArrayList<CaveObject> damagedObjects, CaveObject object1, CaveObject object2) {
		boolean isCloserThanCombinedRadiuses = CavePosition.distance(object1.getCenterPosition(cave), object2.getCenterPosition(cave)) < (object1.getSize() + object2.getSize());
		if (!object1.equals(object2) && isCloserThanCombinedRadiuses) {
			CaveMove object2Move = object2.getCaveMove();
			double speed = object2Move.getSpeed();
			//handleWallCollisions(damagedObjects, object1, object2, getIntersectionPosition(object1, object2), speed, delta);
			handleFilledWallCollisions(damagedObjects, object1, object2, speed, delta, cave);
			handleExplosives(object1, object2, speed);
			handleParticles(damagedObjects, object1, object2, object2Move, speed);
			if (object2.equals(cave.getPlayerCharacter())) {
				handleMonsters(cave, delta, damagedObjects, object1, object2);
				handleLightDepletions(cave, damagedObjects, object1);
			}
		}
	}

	private void handleLightDepletions(Cave cave, ArrayList<CaveObject> damagedObjects, CaveObject object1) {
		if (object1.getObjectType().equals(CaveDefaults.CAVELIGHT)) {
			CaveLight caveLight = (CaveLight) object1;
			if (!caveLight.hasFeature(CaveItemFeature.PLAYER_ITEM)
					&& caveLight.getCounter() == CaveDefaults.COUNTER_INVALID) {
				caveLight.setCounter(CaveDefaults.LIGHT_DEPLETED_COUNTER);
				cave.getTorch().setCounter(CaveDefaults.PLAYER_LIGHT_MAX_COUNTER);
				caveLight.playSound(CaveSound.DAMAGE);
				damagedObjects.add(caveLight);
			}
		}
	}

	private void handleParticles(ArrayList<CaveObject> damagedObjects, CaveObject object, CaveObject particle, CaveMove object2Move, double speed) {
		if ((object.getObjectType().equals(CaveDefaults.CAVECHARACTER) && particle.getObjectType().equals(CaveDefaults.CAVEPARTICLE)
		|| object.getObjectType().equals(CaveDefaults.CAVELIGHT) && particle.getObjectType().equals(CaveDefaults.CAVEPARTICLE))) {
			particle.setCaveMove(new CaveMove(speed * CaveDefaults.PARTICLE_FRICTION_ON_HIT, object2Move.getDirection()));
			particle.setCollidable(false);
			if (!object.isIndestructible() && speed > CaveDefaults.PARTICLE_DAMAGE_MINIMUM_SPEED) {
				object.reduceHealth(speed * CaveDefaults.PARTICLE_DAMAGE_MULTIPLIER);
			}
			if (object.getHealth() <= 0) {
				object.setCollidable(false);
			} else if (speed > CaveDefaults.PARTICLE_DAMAGE_MINIMUM_SPEED) {
				damagedObjects.add(object);
			}
		}
	}

	private void handleMonsters(Cave cave, long delta, ArrayList<CaveObject> damagedObjects, CaveObject object1, CaveObject player) {
		if (object1.getObjectType().equals(CaveDefaults.CAVECHARACTER) && player.equals(cave.getPlayerCharacter())) {
			if (!player.isIndestructible()) {
				player.reduceHealth(CaveDefaults.MONSTER_HIT_DAMAGE * delta / 1000);
			}
			damagedObjects.add(player);
		}
	}

	private void handleExplosives(CaveObject object1, CaveObject object2, double speed) {
		if (object1.getObjectType().equals(CaveDefaults.CAVEEXPLOSIVE) && object2.getObjectType().equals(CaveDefaults.CAVEPARTICLE)
				&& speed > CaveDefaults.PARTICLE_DAMAGE_MINIMUM_SPEED) {
			object2.setCaveMove(new CaveMove(0, 0));
			object2.setCollidable(false);
			if (!object1.isIndestructible()) {
				object1.reduceHealth(speed / CaveDefaults.PARTICLE_DAMAGE_MULTIPLIER);
			}
		}
	}

	protected void handleWallCollisions(ArrayList<CaveObject> damagedObjects, CaveObject wall, CaveObject circularObject, CavePosition collisionPositionWithWall, double speed, long delta) {
		if (collisionPositionWithWall != null
				&& (wall.getObjectType().equals(CaveDefaults.CAVEWALL) || wall.getObjectType().equals(CaveDefaults.CAVEFILLEDWALL))  
				&& (circularObject.getObjectType().equals(CaveDefaults.CAVECHARACTER)
						|| circularObject.getObjectType().equals(CaveDefaults.CAVEPARTICLE)
						|| circularObject.getObjectType().equals(CaveDefaults.CAVELIGHT))) {
			collisionPositions.add(collisionPositionWithWall);
		} else {
			return;
		}
		if ((wall.getObjectType().equals(CaveDefaults.CAVEWALL) || wall.getObjectType().equals(CaveDefaults.CAVEFILLEDWALL)) && circularObject.getObjectType().equals(CaveDefaults.CAVEPARTICLE)) {
			double awayFromWall = circularObject.getDirectionAwayFrom(collisionPositionWithWall);
			CaveMover.moveObjectToDirection(circularObject, awayFromWall, delta);
			CaveMove circularObjectMove = circularObject.getCaveMove();
			circularObjectMove.setSpeed(0);
			circularObjectMove.setXSpeed(0);
			circularObjectMove.setYSpeed(0);
			if (!wall.isIndestructible() && speed > CaveDefaults.PARTICLE_DAMAGE_MINIMUM_SPEED) {
				wall.reduceHealth(speed / CaveDefaults.PARTICLE_DAMAGE_MULTIPLIER);
			}
			if (wall.getHealth() <= 0) {
				wall.setCollidable(false);
			}
			damagedObjects.add(wall);
		}
		if ((wall.getObjectType().equals(CaveDefaults.CAVEWALL) || wall.getObjectType().equals(CaveDefaults.CAVEFILLEDWALL))
				&& (circularObject.getObjectType().equals(CaveDefaults.CAVECHARACTER) || circularObject.getObjectType().equals(CaveDefaults.CAVELIGHT))) {
			double awayFromWall = circularObject.getDirectionAwayFrom(collisionPositionWithWall);
			CaveMover.moveObjectToDirection(circularObject, awayFromWall, delta);
			circularObject.getCaveMove().setSpeed(1);
			circularObject.getCaveMove().setXSpeed(0);
			circularObject.getCaveMove().setYSpeed(0);
		}
	}

	private void handleFilledWallCollisions(ArrayList<CaveObject> damagedObjects, CaveObject wall, CaveObject circularObject, double speed, long delta, Cave cave) {
		if (wall.getObjectType().equals(CaveDefaults.CAVEFILLEDWALL) && ! circularObject.getObjectType().equals(CaveDefaults.CAVEFILLEDWALL) ) {
			CavePosition wallPosition = wall.getCavePosition();
			CavePosition circularObjectPosition = circularObject.getCavePosition();
			CaveObject caveObject = new CaveObject(CavePosition.createPosition(circularObjectPosition),new CaveMove(0,circularObject.getDirectionTo(wallPosition)));
			caveObject.setSize(circularObject.getSize());
			CavePosition objectPosition = caveObject.getCavePosition().add(caveObject.getRelativeDisplacementForSize());
			double oX = objectPosition.getX();
			double oY = objectPosition.getY();
			double wallX = wallPosition.getX();
			double wallY = wallPosition.getY();
			int wallHalfSize = wall.getSize()/2;
			if (oX < wallX + wallHalfSize && oX > wallX - wallHalfSize && oY < wallY + wallHalfSize && oY > wallY - wallHalfSize) {
				handleWallCollisions(damagedObjects, wall, circularObject, objectPosition, speed, delta);
			}
//			CaveFilledWall caveFilledWall = (CaveFilledWall) object;
//			CaveWall[] walls = caveFilledWall.createWallsForCollisionChecking(caveFilledWall);
//			for (CaveWall wall : walls) {
//				handleWallCollisions(damagedObjects, wall, circularObject, speed, delta);
//			}
			
		}
	}

	public static CavePosition getIntersectionPosition(CaveObject wall, CaveObject caveObject) {
//		CavePosition wallStartPosition = wall.getCavePosition().add(wall.getRelativeDisplacementForSizeWithAngle(Math.PI, null, false));
		CavePosition wallEndPosition = wall.getCavePosition().add(wall.getRelativeDisplacementForSize());
		CavePosition intersectionPoint = getCircleLineIntersectionPoint(wall.getCavePosition(), wallEndPosition, caveObject.getCavePosition(), caveObject.getSize());
		if (intersectionPoint != null && getCombinedDistanceFromEndpoints(wall, new CaveParticle(intersectionPoint, 1)) < wall.getSize() + .5) {
			return intersectionPoint;
		}
		return null;
	}

	public static double getCombinedDistanceFromEndpoints(CaveObject wall, CaveObject caveObject) {
		return getCombinedDistanceFromEndpoints(wall, caveObject.getCavePosition());
	}

	private static double getCombinedDistanceFromEndpoints(CaveObject wall, CavePosition cavePosition) {
		int endX = (int) (wall.getCavePosition().getX() + wall.getRelativeDisplacementForSize().getX());
		int endY = (int) (wall.getCavePosition().getY() + wall.getRelativeDisplacementForSize().getY());
		double distance1 = CavePosition.accurateDistance(new CavePosition(endX, endY), cavePosition);
		double distance2 = CavePosition.accurateDistance(wall.getCavePosition(), cavePosition);
		double combinedDistance = distance1 + distance2;
		return combinedDistance;
	}

	public static CavePosition getCircleLineIntersectionPoint(CavePosition lineStart,
			CavePosition lineEnd, CavePosition circlePosition, double circleSize) {
		double circleRadius = circleSize / 2;
		double baX = lineEnd.getX() - lineStart.getX();
		double baY = lineEnd.getY() - lineStart.getY();
		double caX = circlePosition.getX() - lineStart.getX();
		double caY = circlePosition.getY() - lineStart.getY();

		double a = baX * baX + baY * baY;
		double bBy2 = baX * caX + baY * caY;
		double c = caX * caX + caY * caY - circleRadius * circleRadius;

		double pBy2 = bBy2 / a;
		double q = c / a;

		double disc = pBy2 * pBy2 - q;
		if (disc < 0) {
			return null;
		}
		double tmpSqrt = Math.sqrt(disc);
		double abScalingFactor1 = -pBy2 + tmpSqrt;
		double abScalingFactor2 = -pBy2 - tmpSqrt;

		CavePosition p1 = new CavePosition(lineStart.getX() - baX * abScalingFactor1, lineStart.getY() - baY * abScalingFactor1);

		if (disc == 0) {
			// System.out.println("Unsupported use of circle to wall interserction");
			// return p1;
			return null;
		}
		CavePosition p2 = new CavePosition(lineStart.getX() - baX * abScalingFactor2, lineStart.getY() - baY * abScalingFactor2);
		// if (CavePosition.distance(p1, lineStart) < CavePosition.distance(p2, lineStart)
		// && CavePosition.distance(p1, lineEnd) < CavePosition.distance(p2, lineEnd))
		if (Math.random() > .5)
			return p1;
		return p2;
	}

}
