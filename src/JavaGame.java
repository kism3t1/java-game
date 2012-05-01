import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class JavaGame {
	
	public static final int FRAME_DELAY = 20;	// frame delay in milliseconds (i.e. 1000/20 = 50 FPS)
	public static final int KEY_DELAY = 75; 	// set delay in milliseconds between key strokes
	
	// tile level identifiers
	public static final int LEVEL_FLOOR = 0;
	public static final int LEVEL_WALL = 1;
	
	// default map info
	public static final int MAP_TILES_HIGH = 100;
	public static final int MAP_TILES_WIDE = 100;
	
	public static World world;
	public static BufferedImage[] tileSkins;
	public static BufferedImage[] enemySkins;
	public static BufferedImage[] entitySkins;
	
	public static Entity entity;
	public static ArrayList<Enemy> enemy = new ArrayList<Enemy>();
	
	// tile offset for scrolling
	public static int xOffset = 0;
	public static int yOffset = 0;
	public static int prevXOffset = 0;
	public static int prevYOffset = 0;
	
	// screen size info to aide scrolling
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static int screenTilesWide = 0;
	public static int screenTilesHigh = 0;
	public static int tileWidth = 32;
	public static int tileHeight = 32;
	
	public static StartScreen start;

	
	public static void main(String[] args) {
	//	/*
		JFrame frame = new JFrame();
		Canvas gui = new Canvas();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui);
		frame.setSize(710, 730);
		frame.setTitle("Java-Game V0.1 - Map Editor");
		frame.setResizable(true);
		frame.setVisible(true); // start AWT painting.
		
		Thread gThread = new Thread(new StartScreen(gui));
		
		gThread.start();
		
		Boolean isMenu = true;
		Boolean isRunning = true;
		do{
			if(!gThread.isAlive()){
				if(isMenu){
					gThread = new Thread(new GameLoop(gui));
				}else{
					gThread = new Thread(new StartScreen(gui));
				}
				isMenu = !isMenu;
				gThread.start();
			}
		}while(isRunning);
	}
	

	private static class GameLoop implements Runnable, MouseListener,
	MouseMotionListener, ComponentListener{
		
		private boolean isRunning;
		private Canvas gui;
		private long cycleTime;
		
		private Marker marker;

		private long keyLastProcessed;
		
		// environment options
		private boolean exclusiveLayer = false; // show only current editing layer
		private boolean shiftKey = false; // shift key pressed?
		private boolean mouseDragging = false; // is mouse being dragged?
		private int mouseButtonDown = 0; // hack for dragging

		
		public GameLoop(Canvas gui){
			this.gui = gui;
			isRunning = true;
			
			gui.addKeyListener(new TAdapter());
			gui.addMouseListener(this);
			gui.addMouseMotionListener(this);
			gui.addComponentListener(this);
			gui.setFocusable(true);
			gui.requestFocusInWindow();
			
			//initial calculation of screen -> tile size
			screenTilesWide = gui.getWidth() / tileWidth;
			screenTilesHigh = gui.getHeight() / tileHeight;
			
			//load resources in to memory
			tileSkins = new BufferedImage[5];
			try{
				tileSkins[0] = optimizedImage("/Images/dirt.png");
				tileSkins[1] = optimizedImage("/Images/grass.png");
				tileSkins[2] = optimizedImage("/Images/stone.png");
				tileSkins[3] = optimizedImage("/Images/tree.png");
				tileSkins[4] = optimizedImage("/Images/water.png");
			}catch(IOException e){
				System.out.println("Error loading tileSkins");
			}
			
			enemySkins = new BufferedImage[3];
			try{
				enemySkins[0] = optimizedImage("/Images/enemy.png");
				enemySkins[1] = optimizedImage("/Images/eye.png");
				enemySkins[2] = optimizedImage("/Images/snake.png");
			}catch(IOException e){
				System.out.println("Error loading enemySkins");
			}
			
			entitySkins = new BufferedImage[1];
			try{
				entitySkins[0] = optimizedImage("/Images/entity.png");
			}catch(IOException e){
				System.out.println("Error loading entitySkins");
			}

			
			//initialize world
			world = new World("Default World", MAP_TILES_WIDE + 2, MAP_TILES_HIGH + 2, 2);

			try {
				loadWorld(false);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			//initialize tile marker
			marker = new Marker();
			
			
			//initialize players
			entity = new Entity();
			

			//initialize enemies
			for (int i = 0; i < 100; i++) { // create 100 enemies at random positions
											// on map
				int im = (int)(Math.random() * enemySkins.length);		//randomize enemySkin, just for fun
				enemy.add(new Enemy(i, im, 32 + (int) (Math.random()
						* (world.floorMap.getWidth() * 32) - 32), 32 + (int) (Math
						.random() * (world.floorMap.getHeight() * 32) - 32)));
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
			
			entity.move();
			marker.move(shiftKey);
			
			//move enemies
			for (int i = 0; i < enemy.size(); i++) {
				enemy.get(i).move();
			}
			
			//update marker position
			if (marker.getFirstTileX() > marker.getLastTileX()
					&& (!mouseDragging && !shiftKey)) {
				int x = marker.getFirstTileX();
				marker.setFirstTileX(marker.getLastTileX());
				marker.setLastTileX(x);
			}
			if (marker.getFirstTileY() > marker.getLastTileY()
					&& (!mouseDragging && !shiftKey)) {
				int y = marker.getFirstTileY();
				marker.setFirstTileY(marker.getLastTileY());
				marker.setLastTileY(y);
			}

			// move enemies and entity for scrolling effect
			if (xOffset != prevXOffset || yOffset != prevYOffset) {
				entity.setPos(entity.getX() - ((xOffset - prevXOffset) * 32),
						entity.getY() - ((yOffset - prevYOffset) * 32));

				for (int i = 0; i < enemy.size(); i++) {
					enemy.get(i).setPos(
							enemy.get(i).getX() - ((xOffset - prevXOffset) * 32),
							enemy.get(i).getY() - ((yOffset - prevYOffset) * 32));
				}
			}
			prevXOffset = xOffset;
			prevYOffset = yOffset;
		}
		
		private void updateGUI(BufferStrategy strategy){
			int tileToX, tileToY;
			Graphics g = strategy.getDrawGraphics();
			 
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
			g.setColor(Color.BLACK);
			
			// draw maps
			if(screenTilesWide + xOffset + 1 > world.floorMap.TileSet.length){
				tileToX = screenTilesWide + xOffset;
			}else{
				tileToX = screenTilesWide + xOffset + 1;
			}
			
			if(screenTilesHigh + yOffset + 1 > world.floorMap.TileSet[0].length){
				tileToY = screenTilesHigh + yOffset;
			}else{
				tileToY = screenTilesHigh + yOffset + 1;
			}
			
			if(!exclusiveLayer || marker.getLevel() == LEVEL_FLOOR){
				for (int x = xOffset; x < tileToX; x++) {
					for (int y = yOffset; y < tileToY; y++) {
						if (world.floorMap.TileSet[x][y].isVisible()) {
							world.floorMap.TileSet[x][y].setPos((x - xOffset) * JavaGame.tileWidth, (y - yOffset) * JavaGame.tileHeight);
							g.drawImage(JavaGame.tileSkins[world.floorMap.TileSet[x][y].getSkin()],
									world.floorMap.TileSet[x][y].getX(), world.floorMap.TileSet[x][y].getY(),
									null);
						}
					}
				}
			}
			
			if(!exclusiveLayer || marker.getLevel() == LEVEL_WALL){
				for (int x = xOffset; x < tileToX; x++) {
					for (int y = yOffset; y < tileToY; y++) {
						if (world.wallMap.TileSet[x][y].isVisible()) {
							world.wallMap.TileSet[x][y].setPos((x - xOffset) * JavaGame.tileWidth, (y - yOffset) * JavaGame.tileHeight);
							g.drawImage(JavaGame.tileSkins[world.wallMap.TileSet[x][y].getSkin()],
									world.wallMap.TileSet[x][y].getX(), world.wallMap.TileSet[x][y].getY(),
									null);
						}
					}
				}
			}

			// draw entity
			g.drawImage(entitySkins[entity.getSkin()], entity.getX(), entity.getY(), null);

			// draw enemies
			for (int i = 0; i < enemy.size(); i++) {
				g.drawImage(enemySkins[enemy.get(i).getSkin()], enemy.get(i).getX(), enemy.get(i).getY(), null);
			}

			// draw tile marker
			g.setColor(marker.getColor());
			g.drawRect(marker.getScreenX(), marker
					.getScreenY(), 32 * (Math.abs(marker.getFirstTileX()
					- marker.getLastTileX()) + 1), 32 * (Math.abs(marker
					.getFirstTileY() - marker.getLastTileY()) + 1));

			// write debug info
			g.setColor(Color.BLACK);
			g.drawString("X = " + entity.getX() + "Y = " + entity.getY(), 20, 20);
			g.drawString(
					"Tile X,Y:X,Y = " + marker.getFirstTileX() + ","
							+ marker.getFirstTileY() + ":" + marker.getLastTileX()
							+ "," + marker.getLastTileY(), 20, 40);
			
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
		
		private void nextTile() {
			int currentSkin;
			int nextSkin;

			if (System.currentTimeMillis() - keyLastProcessed > KEY_DELAY) {
				Map m = readCurrentMap();

				currentSkin = m.TileSet[marker.getFirstTileX()][marker
						.getFirstTileY()].getSkin();
				nextSkin = currentSkin + 1;
				if (nextSkin == tileSkins.length)
					nextSkin = 0;

				for (int x = marker.getFirstTileX(); x <= marker.getLastTileX(); x++) {
					for (int y = marker.getFirstTileY(); y <= marker.getLastTileY(); y++) {
						if (!m.TileSet[x][y].isVisible())
							m.TileSet[x][y].setVisible(true);
						m.TileSet[x][y].setSkin(nextSkin);
					}
				}

				writeCurrentMap(m);

				keyLastProcessed = System.currentTimeMillis();
			}
		}
		
		private void previousTile() {
			int currentSkin;
			int nextSkin;

			if (System.currentTimeMillis() - keyLastProcessed > KEY_DELAY) {
				Map m = readCurrentMap();

				currentSkin = m.TileSet[marker.getFirstTileX()][marker
						.getFirstTileY()].getSkin();
				nextSkin = currentSkin - 1;
				if (nextSkin < 0)
					nextSkin = tileSkins.length - 1;

				for (int x = marker.getFirstTileX(); x <= marker.getLastTileX(); x++) {
					for (int y = marker.getFirstTileY(); y <= marker.getLastTileY(); y++) {
						if (!m.TileSet[x][y].isVisible())
							m.TileSet[x][y].setVisible(true);
						m.TileSet[x][y].setSkin(nextSkin);
					}
				}

				writeCurrentMap(m);

				keyLastProcessed = System.currentTimeMillis();
			}
		}
		
		public void setTile(int skin) {
			Map m = readCurrentMap();

			for (int x = marker.getFirstTileX(); x <= marker.getLastTileX(); x++) {
				for (int y = marker.getFirstTileY(); y <= marker.getLastTileY(); y++) {
					if (!m.TileSet[x][y].isVisible())
						m.TileSet[x][y].setVisible(true);
					m.TileSet[x][y].setSkin(skin);
				}
			}

			writeCurrentMap(m);
		}
		
		private void hideTile() {
			if (System.currentTimeMillis() - keyLastProcessed > KEY_DELAY) {
				Map m = readCurrentMap();

				for (int x = marker.getFirstTileX(); x <= marker.getLastTileX(); x++) {
					for (int y = marker.getFirstTileY(); y <= marker.getLastTileY(); y++) {
						m.TileSet[x][y].toggleVisibility();
					}
				}
				writeCurrentMap(m);
				keyLastProcessed = System.currentTimeMillis();
			}
		}

		private void toggleExclusiveLayer() {
			if (System.currentTimeMillis() - keyLastProcessed > KEY_DELAY) {
				exclusiveLayer = !exclusiveLayer;
				keyLastProcessed = System.currentTimeMillis();
			}
		}

		private Map readCurrentMap() {
			Map m = null;

			switch (marker.getLevel()) {
			case LEVEL_FLOOR:
				m = world.floorMap;
				break;
			case LEVEL_WALL:
				m = world.wallMap;
				break;
			}

			return m;
		}

		private void writeCurrentMap(Map m) {
			switch (marker.getLevel()) {
			case LEVEL_FLOOR:
				world.floorMap = m;
				break;
			case LEVEL_WALL:
				world.wallMap = m;
				break;
			}
		}
		
		// file handling
		public void saveWorld() throws IOException {
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
		public void mouseClicked(MouseEvent e) {
			switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				for (int x = xOffset; x < xOffset + screenTilesWide; x++) {
					for (int y = yOffset; y < yOffset + screenTilesHigh; y++) {
						if (world.floorMap.TileSet[x][y].getBounds().contains(e.getPoint())
								&& x > 0 && y > 0 && x < MAP_TILES_WIDE && y < MAP_TILES_HIGH) {
							marker.selectRange(new Point(x, y), new Point(x, y));
							return;
						}
					}
				}
				break;
			}
		}

		private void showTileMenu(MouseEvent e) {
			TilePopupMenu menu = new TilePopupMenu();
			menu.show(e.getComponent(), e.getX(), e.getY());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseButtonDown = e.getButton();
			switch (mouseButtonDown) {
			case MouseEvent.BUTTON1:
				for (int x = xOffset; x < xOffset + screenTilesWide; x++) {
					for (int y = yOffset; y < yOffset + screenTilesHigh; y++) {
						if (world.floorMap.TileSet[x][y].getBounds().contains(e.getPoint())
								&& x > 0 && y > 0 && x < MAP_TILES_WIDE && y < MAP_TILES_HIGH) {
							marker.selectRange(new Point(x, y), new Point(x, y));
							return;
						}
					}
				}
				break;
			case MouseEvent.BUTTON3:
				if(e.isPopupTrigger())
					showTileMenu(e);
				break;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			switch (mouseButtonDown) {
			case MouseEvent.BUTTON1:
				for (int x = xOffset; x < xOffset + screenTilesWide; x++) {
					for (int y = yOffset; y < yOffset + screenTilesHigh; y++) {
						if (world.floorMap.TileSet[x][y].getBounds().contains(e.getPoint())
								&& x > 0 && y > 0 && x < MAP_TILES_WIDE && y < MAP_TILES_HIGH) {
							marker.setSelectionEnd(new Point(x, y));
							mouseDragging = false;
							return;
						}
					}
				}
				break;
			case MouseEvent.BUTTON3:
				if(e.isPopupTrigger())
					showTileMenu(e);
				break;
			}
			mouseButtonDown = 0;
		}

		@Override
		public void mouseDragged(MouseEvent m) {
			switch (mouseButtonDown) {
			case MouseEvent.BUTTON1:
				for (int x = xOffset; x < xOffset + screenTilesWide; x++) {
					for (int y = yOffset; y < yOffset + screenTilesHigh; y++) {
						if (world.floorMap.TileSet[x][y].getBounds().contains(m.getPoint())
								&& x > 0 && y > 0 && x < MAP_TILES_WIDE && y < MAP_TILES_HIGH) {
							marker.setSelectionEnd(new Point(x, y));
							mouseDragging = true;
							return;
						}
					}
				}
				break;
			}
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void componentHidden(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentResized(ComponentEvent arg0) {
			screenTilesWide = gui.getWidth() / tileWidth;
			screenTilesHigh = gui.getHeight() / tileHeight;
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		private class TAdapter extends KeyAdapter {

			public void keyReleased(KeyEvent e) {

				entity.keyReleased(e);
				marker.keyReleased(e);

			}

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				shiftKey = e.isShiftDown();

				entity.keyPressed(e);
				marker.keyPressed(e);

				switch (key) {
				case KeyEvent.VK_Z: // next tile
					nextTile();
					break;
				case KeyEvent.VK_X: // previous tile
					previousTile();
					break;
				case KeyEvent.VK_DELETE: // toggle tile visibility
					hideTile();
					break;
				case KeyEvent.VK_PAGE_UP: // edit wall map
					marker.changeLevel(LEVEL_WALL);
					break;
				case KeyEvent.VK_PAGE_DOWN: // edit floor map
					marker.changeLevel(LEVEL_FLOOR);
					break;
				case KeyEvent.VK_F8: // toggle exclusive layer mode
					toggleExclusiveLayer();
					break;
				case KeyEvent.VK_F11: // load world
					try {
						loadWorld(true);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case KeyEvent.VK_F12: // save world
					try {
						saveWorld();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case KeyEvent.VK_ESCAPE:	//kill thread and exit to menu
					isRunning = false;
					break;
				}
			}
		}
		
		
		@SuppressWarnings("serial")
		private class TilePopupMenu extends JPopupMenu implements ActionListener{
			JMenuItem menuItem = null;
			GridLayout grid = null;

			public TilePopupMenu(){
				grid = new GridLayout(0,5);
				grid.setHgap(0);
				grid.setVgap(0);
				setLayout(grid);
				
				for(int i = 0; i < tileSkins.length; i++){
					menuItem = new JMenuItem(new ImageIcon(tileSkins[i]));
					menuItem.addActionListener(this);
					menuItem.setActionCommand(Integer.toString(i));
					add(menuItem);
				}
			}
			
			public void actionPerformed(ActionEvent e) {
				setTile(Integer.parseInt(e.getActionCommand()));
			}
		}
	}

}
