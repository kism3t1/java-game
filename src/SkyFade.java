import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/* Keeps track of the alpha value and increases or decreases the value 
 * 
 * (Darken and Lighten the screen :)
 * 
 */

public class SkyFade extends JavaGame {
	
	static int alpha = 0;

	public void draw(Graphics g) {
		/* old code
	    Graphics2D g2d = (Graphics2D) g;
	    
	    //store previous G2D settings to restore when done
	    Composite prevComposite = g2d.getComposite();
	    RenderingHints prevHint = g2d.getRenderingHints();

	    //set the opacity
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    g2d.drawImage(skySkins[5], 0, 0, guiWidth, guiHeight, null);
	    
	    //restore G2D settings
	    g2d.setComposite(prevComposite);
	    g2d.setRenderingHints(prevHint);
	    */
	    
	    //new code:
	    g.drawImage(skyTransparency[alpha], 0, 0, guiWidth, guiHeight, null);
	}
	
	/* Increase Alpha value by 0.005f for testing to speed it up. And 0.0005f for in Game 
	 * 
	 * Increase makes it darker, Decrease makes it lighter.
	 * 
	 */
	
	public static void increaseAlpha(){
		alpha += 1;
	    if (alpha >= skyTransparency.length) {
	        alpha = skyTransparency.length - 1;
	    }
	}
	
	
	
	public static void decreaseAlpha(){
		alpha -= 1;
		if (alpha < 0){
			alpha = 0;
		}
	}
	
}
