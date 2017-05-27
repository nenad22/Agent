package main;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import agents.AgentMapReduceMaster;

@ApplicationPath("/agent")
@Singleton
public class App extends Application {
	
	@PostConstruct
	public void sup() {
		System.out.println(">>> Hi! Ovo je post-construct metoda App-a!");

		System.out.println(">>> Startujem AgentMapReduceMaster");
		AgentMapReduceMaster MRMaster = new AgentMapReduceMaster();
		
		// Ovo dodje sa front-enda
		String dir = "C:\\!AGENTSKE_TEST";
		MRMaster.doWork(dir);
	}
}
