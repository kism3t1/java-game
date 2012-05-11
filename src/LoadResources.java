import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class LoadResources extends JavaGame implements Runnable{

	@Override
	public void run() {
		//load resources in to memory
		try{
			tileSkins = new BufferedImage[]{
					optimizedImage("/Images/dirt.png"),
					optimizedImage("/Images/grass.png"),
					optimizedImage("/Images/stone.png"),
					optimizedImage("/Images/tree.png"),
					optimizedImage("/Images/water.png")
			};
		}catch(IOException e){
			System.out.println("Error loading tileSkins");
		}
		try{
			enemySkins = new BufferedImage[]{		
					optimizedImage("/Images/enemy.png"),
					optimizedImage("/Images/eye.png"),
					optimizedImage("/Images/snake.png"),
			};
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}

		try{
			entitySkins = new BufferedImage[6];
			entitySkins[0] = optimizedImage("/Images/entity.png");		//0 - Old skin
			entitySkins[ANIM_STILL] = optimizedImage("Images/Animation/Entity/Standing.png");
			entitySkins[ANIM_WALK_DOWN] = optimizedImage("Images/Animation/Entity/Walk_Down.png");	
			entitySkins[ANIM_WALK_LEFT] = optimizedImage("Images/Animation/Entity/Walk_Left.png");	
			entitySkins[ANIM_WALK_RIGHT] = optimizedImage("Images/Animation/Entity/Walk_Right.png");	
			entitySkins[ANIM_WALK_UP] = optimizedImage("Images/Animation/Entity/Walk_Up.png");
		}catch(IOException e){
			System.out.println("Error loading entitySkins");
		}

		try{
			entityFriendlySkins = new BufferedImage[]{		
					optimizedImage("/Images/pig.png"),
					optimizedImage("/Images/fox.png"),
			};
		}catch(IOException e){
			System.out.println("Error loading friendlySkins");
		}
		
		try{
			skySkins = new BufferedImage[]{
					optimizedImage("/Images/Sky/sun.png"),	//Sun Image [0]
					optimizedImage("/Images/Sky/moon.png"),	//Moon Image [1]
					optimizedImage("/Images/Sky/sunset.png"),	//sunset Image [2]
					optimizedImage("/Images/Sky/sunrise.png"),	//sunrise Image [3]
					optimizedImage("/Images/Sky/sunrise50.png"), //sunrise 50% opacity [4]
					optimizedImage("/Images/Sky/dark70.png"), //dark 70% opacity [5]
					optimizedImage("/Images/Sky/dark60.png") //dark 60% opacity [6]
					
			};
		}catch(IOException e){
			System.out.println("Error loading skySkins");
		}
		
		try{
			entityFriendlySkins = new BufferedImage[]{		
					optimizedImage("/Images/pig.png"),
					optimizedImage("/Images/fox.png")
			};
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}
		
		try{
			HUDIcons = new BufferedImage[2];
			HUDIcons[HUD_HEART] = optimizedImage("/Images/HUD/heart.png");
			HUDIcons[HUD_SWORD] = optimizedImage("/Images/HUD/sword.png");
		}catch(IOException e){
			System.out.println("Error loading HUDIcons");
		}
	}
	
	//optimize images for current system
		public BufferedImage optimizedImage(String resourceName) throws IOException
		{
			BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(resourceName));
			// obtain the current system graphical settings
			GraphicsConfiguration gfx_config = GraphicsEnvironment.
					getLocalGraphicsEnvironment().getDefaultScreenDevice().
					getDefaultConfiguration();

			/*
			 * if image is already compatible and optimized for current system 
			 * settings, simply return it
			 */
			if (image.getColorModel().equals(gfx_config.getColorModel()))
				return image;

			// image is not optimized, so create a new image that is
			BufferedImage new_image = gfx_config.createCompatibleImage(
					image.getWidth(), image.getHeight(), image.getTransparency());

			// get the graphics context of the new image to draw the old image on
			Graphics2D g2d = (Graphics2D) new_image.getGraphics();

			// actually draw the image and dispose of context no longer needed
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();

			// return the new optimized image
			return new_image; 
		}
}
