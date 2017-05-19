package agents;

public abstract class Agent {
	private AID id;

	public abstract void onMessage();

	public AID getId() {
		return id;
	}

	public void setId(AID id) {
		this.id = id;
	}

}
