package citysim.model.world.infrastructure;

import java.util.*;

import citysim.model.world.map.City;
import citysim.model.world.map.District;
import citysim.model.world.map.DistrictZoneType;
import citysim.model.world.map.exception.DistrictNotEmptyException;


public final class CityHall extends Building {
	private City _city;
	
	public CityHall(City city) {
		this._city = city;
	}
	
	public Residence[] requestEmptyHouses() {
		District[] residentialDistricts = requestDistrictsByZoning(DistrictZoneType.RESIDENTIAL);
		
		List<Residence> residences = new ArrayList<Residence>();
		for(District cur : residentialDistricts) {
			for(Building building : cur.getBuildings()) {
				if(building instanceof Residence) {
					Residence res = Residence.class.cast(building);
					if(res.isVacant()) {
						residences.add(res);
					}
				}
			}
		}
		
		return residences.toArray(new Residence[0]);
	}
	
	public District[] requestAvailableResidentialLand(int numBlocksNeeded) {
		District[] residentialDistricts = requestDistrictsByZoning(DistrictZoneType.RESIDENTIAL);
		
		List<District> available = new ArrayList<District>();
		for(District cur : residentialDistricts) {
			if(cur.getNumEmptyBlocks() >= numBlocksNeeded) {
				available.add(cur);
			}
		}
		
		return available.toArray(new District[0]);
	}
	
	public District requestMoreZoning(DistrictZoneType zoning) {
		for(District cur : this._city.getDistricts()) {
			if(cur.getZoning() == DistrictZoneType.UNZONED) {
				try {
					cur.rezone(zoning);
				} catch (DistrictNotEmptyException e) {
					System.out.println("ERROR: District cannot be rezoned. " + cur.getId());
					System.exit(1);
				}
				return cur;
			}
		}
		
		return null;
	}
	
	public District[] requestDistrictsByZoning(DistrictZoneType zoning) {
		List<District> districts = new ArrayList<District>();
		
		for(District cur : this._city.getDistricts()) {
			if(cur.getZoning() == zoning) {
				districts.add(cur);
			}
		}
		
		return districts.toArray(new District[0]);
	}
	
	public int getTaxRate(Residence residence) {
		if(residence == null) {
			return 0;
		}
		
		return residence.getSize() * 500;
	}
}
