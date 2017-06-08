package agents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AgentMapReduceSlave {

	private String absFilePath;

	public AgentMapReduceSlave() {
		super();
	}

	public AgentMapReduceSlave(String absFilePath) {
		this.absFilePath = absFilePath;
	}

	public int countWords() {

		boolean diag = false;

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
}
