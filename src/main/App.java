package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import agents.AgentFactory;
import model.AgentCenter;
import rest.AgentEndpoints;

@ApplicationPath("/agent")
@Singleton
public class App extends Application {
	public static AgentCenter me;
	public static AgentCenter master;
	public static boolean amIMaster;

	@PostConstruct
	public void init() {

		AgentFactory.getAgentClasses();
		AgentEndpoints.getMethods();
		setup();
	}

	/**
	 * Reads properties from the file and registers this node to master.
	 */
	private void setup() {
		
		PropertiesUtil util = PropertiesUtil.instance();
		
		master = new AgentCenter();
		me = new AgentCenter();
		
		try {
			master.setAddress(util.readProperty("masterAddress"));
			master.setAlias(util.readProperty("masterAlias"));
			me.setAddress(util.readProperty("meAddress"));
			me.setAlias(util.readProperty("meAlias"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		if (master.getAddress().equals(me.getAddress())) {
			amIMaster = true;
		} else {
			amIMaster = false;
			registerMyself();
		}
	}

	private void registerMyself() {
	}

	
	public static void main(String[] args) {

		Set<String> x = AgentFactory.getAgentClasses();
		
		//String commsAgentName = master.getAlias() + " - Comms Agent";
		//Agent a = AgentFactory.makeAgent("agents.AgentCenterComm", commsAgentName);

		System.out.println("\n");
		
		AgentEndpoints ae = new AgentEndpoints();
		x = AgentEndpoints.getMethods();
		
		//String json = new Gson().toJson(x);
		//System.out.println(json);
		
		for (String s : x) {
			
			//System.out.println(s);
			
			Method m = AgentEndpoints.getMethod(s);
			try {
				m.invoke(ae, null);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}
}
