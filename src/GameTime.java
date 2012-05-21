import java.security.InvalidParameterException;


public class GameTime extends Halja {
	
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
	
	public void setTime(int TimeOfDay){
		switch(TimeOfDay){
		case TOD_DAYTIME:
			gameTime = MINUTES_IN_HOUR * 8;	//set time to 0800
			break;
		case TOD_SUNSET:
			gameTime = MINUTES_IN_HOUR * 16; //set time to 1600
			break;
		case TOD_NIGHT:
			gameTime = MINUTES_IN_HOUR * 21; //set time to 2100
			break;
		case TOD_SUNRISE:
			gameTime = MINUTES_IN_HOUR * 4;	//set time to 0400
			break;
		default:
			gameTime = MINUTES_IN_HOUR * 8;	//incorrect value passed, default to 0800
			break;
		}
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
		if (hour >= 21 || hour < 4)
			return TOD_NIGHT;
		else if (hour >= 16)
			return TOD_SUNSET;
		else if (hour >= 8)
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

