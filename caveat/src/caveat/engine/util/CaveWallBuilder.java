package caveat.engine.util;

import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveObject;
import caveat.object.CaveWall;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveWallBuilder {

	public static void createWalls(Cave cave, int wallAmount, int maxWallLength, int wallSize) {
		for (int i = 0; i < wallAmount; i++) {
			double randomDirection = Math.random() * Math.PI * 2;
			
			CaveWall caveWall = new CaveWall(CavePosition.randomPosition(CaveDefaults.PLAY_AREA_X_SIZE, CaveDefaults.PLAY_AREA_Y_SIZE), new CaveMove(0, randomDirection), wallSize);
			CavePosition oldPosition = CavePosition.createPosition(CavePosition.randomPosition(CaveDefaults.PLAY_AREA_X_SIZE, CaveDefaults.PLAY_AREA_Y_SIZE));
			int wallLength = 0;
			do {
				randomDirection = randomDirection + (Math.random() - .5) * CaveDefaults.WALL_CURVE_MULTIPLIER;
				CavePosition calculatedDisplacementForSize = caveWall.getRelativeDisplacementForSize();
				CavePosition newPosition = oldPosition.add(calculatedDisplacementForSize);
				if (CavePosition.outOfBounds(newPosition)
						|| CavePosition.distance(cave.getPlayerCharacter().getCavePosition(), newPosition) < wallSize + CaveDefaults.PLAYER_SIZE) {
					newPosition = getRandomWallPosition(cave);
					randomDirection = Math.random() * Math.PI * 2;
				}
				CaveWall lastWall = caveWall;
				caveWall = new CaveWall(newPosition, new CaveMove(0, randomDirection), wallSize);
				int distanceToWall = turnAwayFromWalls(cave, caveWall, lastWall);
				if (distanceToWall < CaveDefaults.WALL_MINIMUM_DISTANCE) {
					distanceToWall = turnAwayFromWalls(cave, caveWall, lastWall);
				}
				if (distanceToWall < CaveDefaults.WALL_MINIMUM_DISTANCE) {
					newPosition = getRandomWallPosition(cave);
				}
				CavePosition testPosition = newPosition.add(caveWall.getRelativeDisplacementForSize());
				caveWall.setCavePosition(testPosition);
				if (CavePosition.getClosestObject(caveWall.getCavePosition(), CaveDefaults.WALL_MINIMUM_DISTANCE, cave, CaveObjectType.WALL, lastWall) != null) {
					caveWall.setCavePosition(getRandomWallPosition(cave));
				}
				newPosition = newPosition.add(caveWall.getRelativeDisplacementForSize());
				caveWall.setCavePosition(newPosition);
				cave.addCaveObject(caveWall);
				wallLength++;
				oldPosition = caveWall.getCavePosition();
			} while (wallLength < maxWallLength);
		}
	}

	private static int turnAwayFromWalls(Cave cave, CaveWall caveWall, CaveWall lastWall) {
		CavePosition wallPosition = CavePosition.createPosition(caveWall.getCavePosition());
		CavePosition testPosition = wallPosition.add(caveWall.getRelativeDisplacementForSize());
		CaveObject closestObject = CavePosition.getClosestObject(wallPosition, CaveDefaults.WALL_MINIMUM_DISTANCE, cave, CaveObjectType.WALL, lastWall);
		int distance = CaveDefaults.WALL_MINIMUM_DISTANCE;
		if (closestObject != null) {
			distance = CavePosition.distance(testPosition, closestObject.getCavePosition());
		}
		if (distance < CaveDefaults.WALL_MINIMUM_DISTANCE) {
			caveWall.getCaveMove().setDirection(caveWall.getDirectionAwayFrom(closestObject));
		}
		return distance;
	}

	private static CavePosition getRandomWallPosition(Cave cave) {
		ArrayList<CaveObject> caveObjects = cave.getCaveObjects(CaveObjectType.WALL);
		CavePosition cavePosition = null;
		do {
			int randomIndex = (int) (Math.random() * (caveObjects.size() - 1));
			if (randomIndex == 0 || caveObjects.size() == 0) {
				cavePosition = CavePosition.createPosition(CavePosition.randomPosition(CaveDefaults.PLAY_AREA_X_SIZE, CaveDefaults.PLAY_AREA_Y_SIZE));
			} else {
				cavePosition = caveObjects.get(randomIndex).getCavePosition();
			}
		} while (CavePosition.outOfBounds(cavePosition) && CavePosition.distance(cave.getPlayerCharacter().getCavePosition(), cavePosition) < CaveDefaults.WALL_SIZE + CaveDefaults.PLAYER_SIZE);
		return CavePosition.createPosition(cavePosition);
	}

}
