package rest;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import agents.Agent;

public interface AgentAPI {

	@POST
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	@Path("/message")
	public void sendMessageToAgent(String msg);

	@GET
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	@Path("/running")
	public Collection<Agent> getRunning();
	
	@POST
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	@Path("/newRemote")
	public void newRemote(Agent agent);
}
