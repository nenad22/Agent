package agents;

import java.io.File;
import java.util.ArrayList;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.google.gson.Gson;

import messages.ACLMessage;
import messages.Performative;
import rest.AgentAPI;

@SuppressWarnings("serial")
public class AgentMapReduceMaster extends Agent {

	public AgentMapReduceMaster() {
		super();
	}

	@Override
	public void onMessage(ACLMessage message) {

		String content = message.getContent();

		String dir = "";
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget rtarget = client.target("http://" + message.getReplyTo().getHost().getAddress() + "/agent/agent/agents");
		AgentAPI rest = rtarget.proxy(AgentAPI.class);

		ACLMessage messageBack = new ACLMessage();

		ArrayList<AID> receivers = new ArrayList<AID>();
		receivers.add(message.getReplyTo());
		
		messageBack.setRecivers(receivers);
		messageBack.setSender(this.getId());
		messageBack.setReplyTo(this.getId());
		
		int flag = 0;
		if (content.contains(" - ") && (message.getPerformative() == Performative.REQUEST)) {
			String[] parts = content.split(" - ");
			dir = parts[1];

			Integer reci = analyzeDir(dir);

			messageBack.setPerformative(Performative.INFORM);
			messageBack.setContent("Total number of words in the specified folder is: " + reci);
		}

		else if (message.getContent().equals("This Agent did not understand what was asked of it!")){
			messageBack = null;
			flag = 1;
		}
		else {
			System.out.println("MAP REDUCE MASTER: Ne valja msg!\nMAP REDUCE MASTER: Treba da pise Analyze Directory - C:\\!AGENTSKE_TEST");
			
			messageBack.setPerformative(Performative.NOT_UNDERSTOOD);
			messageBack.setContent("This Agent did not understand what was asked of it!");
		}
		
		if (flag==0) { 
			rest.sendMessageToAgent(new Gson().toJson(messageBack));
		}

	}

	public int analyzeDir(String dir) {

		int diag = 0;

		if (diag == 1)
			System.out.println(dir);

		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();

		int sumWords = 0;

		for (File file : listOfFiles) {

			String extension = "";

			int i = file.getName().lastIndexOf('.');

			if (i > 0) {
				extension = file.getName().substring(i + 1).toLowerCase();
			}

			if (diag == 1)
				System.out.println(">>> AgentMapReduceMaster: Ekstenzija fajla je: " + extension);

			if (file.isFile() && (extension.equals("txt"))) {

				String absPath = file.getAbsolutePath();

				if (diag == 1)
					System.out.println(">>> AgentMapReduceMaster: Absolutna putanja fajla je: " + absPath);

				AgentMapReduceSlave MRSlave = new AgentMapReduceSlave(absPath);
				sumWords += MRSlave.countWords();
			}
		}
		System.out.println(">>> AgentMapReduceMaster: Suma reci je: " + sumWords);
		return sumWords;
	}
}
