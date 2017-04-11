package caveat.object;

import java.awt.Graphics2D;

import caveat.engine.CaveDefaults;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CavePickaxe extends CaveItem {

	private CavePosition hitTargetPosition;

	public CavePickaxe(CavePosition position, CaveMove caveMove, int size, int counter) {
		super(position, new CaveMove(0, caveMove.getDirection()), size, 0, counter);
	}

	@Override
	public void draw(Graphics2D g2d, Cave cave, long delta) {
		CaveCharacter character = cave.getPlayerCharacter();
		CavePosition pickPosition = getCavePosition().add(character.getRelativeDisplacementForSize());
		CavePosition pickEndPosition = calculatePickEndPosition(cave, pickPosition);
		drawLine(pickPosition, pickEndPosition, g2d, cave);
	}

	private CavePosition calculatePickEndPosition(Cave cave,
			CavePosition pickPosition) {
		int pickSize = (int) ((Double.valueOf(getCounter()) / CaveDefaults.PLAYER_FUNCTION_DELAY) * CaveDefaults.PICKAXE_HIT_RADIUS);
		if (getHitTargetPosition() == null && getCounter() > CaveDefaults.PLAYER_FUNCTION_DELAY / 2) {
			pickSize = CaveDefaults.PICKAXE_HIT_RADIUS;
		}
		if (getHitTargetPosition() != null) {
			int distanceToTarget = CavePosition.distance(getHitTargetPosition(), getCavePosition());
			if (pickSize > distanceToTarget) {
				pickSize = distanceToTarget;
			}
		}
		setSize((int) (pickSize*cave.getCaveSizeMultiplier()));
		CavePosition endPosition = pickPosition.add(getRelativeDisplacementForSize());
		setSize(CaveDefaults.PICKAXE_SIZE);
		return endPosition;
	}

	@Override
	public void setCourse(Cave cave, long delta) {
		double playerDirection = cave.getPlayerCharacter().getCaveMove().getDirection();
		getCaveMove().setDirection(playerDirection);
		if (getCounter() > CaveDefaults.PLAYER_FUNCTION_DELAY / 2) {
			getCaveMove().setDirection(playerDirection + (Double.valueOf(getCounter() - CaveDefaults.PLAYER_FUNCTION_DELAY / 2) / CaveDefaults.PLAYER_FUNCTION_DELAY * Math.PI * 2 - Math.PI / 2));
		}
		if (getHitTargetPosition() != null)
			getCaveMove().setDirection(getDirectionTo(getHitTargetPosition()));
	}

	public void setHitTargetPosition(CavePosition hitTargetPosition) {
		this.hitTargetPosition = hitTargetPosition;
	}

	public CavePosition getHitTargetPosition() {
		return hitTargetPosition;
	}
}
