
public class GameTimeUpdater implements Runnable{
	
	private GameTime time;
	
	//Updates GameTime
	
	public GameTimeUpdater(GameTime time){
		this.time = time;
	}

	//Increase time by one minute
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 while (true) {
	            //System.out.println("Started thread");
	            try {
					Thread.sleep(1000);	//1000 is 1 second
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		time.increase();
		System.out.println("The time is: " + time.getTime() + " Seconds");
		//System.out.println(GameTime.this);
	}
	}
	

}
