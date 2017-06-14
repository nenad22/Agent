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
public class AgentPong extends Agent {

	@Override
	public void onMessage(ACLMessage message) {

		System.out.println(message);
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget rtarget = client.target("http://" + message.getReplyTo().getHost().getAddress() + "/agent/agent/agents");
		AgentAPI rest = rtarget.proxy(AgentAPI.class);

		ACLMessage messageBack = new ACLMessage();

		ArrayList<AID> receivers = new ArrayList<AID>();
		receivers.add(message.getReplyTo());
		
		messageBack.setRecivers(receivers);
		messageBack.setSender(this.getId());
		messageBack.setReplyTo(this.getId());
		
		int flag = 0;
		
		if ((message.getPerformative() == Performative.REQUEST) && (message.getContent().equals("Ping"))){
			messageBack.setPerformative(Performative.INFORM);
			messageBack.setContent("Pong!");
		}
		else if ((message.getPerformative() == Performative.NOT_UNDERSTOOD) && (message.getContent().equals("This Agent did not understand what was asked of it!"))){
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
