
/*
 * 
 * Used to return the Time Of Day to GameLoop
 * 
 */

public class ReturnTime {
	
	static TimeOfDay dayTime;
	
	public static TimeOfDay returnTimeOfDay(){
		dayTime = JavaGame.returnTime().checkDateTime();
		return dayTime;
		
	}

}
