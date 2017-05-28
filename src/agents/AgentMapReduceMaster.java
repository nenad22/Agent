package agents;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import main.PropertiesUtil;

/**
 * @author Dejan Ribic
 * Glavna klasa za Map Reduce agenta, pokrece onoliko Slave agenata koliko ima .txt fajlova u specificiranom direktorijumu i vrsi brojanja reci u istim. 
 *
 */
@SuppressWarnings("serial")
public class AgentMapReduceMaster extends Agent {
	
	//private ArrayList<AgentMapReduceSlave> slaves;
	
	/**
	 * Created the Map Reduce Master agent from the Agent superclass
	 */
	public AgentMapReduceMaster() {
		super();
	}

	@Override
	public void onMessage() {
	}
	
	/**
	 * Find the designated directory, identifies and counds the .txt files inside, and proceeds to spawn Slave agents for the counting process.
	 * @param dir The full absolute path of the directory withing which to search for files
	 */
	public void doWork(String dir) {
		
		boolean diag = false;
		
		try {
			if (PropertiesUtil.instance().readProperty("diag").equals("true")) diag = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (diag == true)
		System.out.println(">>> AgentMapReduceMaster: Direktorijum fajlova je: " + dir);
		
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();

		int sumWords = 0;
		
		for (File file : listOfFiles) {
			
			String extension = "";

			int i = file.getName().lastIndexOf('.');
			
			if (i > 0) {
			    extension = file.getName().substring(i+1).toLowerCase();
			}
			
			if (diag == true)
			System.out.println(">>> AgentMapReduceMaster: Ekstenzija fajla je: " + extension);
			
		    if (file.isFile() && (extension.equals("txt"))) {
		    	
		    	String absPath = file.getAbsolutePath();
		    	
		    	if (diag == true)
		        System.out.println(">>> AgentMapReduceMaster: Absolutna putanja fajla je: "+ absPath);
		    	
		    	AgentMapReduceSlave MRSlave = new AgentMapReduceSlave(absPath);
		    	sumWords += MRSlave.countWords();
		    }

		    //if (diag == true)
			System.out.println(">>> AgentMapReduceMaster: Suma reci je: " + sumWords);
		}
		
	}

}
