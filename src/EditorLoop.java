import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;


public class EditorLoop implements Runnable, MouseListener,
MouseMotionListener{

	// default map info
	public static final int MAP_TILES_HIGH = 100;
	public static final int MAP_TILES_WIDE = 100;

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

	public EditorLoop(Canvas gui){
		this.gui = gui;
		isRunning = true;

		gui.addKeyListener(new TAdapter());
		gui.addMouseListener(this);
		gui.addMouseMotionListener(this);
		gui.setFocusable(true);
		gui.requestFocusInWindow();

		//initial calculation of screen -> tile size
		JavaGame.screenTilesWide = gui.getWidth() / JavaGame.tileWidth;
		JavaGame.screenTilesHigh = gui.getHeight() / JavaGame.tileHeight;

		//load resources in to memory
		try{
			JavaGame.tileSkins = new BufferedImage[]{
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
			JavaGame.enemySkins = new BufferedImage[]{		
					optimizedImage("/Images/enemy.png"),
					optimizedImage("/Images/eye.png"),
					optimizedImage("/Images/snake.png"),
			};
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}

		
		try{
			JavaGame.entitySkins = new BufferedImage[]{
					optimizedImage("/Images/entity.png")
			};
		}catch(IOException e){
			System.out.println("Error loading entitySkins");
		}


		//initialize world
		JavaGame.world = new World("Default World", MAP_TILES_WIDE + 2, MAP_TILES_HIGH + 2, 2);

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
		JavaGame.entity = new Entity();


		//initialize enemies
		if(JavaGame.enemy.size() > 0)
			JavaGame.enemy.clear();
		for (int i = 0; i < 100; i++) { // create 100 enemies at random positions
			// on map
			int im = (int)(Math.random() * JavaGame.enemySkins.length);		//randomize enemySkin, just for fun
			JavaGame.enemy.add(new Enemy(i, im, 32 + (int) (Math.random()
					* (JavaGame.world.floorMap.getWidth() * 32) - 32), 32 + (int) (Math
							.random() * (JavaGame.world.floorMap.getHeight() * 32) - 32)));
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
		
		//DayCycle.runSunset();	//POC DAY/NIGHT Cycle
		//DayCycle.runCycleup();

		JavaGame.entity.move();
		marker.move(shiftKey);

		//move enemies
		for (int i = 0; i < JavaGame.enemy.size(); i++) {
			JavaGame.enemy.get(i).move();
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
		if (JavaGame.xOffset != JavaGame.prevXOffset || JavaGame.yOffset != JavaGame.prevYOffset) {
			JavaGame.entity.setPos(JavaGame.entity.getX() - ((JavaGame.xOffset - JavaGame.prevXOffset) * 32),
					JavaGame.entity.getY() - ((JavaGame.yOffset - JavaGame.prevYOffset) * 32));

			for (int i = 0; i < JavaGame.enemy.size(); i++) {
				JavaGame.enemy.get(i).setPos(
						JavaGame.enemy.get(i).getX() - ((JavaGame.xOffset - JavaGame.prevXOffset) * 32),
						JavaGame.enemy.get(i).getY() - ((JavaGame.yOffset - JavaGame.prevYOffset) * 32));
			}
		}
		JavaGame.prevXOffset = JavaGame.xOffset;
		JavaGame.prevYOffset = JavaGame.yOffset;
	}

	private void updateGUI(BufferStrategy strategy){
		int tileToX, tileToY;
		Graphics g = strategy.getDrawGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		g.setColor(Color.BLACK);

		// draw maps
		if(JavaGame.screenTilesWide + JavaGame.xOffset + 1 > JavaGame.world.floorMap.TileSet.length){
			tileToX = JavaGame.screenTilesWide + JavaGame.xOffset;
		}else{
			tileToX = JavaGame.screenTilesWide + JavaGame.xOffset + 1;
		}

		if(JavaGame.screenTilesHigh + JavaGame.yOffset + 1 > JavaGame.world.floorMap.TileSet[0].length){
			tileToY = JavaGame.screenTilesHigh + JavaGame.yOffset;
		}else{
			tileToY = JavaGame.screenTilesHigh + JavaGame.yOffset + 1;
		}

		if(!exclusiveLayer || marker.getLevel() == JavaGame.LEVEL_FLOOR){
			for (int x = JavaGame.xOffset; x < tileToX; x++) {
				for (int y = JavaGame.yOffset; y < tileToY; y++) {
					if (JavaGame.world.floorMap.TileSet[x][y].isVisible()) {
						JavaGame.world.floorMap.TileSet[x][y].setPos((x - JavaGame.xOffset) * JavaGame.tileWidth, (y - JavaGame.yOffset) * JavaGame.tileHeight);
						g.drawImage(JavaGame.tileSkins[JavaGame.world.floorMap.TileSet[x][y].getSkin()],
								JavaGame.world.floorMap.TileSet[x][y].getX(), JavaGame.world.floorMap.TileSet[x][y].getY(),
								null);
					}
				}
			}
		}

		if(!exclusiveLayer || marker.getLevel() == JavaGame.LEVEL_WALL){
			for (int x = JavaGame.xOffset; x < tileToX; x++) {
				for (int y = JavaGame.yOffset; y < tileToY; y++) {
					if (JavaGame.world.wallMap.TileSet[x][y].isVisible()) {
						JavaGame.world.wallMap.TileSet[x][y].setPos((x - JavaGame.xOffset) * JavaGame.tileWidth, (y - JavaGame.yOffset) * JavaGame.tileHeight);
						g.drawImage(JavaGame.tileSkins[JavaGame.world.wallMap.TileSet[x][y].getSkin()],
								JavaGame.world.wallMap.TileSet[x][y].getX(), JavaGame.world.wallMap.TileSet[x][y].getY(),
								null);
					}
				}
			}
		}

		// draw entity
		g.drawImage(JavaGame.entitySkins[JavaGame.entity.getSkin()], JavaGame.entity.getX(), JavaGame.entity.getY(), null);

		// draw enemies
		for (int i = 0; i < JavaGame.enemy.size(); i++) {
			g.drawImage(JavaGame.enemySkins[JavaGame.enemy.get(i).getSkin()], JavaGame.enemy.get(i).getX(), JavaGame.enemy.get(i).getY(), null);
		}

		// draw tile marker
		g.setColor(marker.getColor());
		g.drawRect(marker.getScreenX(), marker
				.getScreenY(), 32 * (Math.abs(marker.getFirstTileX()
						- marker.getLastTileX()) + 1), 32 * (Math.abs(marker
								.getFirstTileY() - marker.getLastTileY()) + 1));

		// write debug info
		g.setColor(Color.BLACK);
		g.drawString("X = " + JavaGame.entity.getX() + "Y = " + JavaGame.entity.getY(), 20, 20);
		g.drawString(
				"Tile X,Y:X,Y = " + marker.getFirstTileX() + ","
						+ marker.getFirstTileY() + ":" + marker.getLastTileX()
						+ "," + marker.getLastTileY(), 20, 40);

		g.dispose();
		strategy.show();
	}

	private void syncFPS(){
		cycleTime = cycleTime + JavaGame.FRAME_DELAY;
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

		if (System.currentTimeMillis() - keyLastProcessed > JavaGame.KEY_DELAY) {
			Map m = readCurrentMap();

			currentSkin = m.TileSet[marker.getFirstTileX()][marker
			                                                .getFirstTileY()].getSkin();
			nextSkin = currentSkin + 1;
			if (nextSkin == JavaGame.tileSkins.length)
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

		if (System.currentTimeMillis() - keyLastProcessed > JavaGame.KEY_DELAY) {
			Map m = readCurrentMap();

			currentSkin = m.TileSet[marker.getFirstTileX()][marker
			                                                .getFirstTileY()].getSkin();
			nextSkin = currentSkin - 1;
			if (nextSkin < 0)
				nextSkin = JavaGame.tileSkins.length - 1;

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

	private void toggleTileVisible() {
		if(marker.getLevel() == JavaGame.LEVEL_WALL){
			if (System.currentTimeMillis() - keyLastProcessed > JavaGame.KEY_DELAY) {
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
	}
	
	private void toggleTileDestructible() {
		if(marker.getLevel() == JavaGame.LEVEL_WALL){
			if (System.currentTimeMillis() - keyLastProcessed > JavaGame.KEY_DELAY) {
				Map m = readCurrentMap();

				for (int x = marker.getFirstTileX(); x <= marker.getLastTileX(); x++) {
					for (int y = marker.getFirstTileY(); y <= marker.getLastTileY(); y++) {
						m.TileSet[x][y].toggleDestructible();
					}
				}
				writeCurrentMap(m);
				keyLastProcessed = System.currentTimeMillis();
			}
		}
	}

	private void toggleExclusiveLayer() {
		if (System.currentTimeMillis() - keyLastProcessed > JavaGame.KEY_DELAY) {
			exclusiveLayer = !exclusiveLayer;
			keyLastProcessed = System.currentTimeMillis();
		}
	}

	private Map readCurrentMap() {
		Map m = null;

		switch (marker.getLevel()) {
		case JavaGame.LEVEL_FLOOR:
			m = JavaGame.world.floorMap;
			break;
		case JavaGame.LEVEL_WALL:
			m = JavaGame.world.wallMap;
			break;
		}

		return m;
	}

	private void writeCurrentMap(Map m) {
		switch (marker.getLevel()) {
		case JavaGame.LEVEL_FLOOR:
			JavaGame.world.floorMap = m;
			break;
		case JavaGame.LEVEL_WALL:
			JavaGame.world.wallMap = m;
			break;
		}
	}

	// file handling
	public static void saveWorld() throws IOException {
		int n = JOptionPane.showConfirmDialog(null, "Save current world?",
				"Save Dialog", JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			FileOutputStream saveFile = new FileOutputStream("world.wld");
			GZIPOutputStream gzipFile = new GZIPOutputStream(saveFile);
			ObjectOutputStream saveObject = new ObjectOutputStream(gzipFile);
			saveObject.writeObject(JavaGame.world);
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
			JavaGame.world = (World) loadObject.readObject();
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
			for (int x = JavaGame.xOffset; x < JavaGame.xOffset + JavaGame.screenTilesWide; x++) {
				for (int y = JavaGame.yOffset; y < JavaGame.yOffset + JavaGame.screenTilesHigh; y++) {
					if (JavaGame.world.floorMap.TileSet[x][y].getBounds().contains(e.getPoint())
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
			for (int x = JavaGame.xOffset; x < JavaGame.xOffset + JavaGame.screenTilesWide; x++) {
				for (int y = JavaGame.yOffset; y < JavaGame.yOffset + JavaGame.screenTilesHigh; y++) {
					if (JavaGame.world.floorMap.TileSet[x][y].getBounds().contains(e.getPoint())
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
			for (int x = JavaGame.xOffset; x < JavaGame.xOffset + JavaGame.screenTilesWide; x++) {
				for (int y = JavaGame.yOffset; y < JavaGame.yOffset + JavaGame.screenTilesHigh; y++) {
					if (JavaGame.world.floorMap.TileSet[x][y].getBounds().contains(e.getPoint())
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
			for (int x = JavaGame.xOffset; x < JavaGame.xOffset + JavaGame.screenTilesWide; x++) {
				for (int y = JavaGame.yOffset; y < JavaGame.yOffset + JavaGame.screenTilesHigh; y++) {
					if (JavaGame.world.floorMap.TileSet[x][y].getBounds().contains(m.getPoint())
							&& x > 0 && y > 0 && x < JavaGame.world.getWidth() && y < JavaGame.world.getHeight()) {
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

	private class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {

			JavaGame.entity.keyReleased(e);
			marker.keyReleased(e);

		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			shiftKey = e.isShiftDown();

			JavaGame.entity.keyPressed(e);
			marker.keyPressed(e);

			switch (key) {
			case KeyEvent.VK_Z: // next tile
				nextTile();
				break;
			case KeyEvent.VK_X: // previous tile
				previousTile();
				break;
			case KeyEvent.VK_DELETE: // toggle tile visibility
				toggleTileVisible();
				break;
			case KeyEvent.VK_PAGE_UP: // edit wall map
				marker.changeLevel(JavaGame.LEVEL_WALL);
				break;
			case KeyEvent.VK_PAGE_DOWN: // edit floor map
				marker.changeLevel(JavaGame.LEVEL_FLOOR);
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
				JavaGame.nextThread = "MENU";
				isRunning = false;
				break;
			}
		}
	}


	@SuppressWarnings("serial")
	private class TilePopupMenu extends JPopupMenu implements ActionListener{
		JMenu menu = null;
		JMenu subMenu = null;
		JMenuItem menuItem = null;	
		JCheckBoxMenuItem cbMenuItem = null;
		JRadioButtonMenuItem rbMenuItem = null;

		public TilePopupMenu(){
			menu = new JMenu("File");
				menuItem = new JMenuItem("Save");
				menuItem.setActionCommand("SAVE");
				menuItem.addActionListener(this);
				menu.add(menuItem);
				
				menuItem = new JMenuItem("Load");
				menuItem.setActionCommand("LOAD");
				menuItem.addActionListener(this);
				menu.add(menuItem);
			add(menu);
			
			menu = new JMenu("Tile");
				switch(marker.getLevel()){
				case JavaGame.LEVEL_FLOOR:
					break;
				case JavaGame.LEVEL_WALL:
					cbMenuItem = new JCheckBoxMenuItem("Visible");
					cbMenuItem.setSelected(readCurrentMap().TileSet
							[marker.getFirstTileX()][marker.getFirstTileY()]
									.isVisible());
					cbMenuItem.setActionCommand("VISIBLE");
					cbMenuItem.addActionListener(this);
					menu.add(cbMenuItem);
					
					cbMenuItem = new JCheckBoxMenuItem("Destructible");
					cbMenuItem.setSelected(readCurrentMap().TileSet
							[marker.getFirstTileX()][marker.getFirstTileY()]
									.isDestructible());
					cbMenuItem.setActionCommand("DESTRUCT");
					cbMenuItem.addActionListener(this);
					menu.add(cbMenuItem);
					break;
				}
				
				subMenu = new JMenu("Skin");
				subMenu.setLayout(new GridLayout(0,5));
				for(int i = 0; i < JavaGame.tileSkins.length; i++){
					menuItem = new JMenuItem(new ImageIcon(JavaGame.tileSkins[i]));
					menuItem.addActionListener(this);
					menuItem.setActionCommand("TILE:" + Integer.toString(i));
					subMenu.add(menuItem);
				}
				menu.add(subMenu);
			add(menu);
			
			menu = new JMenu("Add Enemy");
			add(menu);
				
			menu = new JMenu("Options");
				cbMenuItem = new JCheckBoxMenuItem("Hide Inactive Layers");
				cbMenuItem.setSelected(exclusiveLayer);
				cbMenuItem.setActionCommand("EXCL");
				cbMenuItem.addActionListener(this);
				menu.add(cbMenuItem);
				
				menu.addSeparator();
				
				menuItem = new JMenuItem("Edit Layer:");
				menuItem.setEnabled(false);
				menu.add(menuItem);
				
				ButtonGroup group = new ButtonGroup();
				rbMenuItem = new JRadioButtonMenuItem("Floor");
				group.add(rbMenuItem);
				if(marker.getLevel() == JavaGame.LEVEL_FLOOR)
					rbMenuItem.setSelected(true);
				rbMenuItem.setActionCommand("LEVEL:" + JavaGame.LEVEL_FLOOR);
				rbMenuItem.addActionListener(this);
				menu.add(rbMenuItem);
				
				rbMenuItem = new JRadioButtonMenuItem("Wall");
				group.add(rbMenuItem);
				if(marker.getLevel() == JavaGame.LEVEL_WALL)
					rbMenuItem.setSelected(true);
				rbMenuItem.setActionCommand("LEVEL:" + JavaGame.LEVEL_WALL);
				rbMenuItem.addActionListener(this);
				menu.add(rbMenuItem);
			add(menu);
		}

		public void actionPerformed(ActionEvent e) {
			String[] command = e.getActionCommand().split("[:]");
			switch(command[0]){
			case "SAVE":
				try {
					EditorLoop.saveWorld();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "LOAD":
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
			case "TILE":
				setTile(Integer.parseInt(command[1]));
				break;
			case "EXCL":
				toggleExclusiveLayer();
				break;
			case "LEVEL":
				marker.changeLevel(Integer.parseInt(command[1]));
				break;
			case "DESTRUCT":
				toggleTileDestructible();
				break;
			case "VISIBLE":
				toggleTileVisible();
				break;
			}
		}
	}
}
