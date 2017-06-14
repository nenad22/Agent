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
public class AgentCNParticipant extends Agent {
	
	@Override
	public void onMessage(ACLMessage message) {

		//System.out.println(message);
		int flag = 0;
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget rtarget = client.target("http://" + message.getReplyTo() + "/agent/agent/agents");
		AgentAPI rest = rtarget.proxy(AgentAPI.class);

		ACLMessage messageBack = new ACLMessage();

		ArrayList<AID> receivers = new ArrayList<AID>();
		receivers.add(message.getReplyTo());
		
		messageBack.setRecivers(receivers);
		messageBack.setSender(this.getId());
		messageBack.setReplyTo(this.getId());
		
		if (message.getPerformative() == Performative.CFP) {
			
			flag = 0;
			// ODLUCUJEM HOCU PROPOSE ILI REFUSE
			Random randomNum = new Random(); 
			int result = randomNum.nextInt(2);
						
			// REFUSE
		    if (result == 0) {
		    	messageBack.setPerformative(Performative.REFUSE);
				messageBack.setContent("I refuse your Call For Proposal!");
		    // PROPOSE
		    } else {
		    	messageBack.setPerformative(Performative.PROPOSE);
		    	messageBack.setContent("I propose in response to your Call For Proposal!");
		    }
		}
		else if (message.getPerformative() == Performative.ACCEPT_PROPOSAL) {
			
			flag = 0;
			messageBack.setPerformative(Performative.INFORM);
			messageBack.setContent("My proposal was accepted. Yay!");
		}
		else if (message.getPerformative() == Performative.REJECT_PROPOSAL) {
			
			flag = 0;
			messageBack.setPerformative(Performative.INFORM);
			messageBack.setContent("My proposal was rejected. Nay!");
		}	
		
		else if ((message.getPerformative() == Performative.INFORM)	&& (message.getContent().equals("My Call For Proposal was refused!"))) {
			messageBack = null;
			flag = 1;
		}	
		
		else if ((message.getPerformative() == Performative.NOT_UNDERSTOOD) && (message.getContent().equals("This Agent did not understand what was asked of it!"))){
			messageBack = null;
			flag = 1;
		} else {
			messageBack.setPerformative(Performative.NOT_UNDERSTOOD);
			messageBack.setContent("This Agent did not understand what was asked of it!");
		}
	
		///
		if (flag == 0) {
			rest.sendMessageToAgent(new Gson().toJson(messageBack));
		}
	}

}
