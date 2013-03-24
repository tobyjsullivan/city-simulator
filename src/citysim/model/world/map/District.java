package citysim.model.world.map;

import java.util.*;

import citysim.model.world.WorldObject;
import citysim.model.world.infrastructure.Building;
import citysim.model.world.map.exception.DistrictNotEmptyException;
import citysim.model.world.map.exception.DistrictOutOfSpaceException;


public class District extends WorldObject {
	private int _numBlocks;
	private DistrictZoneType _zoning;
	private List<Building> _buildings = new ArrayList<Building>();
	
	public District(int numBlocks) {
		this._numBlocks = numBlocks;
		this._zoning = DistrictZoneType.UNZONED;
	}
	
	public int getNumBlocks() {
		return this._numBlocks;
	}
	
	public int getNumEmptyBlocks() {
		int occupiedBlocks = 0;
		
		for(Building cur : this._buildings) {
			occupiedBlocks += cur.getSize();
		}
		
		return this._numBlocks - occupiedBlocks;
	}
	
	public void rezone(DistrictZoneType zoning) throws DistrictNotEmptyException {
		if(zoning == this._zoning) {
			return;
		}
		
		if(this._buildings.size() > 0) {
			throw new DistrictNotEmptyException();
		}
		
		this._zoning = zoning;
	}
	
	public DistrictZoneType getZoning() {
		return this._zoning;
	}
	
	public Building[] getBuildings() {
		return this._buildings.toArray(new Building[0]);
	}
	
	public void addBuilding(Building building) throws DistrictOutOfSpaceException {
		if(this.getNumEmptyBlocks() < building.getSize()) {
			throw new DistrictOutOfSpaceException();
		}
		
		this._buildings.add(building);
	}
}
