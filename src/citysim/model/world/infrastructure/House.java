package citysim.model.world.infrastructure;

import citysim.model.world.map.District;
import citysim.model.world.map.DistrictZoneType;
import citysim.model.world.map.exception.DistrictOutOfSpaceException;
import citysim.model.world.map.exception.ZoneTypeMismatchException;

public class House extends Residence {
	public final static int HOUSE_SIZE = 1;
	private final static int DEFAULT_HOUSE_VALUE = 5000;
	
	private int _value;
	
	private House() {
		this.setSize(HOUSE_SIZE);
		this.setValue(DEFAULT_HOUSE_VALUE);
	}
	
	public static House buildNew(District district) throws ZoneTypeMismatchException, DistrictOutOfSpaceException {
		if(district.getZoning() != DistrictZoneType.RESIDENTIAL) {
			throw new ZoneTypeMismatchException("Houses can only be built on residential districts");
		}
		
		House house = new House();
		Building.build(house, district);
		
		return house;
	}
}
