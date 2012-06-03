import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;


public class GameLoop extends Halja implements Runnable, MouseListener,
MouseMotionListener{
	private boolean isRunning, inMenu;
	private Canvas gui;
	private long cycleTime;
	private KeyListener kl;
	private HUD hud = new HUD();
	private Sounds sound = new Sounds();
	
	//to calculate FPS
	private static final int MAX_SAMPLES = 10;
	private int tickIndex = 0;
	private int tickSum = 0;
	private Integer[] tickList;
	private int tick = 0;
	private long tickTime = 0;
	private double FPS = 0;
	private boolean displayFPS = true;

	public GameLoop(Canvas gui){
		this.gui = gui;
		isRunning = true;
		inMenu = false;
		
		tickList = new Integer[MAX_SAMPLES];
		for(int i = 0; i < MAX_SAMPLES; i++)
			tickList[i] = 0;

		kl = new TAdapter();
		gui.addKeyListener(kl);
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		gui.setFocusable(true);
		gui.requestFocusInWindow();

		//initial calculation of screen -> tile size
		screenTilesWide = gui.getWidth() / tileWidth;
		screenTilesHigh = gui.getHeight() / tileHeight;
		
		if(world != null)
			world = null;

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
		tickTime = (int) System.currentTimeMillis();
		gui.createBufferStrategy(2);				//2 = double buffer
		BufferStrategy strategy = gui.getBufferStrategy();

		//game loop
		while(isRunning){
			
				updateGameState();			//calculates any necessary changes to game play objects

				updateGUI(strategy);		//redraws GUI

				syncFPS();					//tries to keep game at target FPS
				
				tick++;
				if(System.currentTimeMillis() - tickTime >= 1000){
					FPS = getFPS(tick);
					tickTime = System.currentTimeMillis();
					tick = 0;
				}
				
				
				if(inMenu){
					gui.removeKeyListener(kl);
					latch = new CountDownLatch(1);
					Thread iThread = new Thread(new InventoryMenu(gui));
					iThread.start();
					try {
						latch.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	//wait until resources are loaded to continue
					inMenu = false;
					gui.addKeyListener(kl);
				}
		}
	}


	private void updateGameState(){
		//update global screen size info
		guiWidth = gui.getWidth();
		guiHeight = gui.getHeight();

		//Plays background music
		/*try {
			sound.playMidi();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		world.ollie.move();

		//move enemies
		world.moveEnemies();
		
		//move friendlies
		world.moveFriendlies();

		// move enemies and entity for scrolling effect		
		if(world.ollie.getX() > gui.getWidth() * 0.6
				&& (int)(cameraX / tileWidth) < world.floorMap.TileSet.length - screenTilesWide)
		{
			int dif = (int) (world.ollie.getX() - gui.getWidth() * 0.6);
			cameraX += dif;
			world.ollie.setX(world.ollie.getX() - dif);
			for (int i = 0; i < world.enemy.size(); i++) {
				world.enemy.get(i).setX(world.enemy.get(i).getX() - dif);
			}
			for (int i = 0; i < world.friendly.size(); i++) {
				world.friendly.get(i).setX(world.friendly.get(i).getX() - dif);
			}
		}
		
		if(world.ollie.getX() < gui.getWidth() * 0.4 && cameraX > 0)
		{
			int dif = (int) (gui.getWidth() * 0.4 - world.ollie.getX());
			cameraX -= dif;
			world.ollie.setX(world.ollie.getX() + dif);
			for (int i = 0; i < world.enemy.size(); i++) {
				world.enemy.get(i).setX(world.enemy.get(i).getX() + dif);
			}
			for (int i = 0; i < world.friendly.size(); i++) {
				world.friendly.get(i).setX(world.friendly.get(i).getX() + dif);
			}
		}
		
		if(world.ollie.getY() > gui.getHeight() * 0.6
				&& (int)(cameraY / tileHeight) < world.floorMap.TileSet[0].length - screenTilesHigh)
		{
			int dif = (int) (world.ollie.getY() - gui.getHeight() * 0.6);
			cameraY += dif;
			world.ollie.setY(world.ollie.getY() - dif);
			for (int i = 0; i < world.enemy.size(); i++) {
				world.enemy.get(i).setY(world.enemy.get(i).getY() - dif);
			}
			for (int i = 0; i < world.friendly.size(); i++) {
				world.friendly.get(i).setY(world.friendly.get(i).getY() - dif);
			}
		}
		
		if(world.ollie.getY() < gui.getHeight() * 0.4 && cameraY > 0)
		{
			int dif = (int) (gui.getHeight() * 0.4 - world.ollie.getY());
			cameraY -= dif;
			world.ollie.setY(world.ollie.getY() + dif);
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
		world.ollie.draw(g);
		
		// draw enemies
		world.drawEnemies(g);
		
		//Draw friendlies
		world.drawFriendly(g);
		
		/*	
		 * Day night Cycle routine
		 */	

		if (gameTime.checkDateTime() == TOD_NIGHT){
			g.drawImage(skySkins[1], 30, 30, 100, 100, null);	//Moon
			
		}else if (gameTime.checkDateTime() == TOD_DAYTIME){
			g.drawImage(skySkins[0], 30, 30, 100, 100, null);	//Sun
		
		}else if (gameTime.checkDateTime() == TOD_SUNSET){
			g.drawImage(skySkins[2], 30, 30, 100, 100, null);	//Half Sun with moon
			
		}else if (gameTime.checkDateTime() == TOD_SUNRISE){
			g.drawImage(skySkins[3], 30, 30, 100, 100, null);	//Half sun
		}else{}

		//draw HUD
		hud.draw(g);
		
		//write FPS
		if(displayFPS){
			g.setColor(Color.WHITE);
			g.drawString("FPS: " + String.valueOf(FPS), guiWidth - 60, 20);
		}
		
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
	public void mouseClicked(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			world.ollie.setDestination(world.floorMap.getTileAtPos(e.getPoint(), cameraX, cameraY));
			break;
		}
	}

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

			//world.ollie.keyReleased(e);

		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			//world.ollie.keyPressed(e);

			switch (key) {
			case KeyEvent.VK_ESCAPE:	//kill thread and exit to menu
				nextThread = "MENU";
				isRunning = false;
				break;
			case KeyEvent.VK_I:
				//start inventory menu thread
				inMenu = true;
				break;
			case KeyEvent.VK_F11:
				try {
					LoadResources.loadGame("1.save");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case KeyEvent.VK_F12:
				try {
					LoadResources.saveGame("1.save");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			}
		}
	}
	
	private double getFPS(int newTick)
	{
	    tickSum-=tickList[tickIndex];  /* subtract value falling off */
	    tickSum+=newTick;              /* add new value */
	    tickList[tickIndex]=newTick;   /* save new value so it can be subtracted later */
	    if(tickIndex+1==MAX_SAMPLES){    /* inc buffer index */
	    	tickIndex=0;
	    }else{
	    	tickIndex++;
	    }

	    /* return average */
	    return((double)tickSum/MAX_SAMPLES);
	}
}
