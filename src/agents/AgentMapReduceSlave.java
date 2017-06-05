package agents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Scanner;

import messages.ACLMessage;

public class AgentMapReduceSlave extends Agent{

	private String absFilePath;
	public AgentMapReduceSlave(){
		super();
	}
	
	public AgentMapReduceSlave(String absFilePath) {
		this.absFilePath = absFilePath;		
	}

	

	public int countWords() {
		
		File file = new File(absFilePath);
		try(Scanner sc = new Scanner(new FileInputStream(file))){
		    int count=0;
		    while(sc.hasNext()){
		        sc.next();
		        count++;
		    }
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
