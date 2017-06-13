package agents;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import messages.ACLMessage;


@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
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

	@Override
	public String toString() {
		return "Agent [id=" + id.getName() + "]";
	}

}
