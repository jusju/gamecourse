package caveat.engine.util;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveCharacter;
import caveat.object.CaveObject;
import caveat.object.CaveWall;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveVision {

	public void setVisibility(Cave cave) {
		if (!cave.isFOV()) {
			for (CaveObject caveObject : cave.getCaveObjects())
				caveObject.setVisible(true);
			return;
		}
		CaveCharacter character = cave.getPlayerCharacter();
		for (CaveObject caveObject : cave.getCaveObjects()) {
			caveObject.setVisible(false);
			if (caveObject.equals(character)) {
				caveObject.setVisible(true);
			} else if (CavePosition.distance(character.getCavePosition(), caveObject.getCavePosition())<CaveDefaults.VIEW_SIZE+10) {
//				if (isSightClear(cave, character, caveObject)) {
					caveObject.setVisible(true);
//				}
			}
		}
			
	}

	private boolean isSightClear(Cave cave, CaveCharacter character,
			CaveObject caveObject) {
		CavePosition lookableObjectPosition = caveObject.getCavePosition();
		boolean noBlockers = true;
		for (CaveObject blockingObject : cave.getCaveObjects()) {
			CavePosition characterPosition = character.getCavePosition();
			CavePosition blockingObjectPosition = blockingObject.getCavePosition();
			if (CavePosition.distance(blockingObjectPosition, characterPosition)< CaveDefaults.VIEW_SIZE-1) {
				boolean isNotItself = !caveObject.equals(blockingObject);
				boolean isWall = blockingObject instanceof CaveWall;
				boolean isCollidable = blockingObject.isCollidable();
				if (isNotItself
						&& isWall
						//				&& ! (blockingObject instanceof CaveParticle)
						&& isCollidable
						&& blockingObjectPosition.isCloserToPlayerThan(lookableObjectPosition, characterPosition)
						&& isDirectionSameFromCharacter(character, lookableObjectPosition, blockingObjectPosition)
				) {
					noBlockers = false;
				}
			}
		}
		return noBlockers;
	}

	private boolean isDirectionSameFromCharacter(CaveCharacter character, CavePosition position1, CavePosition position2) {
		double direction1 = character.getDirectionTo(position1);
		double direction2 = character.getDirectionTo(position2);
		return CaveMove.isSimilarDirection(direction1, direction2);
	}

}
