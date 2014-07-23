package pack;

public class Human implements Runnable{
	private int id;
	private String status=Constants.OUT;
	private int beginFloor;
	private int endFloor;
	private int x;
	private int y;
	private Visual v;
	private boolean stop;

	public Human(int id,int max,Visual v) {
		super();
		this.id = id;
		stop=false;
	    this.v=v;
		beginFloor=(int)(Math.random()*max+1);
		endFloor=(int)(Math.random()*max+1);
		while (beginFloor==endFloor)
			endFloor=(int)(Math.random()*max+1);
		v.getLift().addSecondTarget(beginFloor);
		x=-2*Constants.RADIUS;
		new Thread(this).start();
		
	}
	
	
	public void stop(){
		stop=true;
    }
	
	public String getInfo(){
		return "Human"+id+" needs floor #"+endFloor;
	}

	public void run(){
			
			while (status.equals(Constants.OUT))
				{	if (stop)	return;
					v.getLift().enter(this);
				}
			drawMoveIn();
			while (status.equals(Constants.IN))
				{	if (stop)	return;
					v.getLift().exit(this);
				}
			drawMoveOut();		
	}
   
   public void drawMoveIn() {
	    int nextX=v.getSize().width/3-2*Constants.RADIUS;
        x=v.getFloors()[getBeginFloor()]*2*Constants.RADIUS;
        y=(int) v.getSize().getHeight()-v.getHeightFloor()*beginFloor+v.getHeightFloor()/2-Constants.RADIUS;
	 	while (x<nextX){
	 		if (stop)	return;
			x+=v.getSpeed()*5;
			if (x>nextX) x=nextX;
			v.repaint();
			try {
				Thread.sleep(Constants.MAX_WAIT);
			}catch (InterruptedException ex) {}
	 	}
        status=Constants.IN;
        v.repaint();
	}
   
   public void drawMoveOut() {
	   
	   int nextX=v.getSize().width;
	   x=v.getSize().width*2/3;
       while (x<nextX){
    	   	if (stop)	return;
			x+=v.getSpeed()*5;
			if (x>nextX) x=nextX;
			v.repaint();
			try {
				Thread.sleep(Constants.MAX_WAIT/v.getSpeed());
       }	 catch (InterruptedException ex) {}
		}
		status=Constants.EXIT;
	}
   
	
	public int getBeginFloor() {
		return beginFloor;
	}
	public void setBeginFloor(int beginFloor) {
		this.beginFloor = beginFloor;
	}
	public int getEndFloor() {
		return endFloor;
	}
	public void setEndFloor(int endFloor) {
		this.endFloor = endFloor;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}

}
