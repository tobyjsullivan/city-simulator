package citysim.model.world.map;

import java.util.ArrayList;
import java.util.List;

import citysim.model.world.infrastructure.CityHall;


public class City {
	private final static int DEFAULT_MAP_SIZE = 300;
	private final static int DISTRICT_SIZE = 10;
	private final static int STARTING_FUNDS = 1000000;
	
	private int _minuteOfDay = 0;
	private int _dayOfYear = 1;
	private int _year = 1;
	
	private long _funds;
	private CityHall _cityHall;
	private District[] _districts;
	
	private City() { 
		this._cityHall = new CityHall(this);
		this._funds = STARTING_FUNDS;
	}
	
	public void incrementTime() {
		this._minuteOfDay++;
		if(this._minuteOfDay > 24*60) {
			this._minuteOfDay = 1;
			incrementDay();
		}
	}
	
	private void incrementDay() {
		this._dayOfYear++;
		if(this._dayOfYear > 360) {
			this._dayOfYear = 1;
			this._year++;
		}
	}
	
	public int getMinute() {
		return this._minuteOfDay % 24;
	}
	
	public int getHour() {
		return this._minuteOfDay / 24;
	}
	
	public int getDayOfYear() {
		return this._dayOfYear;
	}
	
	public int getYear() {
		return this._year;
	}
	
	public String getFormattedDate() {
		return String.format("%02d:%02d %d, %d", this.getHour(), this.getMinute(), this.getDayOfYear(), this.getYear());
	}

	public boolean isTaxDay() {
		return this._dayOfYear % 30 == 0;
	}
	
	public long getFunds() {
		return this._funds;
	}
	
	public CityHall getCityHall() {
		return this._cityHall;
	}
	
	public District[] getDistricts() {
		return this._districts;
	}
	
	public int getTemperature() {
		return 300;
	}
	
	private static District[] generateMapDistricts(int totalBlocks) {
		List<District> districts = new ArrayList<District>();
		
		while(totalBlocks > 0) {
			int size = DISTRICT_SIZE;
			if(size > totalBlocks) {
				size = totalBlocks;
			}
			
			districts.add(new District(size));
			
			totalBlocks -= size;
		}
		
		return districts.toArray(new District[0]);
	}
	
	private void generate() {
		this._districts = generateMapDistricts(DEFAULT_MAP_SIZE);
	}
	
	public void payTaxes(int amount) {
		this._funds += amount;
	}
	
	public static City generateCity() {
		City city = new City();
		city.generate();
		
		return city;
	}
}
