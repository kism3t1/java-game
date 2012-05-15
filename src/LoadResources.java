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
		tileSkins = new BufferedImage[4][];
		try{
			tileSkins[TOD_DAYTIME] = new BufferedImage[]{
					optimizedImage("/Images/dirt.png"),
					optimizedImage("/Images/grass.png"),
					optimizedImage("/Images/stone.png"),
					optimizedImage("/Images/tree.png"),
					optimizedImage("/Images/water.png")
			};
		}catch(IOException e){
			System.out.println("Error loading tileSkins");
		}

		//generate alternate tile skins
		tileSkins[TOD_NIGHT] = castImage(tileSkins[TOD_DAYTIME], TOD_NIGHT_COLOR);
		tileSkins[TOD_SUNRISE] = castImage(tileSkins[TOD_DAYTIME], TOD_SUNRISE_COLOR);
		tileSkins[TOD_SUNSET] = castImage(tileSkins[TOD_DAYTIME], TOD_SUNSET_COLOR);

		try{
			enemySkins = new Animation[4][3][6];
			//Enemy
			enemySkins[TOD_DAYTIME][0][0] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);		//0 - old skin
			enemySkins[TOD_DAYTIME][0][ANIM_STILL] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
			enemySkins[TOD_DAYTIME][0][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
			enemySkins[TOD_DAYTIME][0][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
			enemySkins[TOD_DAYTIME][0][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);
			enemySkins[TOD_DAYTIME][0][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/enemy.png"), 22, 32, 0);

			//Eye
			enemySkins[TOD_DAYTIME][1][0] = new Animation(optimizedImage("/Images/eye.png"), 22, 32, 0);		//0 - old skin
			enemySkins[TOD_DAYTIME][1][ANIM_STILL] = new Animation(optimizedImage("/Images/Animation/Eye/Standing.png"), 32, 32, 0);
			enemySkins[TOD_DAYTIME][1][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/Animation/Eye/Walk_Down.png"), 32, 32, 0);
			enemySkins[TOD_DAYTIME][1][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/Animation/Eye/Walk_Left.png"), 32, 32, 0);
			enemySkins[TOD_DAYTIME][1][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/Animation/Eye/Walk_Right.png"), 32, 32, 0);
			enemySkins[TOD_DAYTIME][1][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/Animation/Eye/Walk_Up.png"), 32, 32, 0);

			//Snake
			enemySkins[TOD_DAYTIME][2][0] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);		//0 - old skin
			enemySkins[TOD_DAYTIME][2][ANIM_STILL] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);
			enemySkins[TOD_DAYTIME][2][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);	
			enemySkins[TOD_DAYTIME][2][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);
			enemySkins[TOD_DAYTIME][2][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);	
			enemySkins[TOD_DAYTIME][2][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/snake.png"), 22, 32, 0);
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}

		//generate alternate enemy skins
		for(int i=0; i < enemySkins[0].length; i ++){
			enemySkins[TOD_NIGHT][i] = castImage(enemySkins[TOD_DAYTIME][i], TOD_NIGHT_COLOR);
			enemySkins[TOD_SUNRISE][i] = castImage(enemySkins[TOD_DAYTIME][i], TOD_SUNRISE_COLOR);
			enemySkins[TOD_SUNSET][i] = castImage(enemySkins[TOD_DAYTIME][i], TOD_SUNSET_COLOR);
		}

		try{
			entitySkins = new Animation[4][6];
			entitySkins[TOD_DAYTIME][0] = new Animation(optimizedImage("/Images/entity.png"), 22, 32, 0);		//0 - Old skin
			entitySkins[TOD_DAYTIME][ANIM_STILL] = new Animation(optimizedImage("Images/Animation/Entity/Standing.png"), 32, 32, 0);
			entitySkins[TOD_DAYTIME][ANIM_WALK_DOWN] = new Animation(optimizedImage("Images/Animation/Entity/Walk_Down.png"), 32, 32, 0);	
			entitySkins[TOD_DAYTIME][ANIM_WALK_LEFT] = new Animation(optimizedImage("Images/Animation/Entity/Walk_Left.png"), 32, 32, 0);	
			entitySkins[TOD_DAYTIME][ANIM_WALK_RIGHT] = new Animation(optimizedImage("Images/Animation/Entity/Walk_Right.png"), 32, 32, 0);	
			entitySkins[TOD_DAYTIME][ANIM_WALK_UP] = new Animation(optimizedImage("Images/Animation/Entity/Walk_Up.png"), 32, 32, 0);
		}catch(IOException e){
			System.out.println("Error loading entitySkins");
		}

		//generate alternate entity skins
		entitySkins[TOD_NIGHT] = castImage(entitySkins[TOD_DAYTIME], TOD_NIGHT_COLOR);
		entitySkins[TOD_SUNRISE] = castImage(entitySkins[TOD_DAYTIME], TOD_SUNRISE_COLOR);
		entitySkins[TOD_SUNSET] = castImage(entitySkins[TOD_DAYTIME], TOD_SUNSET_COLOR);

		try{
			entityFriendlySkins = new BufferedImage[4][];
			entityFriendlySkins[TOD_DAYTIME] = new BufferedImage[]{		
					optimizedImage("/Images/pig.png"),
					optimizedImage("/Images/fox.png")
			};
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}

		//generate alternate entity skins
		entityFriendlySkins[TOD_NIGHT] = castImage(entityFriendlySkins[TOD_DAYTIME], TOD_NIGHT_COLOR);
		entityFriendlySkins[TOD_SUNRISE] = castImage(entityFriendlySkins[TOD_DAYTIME], TOD_SUNRISE_COLOR);
		entityFriendlySkins[TOD_SUNSET] = castImage(entityFriendlySkins[TOD_DAYTIME], TOD_SUNSET_COLOR);

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

		/*
		skyTransparency = new BufferedImage[150];
			for(int i = 0; i < skyTransparency.length; i++)
				skyTransparency[i] = genPixel(new Color(0, 0, 0, i));
		 */

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

	/*
	//create buffered pixels
	private BufferedImage genPixel(Color color){
		BufferedImage pixel = gfx_config.createCompatibleImage(1, 1, Transparency.TRANSLUCENT);
		Graphics g = pixel.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, 1, 1);
		g.dispose();
		return pixel;
	}
*/
	
	private BufferedImage[] castImage(BufferedImage[] origImage, Color color){
		BufferedImage[] newTiles = new BufferedImage[origImage.length];

		for(int i=0; i < origImage.length; i++){
			//initialize new BufferedImage in array space
			newTiles[i] = gfx_config.createCompatibleImage(
					origImage[i].getWidth(), origImage[i].getHeight(), origImage[i].getTransparency());

			//get graphics from new tile to draw on
			Graphics g = newTiles[i].getGraphics();

			//copy original tile to new tile
			g.drawImage(origImage[i], 0, 0, null);

			//add colour overlay
			g.setColor(color);
			g.fillRect(0, 0, newTiles[i].getWidth(), newTiles[i].getHeight());
			g.dispose();
		}

		return newTiles;
	}

	private Animation[] castImage(Animation[] origAnim, Color color){
		Animation[] newAnim = new Animation[origAnim.length];
		BufferedImage newImage = null;

		for(int i=0; i < origAnim.length; i++){
			//initialize new BufferedImage in array space
			newImage = gfx_config.createCompatibleImage(
					origAnim[i].getImage().getWidth(), 
					origAnim[i].getImage().getHeight(), 
					origAnim[i].getImage().getTransparency());

			//get graphics from new tile to draw on
			Graphics g = newImage.getGraphics();

			//copy original tile to new tile
			g.drawImage(origAnim[i].getImage(), 0, 0, null);

			//add colour overlay
			g.setColor(color);
			g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
			
			//restore transparent areas
			for(int x = 0; x < newImage.getWidth(); x++){
				for(int y = 0; y < newImage.getHeight(); y ++){
					if((origAnim[i].getImage().getRGB(x, y) >> 24) == 0xff000000)
						newImage.setRGB(x, y, origAnim[i].getImage().getRGB(x, y));
				}
			}
			
			g.dispose();
			
			
			newAnim[i] = new Animation(newImage, origAnim[i].getWidth(), origAnim[i].getHeight(), origAnim[i].getFrameDelay());
		}

		return newAnim;
	}
}
