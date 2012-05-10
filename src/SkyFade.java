import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/* Keeps track of the alpha value and increases or decreases the value 
 * 
 * (Darken and Lighten the screen :)
 * 
 */

public class SkyFade extends JavaGame {
	
	static float alpha = 0.0f;

	public void paint(Graphics g) {
		
	    Graphics2D g2d = (Graphics2D) g;

	    //set the opacity
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	    
	    g2d.drawImage(skySkins[5], 0, 0, guiWidth, guiHeight, null);
	}
	
	/* Increase Alpha value by 0.005f for testing to speed it up. And 0.0005f for in Game 
	 * 
	 * Increase makes it darker, Decrease makes it lighter.
	 * 
	 */
	
	public static void increaseAlpha(){
		alpha += 0.0005f;
	    if (alpha >= 1.0f) {
	        alpha = 1.0f;
	    } else {}
	}
	
	
	
	public static void decreaseAlpha(){
		alpha -= 0.0005f;
		if (alpha <=0.0f){
			alpha = 0.0f;
		}else{}
	}
	
}
