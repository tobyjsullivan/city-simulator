package citysim.model.world.life.tasks;

import citysim.model.world.infrastructure.House;
import citysim.model.world.life.tasks.exception.TaskIncompleteException;
import citysim.model.world.map.District;
import citysim.model.world.map.exception.DistrictOutOfSpaceException;
import citysim.model.world.map.exception.ZoneTypeMismatchException;

public class ConstructHouseTask extends Task<House> {
	private static final int EFFORT = 30;
	
	private District _district;
	
	public ConstructHouseTask(District district) {
		super(EFFORT);
		this._district = district;
	}
	
	public House getProduct() throws TaskIncompleteException {
		if(!this.isComplete()) {
			throw new TaskIncompleteException();
		}
		
		House house = null;
		try {
			house = House.buildNew(this._district);
		} catch (ZoneTypeMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DistrictOutOfSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return house;
	}
}
