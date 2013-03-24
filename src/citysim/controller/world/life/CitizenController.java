package citysim.controller.world.life;

import citysim.ErrorHandler;
import citysim.controller.world.WorldController;
import citysim.model.world.infrastructure.CityHall;
import citysim.model.world.infrastructure.House;
import citysim.model.world.infrastructure.Residence;
import citysim.model.world.infrastructure.exception.ResidenceAlreadyOccupiedException;
import citysim.model.world.life.Citizen;
import citysim.model.world.life.exception.NotEnoughCashException;
import citysim.model.world.map.District;
import citysim.model.world.map.DistrictZoneType;

public class CitizenController extends WorldController {
	private Citizen _citizen;
	
	public CitizenController(Citizen citizen) {
		this._citizen = citizen;
	}
	
	public void step() {
		if(!this._citizen.isAlive()) {
			return;
		}
		
		// Apply situational effects
		int temp = this._citizen.getEnvironmentTemperature();
		this._citizen.applyEnvironmentTemperture(temp);
		this._citizen.applyHunger();
		if(this._citizen.isSleeping()) {
			this._citizen.applySleep();
		} else {
			this._citizen.applyExhaustion();
		}
		
		if(this._citizen.getHealthIndex() < 1) {
			this._citizen.kill();
			return;
		}
		
		this._citizen.workOnTask();
		
		
		// Determine present focus
		int hunger = this._citizen.getHunger();
		int cold = this._citizen.getCold();
		int exhaustion = this._citizen.getExhaustion();
		
		if(hunger >= cold && hunger >= exhaustion) {
			// Eat
		} else if(cold >= hunger && cold >= exhaustion) {
			// Warm up
		} else {
			// Sleep
			this._citizen.goToSleep();
		}
	}
	
	private void ensureHoused() {
		if(this._citizen.getHome() == null) {
			CityHall cityHall = this._citizen.getCity().getCityHall();
			
			Residence[] availableHomes = cityHall.requestEmptyHouses();
			
			Residence newRes = null;
			if(availableHomes.length > 0) {
				newRes = availableHomes[0];
			} else {
				District[] districtsWithEmptyRes = cityHall.requestAvailableResidentialLand(House.HOUSE_SIZE);
				
				District availableLand = null;
				if(districtsWithEmptyRes.length > 0) {
					availableLand = districtsWithEmptyRes[0];
				} else {
					availableLand = cityHall.requestMoreZoning(DistrictZoneType.RESIDENTIAL);
				}
				
				if(availableLand != null) {
					newRes = this._citizen.buildHouse(availableLand);
				}
			}

			if(newRes != null && newRes.getValue() < this._citizen.getCash()) {
				try {
					this._citizen.moveIntoHome(newRes);
				} catch (ResidenceAlreadyOccupiedException e) {
					ErrorHandler.FailFromException(e, "Residence unavailable", newRes.getId());
				} catch (NotEnoughCashException e) {
					ErrorHandler.FailFromException(e, "Cannot afford new home", newRes.getId());
				}
			}
		}
	}
}
