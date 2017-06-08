package agents;

import java.util.ArrayList;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

import messages.ACLMessage;
import messages.Performative;
import rest.AgentAPI;

@SuppressWarnings("serial")
public class AgentPing extends Agent {

	@Override
	public void onMessage(ACLMessage message) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget rtarget = client.target("http://localhost:8080/agent/agent/agents");
		AgentAPI rest = rtarget.proxy(AgentAPI.class);

		ACLMessage messageBack = new ACLMessage();

		ArrayList<AID> receivers = new ArrayList<AID>();
		receivers.add(message.getReplyTo());

		messageBack.setRecivers(receivers);
		messageBack.setSender(this.getId());
		messageBack.setReplyTo(this.getId());
		
		int flag = 0;
		
		if (
				(
						(message.getPerformative() == Performative.NOT_UNDERSTOOD) &&
					(message.getContent().equals("This Agent did not understand what was asked of it!"))
				)
				
				||
				
				(
						(message.getPerformative() == Performative.INFORM) &&
						(message.getContent().equals("Pong!"))
				)
				
				
		){
			messageBack = null;
			flag = 1;
		} else {
			messageBack.setPerformative(Performative.NOT_UNDERSTOOD);
			messageBack.setContent("This Agent did not understand what was asked of it!");
		}
	
		if (flag == 0) {
			rest.sendMessageToAgent(new Gson().toJson(messageBack));
		}
	}
}
