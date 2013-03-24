package citysim.model.meta;

import java.util.*;

import citysim.controller.world.life.CitizenController;
import citysim.controller.world.map.CityController;
import citysim.model.world.*;
import citysim.model.world.life.Citizen;
import citysim.model.world.map.City;
import citysim.model.world.map.District;



public class Game {
	private City _city = null;
	private List<Citizen> _citizens = new ArrayList<Citizen>();
	private long _totalSteps = 0;
	
	private int _totalDeaths = 0;
	
	public City getCity() {
		return this._city;
	}
	
	public Citizen[] getCitizens() {
		return this._citizens.toArray(new Citizen[0]);
	}
	
	public void addCitizen(Citizen newCitizen) {
		this._citizens.add(newCitizen);
	}
	
	public void printStatus() {
		System.out.format("DISTRICTS: %d\n", calcTotalDistricts());
		System.out.format("BUILDINGS: %d\n", calcTotalBuildings());
		System.out.format("POPULATION: %d\n", calcTotalCitizens());
		System.out.format("DATE: %s\n", this._city.getFormattedDate());
		System.out.format("CITY FUNDS: %d\n", this._city.getFunds());
		System.out.format("AVG. HEALTH: %d\n", calcAvgHealth());
		
		System.out.format("TOTAL DEATHS: %d\n", this._totalDeaths);
	}
	
	public boolean everyoneDead() {
		return this.calcTotalCitizens() == 0;
	}
	
	private int calcAvgHealth() {
		int numCit = this._citizens.size();
		if(numCit <= 0) {
			return 0;
		}
		
		int totalHealth = 0;
		for(Citizen cur : this._citizens) {
			totalHealth += cur.getHealthIndex();
		}
		
		return totalHealth / numCit;
	}
	
	private int calcAvgWealth() {
		int numCit = this._citizens.size();
		if(numCit <= 0) {
			return 0;
		}
		
		int totalWealth = 0;
		for(Citizen cur : this._citizens) {
			totalWealth += cur.getCash();
		}
		
		return totalWealth / numCit;
	}
	
	private int calcTotalDistricts() {
		return this._city.getDistricts().length;
	}
	
	private int calcTotalBuildings() {
		int total = 0;
		
		for(District cur : this._city.getDistricts()) {
			total += cur.getBuildings().length;
		}
		
		return total;
	}
	
	private int calcTotalCitizens() {
		int popCount = 0;
		
		for(Citizen cur : this._citizens) {
			if(cur.isAlive()) {
				popCount++;
			}
		}
		
		return popCount;
	}
	
	private void initialize() {
		this._city = City.generateCity();
	}
	
	public static Game NewGame() {
		Game game = new Game();
		game.initialize();
		
		return game;
	}
}
