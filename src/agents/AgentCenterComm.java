package agents;

import messages.ACLMessage;

@SuppressWarnings("serial")
public class AgentCenterComm extends Agent {

	@Override
	public void onMessage(ACLMessage message) {
		/*
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget rtarget = client.target("http://localhost:8080/agent/agent/agents");
		AgentAPI rest = rtarget.proxy(AgentAPI.class);

		ACLMessage messageBack = new ACLMessage();

		ArrayList<AID> receivers = new ArrayList<AID>();
		receivers.add(message.getReplyTo());

		messageBack.setPerformative(Performative.INFORM);
		messageBack.setRecivers(receivers);
		messageBack.setSender(this.getId());
		messageBack.setReplyTo(this.getId());
		messageBack.setContent("This Agent does not understand messages sent to it, it is merely a facilitator for sending \"neutral\" messages to other agents!");

		rest.sendMessageToAgent(new Gson().toJson(messageBack));
		*/
	}
}
