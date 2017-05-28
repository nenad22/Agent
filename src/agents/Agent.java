package agents;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Agent implements Serializable {
	private AID id;

	public abstract void onMessage();

	public AID getId() {
		return id;
	}

	public void setId(AID id) {
		this.id = id;
	}

}
