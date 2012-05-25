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


public class LoadResources extends Halja implements Runnable{

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
			friendlySkins = new Animation[4][2][6];
			//Pig
			friendlySkins[TOD_DAYTIME][0][0] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);		//0 - old skin
			friendlySkins[TOD_DAYTIME][0][ANIM_STILL] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);
			friendlySkins[TOD_DAYTIME][0][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);
			friendlySkins[TOD_DAYTIME][0][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);
			friendlySkins[TOD_DAYTIME][0][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);
			friendlySkins[TOD_DAYTIME][0][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/pig.png"), 32, 32, 0);

			//Fox
			friendlySkins[TOD_DAYTIME][1][0] = new Animation(optimizedImage("/Images/fox.png"), 22, 32, 0);		//0 - old skin
			friendlySkins[TOD_DAYTIME][1][ANIM_STILL] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);
			friendlySkins[TOD_DAYTIME][1][ANIM_WALK_DOWN] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);
			friendlySkins[TOD_DAYTIME][1][ANIM_WALK_LEFT] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);
			friendlySkins[TOD_DAYTIME][1][ANIM_WALK_RIGHT] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);
			friendlySkins[TOD_DAYTIME][1][ANIM_WALK_UP] = new Animation(optimizedImage("/Images/fox.png"), 32, 32, 0);

		}catch(IOException e){
			System.out.println("Error loading FriendlySkins");
		}
		//generate alternate entity skins
			for(int i=0; i < friendlySkins[0].length; i ++){
				friendlySkins[TOD_NIGHT][i] = castImageArray(friendlySkins[TOD_DAYTIME][i], TOD_NIGHT_COLOR);
				friendlySkins[TOD_SUNRISE][i] = castImageArray(friendlySkins[TOD_DAYTIME][i], TOD_SUNRISE_COLOR);
				friendlySkins[TOD_SUNSET][i] = castImageArray(friendlySkins[TOD_DAYTIME][i], TOD_SUNSET_COLOR);
			}

		try{
			skySkins = new BufferedImage[]{
					optimizedImage("/Images/Sky/sun.png"),	//Sun Image [0]
					optimizedImage("/Images/Sky/moon.png"),	//Moon Image [1]
					optimizedImage("/Images/Sky/sunset.png"),	//sunset Image [2]
					optimizedImage("/Images/Sky/sunrise.png"),	//sunrise Image [3]

			};
		}catch(IOException e){
			System.out.println("Error loading skySkins");
		}

		try{
			HUDIcons = new BufferedImage[2];
			HUDIcons[HUD_HEART] = optimizedImage("/Images/HUD/heart.png");
			HUDIcons[HUD_SWORD] = optimizedImage("/Images/HUD/sword.png");
		}catch(IOException e){
			System.out.println("Error loading HUDIcons");
		}
		
		//WEAPONS
		try{
			weaponSkin = new BufferedImage[4][6];
			weaponSkin[TOD_DAYTIME][WPN_SLINGSHOT] = optimizedImage("/Images/Weapon/sword.png");
			weaponSkin[TOD_DAYTIME][WPN_IRON_SWORD] = optimizedImage("/Images/Weapon/sword.png");
			weaponSkin[TOD_DAYTIME][WPN_IRON_FIRE_SWORD] = optimizedImage("/Images/Weapon/firesword.png");
			weaponSkin[TOD_DAYTIME][WPN_IRON_FROST_SWORD] = optimizedImage("/Images/Weapon/frostsword.png");
			weaponSkin[TOD_DAYTIME][WPN_IRON_ETHERT_SWORD] = optimizedImage("/Images/Weapon/ethertsword.png");
			weaponSkin[TOD_DAYTIME][WPN_IRON_MAYTH_SWORD] = optimizedImage("/Images/Weapon/maythsword.png");
		}catch(IOException e){
			System.out.println("Error loading weaponSkin");
		}
		
		for(int i=0; i < weaponSkin[0].length; i++){
			weaponSkin[TOD_NIGHT][i] = castImage(weaponSkin[TOD_DAYTIME][i], TOD_NIGHT_COLOR);
			weaponSkin[TOD_SUNRISE][i] = castImage(weaponSkin[TOD_DAYTIME][i], TOD_SUNRISE_COLOR);
			weaponSkin[TOD_SUNSET][i] = castImage(weaponSkin[TOD_DAYTIME][i], TOD_SUNSET_COLOR);
		}
		
		weaponMenuText = new String[6];
		weaponMenuText[WPN_SLINGSHOT] = "Slinghsot";
		weaponMenuText[WPN_IRON_SWORD] = "Iron Sword";
		weaponMenuText[WPN_IRON_FIRE_SWORD] = "Iron Fire Sword";
		weaponMenuText[WPN_IRON_FROST_SWORD] = "Iron Frost Sword";
		weaponMenuText[WPN_IRON_ETHERT_SWORD] = "Iron Ethert Sword";
		weaponMenuText[WPN_IRON_MAYTH_SWORD] = "Iron Mayth Sword";
		
		weapon = new Weapon[6];
		weapon[WPN_SLINGSHOT] = new Weapon(5, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, true);
		weapon[WPN_IRON_SWORD] = new Weapon(15, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, false);
		weapon[WPN_IRON_FIRE_SWORD] = new Weapon(15, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, false);
		weapon[WPN_IRON_FROST_SWORD] = new Weapon(25, 0.0f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f, false);
		weapon[WPN_IRON_ETHERT_SWORD] = new Weapon(30, 0.0f, 0.0f, 0.0f, 0.1f, 0.0f, 0.0f, false);
		weapon[WPN_IRON_MAYTH_SWORD] = new Weapon(35, 0.0f, 0.0f, 0.0f, 0.0f, 0.2f, 0.0f, false);
		
		//POTIONS
		try{
			potionSkin = new BufferedImage[4][3];
			potionSkin[TOD_DAYTIME][PTN_POTION] = optimizedImage("/Images/Potion/firepotion.png");
			potionSkin[TOD_DAYTIME][PTN_FIRE] = optimizedImage("/Images/Potion/maythpotion.png");
			potionSkin[TOD_DAYTIME][PTN_MAYTH] = optimizedImage("/Images/Potion/maythpotion.png");
		}catch(IOException e){
			System.out.println("Error loading potionSkin");
		}
		
		for(int i=0; i < potionSkin[0].length; i++){
			potionSkin[TOD_NIGHT][i] = castImage(potionSkin[TOD_DAYTIME][i], TOD_NIGHT_COLOR);
			potionSkin[TOD_SUNRISE][i] = castImage(potionSkin[TOD_DAYTIME][i], TOD_SUNRISE_COLOR);
			potionSkin[TOD_SUNSET][i] = castImage(potionSkin[TOD_DAYTIME][i], TOD_SUNSET_COLOR);
		}
		
		potionMenuText = new String[3];
		potionMenuText[PTN_POTION] = "Normal Potion";
		potionMenuText[PTN_FIRE] = "Fire Potion";
		potionMenuText[PTN_MAYTH] = "Mayth Potion";
		
		potion = new Potion[3];
		potion[PTN_POTION] = new Potion(5, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, true);
		potion[PTN_FIRE] = new Potion(15, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, true);
		potion[PTN_MAYTH] = new Potion(15, 0.0f, 0.0f, 0.0f, 0.0f, 0.3f, 0.0f, true);
		
		//ITEMS
		
		try{
			itemSkin = new BufferedImage[4][1];
			itemSkin[TOD_DAYTIME][ITM_GOLD] = optimizedImage("/Images/Item/gold.png");
		
		}catch(IOException e){
			System.out.println("Error loading itemSkin");
		}
		
		for(int i=0; i < itemSkin[0].length; i++){
			itemSkin[TOD_NIGHT][i] = castImage(itemSkin[TOD_DAYTIME][i], TOD_NIGHT_COLOR);
			itemSkin[TOD_SUNRISE][i] = castImage(itemSkin[TOD_DAYTIME][i], TOD_SUNRISE_COLOR);
			itemSkin[TOD_SUNSET][i] = castImage(itemSkin[TOD_DAYTIME][i], TOD_SUNSET_COLOR);
		}
		
		itemMenuText = new String[1];
		itemMenuText[ITM_GOLD] = "Gold";

		
		item = new Item[1];
		item[ITM_GOLD] = new Item(15, false);
		
		//Armour
		
		try{
			armourSkin = new BufferedImage[4][2];
			armourSkin[TOD_DAYTIME][ARM_STEAL_SHIELD] = optimizedImage("/Images/Armour/stealshield.png");
			armourSkin[TOD_DAYTIME][ARM_ETHERT_SHIELD] = optimizedImage("/Images/Armour/ethertshield.png");
		
		}catch(IOException e){
			System.out.println("Error loading armourSkin");
		}
		
		for(int i=0; i < armourSkin[0].length; i++){
			armourSkin[TOD_NIGHT][i] = castImage(armourSkin[TOD_DAYTIME][i], TOD_NIGHT_COLOR);
			armourSkin[TOD_SUNRISE][i] = castImage(armourSkin[TOD_DAYTIME][i], TOD_SUNRISE_COLOR);
			armourSkin[TOD_SUNSET][i] = castImage(armourSkin[TOD_DAYTIME][i], TOD_SUNSET_COLOR);
		}
		
		armourMenuText = new String[2];
		armourMenuText[ARM_STEAL_SHIELD] = "Steal Shield";
		armourMenuText[ARM_ETHERT_SHIELD] = "Ethert Shield";

		
		armour = new Armour[2];
		armour[ARM_STEAL_SHIELD] = new Armour(15, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 39);
		armour[ARM_ETHERT_SHIELD] = new Armour(15, 0.0f, 0.0f, 0.0f, 0.3f, 0.0f, 0.0f, 39);


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
			
			world.PrepareSave();
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
			world.Initialise();
			
			gzipFile.close();
			loadObject.close();
			loadFile.close();
		}
	}
	
	public static void saveGame(String fileName) throws IOException{
		String out = null;
		FileOutputStream saveFile = new FileOutputStream(fileName);
		
		out = "CAMERA_X=" + cameraX + ";";
		saveFile.write(out.getBytes());
		
		out = "CAMERA_Y=" + cameraY + ";";
		saveFile.write(out.getBytes());
		
		out = "OLLIE_X=" + world.ollie.getX() + ";";
		saveFile.write(out.getBytes());
		
		out = "OLLIE_Y=" + world.ollie.getY() + ";";
		saveFile.write(out.getBytes());
		
		out = "OLLIE_HEALTH=" + world.ollie.getHealth() + ";";
		saveFile.write(out.getBytes());
		
		out = "OLLIE_ARMOUR=" + world.ollie.getArmour() + ";";
		saveFile.write(out.getBytes());
		
		out = "WEAPON_EQUIPPED=" + eqpWeapon + ";";
		saveFile.write(out.getBytes());
		
		out = "ARMOUR_EQUIPPED=" + eqpArmour + ";";
		saveFile.write(out.getBytes());
		
		out = "INV_WEAPONS=" + invWeapons + ";";
		saveFile.write(out.getBytes());
		
		out = "INV_ARMOUR=" + invArmour + ";";
		saveFile.write(out.getBytes());
		
		out = "INV_POTIONS=" + invPotions + ";";
		saveFile.write(out.getBytes());
		
		out = "INV_ITEMS=" + invItems + ";";
		saveFile.write(out.getBytes());
		
		out = "STR_WEAPONS=" + strWeapons + ";";
		saveFile.write(out.getBytes());
		
		out = "STR_ARMOUR=" + strArmour + ";";
		saveFile.write(out.getBytes());

		saveFile.close();
	}
	
	public static void loadGame(String fileName) throws IOException{
		FileInputStream loadFile = new FileInputStream(fileName);
		
		String in = "";
		int i;
		while((i = loadFile.read()) != -1){
			in += (char)i;
		}
		loadFile.close();
		
		String[] loaded = in.split("[;]");
		
		for(int x = 0; x < loaded.length; x++){
			String[] value = loaded[x].split("[=]");
			
			switch(value[0]){
			case "CAMERA_X":
				cameraX = Integer.parseInt(value[1]);
				break;
			case "CAMERA_Y":
				cameraY = Integer.parseInt(value[1]);
				break;
			case "OLLIE_X":
				world.ollie.setX(Integer.parseInt(value[1]));
				break;
			case "OLLIE_Y":
				world.ollie.setY(Integer.parseInt(value[1]));
				break;
			case "OLLIE_HEALTH":
				world.ollie.setHealth(Integer.parseInt(value[1]));
				break;
			case "OLLIE_ARMOUR":
				world.ollie.setArmour(Integer.parseInt(value[1]));
				break;
			case "WEAPON_EQUIPPED":
				eqpWeapon = Integer.parseInt(value[1]);
				break;
			case "ARMOUR_EQUIPPED":
				eqpArmour = Integer.parseInt(value[1]);
				break;
			case "INV_WEAPONS":
				invWeapons = value[1];
				break;
			case "INV_ARMOUR":
				invArmour = value[1];
				break;
			case "INV_POTIONS":
				invPotions = value[1];
				break;
			case "INV_ITEMS":
				invItems = value[1];
				break;
			case "STR_WEAPONS":
				strWeapons = value[1];
				break;
			case "STR_ARMOUR":
				strArmour = value[1];
				break;
			}
		}
	}
}
