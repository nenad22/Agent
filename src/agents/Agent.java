package agents;

import java.io.Serializable;

import messages.ACLMessage;

public abstract class Agent implements Serializable {

	private static final long serialVersionUID = 1L;
	private AID id;

	public abstract void onMessage(ACLMessage message);

	public AID getId() {
		return id;
	}

	public void setId(AID id) {
		this.id = id;
	}

}
