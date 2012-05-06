import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;

/*
 * Start at looking into making the sun set & night time etc...
 */


public class drawTime {
	
	Image black;
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		black = new ImageIcon(this.getClass().getResource("/Images/dark.png")).getImage();
				
		//Set black to 50% opacity
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			g2d.drawImage(black, 0, 0, 1500, 1000, null);

	}

}
