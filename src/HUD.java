import java.awt.Graphics;


public class HUD extends JavaGame{
	
	public void draw(Graphics g){
		//draw entity health
		int centreOffset = (guiWidth / 2) - 
				((HUDIcons[HUD_HEART].getWidth() * world.entity.getHealth()) / 2);
		
		for(int i = 1; i <= world.entity.getHealth(); i++){
			g.drawImage(HUDIcons[HUD_HEART], centreOffset + ((i - 1) * HUDIcons[HUD_HEART].getWidth()), 5, null);
		}
	}

}
