package caveat.object;

import java.awt.Graphics2D;

import caveat.engine.CaveDefaults;
import caveat.engine.util.CaveCollider;
import caveat.engine.util.CaveMover;
import caveat.engine.util.CaveSound;
import caveat.object.character.CaveCharacterInventory;
import caveat.object.type.CaveCharacterType;
import caveat.object.type.CaveItemFeature;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveCharacter extends CaveObject {

	private CaveCharacterInventory caveCharacterInventory;
	private CaveCharacterType caveCharacterType;
	private int timer = 0;
	private boolean hasNoticed;

	public CaveCharacter(CavePosition cavePosition, CaveMove caveMove, CaveCharacterType type, Cave cave) {
		super();
		setCaveCharacterInventory(new CaveCharacterInventory());
		setCaveMove(new CaveMove(0, Math.PI));

		switch (type) {
		case PLAYER:
			CavePosition randomPosition = null;
			do {
				randomPosition = CavePosition.randomPosition(CaveDefaults.PLAY_AREA_X_SIZE, CaveDefaults.PLAY_AREA_Y_SIZE);
			} while (getDistanceToNearestWall(randomPosition, cave) < CaveDefaults.WALL_SIZE + CaveDefaults.PLAYER_SIZE);
			setCavePosition(randomPosition);
			setCavePosition(new CavePosition(CaveDefaults.PLAY_AREA_X_SIZE / 2, CaveDefaults.PLAY_AREA_Y_SIZE / 2)); // TODO remove me

			setCharacterType(CaveCharacterType.PLAYER);
			setSize(CaveDefaults.PLAYER_SIZE);
			setHealth(CaveDefaults.PLAYER_HEALTH);
			setColor(CaveDefaults.PLAYER_COLOR);
			setParticleColor(CaveDefaults.PLAYER_BLOOD_COLOR);
			setCaveMove(new CaveMove(0, getCaveMove().getDirection()));

			setSound(CaveSound.DAMAGE, CaveDefaults.PLAYER_DAMAGE_SOUND_FILENAME);
			setSound(CaveSound.DESTRUCTION, CaveDefaults.PLAYER_DAMAGE_SOUND_FILENAME);
			setSound(CaveSound.MOVE, CaveDefaults.PLAYER_WALK_SOUND_FILENAME);
			setSound(CaveSound.ALT_MOVE1, CaveDefaults.PLAYER_ALT1_WALK_SOUND_FILENAME);
			setSound(CaveSound.ALT_MOVE2, CaveDefaults.PLAYER_ALT2_WALK_SOUND_FILENAME);
			setSound(CaveSound.ALT_MOVE3, CaveDefaults.PLAYER_ALT3_WALK_SOUND_FILENAME);
			setSound(CaveSound.HIT, CaveDefaults.PLAYER_PICK_SOUND_FILENAME);
			setSound(CaveSound.MISS, CaveDefaults.PLAYER_SWITCH_SOUND_FILENAME);
			break;

		case MONSTER:
			setCharacterType(CaveCharacterType.MONSTER);
			setCaveCharacterInventory(new CaveCharacterInventory());
			randomPosition = null;
			do {
				randomPosition = CavePosition.randomPosition(CaveDefaults.PLAY_AREA_X_SIZE, CaveDefaults.PLAY_AREA_Y_SIZE);
			} while (CavePosition.distance(randomPosition, cave.getPlayerCharacter().getCavePosition()) < CaveDefaults.MONSTER_HOMING_DISTANCE + 30
					|| getDistanceToNearestWall(randomPosition, cave) < CaveDefaults.WALL_SIZE + CaveDefaults.MONSTER_SIZE);
			setCavePosition(randomPosition);
			setSize(CaveDefaults.MONSTER_SIZE);
			setHealth(CaveDefaults.MONSTER_HEALTH);
			setColor(CaveDefaults.MONSTER_COLOR);
			setParticleColor(CaveDefaults.MONSTER_BLOOD_COLOR);
			setCaveMove(new CaveMove(CaveDefaults.MONSTER_SPEED, randomDirection()));
//			setImage(new CaveImage(16, 16, 20, 1));
			setSound(CaveSound.DAMAGE, CaveDefaults.MONSTER_DAMAGE_SOUND_FILENAME);
			setSound(CaveSound.DESTRUCTION, CaveDefaults.MONSTER_DIE_SOUND_FILENAME);
			setSound(CaveSound.NOTICE, CaveDefaults.MONSTER_NOTICE_SOUND_FILENAME);
			break;

		default:
			break;
		}

		if (cavePosition != null) {
			setCavePosition(cavePosition);
		}
		if (caveMove != null) {
			setCaveMove(caveMove);
		}
	}

	private int getDistanceToNearestWall(CavePosition cavePosition, Cave cave) {
		int nearestDistance = 100;
		for (CaveObject wall : cave.getAllCaveObjects(CaveObjectType.WALL)) {
			if (wall.getCavePosition() != null) {
				int distance = CavePosition.distance(wall.getCavePosition(), cavePosition);
				if (distance < nearestDistance) {
					nearestDistance = distance;
				}
			}
		}
		return nearestDistance;
	}

	private double randomDirection() {
		return Math.random() * Math.PI * 2;
	}

	private void setCharacterType(CaveCharacterType type) {
		this.setCaveCharacterType(type);
	}

	public void setCaveCharacterInventory(CaveCharacterInventory caveCharacterInventory) {
		this.caveCharacterInventory = caveCharacterInventory;
	}

	public CaveCharacterInventory getCaveCharacterInventory() {
		return caveCharacterInventory;
	}

	@Override
	public void draw(Graphics2D g2d, Cave cave, long delta) {
		int x = (int) getCavePosition().getX();
		int y = (int) getCavePosition().getY();
		if (caveCharacterType.equals(CaveCharacterType.PLAYER)) {
			super.draw(g2d, cave, delta);
//			drawCircle(getCavePosition(), CaveDefaults.VIEW_SIZE*2, g2d, cave);
			drawHeadingDot(g2d, x, y, cave);
		} else {
			CavePosition cavePosition = getCavePosition();
			drawFilledCircle(cavePosition, getSize(), g2d, cave);
		}
	}

	private void drawHeadingDot(Graphics2D g2d, int x, int y, Cave cave) {
		int aimX = (int) (x + getRelativeDisplacementForSize().getX());
		int aimY = (int) (y + getRelativeDisplacementForSize().getY());
		CavePosition headingPosition = new CavePosition(aimX, aimY);
		g2d.setColor(CaveDefaults.EYE_COLOR);
		drawLine(headingPosition, headingPosition, g2d, cave);
	}

	public void setCaveCharacterType(CaveCharacterType caveCharacterType) {
		this.caveCharacterType = caveCharacterType;
	}

	public CaveCharacterType getCaveCharacterType() {
		return caveCharacterType;
	}

	/*
	 * "the AI"
	 */
	@Override
	public void setCourse(Cave cave, long delta) {
		boolean originalHasNoticed = false;
		if (getCaveCharacterType() == CaveCharacterType.MONSTER) {
			originalHasNoticed = hasNoticed();
			CavePosition playerPosition = cave.getPlayerCharacter().getCavePosition();
			int distanceToPlayer = CavePosition.distance(getCavePosition(), playerPosition);
			double targetDirection = Math.atan2(playerPosition.getY() - getCavePosition().getY(), playerPosition.getX() - getCavePosition().getX());

			if (distanceToPlayer < CaveDefaults.MONSTER_HOMING_DISTANCE) {
				setHasNoticed(true);
				getCaveMove().setDirection(targetDirection);
			} else {
				setHasNoticed(false);
				getCaveMove().setDirection(randomMonsterTurn(delta));
				turnAwayFromLight(cave);
				group(cave);
			}
			// turnAwayFromWall(cave);
			// turnAwayFromExplosives(cave);
			preventCollision(cave, delta * 2);
			turnAwayFromBoundaries(cave);
			if (!originalHasNoticed && hasNoticed()) {
				playSound(CaveSound.NOTICE);
			}
		}
	}

	private void turnAwayFromWall(Cave cave) {
		CaveObject nearestWall = getNearestObject(cave, CaveObjectType.WALL, CaveDefaults.MONSTER_WALL_EVASION_DISTANCE);
		int nearestWallDistance = getMinimumDistanceFromWall(nearestWall, CaveDefaults.MONSTER_WALL_EVASION_DISTANCE);

		if (nearestWallDistance < CaveDefaults.MONSTER_WALL_EVASION_DISTANCE) {
			getCaveMove().setDirection(getDirectionAwayFrom(nearestWall));
		}
	}

	private void group(Cave cave) {
		CavePosition nearestMonsterLocation = nearestObjectLocation(cave, CaveObjectType.CHARACTER, CaveDefaults.MONSTER_GROUPING_DISTANCE);
		int nearestMonsterDistance = getMinimumDistance(nearestMonsterLocation, CaveDefaults.MONSTER_GROUPING_DISTANCE);
		if (nearestMonsterDistance < CaveDefaults.MONSTER_GROUPING_DISTANCE
				&& nearestMonsterDistance > CaveDefaults.MONSTER_GROUP_SPACING) {
			getCaveMove().setDirection(getDirectionTo(nearestMonsterLocation));
		}
	}

	private void turnAwayFromExplosives(Cave cave) {
		CavePosition nearestExplosiveLocation = nearestObjectLocation(cave, CaveObjectType.EXPLOSIVE, CaveDefaults.MONSTER_EXPLOSIVE_EVASION_DISTANCE);
		int nearestExplosiveDistance = getMinimumDistance(nearestExplosiveLocation, CaveDefaults.MONSTER_EXPLOSIVE_EVASION_DISTANCE);

		if (nearestExplosiveDistance < CaveDefaults.MONSTER_EXPLOSIVE_EVASION_DISTANCE) {
			getCaveMove().setDirection(getDirectionAwayFrom(nearestExplosiveLocation));
		}
	}

	private int getMinimumDistanceFromWall(CaveObject wall, int minimumDistance) {
		if (wall.getCavePosition() != null) {
			// minimumDistance = CavePosition.distance(getCavePosition(),
			// position);
			minimumDistance = (int) CaveCollider.getCombinedDistanceFromEndpoints(wall, this);
		}
		return minimumDistance;
	}

	private int getMinimumDistance(CavePosition position, int minimumDistance) {
		if (position != null) {
			minimumDistance = CavePosition.distance(getCavePosition(), position);
		}
		return minimumDistance;
	}

	private double randomMonsterTurn(long delta) {
		return getCaveMove().getDirection() + ((Math.random() - .5) * CaveDefaults.MONSTER_TURN_SPEED * delta / 1000);
	}

	private CavePosition nearestObjectLocation(Cave cave, CaveObjectType caveObjectType, int farthestDistance) {
		CavePosition goal = getNearestObject(cave, caveObjectType, farthestDistance).getCavePosition();
		return goal;
	}

	private CaveObject getNearestObject(Cave cave, CaveObjectType caveObjectType, int farthestDistance) {
		int closestDistance = farthestDistance;
		CaveObject goal = new CaveWall(null, null, CaveDefaults.WALL_SIZE);
		for (CaveObject object : cave.getCaveObjects(caveObjectType)) {
			if (!object.equals(cave.getPlayerCharacter()) && !object.equals(this)) {
				CavePosition objectPosition = object.getCavePosition();
				int distanceToThisObject = CavePosition.distance(getCavePosition(), objectPosition);
				if (distanceToThisObject < closestDistance) {
					closestDistance = distanceToThisObject;
					goal = object;
				}
			}
		}
		return goal;
	}

	private void setHasNoticed(boolean hasNoticed) {
		this.hasNoticed = hasNoticed;
	}

	public boolean hasNoticed() {
		return hasNoticed;
	}

	private void preventCollision(Cave cave, long delta) {
		for (CaveObject caveCharacter : cave.getCaveObjects(CaveObjectType.CHARACTER)) {
//			if (!caveCharacter.equals(cave.getPlayerCharacter())) {
				CavePosition characterPosition = caveCharacter.getCavePosition();
				int distanceToCharacter = CavePosition.distance(getCavePosition(), characterPosition);
				if (!caveCharacter.equals(this) && distanceToCharacter < CaveDefaults.MONSTER_SIZE) {
					distanceToCharacter = CavePosition.distance(getCavePosition(), characterPosition);
					// getCaveMove().setDirection(getDirectionAwayFrom(characterPosition));
					CaveMover.moveObjectToDirection(this, getDirectionAwayFrom(characterPosition), delta);
				}
//			}
		}
		double speed = getCaveMove().getSpeed();
		if (speed < CaveDefaults.MONSTER_SPEED) {
			getCaveMove().setSpeed(speed + CaveDefaults.MONSTER_ACCELERATION);
		}
	}

	private void turnAwayFromBoundaries(Cave cave) {
		if (CavePosition.outOfBounds(getCavePosition())) {
			int centerOfScreenX = CaveDefaults.CENTER_POSITION_X;
			int centerOfScreenY = CaveDefaults.CENTER_POSITION_Y;
			CavePosition center = new CavePosition(centerOfScreenX, centerOfScreenY);
			getCaveMove().setDirection(getDirectionTo(center));
			getCaveMove().setSpeed(1);
		}
	}

	private void turnAwayFromLight(Cave cave) {
		int closestDistance = CaveDefaults.PLAY_AREA_X_SIZE + CaveDefaults.PLAY_AREA_Y_SIZE;
		CaveItem closestLight = null;
		for (CaveObject caveObject : cave.getCaveObjects(CaveObjectType.ITEM)) {
			if (caveObject instanceof CaveLight) {
				CaveLight caveLight = (CaveLight) caveObject;
				if (caveLight.hasFeature(CaveItemFeature.LIGHT) && caveLight.isCollidable()) {
					int newDistance = CavePosition.distance(caveLight.getCavePosition(), getCavePosition());
					if (newDistance < closestDistance) {
						closestDistance = newDistance;
						closestLight = caveLight;
					}
				}
			}
		}
		if (closestLight != null)
			if (closestDistance < closestLight.getFeature(CaveItemFeature.LIGHT) * 0.7) {
				CavePosition lightPosition = closestLight.getCavePosition();
				double targetDirection = getDirectionAwayFrom(lightPosition);
				getCaveMove().setDirection(targetDirection);
			}
	}

	public int getFunctionTimer() {
		return timer;
	}

	public void setFunctionTimer(int timer) {
		this.timer = timer;
	}

	public void reduceFunctionTimer(long delta) {
		if (timer >= 0) {
			this.timer -= delta;
		}
	}

	public CaveObject getHitGoal(Cave cave) {
		int closestDistance = CaveDefaults.PICKAXE_HIT_RADIUS;
		CaveObject goal = null;
		CavePosition characterEyePosition = getCavePosition().add(getRelativeDisplacementForSize());
		CavePickaxe cavePickaxe = new CavePickaxe(characterEyePosition, getCaveMove(), CaveDefaults.PICKAXE_SIZE, CaveDefaults.PLAYER_FUNCTION_DELAY);
		CavePosition targetPosition = cavePickaxe.getCavePosition().add(cavePickaxe.getRelativeDisplacementForSize());
		goal = CavePosition.getClosestObject(targetPosition, closestDistance, cave, CaveObjectType.WALL);
		if (goal == null) {
			goal = CavePosition.getClosestObject(targetPosition, closestDistance, cave, CaveObjectType.CHARACTER, this);
		}
		return goal;
	}

	// @Override
	// public CavePosition getCenterPosition() {
	// return
	// getCavePosition().add(getRelativeDisplacementForSizeWithAngle(Math.PI/4-getCaveMove().getDirection()));
	// }
}
