import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class skyFade extends JavaGame {
	
	static float alpha = 1.0f;

	public void paint(Graphics g) {
		
	    Graphics2D g2d = (Graphics2D) g;

	    //set the opacity
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

	    //Need to change to gui.height gui.width
	    
	    g2d.drawImage(skySkins[5], 0,0,1500,1000,null);
	}
	
	
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
