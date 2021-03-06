package rest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.websocket.Session;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

import agents.Agent;
import agents.AgentFactory;
import exceptions.AliasExistsException;
import main.PropertiesUtil;
import messages.WebSocket;
import model.AgentCenter;

@Produces({ "application/json" })
@Consumes({ "application/json" })
@Path("/agent_centers")
@Stateless
public class AgentCenterEndpoints {

	public static AgentCenter me;
	public static AgentCenter master;
	public static HashMap<String, AgentCenter> agentCenters = new HashMap<String, AgentCenter>();

	public static boolean amMaster;

	public static void setup() {
		PropertiesUtil util = PropertiesUtil.instance();

		master = new AgentCenter();
		me = new AgentCenter();

		try {
			master.setAddress(util.readProperty("masterAddress"));
			master.setAlias(util.readProperty("masterAlias"));
			me.setAddress(util.readProperty("meAddress"));
			me.setAlias(util.readProperty("meAlias"));
		} catch (FileNotFoundException e1) {
			System.out.println("So useful:");
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("So useful:");
			e1.printStackTrace();
		}

		int diag = 1;
		// int diag = 0;

		if (diag == 1) {
			System.out.println(me.getAlias() + " :: " + ">>>>\tME:");
			System.out.println(me.getAlias() + " :: " + ">>>>\t" + me.getAlias());
			System.out.println(me.getAlias() + " :: " + ">>>>\t" + me.getAddress());
		}

		amMaster = (master.getAddress().equals(me.getAddress())) ? true : false;
		registerMyself();

		if (!amMaster) {
			// getRunningAgents();
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = client.target("http://" + master.getAddress() + "/agent/agent/agents");
			AgentAPI rest = (AgentAPI) rtarget.proxy(AgentAPI.class);
			Collection<Agent> runningAgentsFromMaster = rest.getRunning();

			System.out.println(runningAgentsFromMaster.size());

			for (Agent agent : runningAgentsFromMaster) {
				System.out.println(me.getAlias() + " :: " + "Dobio sam agenta: " + agent + ", od mastera.");
				AgentFactory.runningAgents.put(agent.getId().getName(), agent);

			}
		}
	}

	private static void registerMyself() {

		System.out.println(me.getAlias() + " :: " + "Registering myself!");

		// AKO JESAM MASTER SAMO ME UBACI KOD MENE U SPISAK
		if (amMaster) {
			agentCenters.clear();
			agentCenters.put(me.getAlias(), me);
		}

		// AKO NISAM MASTER, REGISTRUJ ME KOD MASTERA
		else {
			ResteasyClient client = new ResteasyClientBuilder().build();

			String targetAdress = "http://" + master.getAddress() + "/agent/agent/agent_centers";

			System.out.println(me.getAlias() + " :: " + "Pucam register na: " + targetAdress);

			ResteasyWebTarget rtarget = client.target(targetAdress);
			AgentCenterAPI rest = rtarget.proxy(AgentCenterAPI.class);
			Collection<AgentCenter> agentCentersFromMaster = rest.node(me);

			agentCenters = new HashMap<>();

			for (AgentCenter acfm : agentCentersFromMaster) {
				agentCenters.put(acfm.getAlias(), acfm);
			}
		}

		System.out.println(me.getAlias() + " :: " + "Svi registrovani kod mene su:\n" + Arrays.asList(agentCenters));
	}

	@POST
	@Path("/node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<AgentCenter> node(AgentCenter agentCenter) throws AliasExistsException {

		System.out.println(
				me.getAlias() + " :: " + "Stigo mi je request /node!\nAgent za registraciju je: " + agentCenter);

		// AKO SAM MASTER - JAVIM SVIM OSTALIMA DA REGISTRUJU
		if (amMaster) {

			System.out.println(
					me.getAlias() + " :: " + "Ja sam master dakle saljem sledecim agentskim centrima ovaj novi!");

			// ZA SVE AGENTSKE CENTRE NA SPISKU
			for (AgentCenter ac : agentCenters.values()) {

				// SAMOM SEBI NEMOJ DA JAVLJAS
				if (ac.getAlias().equals(master.getAlias()))
					continue;

				String targetAdress = "http://" + ac.getAddress() + "/agent/agent/agent_centers";

				System.out.println(me.getAlias() + " :: " + "REST /node POZIV IDE NA: " + targetAdress);

				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = client.target(targetAdress);
				AgentCenterAPI rest = rtarget.proxy(AgentCenterAPI.class);
				rest.node(agentCenter);
			}
		}

		// A ONDA TI REGISTRUJ
		String aliasCenter = agentCenter.getAlias();

		if (agentCenters.containsKey(aliasCenter)) {
			System.out.println(me.getAlias() + " :: " + "! > ! > ! > Vec postoji alias!");
			throw new AliasExistsException(aliasCenter);
		}

		agentCenters.put(agentCenter.getAlias(), agentCenter);

		System.out.println(me.getAlias() + " :: " + "Svi registrovani kod mene su:\n" + Arrays.asList(agentCenters));

		return agentCenters.values();
	}

	@DELETE
	@Path("/destroy_node")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void destroy_node(String destroyedAlias) {

		System.out.println("Stigo mi je request /destroy_node!\nAgent za brisanje je: " + destroyedAlias);

		// SKLONI AGENTSKI CENTAR IZ SPISKA
		agentCenters.remove(destroyedAlias);

		// Skloni njegove agente
		Gson gson = new Gson();
		ArrayList<String> toRemove = new ArrayList<String>();
		for (Agent a : AgentFactory.runningAgents.values()) {
			if (a.getId().getHost().getAlias().equals(destroyedAlias)) {
				toRemove.add(a.getId().getName());
				for (Session sess : WebSocket.sessions.values()) {
					try {
						System.out.println("stopAgent");
						sess.getBasicRemote()
								.sendText("stopAgent" + gson.toJson(a, Class.forName(a.getId().getType().getModule())));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		for (String s : toRemove) {
			System.out.println(s);
			AgentFactory.runningAgents.remove(s);
		}

		// AKO SI I MASTER, JAVI OSTALIM DA SKLONE
		if (amMaster) {
			// ZA SVE AGENTSKE CENTRE NA SPISKU
			for (AgentCenter ac : agentCenters.values()) {

				// SAMOM SEBI NEMOJ DA JAVLJAS
				if (ac.getAlias().equals(master.getAlias()))
					continue;

				String targetAdress = "http://" + ac.getAddress() + "/agent/agent/agent_centers";

				System.out.println("REST POZIV IDE NA: " + targetAdress);

				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = client.target(targetAdress);
				AgentCenterAPI rest = rtarget.proxy(AgentCenterAPI.class);
				rest.destroy_node(destroyedAlias);
			}
		}

		// OBJAVI KOJI SU TI OSTALI
		System.out.println("Ja sam " + me.getAlias() + " i svi registrovani kod mene su:");
		System.out.println(Arrays.asList(agentCenters));
	}

	@GET
	@Path("/heartbeat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean heartbeat() {
		System.out.println(me.getAlias() + " :: " + "Stigo mi je request heartbeat!");
		return true;
	}

	public static void killMe() {
		if (!amMaster) {
			String targetAdress = "http://" + master.getAddress() + "/agent/agent/agent_centers";

			System.out.println("DELETE REST POZIV IDE NA: " + targetAdress);

			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = client.target(targetAdress);
			AgentCenterAPI rest = rtarget.proxy(AgentCenterAPI.class);
			rest.destroy_node(me.getAlias());
		}
	}
}
