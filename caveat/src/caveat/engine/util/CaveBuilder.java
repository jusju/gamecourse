package caveat.engine.util;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveCharacter;
import caveat.object.CaveExplosive;
import caveat.object.CaveGround;
import caveat.object.CaveImage;
import caveat.object.CaveLight;
import caveat.object.CaveObject;
import caveat.object.type.CaveCharacterType;
import caveat.object.type.CaveItemFeature;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveBuilder {

	public int monsterAmount = CaveDefaults.INITIAL_MONSTER_AMOUNT;
	public int groundAmount = CaveDefaults.INITIAL_GROUND_AMOUNT;
	public int lightAmount = CaveDefaults.INITIAL_LIGHT_AMOUNT;
	public int wallLength = CaveDefaults.WALL_LENGTH;
	public int wallAmount = CaveDefaults.WALL_AMOUNT;
	public int wallSize = CaveDefaults.WALL_SIZE;
	public int explosiveAmount = CaveDefaults.INITIAL_EXPLOSIVE_AMOUNT;

	public Cave build() {
		Cave cave = new Cave(new CaveSoundBank());
		return cave;
	}

	public void fill(Cave cave, int step) {
		switch (step) {
		case 0:
			cave.addCaveObject(new CaveCharacter(null, null, CaveCharacterType.PLAYER, cave));
			break;
		case 1:
			CaveCharacter playerCharacter = cave.getPlayerCharacter();
			for (int i = 0; i < explosiveAmount; i++) {
				playerCharacter.getCaveCharacterInventory().add(new CaveExplosive());
			}
			CaveLight torch = new CaveLight(playerCharacter.getCavePosition(), new CaveMove(0, 0));
			torch.setFeature(CaveItemFeature.LIGHT, CaveDefaults.PLAYER_LIGHT_BRIGHTNESS);
			torch.setFeature(CaveItemFeature.TORCH, 1);
			torch.setFeature(CaveItemFeature.PLAYER_ITEM, 1);
			torch.setIndestructible(true);
			torch.setCollidable(false);
			torch.setSound(CaveSound.DAMAGE, null);
			torch.setCounter(CaveDefaults.PLAYER_LIGHT_MAX_COUNTER);
			cave.addCaveObject(torch);
			break;
		case 2:
			createMonsters(cave);
			break;
		case 3:
//			CaveWallBuilder.createWalls(cave, wallAmount, wallLength, wallSize);
			CaveFilledWallBuilder.createWalls(cave, wallAmount, wallLength, wallSize);
			break;
		case 4:
			createWallLights(cave);
			break;
		case 5:
			createFreeLights(cave);
			break;
		case 6:
			createGround(cave);
			break;
		}
	}

	public void createWallLights(Cave cave) {
		for (CaveObject wall : cave.getAllCaveObjects(CaveObjectType.WALL)) {
			if (Math.random() < CaveDefaults.WALL_LIGHT_POSITIONING_PROBABILITY) {
				createLight(CavePosition.createPosition(wall.getCavePosition().add(wall.getRelativeDisplacementForSizeWithAngle(Math.PI / 2))), cave);
			}
		}
	}

	public void createFreeLights(Cave cave) {
		CavePosition randomPosition = null;
		for (int i = 0; i < lightAmount; i++) {
			int retryCount = 0;
			int minDistance = (int) (CaveDefaults.LIGHT_RADIUS * 1.5);
			boolean lightNear = true;
			int maxRetryCount = 100;
			while (lightNear && retryCount <= maxRetryCount) {
				randomPosition = CavePosition.randomPosition(CaveDefaults.PLAY_AREA_X_SIZE, CaveDefaults.PLAY_AREA_Y_SIZE);
				lightNear = false;
				for (CaveObject object : cave.getCaveObjects(CaveObjectType.LIGHT)) {
					int distance = CavePosition.distance(object.getCavePosition(), randomPosition);
					if (distance < minDistance) {
						lightNear = true;
					}
				}
				if (CavePosition.distance(cave.getPlayerCharacter().getCavePosition(), randomPosition) < CaveDefaults.WALL_SIZE) {
					lightNear = true;
				}
				retryCount++;
			}
			if (retryCount > maxRetryCount) {
				System.out.println("Too many lights, no dark place found.");
			} else {
				createLight(randomPosition, cave);
			}
		}
	}

	public void createLight(CavePosition position, Cave cave) {
		CaveLight light = new CaveLight(position, new CaveMove(0, 0));
		light.setFeature(CaveItemFeature.LIGHT, CaveDefaults.LIGHT_RADIUS);
		light.setFeature(CaveItemFeature.TORCH, 1);
		// light.setIndestructible(true);
		light.setImage(new CaveImage(16,16,0,7,20)); //TODO re-activate me after tests
		cave.addCaveObject(light);
	}

	public void createMonsters(Cave cave) {
		for (int i = 0; i < monsterAmount; i++) {
			cave.addCaveObject(new CaveCharacter(null, null, CaveCharacterType.MONSTER, cave));
		}
	}

	private void createGround(Cave cave) {
		for (int i = 0; i < groundAmount; i++) {
			cave.addCaveObject(new CaveGround(CavePosition.randomPosition(CaveDefaults.PLAY_AREA_X_SIZE, CaveDefaults.PLAY_AREA_Y_SIZE), new CaveMove(0, Math.random() * Math.PI)));
		}
	}

}
