package caveat.object;

import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.engine.util.CaveSound;
import caveat.engine.util.CaveSoundBank;
import caveat.object.type.CaveCharacterType;
import caveat.object.type.CaveItemFeature;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class Cave {

	private ArrayList<CaveObject> caveObjects;

	private CaveSoundBank caveSoundBank;

	private int restartCounter = -1000;

	private boolean fov = CaveDefaults.FOV;

	private boolean paused;

	private double multiplier = CaveDefaults.INITIAL_CAVE_SIZE_MULTIPLIER;

	private boolean initialised = false;
	
	public Cave(CaveSoundBank caveSound) {
		caveObjects = new ArrayList<CaveObject>();
		this.caveSoundBank = caveSound;
		if (caveSoundBank != null) {
			caveSoundBank.initSounds();
		}
	}

	public void setCaveObjects(ArrayList<CaveObject> caveObjects) {
		this.caveObjects = caveObjects;
	}

	public ArrayList<CaveObject> getCaveObjects() {
		ArrayList<CaveObject> objects = new ArrayList<CaveObject>();
		for (CaveObject caveObject : caveObjects)
			if (caveObject.isUpdate()) 
				objects.add(caveObject);
		return objects;
	}

	public ArrayList<CaveObject> getCaveObjects(CaveObjectType type) {
		ArrayList<CaveObject> objects = new ArrayList<CaveObject>();
		for (CaveObject caveObject : getCaveObjects()) {
			if (caveObject.isUpdate()) 
				addObjectToListIfType(caveObject, type, objects);
		}
		return objects;
		
	}

	public ArrayList<CaveObject> getAllCaveObjects(CaveObjectType type) {
		ArrayList<CaveObject> objects = new ArrayList<CaveObject>();
		for (CaveObject caveObject : getAllCaveObjects()) {
			addObjectToListIfType(caveObject, type, objects);
		}
		return objects;
	}

	private void addObjectToListIfType(CaveObject caveObject,
			CaveObjectType type, ArrayList<CaveObject> objects) {
		switch (type) {
		case CHARACTER:
			if (caveObject instanceof CaveCharacter) {
				objects.add(caveObject);
			}
			break;
		case ITEM:
			if (caveObject instanceof CaveItem) {
				objects.add(caveObject);
			}
			break;
		case PARTICLE:
			if (caveObject instanceof CaveParticle) {
				objects.add(caveObject);
			}
			break;
		case WALL:
			if (caveObject instanceof CaveWall) {
				objects.add(caveObject);
			}
			break;
		case GROUND:
			if (caveObject instanceof CaveGround) {
				objects.add(caveObject);
			}
			break;
		case LIGHT:
			if (caveObject instanceof CaveItem) {
				CaveItem caveItem = (CaveItem) caveObject;
				if (caveItem.hasFeature(CaveItemFeature.LIGHT)) {
					objects.add((CaveLight) caveItem);
				}
			}
			break;
		case EXPLOSIVE:
			if (caveObject instanceof CaveItem) {
				CaveItem caveItem = (CaveItem) caveObject;
				if (caveItem.hasFeature(CaveItemFeature.EXPLOSIVE)) {
					objects.add((CaveExplosive) caveItem);
				}
			}
		default:
			break;
		}
	}

	public void setAllCaveObjects(ArrayList<CaveObject> allCaveObjects) {
		this.caveObjects = allCaveObjects;
	}

	public ArrayList<CaveObject> getAllCaveObjects() {
		return caveObjects;
	}

	public void updateCurrentCaveObjects() {
		for (CaveObject caveObject: getAllCaveObjects()) {
			if (isCloseEnoughToPlayer(caveObject) || !isFOV()) {
				caveObject.setUpdate(true);
			} else {
				caveObject.setUpdate(false);
			}
		}
	}

	public void addCurrentCaveObject(CaveObject caveObject) {
		getCaveObjects().add(caveObject);
	}

	public void addCaveObject(CaveObject caveObject) {
		getAllCaveObjects().add(caveObject);
	}

	private boolean isCloseEnoughToPlayer(CaveObject object) {
		int distance = CavePosition.distance(getPlayerCharacter().getCavePosition(), object.getCavePosition());
		if (distance < CaveDefaults.VIEW_SIZE+10) {
			return true;
		}
		return false;
	}

	public CaveCharacter getPlayerCharacter() {
//		for (CaveObject caveObject : getAllCaveObjects()) {
//			if (caveObject instanceof CaveCharacter) {
//				return (CaveCharacter) caveObject;
//			}
//		}
		CaveObject caveObject = getAllCaveObjects().get(0);
		if (caveObject == null) {
			System.out.println("Failed to find player character from cave!");
			return new CaveCharacter(new CavePosition(0, 0), new CaveMove(), CaveCharacterType.PLAYER, this);
		}
		return (CaveCharacter) caveObject;
	}
	
	public CaveItem getTorch() {
		return (CaveItem) getAllCaveObjects().get(1);
	}
	
	public int getRestartCounter() {
		return restartCounter;
	}

	public void setRestartCounter(int counter) {
		restartCounter = counter;
	}

	public void playSound(CaveSound sound) {
		caveSoundBank.playSound(sound);
	}

	public void stopSound(CaveSound sound) {
		caveSoundBank.stopSound(sound);
	}

	public void loopSound(CaveSound sound) {
		caveSoundBank.loopSound(sound);
	}

	public CaveSoundBank getCaveSoundBank() {
		return caveSoundBank;
	}

	public boolean isFOV() {
		return fov;
	}

	public void setFOV(boolean isFov) {
		this.fov = isFov;
	}

	public boolean isPaused() {
		return paused;
	}
	
	public void setPaused(boolean isPaused) {
		this.paused = isPaused;
	}

	public double getCaveSizeMultiplier() {
		return multiplier;
	}

	public void setCaveSizeMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public boolean isInitialised() {
		return initialised;
	}

	public void setInitialised(boolean initialised) {
		this.initialised = initialised;
	}

}
