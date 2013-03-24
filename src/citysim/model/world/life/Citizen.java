package citysim.model.world.life;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import citysim.model.world.WorldObject;
import citysim.model.world.infrastructure.House;
import citysim.model.world.infrastructure.Residence;
import citysim.model.world.infrastructure.exception.ResidenceAlreadyOccupiedException;
import citysim.model.world.life.exception.NotEnoughCashException;
import citysim.model.world.life.tasks.Task;
import citysim.model.world.map.City;
import citysim.model.world.map.District;
import citysim.model.world.map.exception.DistrictOutOfSpaceException;
import citysim.model.world.map.exception.ZoneTypeMismatchException;

public class Citizen extends WorldObject {
	private static final int STARTING_CASH = 10000;
	private static final int IDEAL_ENERGY = 480;
	private static final int IDEAL_NURISHMENT = 500;
	private static final int IDEAL_TEMPERATURE = 1000;
	
	private City _city;
	private Residence _home = null;
	private Company _employer = null;
	private Task _currentTask = null;
	private Queue<Task> _taskQueue = new LinkedList<Task>();

	private int _cash;
	private List<WorldObject> _inventory = new ArrayList<WorldObject>();
	
	private int _energy;
	private int _nurishment;
	private int _temperature;
	
	private int _sleepDuration = 0;
	
	private boolean _alive = true;
	
	public Citizen(City city) {
		this._city = city;
		this._cash = STARTING_CASH;
		this._energy = IDEAL_ENERGY;
		this._nurishment = IDEAL_NURISHMENT;
		this._temperature = IDEAL_TEMPERATURE;
	}
	
	public static boolean collectTax(Citizen citizen) {
		if(!citizen._city.isTaxDay()) {
			return true;
		}
		
		int taxes = calculateTax(citizen);
		
		if(taxes > citizen._cash) {
			return false;
		}
		
		citizen._cash -= taxes;
		citizen._city.payTaxes(taxes);
		return true;
	}
	
	private static int calculateTax(Citizen citizen) {
		int taxes = 0;
		
		taxes += citizen._city.getCityHall().getTaxRate(citizen._home);
		
		return taxes;
	}
	
	public void workOnTask() {
		if(this._currentTask != null && !this._currentTask.isComplete()) {
			this._currentTask.applyEffort();
		} else if(this.hasNextTask()) {
			this.startNextTask();
		}
	}
	
	public Task getCurrentTask() {
		return this._currentTask;
	}
	
	public boolean hasNextTask() {
		return this._taskQueue.size() > 0;
	}
	
	public void startNextTask() {
		if(!this.hasNextTask()) {
			return;
		}
		
		this._currentTask = this._taskQueue.poll();
		if(this._currentTask != null) {
			this._currentTask.start();
		}
	}
	
	public int getHealthIndex() {
		if(!this._alive) {
			return 0;
		}
		
		double nurishment = (double)Math.min(IDEAL_NURISHMENT, this._nurishment) / IDEAL_NURISHMENT;
		// Temp is a little complex to deal with being cold as well as overheating
		double temperature = (double)Math.min(IDEAL_TEMPERATURE, IDEAL_TEMPERATURE - Math.abs(IDEAL_TEMPERATURE - this._temperature)) / IDEAL_TEMPERATURE;
		double energy = (double)Math.min(IDEAL_ENERGY, this._energy) / IDEAL_ENERGY;
		
		System.out.format("NUR: %.2f; TEM: %.2f; NRG: %.2f\n", nurishment, temperature, energy);
		
		int index = (int)((nurishment * temperature * energy) * 100);
		
		return index;
	}
	
	public int getHunger() {
		return IDEAL_NURISHMENT - this._nurishment;
	}
	
	public int getCold() {
		return IDEAL_TEMPERATURE - this._temperature;
	}
	
	public int getExhaustion() {
		return IDEAL_ENERGY - this._energy;
	}
	
	public int getEnvironmentTemperature() {
		if(this._home == null) {
			return this._city.getTemperature();
		} else {
			return IDEAL_TEMPERATURE;
		}
	}
	
	public void applyEnvironmentTemperture(int envTemp) {
		int diff = this._temperature - envTemp;
		// Calc for fluid effect
		double shift = (double)diff * 0.01;
		this._temperature = (int)Math.ceil(this._temperature - shift);
	}
	
	public void applyHunger() {
		this._nurishment--;
	}
	
	public void applyExhaustion() {
		if(this._sleepDuration == 0) {
			this._energy--;
		}
	}
	
	public void applySleep() {
		if(this.isSleeping()) {
			this._sleepDuration--;
			this._energy += 2;
		}
	}
	
	public void goToSleep() {
		if(this.isSleeping()) {
			return;
		}
		
		// Use IDEAL_ENERGY as sleep duration so max energy = IDEAL * 2
		this._sleepDuration = IDEAL_ENERGY;
	}
	
	public boolean isSleeping() {
		return this._sleepDuration > 0;
	}
	
	public House buildHouse(District district) {
		try {
			return House.buildNew(district);
		} catch (ZoneTypeMismatchException e) {
			System.out.println("ERROR: District not residential. " + district.getId());
			System.exit(1);
		} catch (DistrictOutOfSpaceException e) {
			System.out.println("ERROR: No space for house in district. " + district.getId());
			System.exit(1);
		}

		return null;
	}
	
	public void moveIntoHome(Residence home) throws ResidenceAlreadyOccupiedException, NotEnoughCashException {
		if(!home.isVacant()) {
			throw new ResidenceAlreadyOccupiedException();
		}
		
		if(home.getValue() > this._cash) {
			throw new NotEnoughCashException();
		}
		
		this._cash -= home.getValue();
		
		home.setOwner(this);
		this._home = home;
	}
	
	public static void evictFromHome(Citizen citizen) {
		if(citizen._home == null) {
			return;
		}
		
		citizen._home.setOwner(null);
		citizen._home = null;
	}
	
	public int getCash() {
		return this._cash;
	}
	
	public String getName() {
		return this.getId().toString();
	}
	
	public Residence getHome() {
		return this._home;
	}
	
	public City getCity() {
		return this._city;
	}
	
	public boolean isAlive() {
		return this._alive;
	}
	
	public void kill() {
		this._energy = this._temperature = this._nurishment = 0;
		this._alive = false;
		
	}
}
