package caveat.object.util;

import java.util.Random;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveObject;
import caveat.object.CaveWall;
import caveat.object.type.CaveObjectType;

public class CavePosition {

	private double x;
	private double y;

	public CavePosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setX(double x) {
		if (x > CaveDefaults.PLAY_AREA_X_SIZE) {
			x = CaveDefaults.PLAY_AREA_X_SIZE;
		}
		if (x < 0) {
			x = 0;
		}
		this.x = x;
	}

	public void setUncheckedX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		if (y > CaveDefaults.PLAY_AREA_Y_SIZE) {
			y = CaveDefaults.PLAY_AREA_Y_SIZE;
		}
		if (y < 0) {
			y = 0;
		}
		this.y = y;
	}

	public void setUncheckedY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public static CavePosition randomPosition(int areaXSize, int areaYSize) {
		CavePosition cavePosition = null;
		do {
			cavePosition = randomUncheckedPositionWithPositiveValues(areaXSize, areaYSize);
		} while (CavePosition.outOfBounds(cavePosition));
		return cavePosition;
	}

	private static CavePosition randomUncheckedPositionWithPositiveValues(int areaXSize, int areaYSize) {
		return randomUncheckedPosition(areaXSize, areaYSize, null, true);
	}

	public static CavePosition randomUncheckedPosition(int areaXSize, int areaYSize, Random rnd, boolean absValue) {
		if (rnd == null) {
			rnd = new Random();
		}
		CavePosition cavePosition;
		int x = (int) ((rnd.nextDouble() - .5) * areaXSize * 2);
		int y = (int) ((rnd.nextDouble() - .5) * areaYSize * 2);
		if (absValue) {
			x = Math.abs(x);
			y = Math.abs(y);
		}
		cavePosition = new CavePosition(x, y);
		return cavePosition;
	}

	public static double accurateDistance(CavePosition cavePosition1,
			CavePosition cavePosition2) {
		double xPow2 = Math.pow(Math.abs(cavePosition1.getX() - cavePosition2.getX()), 2);
		double yPow2 = Math.pow(Math.abs(cavePosition1.getY() - cavePosition2.getY()), 2);
		return Math.sqrt(xPow2 + yPow2);
	}

	public static int distance(CavePosition cavePosition1,
			CavePosition cavePosition2) {
		return (int) accurateDistance(cavePosition1, cavePosition2);
	}

	public static CavePosition createPosition(CavePosition cavePosition) {
		return new CavePosition(cavePosition.getX(), cavePosition.getY());
	}

	public CavePosition add(CavePosition relativeDisplacementForSize) {
		return new CavePosition(getX() + relativeDisplacementForSize.getX(), getY() + relativeDisplacementForSize.getY());
	}

	public static boolean outOfBounds(CavePosition position) {
		return position == null
				|| position.getX() > CaveDefaults.PLAY_AREA_X_SIZE - 10
				|| position.getX() < 10
				|| position.getY() > CaveDefaults.PLAY_AREA_Y_SIZE - 10
				|| position.getY() < 20;
	}

	public static CavePosition getCalculatedActualPositionAfterMove(double multiplier, long delta, CaveObject object2) {
		return object2.getCavePosition().add(object2.getRelativePositionForMove(multiplier, delta));
	}

	public boolean isCloserToPlayerThan(CavePosition objectPosition, CavePosition characterPosition) {
		return CavePosition.distance(objectPosition, characterPosition) > CavePosition.distance(this, characterPosition);
	}

	public static CaveObject getClosestObject(CavePosition position, int maxDistance, Cave cave, CaveObjectType caveObjectType, CaveObject... excludes) {
		CaveObject goal = null;
		for (CaveObject object : cave.getCaveObjects(caveObjectType)) {
			boolean excludedObject = false;
			for (CaveObject excludeObject : excludes) {
				if (object.equals(excludeObject)) {
					excludedObject = true;
				}
			}
			if (!excludedObject) {
				CavePosition objectPosition = object.getCavePosition();
				int distanceToThisObject = CavePosition.distance(position, objectPosition);
				if (distanceToThisObject < maxDistance) {
					maxDistance = distanceToThisObject;
					goal = object;
				} else if (object.getObjectType().equals(CaveDefaults.CAVEFILLEDWALL) && position.isInside(object)) {
					return object;
				}
			}
		}
		return goal;
	}

	private boolean isInside(CaveObject object) {
		if (object.getObjectType().equals(CaveDefaults.CAVEFILLEDWALL)) {
			double objectX = object.getCavePosition().getX();
			double objectY = object.getCavePosition().getY();
			int halfSize = object.getSize() / 2;
			if (getX() <= objectX + halfSize && getX() >= objectX - halfSize
					&& getY() <= objectY + halfSize && getY() >= objectY - halfSize) {
				return true;
			}
		}
		return false;
	}

}
