import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


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
					Thread.sleep(100);	//1000 is 1 second // 100 for testing to speed things up a little
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		time.increase();

		
		if (time.checkDateTime() == TimeOfDay.NIGHT){
			//cycle.runNight();
		}else if (time.checkDateTime() == TimeOfDay.DAYTIME){
			//cycle.showSun(g, 200, 200);
		}else if (time.checkDateTime() == TimeOfDay.SUNRISE){
			//cycle.runSunrise();
		}else if (time.checkDateTime() == TimeOfDay.SUNSET){
			//cycle.runSunset();
		}else{}
	}
	}
	

}
