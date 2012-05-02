import java.security.InvalidParameterException;


public class GameTime {
	
	public static int MINUTES_IN_HOUR = 60;
	public static int MINUTES_IN_DAY = 60 * 24;
	
	private int gameTime = 0;	
	
	//GameTime constructor
	//Call GameTime (int) set gametTime as (int)
	
	public GameTime(int time){
		if (time < 0)
			throw new InvalidParameterException("MMmmm something whent wrong, time must be >= 0");
		gameTime = time;
	}

	//Gets the ingame time in minutes
	//returns gameTime int
	
	public int getTime(){
		return gameTime;
	}
	
	//Increase game time by a minute
	
	protected void increase(){
		if (getMinute() == 0){
			checkDateTime();
		}
		++gameTime;
	}

	private void checkDateTime() {
		// TODO Auto-generated method stub
		TimeOfDay newDateTime = null;
		int hour = getHour();
		if (hour > 21 || hour < 4)
			newDateTime = TimeOfDay.NIGHT;
		else if (hour > 16)
			newDateTime = TimeOfDay.SUNSET;
		else if (hour > 8)
			newDateTime = TimeOfDay.DAYTIME;
		else
			newDateTime = TimeOfDay.SUNRISE;
		
        if (gameTime > 1) {
            System.out.println("It is " + newDateTime);
        }
    }

	//Gets the hour in game 0-23
	private int getHour() {
		// TODO Auto-generated method stub
		return (gameTime % MINUTES_IN_DAY) / (MINUTES_IN_HOUR);
	}

	private int getMinute() {
		// TODO Auto-generated method stub
		return (gameTime % MINUTES_IN_HOUR);
	}
	
	
}

