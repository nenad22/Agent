package aspects;

import java.io.IOException;

import javax.websocket.Session;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

import agents.Agent;
import messages.ACLMessage;
import messages.WebSocket;
import model.AgentCenter;
import rest.AgentAPI;
import rest.AgentCenterAPI;
import rest.AgentCenterEndpoints;

public aspect Aspect {

	@SuppressWarnings("unused")
	private static Gson gson = new Gson();

	/*
	 * pointcut webSocketResponse() : execution (* rest.AgentEndpoints.*(..));
	 * 
	 * Object around():webSocketResponse(){ Object r = proceed(); String
	 * methName = thisJoinPointStaticPart.getSignature().getName();
	 * 
	 * try { String json = gson.toJson(r); System.out.println(methName +
	 * " Json: " + gson.toJson(r)); for (Session session :
	 * WebSocket.sessions.values()) { session.getAsyncRemote().sendText(methName
	 * + json); } } catch (Exception e) {
	 * 
	 * System.out.println("JSON NEVALJA " + e); }
	 * 
	 * System.out.println(methName + " VRACA: " + r); return r;
	 * 
	 * }
	 */

	pointcut agentLogger() : execution (void agents.Agent.onMessage(..));

	before():agentLogger(){
		Agent a = (Agent) thisJoinPoint.getTarget();
		ACLMessage aclm = (ACLMessage) thisJoinPoint.getArgs()[0];

		String logMsg = "[AGENT CENTER] " + a.getId().getName() + " on " + a.getId().getHost().getAlias()
				+ " received a message from " + aclm.getSender().getName() + " on "
				+ aclm.getSender().getHost().getAlias() + ": " + aclm.getPerformative() + ", " + aclm.getContent();

		System.out.println(logMsg);

		for (Session s : WebSocket.sessions.values()) {
			try {
				s.getBasicRemote().sendText("message{" + logMsg + "}");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	pointcut newRemote() : execution (void rest.AgentEndpoints.newRemote(..));

	before():newRemote(){
		Agent agent = (Agent) thisJoinPoint.getArgs()[0];
		for (Session s : WebSocket.sessions.values()) {
			try {
				s.getBasicRemote().sendText(
						"startAgent" + gson.toJson(agent, Class.forName(agent.getId().getType().getModule())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	pointcut sendToOthers() : execution (Agent rest.AgentEndpoints.startAgent(..));

	Agent around():sendToOthers(){
		Agent agent = (Agent) proceed();
		System.out.println("Notifying other agent centers about creation...");
		for (AgentCenter ac : AgentCenterEndpoints.agentCenters.values()) {

			if (ac.getAlias().equals(agent.getId().getHost().getAlias()))
				continue;

			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = client.target("http://" + ac.getAddress() + "/agent/agent/agents");
			AgentAPI rest = rtarget.proxy(AgentAPI.class);
			rest.newRemote(agent);
		}

		return agent;

	}
	
	

}
