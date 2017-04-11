package caveat.engine.util;

import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveFilledWall;
import caveat.object.CaveWall;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveFilledWallBuilder {

	public static void createWalls(Cave cave, int wallAmount, int wallLength, int wallSize) {
		ArrayList<CaveFilledWall> walls = new ArrayList<CaveFilledWall>();
		for (int y = CaveDefaults.FILLED_WALL_SIZE / 2; y < CaveDefaults.PLAY_AREA_Y_SIZE; y += CaveDefaults.FILLED_WALL_SIZE) {
			for (int x = CaveDefaults.FILLED_WALL_SIZE / 2; x < CaveDefaults.PLAY_AREA_X_SIZE; x += CaveDefaults.FILLED_WALL_SIZE) {
				CaveFilledWall caveFilledWall = new CaveFilledWall(new CavePosition(x, y), new CaveMove(0, 0), CaveDefaults.FILLED_WALL_SIZE);
				walls.add(caveFilledWall);
			}
		}
		
		ArrayList<CavePosition> tunnelPositions = new ArrayList<CavePosition>();
		double randomDirection = Math.random() * Math.PI * 2;
		CavePosition randomPosition = new CavePosition(CaveDefaults.PLAY_AREA_X_SIZE / 2, CaveDefaults.PLAY_AREA_Y_SIZE / 2);
		for (int amount = 0; amount < CaveDefaults.FILLED_WALL_AMOUNT; amount++) {
			for (int length = 0; length < 50 + Math.random() * 100; length++) {
				CaveWall tempWall = new CaveWall(randomPosition, new CaveMove(0, randomDirection), CaveDefaults.FILLED_WALL_SIZE);
				randomDirection = randomDirection + (Math.random() - .5) * CaveDefaults.WALL_CURVE_MULTIPLIER;
				tunnelPositions.add(tempWall.getCavePosition());
				randomPosition = tempWall.getCavePosition().add(tempWall.getRelativeDisplacementForSize());
				if (CavePosition.outOfBounds(randomPosition)) {
					int tunnelPositionIndex = (int) (Math.random() * tunnelPositions.size() / 2 + tunnelPositions.size() / 2);
					randomPosition = tunnelPositions.get(tunnelPositionIndex);
				}
				// for (CavePosition tunnelPosition: tunnelPositions) {
				// if (!tunnelPosition.equals(tempWall.getCavePosition())
				// && CavePosition.distance(tunnelPosition, randomPosition) < CaveDefaults.FILLED_WALL_SIZE) {
				// int tunnelPositionIndex = (int) (Math.random() * tunnelPositions.size() / 2 + tunnelPositions.size() / 2);
				// randomPosition = tunnelPositions.get(tunnelPositionIndex);
				// }
				// }
			}
			int tunnelPositionIndex = (int) (Math.random() * (tunnelPositions.size() / 2-2) + (tunnelPositions.size() / 2-1));
			randomPosition = tunnelPositions.get(tunnelPositionIndex);
			if (Math.random() < .5) {
				CavePosition addedPosition = randomPosition;
				randomDirection = Math.random() * Math.PI * 2;
				for (int i = 0; i < 4; i++) {
					CaveWall tempWall = new CaveWall(randomPosition, new CaveMove(0, randomDirection), CaveDefaults.FILLED_WALL_SIZE * 6);
					addedPosition = randomPosition.add(tempWall.getRelativeDisplacementForSize());
					int distance = CavePosition.distance(addedPosition, randomPosition);
					CavePosition nextPosition = tunnelPositions.get(tunnelPositionIndex + 1);
					CavePosition previousPosition = tunnelPositions.get(tunnelPositionIndex - 1);
					if (CavePosition.distance(randomPosition, nextPosition) < distance
							&& CavePosition.distance(randomPosition, previousPosition) < distance) {
						i = 3; // found a suitable direction
					} else {
						randomDirection += Math.PI / 4;
					}
				}
				randomPosition = addedPosition;
			}
		}
		ArrayList<CaveFilledWall> deletableWalls = new ArrayList<CaveFilledWall>();
		for (CaveFilledWall wall : walls) {
			int closestPosition = CaveDefaults.FILLED_WALL_SIZE * 10;
			for (CavePosition tunnelPosition : tunnelPositions) {
				CavePosition wallPosition = wall.getCavePosition();
				int distance = CavePosition.distance(wallPosition, tunnelPosition);
				if (distance < CaveDefaults.FILLED_WALL_SIZE) {
					deletableWalls.add(wall);
				} else if (distance < closestPosition) {
					closestPosition = distance;
				}
			}
			if (closestPosition > CaveDefaults.FILLED_WALL_SIZE * 4) {
				deletableWalls.add(wall);
			}
		}
		for (CaveFilledWall deletable : deletableWalls) {
			walls.remove(deletable);
		}
		for (CaveFilledWall wall : walls) {
			cave.addCaveObject(wall);
		}
		int i = 0;
		for (CavePosition tunnelPosition : tunnelPositions) {
			i++;
			if (i % 5 == 0) {
				new CaveBuilder().createLight(tunnelPosition, cave);
			}
		}
	}

}
