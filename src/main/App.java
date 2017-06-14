package main;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import agents.AgentFactory;
import model.AgentCenter;
import rest.AgentCenterAPI;
import rest.AgentCenterEndpoints;
import rest.AgentEndpoints;

@ApplicationPath("/agent")
@Singleton
public class App extends Application {

	@PostConstruct
	public void init() {

		AgentFactory.getAgentClasses();
		AgentEndpoints.getMethods();
		AgentCenterEndpoints.setup();
		
		String master = "Master";
		String slave1 = "Slave 1";
		String slave2 = "Slave 2";
		
		if (AgentCenterEndpoints.amMaster) {
			AgentFactory.makeAgent("agents.AgentPing", master + " Ping");
			AgentFactory.makeAgent("agents.AgentPong", master + " Pong");
		}
		
		else if (AgentCenterEndpoints.me.getAlias().equals("slave1")) {
			AgentFactory.makeAgent("agents.AgentPing", slave1 + " Ping");
			AgentFactory.makeAgent("agents.AgentPong", slave1 + " Pong");
		}
		
		else if (AgentCenterEndpoints.me.getAlias().equals("slave2")) {
			AgentFactory.makeAgent("agents.AgentPing", slave2 + " Ping");
			AgentFactory.makeAgent("agents.AgentPong", slave2 + " Pong");
		}
	}
	
	@PreDestroy
	public void unregisterMyself() {
		AgentCenterEndpoints.killMe();
	}

	// HEARTBEAT
	
	//@Schedule(hour = "*", minute = "*/5", second = "*", info = "Every five minutes")
	
	@Schedule(hour = "*", minute = "*", second = "*/5", info = "Every fifth second")
	public void deklarativniTajmer() {
		
		Boolean skywalker = AgentCenterEndpoints.amMaster;
		
		if (skywalker) {
			System.out.println("Current time: " + new java.util.Date() + "");
			
			for (AgentCenter ac : AgentCenterEndpoints.agentCenters.values()) {
				
				if (ac.getAlias().equals(AgentCenterEndpoints.master.getAlias()))
					continue;
				
				String targetAdress = "http://" + ac.getAddress() + "/agent/agent/agent_centers";

				System.out.println("HEARTBEAT REST POZIV IDE NA: " + targetAdress);

				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = client.target(targetAdress);
				AgentCenterAPI rest = rtarget.proxy(AgentCenterAPI.class);

				Boolean alive = rest.heartbeat();
				
				if (alive) System.out.println((ac.getAlias() + " je ZIV!"));
				else  System.out.println((ac.getAlias() + " je MRTAV!"));

				
				Boolean dead = !alive;
				
				if (dead) {
					String targetAdressKill = "http://" + AgentCenterEndpoints.master.getAddress() + "/agent/agent/agent_centers";

					System.out.println("DELETE REST POZIV IDE NA: " + targetAdressKill);

					ResteasyClient client2 = new ResteasyClientBuilder().build();
					ResteasyWebTarget rtarget2 = client2.target(targetAdress);
					AgentCenterAPI rest2 = rtarget2.proxy(AgentCenterAPI.class);
					rest2.destroy_node(ac.getAlias());
				}
			}
		}
	}
}
