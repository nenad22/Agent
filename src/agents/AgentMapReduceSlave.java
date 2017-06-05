package agents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import main.PropertiesUtil;
import messages.ACLMessage;

@SuppressWarnings("serial")
public class AgentMapReduceSlave extends Agent {

	private String absFilePath;

	public AgentMapReduceSlave() {
		super();
	}

	public AgentMapReduceSlave(String absFilePath) {
		this.absFilePath = absFilePath;
	}

	public int countWords() {

		boolean diag = false;

		try {
			if (PropertiesUtil.instance().readProperty("diag").equals("true"))
				diag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File file = new File(absFilePath);
		try (Scanner sc = new Scanner(new FileInputStream(file))) {
			int count = 0;
			while (sc.hasNext()) {
				sc.next();
				count++;
			}

			if (diag == true)
				System.out.println(">>> AgentMapReduceSlave: Broj reci u fajlu: " + absFilePath + " je: " + count);

			return count;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return -1;

	}

	@Override
	public void onMessage(ACLMessage message) {
		// TODO Treba da od mastera dobije poruku pa onda da radi poso

	}

}
