package caveat.object;

import java.util.ArrayList;
import java.util.HashMap;

import caveat.engine.CaveDefaults;
import caveat.object.type.CaveItemFeature;
import caveat.object.util.CaveMove;
import caveat.object.util.CavePosition;

public abstract class CaveItem extends CaveObject {

	private int counter = CaveDefaults.COUNTER_INVALID;
	private HashMap<CaveItemFeature,Integer> features;
	public CaveItem() {
	}
	
	public CaveItem(CavePosition cavePosition, CaveMove caveMove) {
		this(cavePosition, caveMove, CaveDefaults.ITEM_SIZE,  CaveDefaults.ITEM_WEIGHT, CaveDefaults.COUNTER_INVALID);
	}
	
	public CaveItem(CavePosition position, CaveMove move, int size, int weight, int counter) {
		super();
		setCavePosition(position);
		setCaveMove(move);
		this.counter = counter;
		setSize(size);
		setWeight(weight);
		features = new HashMap<CaveItemFeature, Integer>();
		features.put(CaveItemFeature.LIGHT, -1);
		features.put(CaveItemFeature.EXPLOSIVE, -1);
		features.put(CaveItemFeature.TORCH, -1);
		features.put(CaveItemFeature.PLAYER_ITEM, -1);
	}
	
	public int getCounter() {
		return counter;
	}

	public boolean hasFeature(CaveItemFeature feature) {
		return features.get(feature) > 0;
	}

	public Integer getFeature(CaveItemFeature feature) {
		return features.get(feature);
	}

	public void setFeature(CaveItemFeature feature, int value) {
		features.put(feature, value);
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public CavePosition getCenterPosition(Cave cave) {
		CavePosition halfSizeToMiddle = getRelativeDisplacementForSizeWithAngle(Math.PI/4-getCaveMove().getDirection());
		return getCavePosition().add(halfSizeToMiddle);
	}
}
