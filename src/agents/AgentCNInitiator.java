package agents;

import java.util.ArrayList;
import java.util.Random;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

import messages.ACLMessage;
import messages.Performative;
import rest.AgentAPI;

@SuppressWarnings("serial")
public class AgentCNInitiator extends Agent {

	@Override
	public void onMessage(ACLMessage message) {

		//System.out.println(message);
		int flag = 0;


		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget rtarget = client.target("http://localhost:8080/agent/agent/agents");
		AgentAPI rest = rtarget.proxy(AgentAPI.class);

		ACLMessage messageBack = new ACLMessage();

		ArrayList<AID> receivers = new ArrayList<AID>();
		receivers.add(message.getReplyTo());

		messageBack.setRecivers(receivers);
		messageBack.setSender(this.getId());
		messageBack.setReplyTo(this.getId());

		// STIGO REFUSE NA CFP
		if (message.getPerformative() == Performative.REFUSE) {
			flag = 0;
			messageBack.setPerformative(Performative.INFORM);
			messageBack.setContent("My Call For Proposal was refused!");
		}

		// STIGO PROPOSE NA CFP
		else if (message.getPerformative() == Performative.PROPOSE) {
			flag = 0;

			// ODLUCUJEM HOCU ACCEPT-PROPOSAL ILI REJECT-PROPOSAL
			Random randomNum = new Random(); 
			int result = randomNum.nextInt(2);

			// REJECT
			if (result == 0) {
				messageBack.setPerformative(Performative.REJECT_PROPOSAL);
				messageBack.setContent("I reject your proposal!");
			// ACCEPT
			} else {
				messageBack.setPerformative(Performative.ACCEPT_PROPOSAL);
				messageBack.setContent("I accept your proposal!");
			}
		}
		else if ((message.getPerformative() == Performative.NOT_UNDERSTOOD) && (message.getContent().equals("This Agent did not understand what was asked of it!"))){
			messageBack = null;
			flag = 1;
		}
		else if ((message.getPerformative() == Performative.INFORM) && (message.getContent().equals("My proposal was accepted. Yay!"))){
			messageBack = null;
			flag = 1;
		}
		else if ((message.getPerformative() == Performative.INFORM) && (message.getContent().equals("My proposal was rejected. Nay!"))){
			messageBack = null;
			flag = 1;
		}
		else {
			messageBack.setPerformative(Performative.NOT_UNDERSTOOD);
			messageBack.setContent("This Agent did not understand what was asked of it!");
		}

		///
		if (flag == 0) {
			rest.sendMessageToAgent(new Gson().toJson(messageBack));
		}
	}

}
