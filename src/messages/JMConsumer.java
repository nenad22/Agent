package messages;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

import agents.AID;
import agents.Agent;
import agents.AgentFactory;
import rest.AgentAPI;
import rest.AgentCenterEndpoints;

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
			for (AID aid : aclm.getRecivers()) {
				Agent a = AgentFactory.getAgent(aid.getName());
				if (a != null) {
					if (a.getId().getHost().getAlias().equals(AgentCenterEndpoints.me.getAlias()))
						a.onMessage(aclm);
					else {
						ResteasyClient client = new ResteasyClientBuilder().build();
						ResteasyWebTarget rtarget = client.target("http://" + a.getId().getHost().getAddress() + "/agent/agent/agents");
						AgentAPI rest = (AgentAPI) rtarget.proxy(AgentAPI.class);
						rest.sendMessageToAgent(tmsg.getText());
					}
				}
			}

		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}