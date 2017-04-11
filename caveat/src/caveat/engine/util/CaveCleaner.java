package caveat.engine.util;

import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveItem;
import caveat.object.CaveObject;
import caveat.object.CaveWall;
import caveat.object.type.CaveObjectType;

public class CaveCleaner {

	public void clean(Cave cave, long delta) throws InterruptedException {
		handleDestroyed(cave);
		handleCounters(cave, delta);
		removeUnusedObjects(cave);
	}

	private void removeUnusedObjects(Cave cave) {
		ArrayList<CaveObject> caveObjects = cave.getAllCaveObjects();
		ArrayList<CaveObject> newCaveObjects = new ArrayList<CaveObject>(); 
		for (CaveObject caveObject : caveObjects) {
			if (!caveObject.getRemovable()) {
				newCaveObjects.add(caveObject);
			}
			if (caveObject.getHealth() <= 0
					|| (caveObject instanceof CaveItem 
							&& (((CaveItem)caveObject).getCounter() <= 0 
							&& ((CaveItem)caveObject).getCounter() != CaveDefaults.COUNTER_INVALID))) {
				caveObject.setVisible(false);
				if (caveObject.equals(cave.getPlayerCharacter())) {
					if (cave.getRestartCounter() == CaveDefaults.COUNTER_INVALID)
						cave.setRestartCounter(CaveDefaults.RESTART_COUNTER);
						caveObject.setSound(CaveSound.DESTRUCTION, null);
				} else {
					caveObject.setRemovable(true);
				}
				if (caveObject instanceof CaveWall) {
					cave.getCaveSoundBank().playSound(CaveSound.WALL_DESTRUCTION_SOUND);
				} else if (caveObject.getHealth()<= 0) {
					caveObject.playSound(CaveSound.DESTRUCTION);
				}
			}
		}
		cave.setCaveObjects(newCaveObjects);
	}

	private void handleCounters(Cave cave, long delta) {
		ArrayList<CaveObject> caveItems = cave.getAllCaveObjects(CaveObjectType.ITEM);
		for (CaveObject caveObject : caveItems) {
			CaveItem item = (CaveItem) caveObject;
			if (item.getCounter()>0) {
				item.setCounter((int)(item.getCounter()-1*delta));
			} else if (item.getCounter() != CaveDefaults.COUNTER_INVALID) {
				item.setVisible(false);
			}
		}
	}

	private void handleDestroyed(Cave cave) {
		ArrayList<CaveObject> destroyedObjects = new ArrayList<CaveObject>();
		ArrayList<CaveObject> caveObjects = cave.getAllCaveObjects();
		for (CaveObject caveObject : caveObjects) {
			if (caveObject.getHealth() <= 0) {
				destroyedObjects.add(caveObject);
			}
		}
		for (CaveObject destroyedObject: destroyedObjects) {
			destroyedObject.setCollidable(false);
			if (!destroyedObject.equals(cave.getPlayerCharacter()) && cave.getRestartCounter() < 0) {
				destroyedObject.destroy(cave);
			}
		}
	}
	
}
