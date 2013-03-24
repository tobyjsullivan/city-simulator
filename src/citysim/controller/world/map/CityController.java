package citysim.controller.world.map;

import citysim.controller.world.WorldController;
import citysim.model.world.map.City;

public class CityController extends WorldController {
	private City _city;
	
	public CityController (City city) {
		this._city = city;
	}
	
	public void step() {
		this._city.incrementTime();
	}
}
