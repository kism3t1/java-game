import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class LoadResources extends JavaGame implements Runnable{
	
	// obtain the current system graphical settings
	GraphicsConfiguration gfx_config = GraphicsEnvironment.
			getLocalGraphicsEnvironment().getDefaultScreenDevice().
			getDefaultConfiguration();

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
			enemySkins = new Animation[3][6];
				//Enemy
				enemySkins[0][0] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);		//0 - old skin
				enemySkins[0][ANIM_STILL] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
				enemySkins[0][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
				enemySkins[0][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
				enemySkins[0][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
				enemySkins[0][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
				
				//Eye
				enemySkins[1][0] = new Animation(optimizedImage("/Images/eye.png"), 22, 32, 0);		//0 - old skin
				enemySkins[1][ANIM_STILL] = new Animation(optimizedImage("/Images/Animation/Eye/Standing.png"), 32, 32, 0);
				enemySkins[1][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/Animation/Eye/Walk_Down.png"), 32, 32, 0);
				enemySkins[1][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/Animation/Eye/Walk_Left.png"), 32, 32, 0);
				enemySkins[1][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/Animation/Eye/Walk_Right.png"), 32, 32, 0);
				enemySkins[1][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/Animation/Eye/Walk_Up.png"), 32, 32, 0);
				
				//Snake
				enemySkins[2][0] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);		//0 - old skin
				enemySkins[2][ANIM_STILL] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);
				enemySkins[2][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);	
				enemySkins[2][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);
				enemySkins[2][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);	
				enemySkins[2][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}

		try{
			entitySkins = new Animation[6];
			entitySkins[0] = new Animation(optimizedImage("/Images/entity.png"), 22, 32, 0);		//0 - Old skin
			entitySkins[ANIM_STILL] = new Animation(optimizedImage("Images/Animation/Entity/Standing.png"), 32, 32, 0);
			entitySkins[ANIM_WALK_DOWN] = new Animation(optimizedImage("Images/Animation/Entity/Walk_Down.png"), 32, 32, 0);	
			entitySkins[ANIM_WALK_LEFT] = new Animation(optimizedImage("Images/Animation/Entity/Walk_Left.png"), 32, 32, 0);	
			entitySkins[ANIM_WALK_RIGHT] = new Animation(optimizedImage("Images/Animation/Entity/Walk_Right.png"), 32, 32, 0);	
			entitySkins[ANIM_WALK_UP] = new Animation(optimizedImage("Images/Animation/Entity/Walk_Up.png"), 32, 32, 0);
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
		
		skyTransparency = new BufferedImage[150];
			for(int i = 0; i < 150; i++)
				skyTransparency[i] = genPixel(new Color(0, 0, 0, i));
		
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
	private BufferedImage optimizedImage(String resourceName) throws IOException
	{
		BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(resourceName));

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
	
	//create buffered pixels
	private BufferedImage genPixel(Color color){
		BufferedImage pixel = gfx_config.createCompatibleImage(1, 1, Transparency.TRANSLUCENT);
		Graphics g = pixel.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, 1, 1);
		g.dispose();
		return pixel;
	}
}
