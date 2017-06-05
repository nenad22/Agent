package agents;

import model.AgentCenter;

public class AID{
	private String name;
	private AgentCenter host;
	private AgentType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AgentCenter getHost() {
		return host;
	}

	public void setHost(AgentCenter host) {
		this.host = host;
	}

	public AgentType getType() {
		return type;
	}

	public void setType(AgentType type) {
		this.type = type;
	}
/*
	@Override
	public boolean equals(Object obj) {
		AID a = (AID) obj;
		if(a.getHost().equals(this.getHost())){
			if(a.getName().equals(this.getName())){
				if(a.getType().equals(this.getType())){
					return true;
				}
			}
		}
		return false;
	}
	*/
}
