import java.security.InvalidParameterException;


public class GameTime extends JavaGame {
	
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

	//Checks the minutes of day passed so far 0-23 and returns the time of day
	
	public int checkDateTime() {
		int hour = getHour();
		if (hour > 21 || hour < 4)
			return TOD_NIGHT;
		else if (hour > 16)
			return TOD_SUNSET;
		else if (hour > 8)
			return TOD_DAYTIME;
		else
			return TOD_SUNRISE;
	}
	
	//Gets the hour in game 0-23
	private int getHour() {
		return (gameTime % MINUTES_IN_DAY) / (MINUTES_IN_HOUR);
	}

	private int getMinute() {
		return (gameTime % MINUTES_IN_HOUR);
	}
	
	
}

