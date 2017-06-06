package agents;

import java.io.File;

import messages.ACLMessage;

@SuppressWarnings("serial")
public class AgentMapReduceMaster extends Agent {

	// private ArrayList<AgentMapReduceSlave> slaves;

	public AgentMapReduceMaster() {
		super();
	}

	public void doWork(String dir) {

		int diag = 1;

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

			System.out.println(">>> AgentMapReduceMaster: Suma reci je: " + sumWords);
		}

	}

	@Override
	public void onMessage(ACLMessage message) {
		// TODO Uklopi do work ovde nekako

	}

}
