package citysim.model.world.infrastructure;

import citysim.model.world.life.Citizen;

public abstract class Residence extends Building {
	private Citizen _owner = null;
	private int _value;
	
	public boolean isVacant() {
		return this._owner == null;
	}
	
	public void setOwner(Citizen owner) {
		this._owner = owner;
	}
	
	protected void setValue(int value) {
		this._value = value;
	}
	
	public int getValue() {
		return this._value;
	}
}
