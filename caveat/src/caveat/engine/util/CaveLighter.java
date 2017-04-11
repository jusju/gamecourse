package caveat.engine.util;

import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveCharacter;
import caveat.object.CaveLight;
import caveat.object.CaveObject;
import caveat.object.type.CaveItemFeature;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveLighter {

	public void light(Cave cave) {
		ArrayList<CaveObject> caveLights = cave.getCaveObjects(CaveObjectType.LIGHT);
		ArrayList<CaveObject> objects = cave.getCaveObjects();
		CaveCharacter player = cave.getPlayerCharacter();
		for (CaveObject caveObject : objects) {
			if (caveObject.isVisible()) {
				caveObject.resetLight();
				for (CaveObject caveLightObject : caveLights) {
					CaveLight caveLight = (CaveLight) caveLightObject;
					int distanceToPlayer = CavePosition.distance(caveObject.getCavePosition(), player.getCavePosition());
					if (CavePosition.distance(caveObject.getCavePosition(), caveLight.getCavePosition()) < (caveLight.getFeature(CaveItemFeature.LIGHT))
							&& (caveLight.getCounter() > 0 || caveLight.getCounter() == -1000)
							&& (distanceToPlayer < CaveDefaults.VIEW_SIZE || !cave.isFOV())) {
						caveObject.addBrightness(caveLight, cave);
						// addFlashlightBrightness(player, caveObject, distanceToPlayer);
						caveObject.setViewLightLevel(player);
					}
				}
			}
		}
	}

	private void addFlashlightBrightness(CaveCharacter player, CaveObject caveObject, int distanceToPlayer) {
		boolean closeTo = CaveMove.isSimilarDirection(player.getDirectionTo(caveObject.getCavePosition()), (player.getCaveMove().getDirection()));
		if (!caveObject.equals(player)
				&& closeTo) {
			for (int i = 0; i < 3; i++) {
				caveObject.addBrightness((CaveDefaults.VIEW_SIZE + 1) / (distanceToPlayer + 1));
			}
		}
	}

}
