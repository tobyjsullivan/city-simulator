package citysim.model.world;

import java.util.UUID;

public abstract class WorldObject {
	private UUID _id;
	
	public WorldObject() {
		this._id = UUID.randomUUID();
	}
	
	public UUID getId() {
		return this._id;
	}
}
