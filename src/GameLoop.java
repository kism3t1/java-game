import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class GameLoop extends JavaGame implements Runnable, MouseListener,
MouseMotionListener{
	private boolean isRunning;
	private Canvas gui;
	private long cycleTime;
	private HUD hud = new HUD();
	
	private int cameraX, cameraY = 0;

	public GameLoop(Canvas gui){
		this.gui = gui;
		isRunning = true;
		
		guiWidth = gui.getWidth();
		guiHeight = gui.getHeight();

		gui.addKeyListener(new TAdapter());
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		gui.setFocusable(true);
		gui.requestFocusInWindow();

		//initial calculation of screen -> tile size
		screenTilesWide = gui.getWidth() / tileWidth;
		screenTilesHigh = gui.getHeight() / tileHeight;

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
			entitySkins = new BufferedImage[]{
					optimizedImage("/Images/entity.png")
					
			};
		}catch(IOException e){
			System.out.println("Error loading entitySkins");
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
			HUDIcons = new BufferedImage[1];
			HUDIcons[HUD_HEART] = optimizedImage("/Images/HUD/heart.png");
		}catch(IOException e){
			System.out.println("Error loading HUDIcons");
		}

		try {
			loadWorld(false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		cycleTime = System.currentTimeMillis();
		gui.createBufferStrategy(2);				//2 = double buffer
		BufferStrategy strategy = gui.getBufferStrategy();

		//game loop
		while(isRunning){
		
			updateGameState();			//calculates any necessary changes to game play objects

			updateGUI(strategy);		//redraws GUI

			syncFPS();					//tries to keep game at target FPS
		}
	}


	private void updateGameState(){
		//TODO add movement and scrolling logic 

		world.entity.move();

		//move enemies
		world.moveEnemies();
		
		//move friendlies
		world.moveFriendlies();

		// move enemies and entity for scrolling effect		
		if(world.entity.getX() > gui.getWidth() * 0.6
				&& (int)(cameraX / tileWidth) < world.floorMap.TileSet.length - screenTilesWide)
		{
			int dif = (int) (world.entity.getX() - gui.getWidth() * 0.6);
			cameraX += dif;
			world.entity.setX(world.entity.getX() - dif);
			for (int i = 0; i < world.enemy.size(); i++) {
				world.enemy.get(i).setX(world.enemy.get(i).getX() - dif);
			}
			for (int i = 0; i < world.friendly.size(); i++) {
				world.friendly.get(i).setX(world.friendly.get(i).getX() - dif);
			}
		}
		
		if(world.entity.getX() < gui.getWidth() * 0.4 && cameraX > 0)
		{
			int dif = (int) (gui.getWidth() * 0.4 - world.entity.getX());
			cameraX -= dif;
			world.entity.setX(world.entity.getX() + dif);
			for (int i = 0; i < world.enemy.size(); i++) {
				world.enemy.get(i).setX(world.enemy.get(i).getX() + dif);
			}
			for (int i = 0; i < world.friendly.size(); i++) {
				world.friendly.get(i).setX(world.friendly.get(i).getX() + dif);
			}
		}
		
		if(world.entity.getY() > gui.getHeight() * 0.6
				&& (int)(cameraY / tileHeight) < world.floorMap.TileSet[0].length - screenTilesHigh)
		{
			int dif = (int) (world.entity.getY() - gui.getHeight() * 0.6);
			cameraY += dif;
			world.entity.setY(world.entity.getY() - dif);
			for (int i = 0; i < world.enemy.size(); i++) {
				world.enemy.get(i).setY(world.enemy.get(i).getY() - dif);
			}
			for (int i = 0; i < world.friendly.size(); i++) {
				world.friendly.get(i).setY(world.friendly.get(i).getY() - dif);
			}
		}
		
		if(world.entity.getY() < gui.getHeight() * 0.4 && cameraY > 0)
		{
			int dif = (int) (gui.getHeight() * 0.4 - world.entity.getY());
			cameraY -= dif;
			world.entity.setY(world.entity.getY() + dif);
			for (int i = 0; i < world.enemy.size(); i++) {
				world.enemy.get(i).setY(world.enemy.get(i).getY() + dif);
			}
			for (int i = 0; i < world.friendly.size(); i++) {
				world.friendly.get(i).setY(world.friendly.get(i).getY() + dif);
			}
		}
	
	}

	private void updateGUI(BufferStrategy strategy){
		Graphics g = strategy.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		
		// draw maps
		world.floorMap.draw(g, cameraX, cameraY);
		world.wallMap.draw(g, cameraX, cameraY);

		// draw entity
		world.entity.draw(g);
		
		// draw enemies
		for (int i = 0; i < world.enemy.size(); i++) {
			g.drawImage(enemySkins[world.enemy.get(i).getSkin()], world.enemy.get(i).getX(), world.enemy.get(i).getY(), null);
		}
		
		//Draw friendlies
		for (int i = 0; i < world.friendly.size(); i++) {
			g.drawImage(entityFriendlySkins[world.friendly.get(i).getSkin()], world.friendly.get(i).getX(), world.friendly.get(i).getY(), null);
		}
		
		//draw HUD
		hud.draw(g);
		
		/*	
		 * Day night Cycle routine
		 */	

		if (ReturnTime.returnTimeOfDay() == TimeOfDay.NIGHT){
			g.drawImage(skySkins[1], 30, 30, 100, 100, null);	//Moon
			g.drawImage(skySkins[5], 0, 0, gui.getWidth(), gui.getHeight(), null); //Nightime 70% dark
			
		}else if (ReturnTime.returnTimeOfDay() == TimeOfDay.DAYTIME){
			g.drawImage(skySkins[0], 30, 30, 100, 100, null);	//Sun
		
		}else if (ReturnTime.returnTimeOfDay() == TimeOfDay.SUNSET){
			g.drawImage(skySkins[2], 30, 30, 100, 100, null);	//Half Sun with moon
			// Paints a 70% dark tile and fades in from 0-50. Gets darker
			fadeSky.paint(g);	
			SkyFade.increaseAlpha();
			
		}else if (ReturnTime.returnTimeOfDay() == TimeOfDay.SUNRISE){
			g.drawImage(skySkins[3], 30, 30, 100, 100, null);	//Half sun
			// Paints a 70% dark tile and fades out from 50-0. Gets lighter
			fadeSky.paint(g);
			SkyFade.decreaseAlpha();
		}else{}
		
		g.dispose();
		strategy.show();
		
		
	}

	private void syncFPS(){
		cycleTime = cycleTime + FRAME_DELAY;
		long difference = cycleTime - System.currentTimeMillis();
		try {
			Thread.sleep(Math.max(0, difference));
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public void loadWorld(boolean confirm) throws IOException,
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

	// mouse control
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent m) {}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	private class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {

			world.entity.keyReleased(e);

		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			world.entity.keyPressed(e);

			switch (key) {
			case KeyEvent.VK_ESCAPE:	//kill thread and exit to menu
				nextThread = "MENU";
				isRunning = false;
				break;
			}
		}
	}
}
