package caveat.engine.util;

import java.util.ArrayList;

import caveat.engine.CaveDefaults;
import caveat.object.Cave;
import caveat.object.CaveCharacter;
import caveat.object.CaveExplosive;
import caveat.object.CaveGround;
import caveat.object.CaveLight;
import caveat.object.CaveObject;
import caveat.object.CaveParticle;
import caveat.object.CaveWall;
import caveat.object.type.CaveItemFeature;
import caveat.object.type.CaveObjectType;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public class CaveExploder {

	public void handleExplosives(Cave cave, long delta) {
		ArrayList<CaveObject> caveItems = cave.getCaveObjects(CaveObjectType.ITEM);
		for (CaveObject caveItem : caveItems) {
			if (caveItem instanceof CaveExplosive) {
				CaveExplosive caveExplosive = (CaveExplosive) caveItem;
				if (caveExplosive.hasFeature(CaveItemFeature.EXPLOSIVE) && caveExplosive.getCavePosition() != null) {
					if (caveExplosive.getCounter() <= 0) {
						caveExplosive.playSound(CaveSound.EXPLOSION);
						caveExplosive.setHealth(0);
						thrustCaveObjects(cave, caveExplosive);
						caveExplosive.setFeature(CaveItemFeature.EXPLOSIVE, -1);
						caveExplosive.stopSound(CaveSound.COUNTER);
					} else if (Math.random()<CaveDefaults.EXPLOSIVE_COUNTER_RANDOMNESS) {
						caveExplosive.setCounter((int) (caveExplosive.getCounter()-delta/3));
					}
				}
			}
		}
	}

	private void thrustCaveObjects(Cave cave, CaveExplosive caveExplosive) {
		ArrayList<CaveObject> caveObjects = cave.getCaveObjects();
		ArrayList<CaveObject> damagedObjects = new ArrayList<CaveObject>();
		for (CaveObject caveObject : caveObjects) {
			double distance = CavePosition.distance(caveExplosive.getCavePosition(), caveObject.getCavePosition());
			if (distance < caveExplosive.getPower()) {
				CaveMove caveMove = caveObject.getCaveMove();
				double x = caveExplosive.getCavePosition().getX()-caveObject.getCavePosition().getX();
				double y = caveExplosive.getCavePosition().getY()-caveObject.getCavePosition().getY();
				double angle = Math.atan2(y,x);
				if (!(caveObject instanceof CaveWall) && !(caveObject instanceof CaveGround)) {
					caveMove.addXSpeed(-Math.cos(angle)*(CaveDefaults.EXPLOSIVE_THRUST_POWER_MULTIPLIER)*(caveExplosive.getPower()-distance));
					caveMove.addYSpeed(-Math.sin(angle)*(CaveDefaults.EXPLOSIVE_THRUST_POWER_MULTIPLIER)*(caveExplosive.getPower()-distance));
				}
				if (!(caveObject instanceof CaveParticle)
						&& distance < caveExplosive.getPower()*CaveDefaults.EXPLOSIVE_PRESSURE_DAMAGE_AREA) {
					double thrustDamage = (caveExplosive.getPower()/distance+1)*CaveDefaults.EXPLOSIVE_PRESSURE_DAMAGE_MULTIPLIER;
					if (! caveObject.isIndestructible() && caveObject.isCollidable() && thrustDamage > 0) {
						caveObject.reduceHealth(thrustDamage);
						caveObject.playSound(CaveSound.DAMAGE);
						if (! caveObject.isIndestructible()) {
							damagedObjects.add(caveObject);
						}
					}
				}
			}
		}
		for(CaveObject object : damagedObjects) {
			object.createDestructionParticles(CaveDefaults.OBJECT_HIT_PARTICLE_COUNT, cave);
		}
	}

}
