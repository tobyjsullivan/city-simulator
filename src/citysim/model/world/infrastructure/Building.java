package citysim.model.world.infrastructure;

import citysim.model.world.WorldObject;
import citysim.model.world.map.District;
import citysim.model.world.map.exception.DistrictOutOfSpaceException;

public abstract class Building extends WorldObject {
	private int _size;
	private District _district;
	
	
	protected void setSize(int size) {
		this._size = size;
	}
	
	public int getSize() {
		return this._size;
	}
	
	protected static void build(Building building, District district) throws DistrictOutOfSpaceException {		
		if(district.getNumEmptyBlocks() < building.getSize()) {
			throw new DistrictOutOfSpaceException();
		}
		
		building._district = district;
		district.addBuilding(building);
	}
}
