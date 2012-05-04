
public class GameTimeUpdater implements Runnable{
	
	private GameTime time; //New GameTime object --- time
	private DayCycle cycle;
	
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
		//System.out.println("The time is: " + time.getTime() + " Seconds");
		//System.out.println(GameTime.this);
		//System.out.println ("Time of Day is : " + time.checkDateTime());
		
		if (time.checkDateTime() == TimeOfDay.NIGHT){
			System.out.println("Oh no is it very dark - NIGHT");
			//Set overlay black
		}else if (time.checkDateTime() == TimeOfDay.DAYTIME){
			System.out.println("FEW it is DAYTIME");
			//Set overlay to light
		}else if (time.checkDateTime() == TimeOfDay.SUNRISE){
			System.out.println("The sun is coming up - SUNRISE");
			//Set overlay to orange - light
		}else if (time.checkDateTime() == TimeOfDay.SUNSET){
			System.out.println("Its getting dark! - SUNSET");
			//Set overlay to orange - dark
		}else{}
	}
	}
	

}
