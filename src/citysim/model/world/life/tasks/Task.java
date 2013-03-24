package citysim.model.world.life.tasks;

import citysim.model.world.WorldObject;
import citysim.model.world.life.tasks.exception.TaskIncompleteException;

public abstract class Task<T extends WorldObject> extends WorldObject {
	private int _effort;
	private int _progress = -1;
	
	protected Task(int effort) {
		this._effort = effort;
	}
	
	public int getEffort() {
		return this._effort;
	}
	
	public void start() {
		this._progress = 0;
	}
	
	public boolean isStarted() {
		return this._progress > -1;
	}
	
	public void applyEffort() {
		this._progress++;
	}
	
	public int getProgress() {
		return this._progress;
	}
	
	public boolean isComplete() {
		return this._progress >= this._effort;
	}
	
	public abstract T getProduct() throws TaskIncompleteException;
}
