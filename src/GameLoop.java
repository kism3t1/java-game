import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;


public class GameLoop extends JavaGame implements Runnable, MouseListener,
MouseMotionListener{
	private boolean isRunning;
	private Canvas gui;
	private long cycleTime;
	private HUD hud = new HUD();
	private Sounds sound = new Sounds();
	
	private int cameraX, cameraY = 0;

	public GameLoop(Canvas gui){
		this.gui = gui;
		isRunning = true;

		gui.addKeyListener(new TAdapter());
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		gui.setFocusable(true);
		gui.requestFocusInWindow();

		//initial calculation of screen -> tile size
		screenTilesWide = gui.getWidth() / tileWidth;
		screenTilesHigh = gui.getHeight() / tileHeight;

		try {
			LoadResources.loadWorld(false);
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
		//update global screen size info
		guiWidth = gui.getWidth();
		guiHeight = gui.getHeight();

		//Plays background music
		try {
			sound.play();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		world.drawEnemies(g);
		
		//Draw friendlies
		world.drawFriendly(g);
		
		/*	
		 * Day night Cycle routine
		 */	

		if (gameTime.checkDateTime() == TOD_NIGHT){
			g.drawImage(skySkins[1], 30, 30, 100, 100, null);	//Moon
			//g.drawImage(skySkins[5], 0, 0, gui.getWidth(), gui.getHeight(), null); //Nightime 70% dark
			
		}else if (gameTime.checkDateTime() == TOD_DAYTIME){
			g.drawImage(skySkins[0], 30, 30, 100, 100, null);	//Sun
		
		}else if (gameTime.checkDateTime() == TOD_SUNSET){
			g.drawImage(skySkins[2], 30, 30, 100, 100, null);	//Half Sun with moon
			// Paints a 70% dark tile and fades in from 0-50. Gets darker
			//fadeSky.draw(g);	
			//SkyFade.increaseAlpha();
			
		}else if (gameTime.checkDateTime() == TOD_SUNRISE){
			g.drawImage(skySkins[3], 30, 30, 100, 100, null);	//Half sun
			// Paints a 70% dark tile and fades out from 50-0. Gets lighter
			//fadeSky.draw(g);
			//SkyFade.decreaseAlpha();
		}else{}

		//draw HUD
		hud.draw(g);
		
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
