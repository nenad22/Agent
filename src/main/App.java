package main;

import java.io.FileNotFoundException;
import java.io.IOException;

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
		
		// Ovo treba da dodje sa front-enda:
		
		String MRdir = null;
		try {
			MRdir = PropertiesUtil.instance().readProperty("MRDir");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MRMaster.doWork(MRdir);
	}
}
