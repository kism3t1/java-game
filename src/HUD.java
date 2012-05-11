import java.awt.Graphics;


public class HUD extends JavaGame{
	
	public void draw(Graphics g){
		//draw entity health
		int centreOffset1 = (guiWidth / 2 - 100) - 
				((HUDIcons[HUD_HEART].getWidth() * world.entity.getHealth()) / 2);
		int height = guiHeight - 50;
		
		for(int i = 1; i <= world.entity.getHealth(); i++){
			g.drawImage(HUDIcons[HUD_HEART], centreOffset1 + ((i - 1) * HUDIcons[HUD_HEART].getWidth()), height, null);
		}
		//draw entity armour
		int centreOffset2 = (guiWidth / 2 + 100) - 
				((HUDIcons[HUD_SWORD].getWidth() * world.entity.getArmour()) / 2);
		
		for(int i = 1; i <= world.entity.getArmour(); i++){
			g.drawImage(HUDIcons[HUD_SWORD], centreOffset2 + ((i - 1) * HUDIcons[HUD_SWORD].getWidth()), height, null);
		}
	}

}
