import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
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



public class Sounds extends Halja{
	
	String theFile = null;
	boolean isPlaying;
	boolean playing;
	
	public void playWav() throws Exception{
		
		//Checks to see if the music is playing
		if (isPlaying == false){
		//&&  isPlaying kept in just in case, cant do any harm
			
		if (gameTime.checkDateTime() == TOD_NIGHT && isPlaying == false){
			theFile = "Night.wav";
		}else if (gameTime.checkDateTime() == TOD_DAYTIME && isPlaying == false){
			theFile = "Sunrise.wav";			
		}else if (gameTime.checkDateTime() == TOD_SUNRISE && isPlaying == false){
			theFile = "Night.wav";
		}else if (gameTime.checkDateTime() == TOD_SUNSET && isPlaying == false){
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
	
	public void playMidi(){
		
		if (playing == false){
			//Check what midi file to play dependent on the time of day
			if (gameTime.checkDateTime() == TOD_NIGHT && isPlaying == false){
				theFile = "night.mid";
			}else if (gameTime.checkDateTime() == TOD_DAYTIME && isPlaying == false){
				theFile = "day.mid";			
			}else if (gameTime.checkDateTime() == TOD_SUNRISE && isPlaying == false){
				theFile = "day.mid";
			}else if (gameTime.checkDateTime() == TOD_SUNSET && isPlaying == false){
				theFile = "night.mid";
			}else{}
		
		try {
	        // From file
	        Sequence sequence = MidiSystem.getSequence(new File(theFile));
	    
	        // From URL
	       // sequence = MidiSystem.getSequence(new URL("http://hostname/midiaudiofile"));
	    
	        // Create a sequencer for the sequence
	        Sequencer sequencer = MidiSystem.getSequencer();
	        sequencer.open();
	        sequencer.setSequence(sequence);
	        sequencer.start();
	        playing = true;
	    
	        // Start playing
	        sequencer.addMetaEventListener(new MetaEventListener(){
	        	public void meta(MetaMessage event){
	        		if (event.getType() == 47){
	        			playing = false;
	        		}
	        	}
	        });
	    } catch (MalformedURLException e) {
	    } catch (IOException e) {
	    } catch (MidiUnavailableException e) {
	    } catch (InvalidMidiDataException e) {
	    }
	}
		else{}
	}
}
