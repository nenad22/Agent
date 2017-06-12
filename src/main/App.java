package main;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import agents.AgentFactory;
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
		
		AgentFactory.makeAgent("agents.AgentPing", "Master_Ping");
		AgentFactory.makeAgent("agents.AgentPong", "Master_Pong");
	}
	
	@PreDestroy
	public void unregisterMyself() {
		AgentCenterEndpoints.killMe();
	}

}
