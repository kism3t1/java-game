import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/*
 * The sound class to load sounds and background music
 * 
 * Checks the time of day and plays the specific music for the time
 * 
 * 
 */

public class Sounds extends Halja{
	
	String theFile = null;
	boolean playing;
	
	public void playMidi(){
		
		if (playing == false){
			//Check what midi file to play dependent on the time of day
			if (gameTime.checkDateTime() == TOD_NIGHT && playing == false){
				theFile = "night.mid";
			}else if (gameTime.checkDateTime() == TOD_DAYTIME && playing == false){
				theFile = "day.mid";			
			}else if (gameTime.checkDateTime() == TOD_SUNRISE && playing == false){
				theFile = "day.mid";
			}else if (gameTime.checkDateTime() == TOD_SUNSET && playing == false){
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
	    
	        // Check for end of song
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
