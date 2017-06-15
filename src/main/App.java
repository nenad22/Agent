package main;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
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

		if (AgentCenterEndpoints.amMaster) {
			AgentFactory.makeAgent("agents.AgentPing", master + " Ping");
			AgentFactory.makeAgent("agents.AgentPong", master + " Pong");
		}

	}

	@PreDestroy
	public void unregisterMyself() {
		AgentCenterEndpoints.killMe();
	}

	// HEARTBEAT

	// @Schedule(hour = "*", minute = "*/5", second = "*", info = "Every five
	// minutes")
	@EJB
	AgentCenterEndpoints ace;

	@Schedule(hour = "*", minute = "*", second = "*/10", info = "Every tenth second")
	public void deklarativniTajmer() {

		Boolean skywalker = AgentCenterEndpoints.amMaster;
		Collection<AgentCenter> list = AgentCenterEndpoints.agentCenters.values();
		if (skywalker) {
			System.out.println("Current time: " + new java.util.Date() + "");

			for (AgentCenter ac : list) {

				if (ac.getAlias().equals(AgentCenterEndpoints.master.getAlias()))
					continue;

				String targetAdress = "http://" + ac.getAddress() + "/agent/agent/agent_centers";

				System.out.println("HEARTBEAT REST POZIV IDE NA: " + targetAdress);

				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = client.target(targetAdress);
				AgentCenterAPI rest = rtarget.proxy(AgentCenterAPI.class);

				try {
					rest.heartbeat();
				} catch (Exception e) {
					ace.destroy_node(ac.getAlias());
					return;
				}

			}
		}
	}
}
