
/*
 * 
 * POC code, needs tidying up when I get the chance
 * 
 * Used to return a boolean value on what time of day it is
 * 
 */

public class ReturnTime {
	
	static boolean night;
	static boolean day;
	static boolean sunset;
	static boolean sunrise;
	
	public static boolean returnNight(){
		night = false;
		
		if (JavaGame.returnTime().checkDateTime() == TimeOfDay.NIGHT){
			night = true;
		}
		return night;
	}
	
	public static boolean returnDay(){
		day = false;
		
		if (JavaGame.returnTime().checkDateTime() == TimeOfDay.DAYTIME){
			day = true;
		}
		return day;
	}
	
	public static boolean returnSunset(){
		sunset = false;
		
		if (JavaGame.returnTime().checkDateTime() == TimeOfDay.SUNSET){
			sunset = true;
		}
		return sunset;
	}
	
	public static boolean returnSunrise(){
		sunrise = false;
		
		if (JavaGame.returnTime().checkDateTime() == TimeOfDay.SUNRISE){
			sunrise = true;
		}
		return sunrise;
	}

}
