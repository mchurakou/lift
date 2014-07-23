package pack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class Visual extends javax.swing.JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private static int countHuman;
	private static int capacity;
	private static Trace trace;
	private static int speed;
	
	private JLabel l1,l2,l3;
	private JTextField t1,t2;
	private JSlider js;
	private JButton b1;
		
	private int heightFloor;
	private String stat=Constants.OFF;
	
	private List<Human> humans;
	private int[] floors;	
	
	private Lift lift;
	private static boolean printStart=false;
	

	Visual(String s){
			super(s);
			this.setLayout(null);
			
			l1=new JLabel("Humans:");
			l1.setBounds(0,0,50,20);
			this.add(l1);
		
			t1=new JTextField();
			t1.setBounds(50,0,30,20);
			this.add(t1);
			
			l2=new JLabel("Capacity:");
			l2.setBounds(80,0,55,20);
			this.add(l2);
		
			t2=new JTextField();
			t2.setBounds(135,0,30,20);
			this.add(t2);
			
			b1=new JButton(Constants.START);
			b1.setBounds(165,0,90,20);
			b1.addActionListener(this);
			this.add(b1);
			
			l3=new JLabel("Speed:");
			l3.setBounds(260,0,55,20);
			this.add(l3);
			
			js=new JSlider();
			js.setMinimum(1);
			js.setMaximum(5);
			js.setExtent(1);
			js.setValue(3);
			js.setBounds(300, 0, 100, 20);
			js.setMinorTickSpacing(1);
			js.setPaintTicks(true);
			this.add(js);
			
			this.setResizable(false);
			this.humans=new ArrayList<Human>();
		    this.setBounds(50,50,500,500);
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        heightFloor=((int)this.getSize().getHeight()-50)/Constants.COUNTFLOOR;
	        floors=new int[Constants.COUNTFLOOR+1]; 
	        setVisible(true);
		}
	  
	   private void drawFloors(Graphics g){
		   for (int i=0;i<Constants.COUNTFLOOR;i++)
	        {
	        	g.drawRect(0,(int) getSize().getHeight()-heightFloor*(i+1),(int) getSize().getWidth(),(int) getSize().getHeight());
	        	g.drawString("Floor "+(i+1),( int) getSize().getWidth()*2/3,( int) getSize().getHeight()-heightFloor*(i)-7);
	        }
	   }
   
	   private void drawLift(Graphics g){
		   g.setColor(Color.GREEN);
		   g.drawRect(getSize().width/3,lift.getY(),(int) getSize().getWidth()/3,heightFloor);
	   }
	   
	   private void drawWaitHuman(Graphics g){
		   
		   g.setFont(new Font("",Font.PLAIN,Constants.RADIUS));
		   g.setColor(Color.black);
		   floors=new int[Constants.COUNTFLOOR+1]; 
		   for (int j=1;j<Constants.COUNTFLOOR+1;j++)
		    {
		    	int[] tmp=new int[Constants.COUNTFLOOR+1];
		    	for (int i=0;i<humans.size();i++)
		    		{
		    				Human h=(Human)humans.get(i);
		    					    				
		    				if ((h.getStatus().equals(Constants.OUT))&&(h.getBeginFloor()==j)) tmp[h.getEndFloor()]++;
		    		}
		    	int count=0;
		    	for (int i=1;i<Constants.COUNTFLOOR+1;i++)
		    	{
		    		if (tmp[i]!=0){
		    	   		int x=count*2*Constants.RADIUS;
		    	   		int y=(int) getSize().getHeight()-heightFloor*j+heightFloor/2-Constants.RADIUS;
		    	   		
		    	   		count++;
		    	   		g.drawString(String.valueOf(i),x+10,y+17);
    					g.drawOval(x,y , 2*Constants.RADIUS, 2*Constants.RADIUS);
    			        			    	
    					if (tmp[i]<10)
    						g.drawString(String.valueOf(tmp[i]),x+10,y+10-Constants.RADIUS);
    					else
    						if (tmp[i]<100)
    							g.drawString(String.valueOf(tmp[i]),x+8,y+10-Constants.RADIUS);
    						else
    							g.drawString(String.valueOf(tmp[i]),x+6,y+10-Constants.RADIUS);
    					floors[i]++;
		    	   	}
		    	}
		    }
	   }
	   
	   private void drawInHuman(Graphics g){
		   g.setFont(new Font("",Font.PLAIN,Constants.RADIUS));
		   
		   int[] tmp=new int[Constants.COUNTFLOOR+1];
	    	for (int i=0;i<humans.size();i++)
	    		{
	    				Human h=(Human)humans.get(i);
	    				if (h.getStatus().equals(Constants.IN)) tmp[h.getEndFloor()]++;
	    		}
	    	int count=0;
	    	for (int i=1;i<Constants.COUNTFLOOR+1;i++)
	    	{
	    		if (tmp[i]!=0){
	    	   		int x=getSize().width/3+count*Constants.RADIUS*2;
	    	   		int y=lift.getY()+ heightFloor/2-Constants.RADIUS;   	   			
	    	   		count++;
	    	   		
					g.drawOval(x,y, 2*Constants.RADIUS, 2*Constants.RADIUS);
					
					g.drawString(String.valueOf(i),x+10,y+17);
											
					if (tmp[i]<10)
						g.drawString(String.valueOf(tmp[i]),x+10,y+10-Constants.RADIUS);
					else
						if (tmp[i]<100)
							g.drawString(String.valueOf(tmp[i]),x+8,y+10-Constants.RADIUS);
						else
							g.drawString(String.valueOf(tmp[i]),x+6,y+10-Constants.RADIUS);
					floors[i]++;
					
	    		}	
	    	}
	   }
  
	   private void drawMoveHuman(Graphics g){
		   g.setColor(Color.BLACK);	
		  
		  
		   for (int i=0;i<humans.size();i++){
			   
			   Human h=(Human)humans.get(i);
			   if ((!h.getStatus().equals(Constants.OUT))&&(!h.getStatus().equals(Constants.IN)))
			   {
				   if ((h.getX()==getSize().width/3-2*Constants.RADIUS)&&(h.getStatus().equals(Constants.MOVEOUT))) break;
				   int x=h.getX();
				   int y=h.getY();
				   g.drawOval(x,y , 2*Constants.RADIUS, 2*Constants.RADIUS);
				   
				   g.drawString(String.valueOf(h.getEndFloor()),x+10,y+17);
			   }
	  
		   }
   }
	   
		public void paint (Graphics g){
			super.paint(g);
	      	g.setColor(Color.black);
	      	drawFloors(g);
	      	if (stat.equals(Constants.ON))
	      	{	
	      		drawLift(g);
	      		drawWaitHuman(g);
	      		drawInHuman(g);
	      		drawMoveHuman(g);
			}
	     
		}

		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getActionCommand().equals(Constants.START))
			{	
						try{
							countHuman=Integer.parseInt(t1.getText());
							capacity=Integer.parseInt(t2.getText());
							speed=js.getValue();
							if ((countHuman<=0)||(capacity<=0))
								throw new Exception();
							}
						catch (Exception e) {
							this.setTitle("Incorrect input!");
							return;}
						this.setTitle("Lift");
						b1.setText(Constants.STOP);
						printStart=false;
						trace=new Trace("trace.txt");
						humans=new ArrayList<Human>();
						
						lift=new Lift(Constants.COUNTFLOOR,capacity,this,(int) getSize().getHeight()-heightFloor);
																
						for (int i=0;i<countHuman;i++)
							humans.add(new Human(i+1,Constants.COUNTFLOOR,this));
											
						this.getTrace().printStartInfo(Constants.COUNTFLOOR,humans);
						printStart=true;
						stat=Constants.ON;
						this.repaint();
	    	}
			if (arg0.getActionCommand().equals(Constants.STOP)){
				for (int i=0;i<humans.size();i++)
					((Human)humans.get(i)).stop();
				lift.stop();
				b1.setText(Constants.START);
				return;
			}
		}
		

public static int getCapacity() {
	return capacity;
}

public static void setCapacity(int capacity) {
	Visual.capacity = capacity;
}

public  Trace getTrace()
{ 	
return trace;
}

public static void setTrace(Trace trace) {
	Visual.trace = trace;
}

public void setTextForB1(String s){
	b1.setText(s);
}


public int getHeightFloor() {
	return heightFloor;
}

public void setHeightFloor(int heightFloor) {
	this.heightFloor = heightFloor;
}

public int getSpeed() {
	return speed;
}

public void setSpeed(int speed) {
	Visual.speed = speed;
}

public List<Human> getHumans() {
	return humans;
}

public void setHumans(List<Human> humans) {
	this.humans = humans;
}

public Lift getLift() {
	return lift;
}

public void setLift(Lift lift) {
	this.lift = lift;
}

public int[] getFloors() {
	return floors;
}

public void setFloors(int[] floors) {
	this.floors = floors;
}
public static void main(String[] args) {
	new Visual("Lift"); 
}

public static boolean isPrintStart() {
	return printStart;
}

}
