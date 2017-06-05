package rest;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import agents.AID;
import agents.Agent;
import agents.AgentFactory;
import messages.AgentHelper;
import messages.Performative;

@Path("/agents")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
@Stateless
public class AgentEndpoints {

	public static HashMap<String, Method> methods = new HashMap<>();
	private static Gson gson = new Gson();
	@GET
	@Path("/classes")
	public Set<String> getClasses() {
		return AgentFactory.agentClasses.keySet();
	}

	@GET
	@Path("/running")
	public Collection<Agent> getRunning() {
		return AgentFactory.runningAgents.values();
	}

	@POST
	@Path("/start")
	public Agent startAgent(AgentHelper ah) {
		Agent a = AgentFactory.makeAgent(ah.getClassName(), ah.getAgentName());
		return a;

	}

	@DELETE
	@Path("/stop")
	public void stopAgent(AID aid) {
		AgentFactory.runningAgents.remove(aid.getName());
	}

	@POST
	@Path("/message")
	public void sendMessageToAgent(String message) {
		Context context;
		try {
			context = new InitialContext();

			ConnectionFactory cf = (ConnectionFactory) context
					.lookup("java:jboss/exported/jms/RemoteConnectionFactory");
			final Queue queue = (Queue) context.lookup("java:/jms/queue/ToAgents");
			context.close();
			Connection connection = cf.createConnection("guest", "guest");
			final javax.jms.Session sess = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
			connection.start();
			TextMessage msg = sess.createTextMessage(message);
			MessageProducer producer = sess.createProducer(queue);
			producer.send(msg);
			producer.close();
			connection.stop();
			connection.close();
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("/performatives")
	public Performative[] getPerformatives() {
		return Performative.values();
	}

	@GET
	@Path("/methods")
	public static Set<String> getMethods() {
		if (methods.isEmpty()) {
			try {
				Class c = Class.forName("java.lang.Object");
				Method[] met = c.getMethods();
				for (Method m : met) {
					methods.put(m.getName(), null);
				}
				c = Class.forName("rest.AgentEndpoints");
				met = c.getMethods();
				for (Method m : met) {
					if (methods.containsKey(m.getName()))
						methods.remove(m.getName());
					else
						methods.put(m.getName(), m);
				}
				methods.remove("getMethod");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return methods.keySet();

	}

	public static Method getMethod(String methodName) {
		if (methods.containsKey(methodName)) {
			return methods.get(methodName);
		}
		return null;

	}
}