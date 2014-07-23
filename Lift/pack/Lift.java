package pack;

import java.util.ArrayList;
import java.util.List;

public class Lift implements Runnable {
	private int countFloor;
    private int currentFloor=1;
	private int dest=1;
	private int capacity;
	private String condition=Constants.CLOSE;
	private List<Human> humans=new ArrayList<Human>();
	private int firstNextFloor=1;
	private int secondNextFloor;
	private int[] firstTargets;
	private int[] secondTargets;
	private int countFirstTargets;
	private int countSecondTargets;
    private Visual visual;
    private int y;
    private boolean stop;
	
	public Lift(int countFloor, int capacity,Visual v,int y) {
		super();
		this.visual=v;
		this.y=y;
		stop=false;
		this.countFloor = countFloor;
		this.capacity = capacity;
		firstTargets=new int[countFloor+1];
		secondTargets=new int[countFloor+1];
		new Thread(this).start();
	}
	
	public void stop(){
		stop=true;
		condition=Constants.CLOSE;
		visual.getTrace().close();
		
	}

	public void run(){
		while (!Visual.isPrintStart()){
			try {
				Thread.sleep(Constants.MAX_WAIT/10);
			} catch (InterruptedException e) {}
		}
		
		
		while (firstNextFloor!=0){
			if ((currentFloor==firstNextFloor)||(currentFloor==secondNextFloor))
			{	if (stop)	return;
				try {
					Thread.sleep(Constants.MAX_WAIT);
				} catch (InterruptedException e) {}
				condition=Constants.OPEN;
				visual.getTrace().printLiftCondition(currentFloor, condition,humans);
				synchronized (this){
					notifyAll();}
				while (waiting()){
					if (stop)	return;
					try {
						Thread.sleep(Constants.MAX_WAIT/10);
					} catch (InterruptedException e) {}
				}
			    			
				condition=Constants.CLOSE;
				visual.getTrace().printLiftCondition(currentFloor, condition,humans);
				next();	
			}
			
			
			if (currentFloor<firstNextFloor)
				dest=1;
			else
				dest=-1;
			
			if (firstNextFloor!=0)
				{	
					if (stop)	return;
					drawMoveLift();
				}
			currentFloor+=dest;
		}
		visual.setTextForB1(Constants.START);
		visual.getTrace().close();
	}
	
	private void drawMoveLift(){
		int Next=currentFloor+dest;
		int Nexty=(int)visual.getSize().getHeight() -visual.getHeightFloor()*Next;

		while (y!=Nexty){
			if (stop)	return;
			y-=dest*visual.getSpeed()*5;
	       			
			if (((dest>0)&& (y<Nexty))||
			((dest<0)&& (y>Nexty)))
				y=Nexty;
			
			for (int i=0;i<visual.getHumans().size();i++)
			{
				if(((Human)visual.getHumans().get(i)).getStatus().equals(Constants.IN))
					((Human)visual.getHumans().get(i)).setY(y+visual.getHeightFloor()/2-Constants.RADIUS);
			}
			visual.repaint();
			try {
				Thread.sleep(Constants.MAX_WAIT/visual.getSpeed());
        }	 catch (InterruptedException ex) {}
		}
	}
	
private synchronized boolean waiting() {
		boolean w=false;
		Human h;
		for (int i=0;i<visual.getHumans().size();i++)
		{	if (stop)	return w;
			h=(Human)visual.getHumans().get(i);
			if (((h.getStatus().equals(Constants.OUT))&&(capacity!=humans.size())&&(h.getBeginFloor()==currentFloor))||
					((h.getStatus().equals(Constants.IN))&&	(currentFloor==h.getEndFloor()))||
					h.getStatus().equals(Constants.MOVEIN))
					{	w=true;
						break;
					}
		}
		return w;
	}

	synchronized public void enter(Human x){
		if ((condition.equals(Constants.CLOSE))||(capacity==humans.size()||currentFloor!=x.getBeginFloor()))
			try {
				wait();
			} catch (InterruptedException e) {}
		else {
				x.setStatus(Constants.MOVEIN);
				humans.add(x);
				delSecondTarget(x.getBeginFloor());
				addFirstTarget(x.getEndFloor());
							
			    visual.getTrace().printHumanEvent(x);
			    try {
					Thread.sleep(10*Constants.MAX_WAIT/visual.getSpeed());
	        }	 catch (InterruptedException ex) {}
		  	}	
					
	}
	
	
	synchronized public void exit(Human x){
		if ((condition==Constants.CLOSE)||(currentFloor!=x.getEndFloor()))
			try {
				wait();
			} catch (InterruptedException e) {}
		else{
			
			for (int i=0;i<humans.size();i++)
				if (((Human)humans.get(i)).getId()==x.getId()) 
				{
					humans.remove(i);
					break;
				}
	    	x.setStatus(Constants.MOVEOUT);
	    	delFirstTarget(x.getEndFloor());
			notifyAll();
			
		    visual.getTrace().printHumanEvent(x);
		    try {
				Thread.sleep(7*Constants.MAX_WAIT/visual.getSpeed());
        }	 catch (InterruptedException ex) {}
		    
	   		}
	}
	
	public void addFirstTarget(int n){
		firstTargets[n]++;
		countFirstTargets++;
	}
	
	public void delFirstTarget(int n){
		firstTargets[n]--;
		countFirstTargets--;
	}
	
	public void addSecondTarget(int n){
		secondTargets[n]++;
		countSecondTargets++;
	}
	
	public void delSecondTarget(int n){
		secondTargets[n]--;
		countSecondTargets--;
	}
	
	private void next(){
		int tmp=currentFloor;
		int tmpdest=dest;
		if (tmp==countFloor) tmpdest=-1;
		if (tmp==1) tmpdest=1;
		tmp+=tmpdest;
		
		if	(countSecondTargets!=0)
		{
			while (secondTargets[tmp]==0){
				if (tmp==countFloor) tmpdest=-1;
				if (tmp==1) tmpdest=1;
				tmp+=tmpdest;
			}
			secondNextFloor=tmp;
		}
		else 
			secondNextFloor=0;
		
		tmp=currentFloor;
		tmpdest=dest;
		if (tmp==countFloor) tmpdest=-1;
		if (tmp==1) tmpdest=1;
		tmp+=tmpdest;
		
		if (countFirstTargets!=0)
		{
			while (firstTargets[tmp]==0){
				if (tmp==countFloor) tmpdest=-1;
				if (tmp==1) tmpdest=1;
				tmp+=tmpdest;
			}
			firstNextFloor=tmp;
		}
		else
			firstNextFloor=secondNextFloor;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}

