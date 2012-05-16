import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


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
		tileSkins[TOD_NIGHT] = castImageArray(tileSkins[TOD_DAYTIME], TOD_NIGHT_COLOR);
		tileSkins[TOD_SUNRISE] = castImageArray(tileSkins[TOD_DAYTIME], TOD_SUNRISE_COLOR);
		tileSkins[TOD_SUNSET] = castImageArray(tileSkins[TOD_DAYTIME], TOD_SUNSET_COLOR);

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
			enemySkins[TOD_DAYTIME][2][ANIM_STILL] = new Animation(optimizedImage("/Images/Animation/Snake/Standing.png"), 32, 32, 0);
			enemySkins[TOD_DAYTIME][2][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/Animation/Snake/Walk_Down.png"), 32, 32, 0);	
			enemySkins[TOD_DAYTIME][2][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/Animation/Snake/Walk_Left.png"), 32, 32, 0);
			enemySkins[TOD_DAYTIME][2][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/Animation/Snake/Standing.png"), 32, 32, 0);	
			enemySkins[TOD_DAYTIME][2][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/Animation/Snake/Walk_Up.png"), 32, 32, 0);
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}

		//generate alternate enemy skins
		for(int i=0; i < enemySkins[0].length; i ++){
			enemySkins[TOD_NIGHT][i] = castImageArray(enemySkins[TOD_DAYTIME][i], TOD_NIGHT_COLOR);
			enemySkins[TOD_SUNRISE][i] = castImageArray(enemySkins[TOD_DAYTIME][i], TOD_SUNRISE_COLOR);
			enemySkins[TOD_SUNSET][i] = castImageArray(enemySkins[TOD_DAYTIME][i], TOD_SUNSET_COLOR);
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
		entitySkins[TOD_NIGHT] = castImageArray(entitySkins[TOD_DAYTIME], TOD_NIGHT_COLOR);
		entitySkins[TOD_SUNRISE] = castImageArray(entitySkins[TOD_DAYTIME], TOD_SUNRISE_COLOR);
		entitySkins[TOD_SUNSET] = castImageArray(entitySkins[TOD_DAYTIME], TOD_SUNSET_COLOR);

		try{
			//Friendly Entities
			entityFriendlySkins = new Animation[4][2][6];
			//Pig
			entityFriendlySkins[TOD_DAYTIME][0][0] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);		//0 - old skin
			entityFriendlySkins[TOD_DAYTIME][0][ANIM_STILL] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);
			entityFriendlySkins[TOD_DAYTIME][0][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);
			entityFriendlySkins[TOD_DAYTIME][0][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);
			entityFriendlySkins[TOD_DAYTIME][0][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);
			entityFriendlySkins[TOD_DAYTIME][0][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);

			//Fox
			entityFriendlySkins[TOD_DAYTIME][1][0] = new Animation(optimizedImage("/Images/fox.png"), 22, 32, 0);		//0 - old skin
			entityFriendlySkins[TOD_DAYTIME][1][ANIM_STILL] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);
			entityFriendlySkins[TOD_DAYTIME][1][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);
			entityFriendlySkins[TOD_DAYTIME][1][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);
			entityFriendlySkins[TOD_DAYTIME][1][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);
			entityFriendlySkins[TOD_DAYTIME][1][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);

		}catch(IOException e){
			System.out.println("Error loading FriendlySkins");
		}
		//generate alternate entity skins
			for(int i=0; i < entityFriendlySkins[0].length; i ++){
				entityFriendlySkins[TOD_NIGHT][i] = castImageArray(entityFriendlySkins[TOD_DAYTIME][i], TOD_NIGHT_COLOR);
				entityFriendlySkins[TOD_SUNRISE][i] = castImageArray(entityFriendlySkins[TOD_DAYTIME][i], TOD_SUNRISE_COLOR);
				entityFriendlySkins[TOD_SUNSET][i] = castImageArray(entityFriendlySkins[TOD_DAYTIME][i], TOD_SUNSET_COLOR);
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
	
	private BufferedImage[] castImageArray(BufferedImage[] origImage, Color color){
		BufferedImage[] newTiles = new BufferedImage[origImage.length];

		for(int i=0; i < origImage.length; i++)
			newTiles[i] = castImage(origImage[i], color);

		return newTiles;
	}

	private Animation[] castImageArray(Animation[] origAnim, Color color){
		Animation[] newAnim = new Animation[origAnim.length];

		for(int i=0; i < origAnim.length; i++)
			newAnim[i] = new Animation(castImage(origAnim[i].getImage(), color), origAnim[i].getWidth(), origAnim[i].getHeight(), origAnim[i].getFrameDelay());

		return newAnim;
	}
	
	private BufferedImage castImage(BufferedImage origImage, Color color){
		//initialize new BufferedImage in array space
		BufferedImage newImage = gfx_config.createCompatibleImage(
				origImage.getWidth(), origImage.getHeight(), origImage.getTransparency());

		//get graphics from new tile to draw on
		Graphics g = newImage.getGraphics();

		//copy original tile to new tile
		g.drawImage(origImage, 0, 0, null);

		//add colour overlay
		g.setColor(color);
		g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
		
		//restore transparent areas
		for(int x = 0; x < newImage.getWidth(); x++){
			for(int y = 0; y < newImage.getHeight(); y ++){
				//System.out.println(origAnim[i].getImage().getRGB(x, y));
				if(origImage.getRGB(x, y) == 0)
					newImage.setRGB(x, y, origImage.getRGB(x, y));
			}
		}
		
		g.dispose();
		
		return newImage;
	}
	
	// file handling
	public static void saveWorld() throws IOException {
		int n = JOptionPane.showConfirmDialog(null, "Save current world?",
				"Save Dialog", JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			FileOutputStream saveFile = new FileOutputStream("world.wld");
			GZIPOutputStream gzipFile = new GZIPOutputStream(saveFile);
			ObjectOutputStream saveObject = new ObjectOutputStream(gzipFile);
			saveObject.writeObject(world);
			saveObject.flush();
			saveObject.close();
			gzipFile.close();
			saveFile.close();
		}

	}

	public static void loadWorld(boolean confirm) throws IOException,
	ClassNotFoundException {
		int n = 0;
		if (confirm)
			n = JOptionPane.showConfirmDialog(null,
					"Load previously saved world?", "Load Dialog",
					JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION || !confirm) {
			FileInputStream loadFile = new FileInputStream("world.wld");
			GZIPInputStream gzipFile = new GZIPInputStream(loadFile);
			ObjectInputStream loadObject = new ObjectInputStream(gzipFile);
			world = (World) loadObject.readObject();
			gzipFile.close();
			loadObject.close();
			loadFile.close();
		}
	}
}
