package messages;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import com.google.gson.Gson;

import agents.AID;
import agents.Agent;
import agents.AgentFactory;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/ToAgents") })
public class JMConsumer implements MessageListener {
	public static Gson gson = new Gson();
	@Override
	public void onMessage(Message message) {
		TextMessage tmsg = (TextMessage) message;
		try {
			ACLMessage aclm = (ACLMessage) gson.fromJson(tmsg.getText(), ACLMessage.class);
			for(AID aid: aclm.getRecivers()){
				Agent a = AgentFactory.getAgent(aid.getName());
				if(a!=null){
					a.onMessage(aclm);
				}
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
