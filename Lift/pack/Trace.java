package pack;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

public class Trace {
	private PrintWriter pw;
	public Trace(String fname) {
		super();
		try {
			pw = new PrintWriter(new FileWriter(fname));
		} catch (IOException e) {}

	}
	
	public void printString(String s){
		pw.println(s);
	}
	
	public void close(){
		pw.close();
	}
	
	synchronized public void printStartInfo(int CountFloor,List humans){
		for (int i=1;i<CountFloor+1;i++)
		{	System.out.println("Floor "+i+":");
			printString("Floor "+i+":");
				for (int j=0;j<humans.size();j++){
					if (i==((Human)humans.get(j)).getBeginFloor()){
						System.out.println(((Human)humans.get(j)).getInfo());
						printString(((Human)humans.get(j)).getInfo());
					}
				}
		}
		System.out.println("\nTrace:");
		printString("");
		printString("Trace:");
	}
	
	synchronized public void printLiftCondition(int CurrentFloor,String condition,List humans){
		if (condition.equals(Constants.OPEN)){
		System.out.println("\n"+Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE)+
				":"+Calendar.getInstance().get(Calendar.SECOND)+" Lift opened at "+CurrentFloor+" floor");
		printString("");
		printString(Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE)+
				":"+Calendar.getInstance().get(Calendar.SECOND)+" Lift opened at "+CurrentFloor+" floor");
		}
		
	}
	
	synchronized public void printHumanEvent(Human x){
		String tmp=null;
		if (x.getStatus().equals(Constants.EXIT)||x.getStatus().equals(Constants.MOVEOUT)){
			tmp=" exited";
		}
		if (x.getStatus().equals(Constants.IN)||x.getStatus().equals(Constants.MOVEIN)){
			tmp=" entered";
		}
		System.out.println(Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE)+
				":"+Calendar.getInstance().get(Calendar.SECOND)+" Human"+x.getId()+tmp);
	    printString(Calendar.getInstance().get(Calendar.HOUR)+":"+Calendar.getInstance().get(Calendar.MINUTE)+
				":"+Calendar.getInstance().get(Calendar.SECOND)+" Human"+x.getId()+tmp);
	}
	
}
