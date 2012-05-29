import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class HUD extends Halja{
	
	private float pcntHealth, pcntArmour;
	
	public void draw(Graphics g){
		//draw entity health
		/*
		int centreOffset1 = (guiWidth / 2 - 100) - 
				((HUDIcons[HUD_HEART].getWidth() * world.ollie.getHealth()) / 2);
		int height = guiHeight - 50;
		
		for(int i = 1; i <= world.ollie.getHealth(); i++){
			g.drawImage(HUDIcons[HUD_HEART], centreOffset1 + ((i - 1) * HUDIcons[HUD_HEART].getWidth()), height, null);
		}
		*/
		Rectangle rectHealth = new Rectangle((guiWidth / 2) - 100, guiHeight - 25, 200, 20);
		g.setColor(Color.BLACK);
		g.fillRect(rectHealth.x, rectHealth.y, rectHealth.width, rectHealth.height);
		
		g.setColor(Color.RED);
		pcntHealth = (float)world.ollie.getHealth() / (float)world.ollie.getMaxHealth();
		g.fillRect(rectHealth.x + 1, rectHealth.y + 1, (int) ((rectHealth.width - 2) * pcntHealth), rectHealth.height - 2);
		
		
		//draw entity armour
		/*
		int centreOffset2 = (guiWidth / 2 + 100) - 
				((HUDIcons[HUD_SWORD].getWidth() * world.ollie.getArmour()) / 2);
		
		for(int i = 1; i <= world.ollie.getArmour(); i++){
			g.drawImage(HUDIcons[HUD_SWORD], centreOffset2 + ((i - 1) * HUDIcons[HUD_SWORD].getWidth()), height, null);
		}
		*/
		if(world.ollie.getArmour() > 0){
			Rectangle rectArmour = new Rectangle((guiWidth / 2) - 100, guiHeight - 40, 200, 10);
			g.setColor(Color.BLACK);
			g.fillRect(rectArmour.x, rectArmour.y, rectArmour.width, rectArmour.height);

			g.setColor(Color.LIGHT_GRAY);
			pcntArmour = (float)world.ollie.getArmour() / (float)world.ollie.getMaxArmour();
			g.fillRect(rectArmour.x + 1, rectArmour.y + 1, (int) ((rectArmour.width - 2) * pcntArmour), rectArmour.height - 2);
		}
	}

}
