package citysim.controller.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import citysim.controller.world.life.CitizenController;
import citysim.controller.world.map.CityController;
import citysim.model.meta.Game;
import citysim.model.world.life.Citizen;

public class GameController {
	private Game _game;
	
	public GameController(Game game) {
		this._game = game;
	}
	
	public void start() {
		Collection<Citizen> newArrivals = generateNewArrivals(2);
		
		for(Citizen newCitizen : newArrivals) {
			this._game.addCitizen(newCitizen);
		}
	}
	
	public void step() {
		CityController cityCtrl = new CityController(this._game.getCity());
		cityCtrl.step();
		
		// Step all citizens
		List<Citizen> died = new ArrayList<Citizen>();
		for (Citizen cur : this._game.getCitizens()) {
			CitizenController citizenCtrl = new CitizenController(cur);
			
			citizenCtrl.step();
		}
		
	}
	
	private Collection<Citizen> generateNewArrivals(int num) {
		List<Citizen> citizens = new ArrayList<Citizen>();
		
		for(int i = 0; i < num; i++){
			citizens.add(new Citizen(this._game.getCity()));
		}
		
		return citizens;
	}
}
