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
 */


public class Sounds {
	
	public void play() throws Exception{
		
		String theFile = "Midnight.wav";
	
		File soundFile = new File(theFile);
		AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
		
		DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(sound);
		
		//Error handling due to bug in java...
		
		clip.addLineListener(new LineListener() {
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP) {
					event.getLine().close();
					System.exit(0);
				}
			}
		});
		
		clip.start();
	}

}
