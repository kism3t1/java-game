import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.RescaleOp;

import javax.swing.ImageIcon;

//PROOF OF CONCEPT

// Need to get the scale and offsets right

public class DayCycle {
	
	private static long dLast;
	
	Image sun;
	Image moon;
	
	static float scaledown = 0.9f;
	static float offsetdown = 0.1f;
	
	static float scaleup = 1.3f;
	static float offsetup = 0.9f;
	
	public void showSun(Graphics g, int x, int y){
		sun = new ImageIcon(this.getClass().getResource("/Images/sun.png")).getImage();
		Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(sun, x, y, null);
        x += x; // Move the sun across and up
        y += y; // Move the sun across and up
		//System.out.println("SUN!!!!!");
	}
	
	public void showMoon(Graphics g, int x, int y){
		moon = new ImageIcon(this.getClass().getResource("/Images/moon.png")).getImage();
		Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(moon, x, y, null);
        x += x; // Move the moon across and up
        y += y; // Move the moon across and up
		//System.out.println("Moon!!!!!");
	}
	
	public static void runSunset(){
			if (System.currentTimeMillis() - dLast > 500) {
		RescaleOp rescaleOp = new RescaleOp(scaledown, offsetdown, null);
		rescaleOp.filter(JavaGame.tileSkins[0], JavaGame.tileSkins[0]);
		rescaleOp.filter(JavaGame.tileSkins[1], JavaGame.tileSkins[1]);
		rescaleOp.filter(JavaGame.tileSkins[2], JavaGame.tileSkins[2]);
		rescaleOp.filter(JavaGame.tileSkins[3], JavaGame.tileSkins[3]);
		rescaleOp.filter(JavaGame.entitySkins[0], JavaGame.entitySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[0], JavaGame.enemySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[1], JavaGame.enemySkins[1]);
		rescaleOp.filter(JavaGame.enemySkins[2], JavaGame.enemySkins[2]);
		System.out.println("Sun Setting");
		dLast = System.currentTimeMillis();
		}
	}
		
	
	public static void runSunrise(){
			if (System.currentTimeMillis() - dLast > 500) {
		RescaleOp rescaleOp = new RescaleOp(scaleup, offsetup, null);
		rescaleOp.filter(JavaGame.tileSkins[0], JavaGame.tileSkins[0]);
		rescaleOp.filter(JavaGame.tileSkins[1], JavaGame.tileSkins[1]);
		rescaleOp.filter(JavaGame.tileSkins[2], JavaGame.tileSkins[2]);
		rescaleOp.filter(JavaGame.tileSkins[3], JavaGame.tileSkins[3]);
		rescaleOp.filter(JavaGame.entitySkins[0], JavaGame.entitySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[0], JavaGame.enemySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[1], JavaGame.enemySkins[1]);
		rescaleOp.filter(JavaGame.enemySkins[2], JavaGame.enemySkins[2]);
		System.out.println("Sun Rising");
		dLast = System.currentTimeMillis();
		}
	}
	
	public static void runNight(){
			if (System.currentTimeMillis() - dLast > 500) {
		RescaleOp rescaleOp = new RescaleOp(scaledown, offsetdown, null);
		rescaleOp.filter(JavaGame.tileSkins[0], JavaGame.tileSkins[0]);
		rescaleOp.filter(JavaGame.tileSkins[1], JavaGame.tileSkins[1]);
		rescaleOp.filter(JavaGame.tileSkins[2], JavaGame.tileSkins[2]);
		rescaleOp.filter(JavaGame.tileSkins[3], JavaGame.tileSkins[3]);
		rescaleOp.filter(JavaGame.entitySkins[0], JavaGame.entitySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[0], JavaGame.enemySkins[0]);
		rescaleOp.filter(JavaGame.enemySkins[1], JavaGame.enemySkins[1]);
		rescaleOp.filter(JavaGame.enemySkins[2], JavaGame.enemySkins[2]);
		System.out.println("Night Time");
		dLast = System.currentTimeMillis();
		}
	}
}
