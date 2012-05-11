import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/*
 * The sound class to load sounds and background music
 * 
 * Midnight.wav is from www.purple-planet.com
 * 
 * 
 * Checks the time of day and plays the specific music for the time
 * 
 *  ** While testing the Sunrise.wav runs over the day cycle as it is 4mins long, 
 *  	when the Day/night cycle is set correct this wont happen **
 * 
 */



public class Sounds {
	
	String theFile = null;
	boolean isPlaying;
	
	public void play() throws Exception{
		
		//Checks to see if the music is playing
		if (isPlaying == false){
		//&&  isPlaying kept in just in case, cant do any harm
			
		if (ReturnTime.returnTimeOfDay() == TimeOfDay.NIGHT && isPlaying == false){
			theFile = "Night.wav";
		}else if (ReturnTime.returnTimeOfDay() == TimeOfDay.DAYTIME && isPlaying == false){
			theFile = "Sunrise.wav";			
		}else if (ReturnTime.returnTimeOfDay() == TimeOfDay.SUNRISE && isPlaying == false){
			theFile = "Night.wav";
		}else if (ReturnTime.returnTimeOfDay() == TimeOfDay.SUNSET && isPlaying == false){
			theFile = "Night.wav";
		}else{}
	
		File soundFile = new File(theFile);
		AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
		DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(sound);
		
		  clip.addLineListener(new LineListener() {
		      public void update(LineEvent event) {
		        if (event.getType() == LineEvent.Type.STOP) {
		          event.getLine().close();
		          isPlaying = false;
		        }
		      }
		    });
		  isPlaying = true;
		clip.start();
	}else{}
	}
}
