package agents;

import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;

import rest.AgentCenterEndpoints;

/**
 * Class for managing agents.
 * 
 * @author Nenad
 *
 */
@SuppressWarnings("rawtypes")
public class AgentFactory {
	/**
	 * Contains available agent classes on this node <Class name, Class object>
	 */
	public static HashMap<String, Class> agentClasses = new HashMap<String, Class>();

	public static Set<String> getAgentClasses() {
		if (agentClasses.isEmpty()) {
			Reflections reflections = new Reflections("agents");
			Set<Class<? extends Agent>> classes = reflections.getSubTypesOf(Agent.class);
			for (Class c : classes) {
				//System.out.println(c.getName());
				agentClasses.put(c.getName(), c);
			}
		}
		return agentClasses.keySet();
	}

	/**
	 * Contains created agents on this node <Agent name, Agent> or AID?
	 */
	public static HashMap<String, Agent> runningAgents = new HashMap<String, Agent>();

	/**
	 * Makes an agent given the class name. Agent class must have a default
	 * constructor
	 * 
	 * @param className
	 * @return
	 */
	public static Agent makeAgent(String className, String agentName) {
		if (!agentClasses.containsKey(className))
			return null;
		if (runningAgents.containsKey(agentName))
			return null;
		Class c = agentClasses.get(className);
		try {
			Agent a = (Agent) c.newInstance();
			AID aid = new AID();
			//aid.setHost(App.me);
			aid.setHost(AgentCenterEndpoints.me);
			aid.setName(agentName);
			AgentType at = new AgentType();
			at.setModule(className);
			at.setName(className);
			aid.setType(at);
			a.setId(aid);
			runningAgents.put(agentName, a);
			return a;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Agent getAgent(String agentName) {
		if (runningAgents.containsKey(agentName)) {
			return runningAgents.get(agentName);
		}
		return null;
	}

}
