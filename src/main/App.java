package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.gson.Gson;

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
		readProperties();
		/*
		 * System.out.println(">>> Hi! Ovo je post-construct metoda App-a!");
		 * 
		 * System.out.println(">>> Startujem AgentMapReduceMaster");
		 * AgentMapReduceMaster MRMaster = new AgentMapReduceMaster();
		 * 
		 * // Ovo dodje sa front-enda String dir = "C:\\!AGENTSKE_TEST";
		 * MRMaster.doWork(dir);
		 */
		// Ovo treba da dodje sa front-enda:

		/*
		 * String MRdir = null; try { MRdir =
		 * PropertiesUtil.instance().readProperty("MRDir"); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); } catch (IOException
		 * e) { e.printStackTrace(); }
		 * 
		 * MRMaster.doWork(MRdir);
		 */
	}

	/**
	 * Reads properties from the file and registers this node to master.
	 */
	private void readProperties() {

		Properties prop = new Properties();
		InputStream input = null;
		master = new AgentCenter();
		me = new AgentCenter();
		try {

			input = new FileInputStream("properties.properties");

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			master.setAddress(prop.getProperty("masterAddress"));
			master.setAlias(prop.getProperty("masterAlias"));
			me.setAddress(prop.getProperty("meAddress"));
			me.setAlias(prop.getProperty("meAlias"));

			if (master.getAddress().equals(me.getAddress())) {
				amIMaster = true;
			} else {
				amIMaster = false;
				registerMyself();
			}

			System.out.println(prop.getProperty("meAlias"));
			System.out.println(prop.getProperty("meAddress"));
			System.out.println(prop.getProperty("masterAlias"));
			System.out.println(prop.getProperty("masterAddress"));

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void registerMyself() {
		// TODO zeroMQ to master

	}

	public static void main(String[] args) {
		Set<String> x = AgentFactory.getAgentClasses();
		for (String s : x) {
			// Agent a = AgentFactory.makeAgent(s,"asd");
			// System.out.println(a.getClass());
		}
		System.out.println("\n");
		AgentEndpoints ae = new AgentEndpoints();
		x = AgentEndpoints.getMethods();
		String json = new Gson().toJson(x);
		System.out.println(json);
		for (String s : x) {
			System.out.println(s);
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
