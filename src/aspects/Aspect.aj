package aspects;

import javax.websocket.Session;

import com.google.gson.Gson;

import agents.Agent;
import messages.ACLMessage;
import messages.WebSocket;

public aspect Aspect {
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
		System.out.println("[AGENT CENTER] "+ a.getId().getName() + " on " + a.getId().getHost().getAlias() + " reviced a message from "
				+ aclm.getSender().getName() + " on " + aclm.getSender().getHost().getAlias() + ": "
				+ aclm.getPerformative() + ", " + aclm.getContent());

	}

}
