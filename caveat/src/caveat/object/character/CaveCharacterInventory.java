package caveat.object.character;

import java.util.ArrayList;

import caveat.object.CaveItem;
import caveat.object.CaveObject;
import caveat.object.type.CaveObjectType;

public class CaveCharacterInventory {

	ArrayList<CaveItem> caveItems;
	
	private CaveObjectType activeItemType;
	
	public CaveCharacterInventory() {
		caveItems = new ArrayList<CaveItem>();
		setActiveItemType(CaveObjectType.EXPLOSIVE);
	}

	public ArrayList<CaveItem> getItems() {
		return caveItems;
	}

	public void remove(CaveObject item) {
		if (caveItems.contains(item)) {
			caveItems.remove(item);
		}
	}

	public void add(CaveItem caveItem) {
		caveItems.add(caveItem);
	}

	public void setActiveItemType(CaveObjectType activeItemType) {
		this.activeItemType = activeItemType;
	}

	public CaveObjectType getActiveItemType() {
		return activeItemType;
	}

}
