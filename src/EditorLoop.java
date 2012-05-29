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
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;


public class EditorLoop extends Halja implements Runnable, MouseListener,
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
		screenTilesWide = gui.getWidth() / Halja.tileWidth;
		screenTilesHigh = gui.getHeight() / Halja.tileHeight;
		
		//initialize world
		world = new World("Default World", MAP_TILES_WIDE, MAP_TILES_HIGH, 2);

		try {
			LoadResources.loadWorld(false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//initialize tile marker
		marker = new Marker();
	}

	@Override
	public void run() {
		cycleTime = System.currentTimeMillis();
		gui.createBufferStrategy(2);				//2 = double buffer
		BufferStrategy strategy = gui.getBufferStrategy();
		
		gameTime.setTime(TOD_DAYTIME);	//default editor to daytime

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

		marker.move(shiftKey);

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

		// move enemies, entity, and friendlies for scrolling effect
		if (xOffset != prevXOffset || yOffset != prevYOffset) {
			world.ollie.setPos(world.ollie.getX() - ((xOffset - prevXOffset) * tileWidth),
					world.ollie.getY() - ((yOffset - prevYOffset) * tileHeight));

			for (int i = 0; i < world.enemy.size(); i++) {
				world.enemy.get(i).setPos(
						world.enemy.get(i).getX() - ((xOffset - prevXOffset) * tileWidth),
						world.enemy.get(i).getY() - ((yOffset - prevYOffset) * tileHeight));
			}
			
			for (int f = 0; f < world.friendly.size(); f++) {
				world.friendly.get(f).setPos(
						world.friendly.get(f).getX() - ((xOffset - prevXOffset) * tileWidth),
						world.friendly.get(f).getY() - ((yOffset - prevYOffset) * tileHeight));
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
					if (world.floorMap.TileSet[x][y] != null) {
						world.floorMap.TileSet[x][y].setPos((x - xOffset) * tileWidth, (y - yOffset) * tileHeight);
						g.drawImage(tileSkins[gameTime.checkDateTime()][world.floorMap.TileSet[x][y].getSkin()],
								world.floorMap.TileSet[x][y].getX(), world.floorMap.TileSet[x][y].getY(),
								null);
					}
				}
			}
		}

		if(!exclusiveLayer || marker.getLevel() == LEVEL_WALL){
			for (int x = xOffset; x < tileToX; x++) {
				for (int y = yOffset; y < tileToY; y++) {
					if (world.wallMap.TileSet[x][y] != null) {
						world.wallMap.TileSet[x][y].setPos((x - xOffset) * tileWidth, (y - yOffset) * tileHeight);
						g.drawImage(tileSkins[gameTime.checkDateTime()][world.wallMap.TileSet[x][y].getSkin()],
								world.wallMap.TileSet[x][y].getX(), world.wallMap.TileSet[x][y].getY(),
								null);
					}
				}
			}
		}

		// draw entity
		world.ollie.draw(g);

		// draw enemies
		world.drawEnemies(g);
		
		//Draw friendlies
		world.drawFriendly(g);

		// draw tile marker
		g.setColor(marker.getColor());
		g.drawRect(marker.getScreenX(), marker
				.getScreenY(), tileWidth * (Math.abs(marker.getFirstTileX()
						- marker.getLastTileX()) + 1), tileHeight * (Math.abs(marker
								.getFirstTileY() - marker.getLastTileY()) + 1));

		// write debug info
		g.setColor(Color.BLACK);
		g.drawString("X = " + world.ollie.getX() + "Y = " + world.ollie.getY(), 20, 20);
		g.drawString(
				"Tile X,Y:X,Y = " + marker.getFirstTileX() + ","
						+ marker.getFirstTileY() + ":" + marker.getLastTileX()
						+ "," + marker.getLastTileY(), 20, 40);

		g.dispose();
		strategy.show();
	}

	private void syncFPS(){
		cycleTime = cycleTime + Halja.FRAME_DELAY;
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
					if (m.TileSet[x][y] == null)
						m.TileSet[x][y] = new Tile(x * tileWidth, y * tileHeight);
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
					if (m.TileSet[x][y] == null)
						m.TileSet[x][y] = new Tile(x * tileWidth, y * tileHeight);
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
				if (m.TileSet[x][y] == null)
					m.TileSet[x][y] = new Tile(x * tileWidth, y * tileHeight);
				m.TileSet[x][y].setSkin(skin);
			}
		}

		writeCurrentMap(m);
	}

	private void deleteTile() {
		if(marker.getLevel() == LEVEL_WALL){
			if (System.currentTimeMillis() - keyLastProcessed > KEY_DELAY) {
				Map m = readCurrentMap();

				for (int x = marker.getFirstTileX(); x <= marker.getLastTileX(); x++) {
					for (int y = marker.getFirstTileY(); y <= marker.getLastTileY(); y++) {
						if(m.TileSet[x][y] != null)
							m.TileSet[x][y] = null;
					}
				}
				writeCurrentMap(m);
				keyLastProcessed = System.currentTimeMillis();
			}
		}
	}
	
	private void toggleTileDestructible() {
		if(marker.getLevel() == LEVEL_WALL){
			if (System.currentTimeMillis() - keyLastProcessed > KEY_DELAY) {
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
							&& x > 0 && y > 0 && x < world.getWidth() && y < world.getHeight()) {
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
	
	private void showEnemyMenu(MouseEvent e, int id){
		EnemyPopupMenu menu = new EnemyPopupMenu(id);
		menu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private void showFriendlyMenu(MouseEvent e, int id) {
		FriendlyPopupMenu menu = new FriendlyPopupMenu(id);
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
							&& x > 0 && y > 0 && x < world.getWidth() && y < world.getHeight()) {
						marker.selectRange(new Point(x, y), new Point(x, y));
						return;
					}
				}
			}
			break;
		case MouseEvent.BUTTON3:
			if(e.isPopupTrigger()){
				int id = collisionDetection.getEnemyID(e.getPoint());
				if(id > -1)
				{
					showEnemyMenu(e, id);
				}else{
					showTileMenu(e);
				}
			}
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
							&& x > 0 && y > 0 && x < world.getWidth() && y < world.getHeight()) {
						marker.setSelectionEnd(new Point(x, y));
						mouseDragging = false;
						return;
					}
				}
			}
			break;
		case MouseEvent.BUTTON3:
			if(e.isPopupTrigger()){
				int enemyID = collisionDetection.getEnemyID(e.getPoint());
				int friendlyID = collisionDetection.getFriendlyID(e.getPoint());
				if(enemyID != CollisionDetection.CD_NULL){
					showEnemyMenu(e, enemyID);
				}else if(friendlyID != CollisionDetection.CD_NULL){
					showFriendlyMenu(e, friendlyID);
				}else{
					showTileMenu(e);
				}
			}
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
							&& x > 0 && y > 0 && x < world.getWidth() && y < world.getHeight()) {
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

			//world.ollie.keyReleased(e);
			marker.keyReleased(e);

		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			shiftKey = e.isShiftDown();

			//world.ollie.keyPressed(e);
			marker.keyPressed(e);

			switch (key) {
			case KeyEvent.VK_Z: // next tile
				nextTile();
				break;
			case KeyEvent.VK_X: // previous tile
				previousTile();
				break;
			case KeyEvent.VK_DELETE: // delete tiles
				if(marker.getLevel() == LEVEL_WALL)
					deleteTile();
				break;
			case KeyEvent.VK_PAGE_UP: // edit wall map
				marker.changeLevel(Halja.LEVEL_WALL);
				break;
			case KeyEvent.VK_PAGE_DOWN: // edit floor map
				marker.changeLevel(Halja.LEVEL_FLOOR);
				break;
			case KeyEvent.VK_F8: // toggle exclusive layer mode
				toggleExclusiveLayer();
				break;
			case KeyEvent.VK_F11: // load world
				try {
					LoadResources.loadWorld(true);
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
					LoadResources.saveWorld();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case KeyEvent.VK_ESCAPE:	//kill thread and exit to menu
				nextThread = "MENU";
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
				
				menu.addSeparator();
				
				menuItem = new JMenuItem("Play Level");
				menuItem.setActionCommand("TEST");
				menuItem.addActionListener(this);
				menu.add(menuItem);
			add(menu);
			
			menu = new JMenu("Tile");
				switch(marker.getLevel()){
				case LEVEL_FLOOR:
					break;
				case LEVEL_WALL:
					if(readCurrentMap().TileSet[marker.getFirstTileX()][marker.getFirstTileY()]!= null){
						menuItem = new JCheckBoxMenuItem("Delete");
						menuItem.setActionCommand("DELETE");
						menuItem.addActionListener(this);
						menu.add(menuItem);
					
						cbMenuItem = new JCheckBoxMenuItem("Destructible");
						cbMenuItem.setSelected(readCurrentMap().TileSet
								[marker.getFirstTileX()][marker.getFirstTileY()]
										.isDestructible());
						cbMenuItem.setActionCommand("DESTRUCT");
						cbMenuItem.addActionListener(this);
						menu.add(cbMenuItem);
					}
					break;
				}
				
				subMenu = new JMenu("Skin");
				subMenu.setLayout(new GridLayout(0,5));
				for(int i = 0; i < tileSkins[0].length; i++){
					menuItem = new JMenuItem(new ImageIcon(tileSkins[gameTime.checkDateTime()][i]));
					menuItem.addActionListener(this);
					menuItem.setActionCommand("TILE:" + Integer.toString(i));
					subMenu.add(menuItem);
				}
				menu.add(subMenu);
			add(menu);
			
			menu = new JMenu("Add Enemy");
				subMenu = new JMenu("Type");
				subMenu.setLayout(new GridLayout(0,5));
				for(int i = 0; i < enemySkins[0].length; i++){
					menuItem = new JMenuItem(new ImageIcon(enemySkins[gameTime.checkDateTime()][i][0].getImage()));
					menuItem.addActionListener(this);
					menuItem.setActionCommand("ENEMY:" + Integer.toString(i));
					subMenu.add(menuItem);
				}
				menu.add(subMenu);
			add(menu);
			
			menu = new JMenu("Add Friendly");
			subMenu = new JMenu("Type");
			subMenu.setLayout(new GridLayout(0,5));
			for(int i = 0; i < friendlySkins[0].length; i++){
				menuItem = new JMenuItem(new ImageIcon(friendlySkins[gameTime.checkDateTime()][i][0].getImage()));
				menuItem.addActionListener(this);
				menuItem.setActionCommand("FRIEND:" + Integer.toString(i));
				subMenu.add(menuItem);
			}
			menu.add(subMenu);
		add(menu);
				
			menu = new JMenu("Options");
				menuItem = new JMenuItem("Set Entity Start Point");
				menuItem.setActionCommand("START");
				menuItem.addActionListener(this);
				menu.add(menuItem);
			
				cbMenuItem = new JCheckBoxMenuItem("Hide Inactive Layers");
				cbMenuItem.setSelected(exclusiveLayer);
				cbMenuItem.setActionCommand("EXCL");
				cbMenuItem.addActionListener(this);
				menu.add(cbMenuItem);
				
				menu.addSeparator();
				
				menuItem = new JMenuItem("Edit Layer:");
				menuItem.setEnabled(false);
				menu.add(menuItem);
				
				ButtonGroup levelGroup = new ButtonGroup();
				rbMenuItem = new JRadioButtonMenuItem("Floor");
				levelGroup.add(rbMenuItem);
				if(marker.getLevel() == LEVEL_FLOOR)
					rbMenuItem.setSelected(true);
				rbMenuItem.setActionCommand("LEVEL:" + LEVEL_FLOOR);
				rbMenuItem.addActionListener(this);
				menu.add(rbMenuItem);
				
				rbMenuItem = new JRadioButtonMenuItem("Wall");
				levelGroup.add(rbMenuItem);
				if(marker.getLevel() == LEVEL_WALL)
					rbMenuItem.setSelected(true);
				rbMenuItem.setActionCommand("LEVEL:" + LEVEL_WALL);
				rbMenuItem.addActionListener(this);
				menu.add(rbMenuItem);
				
				menu.addSeparator();
				
				menuItem = new JMenuItem("Time Of Day Preview:");
				menuItem.setEnabled(false);
				menu.add(menuItem);
				
				ButtonGroup todGroup = new ButtonGroup();
				rbMenuItem = new JRadioButtonMenuItem("Daytime");
				todGroup.add(rbMenuItem);
				if(gameTime.checkDateTime() == TOD_DAYTIME)
					rbMenuItem.setSelected(true);
				rbMenuItem.setActionCommand("TOD:" + TOD_DAYTIME);
				rbMenuItem.addActionListener(this);
				menu.add(rbMenuItem);
				
				rbMenuItem = new JRadioButtonMenuItem("Sunrise");
				todGroup.add(rbMenuItem);
				if(gameTime.checkDateTime() == TOD_SUNRISE)
					rbMenuItem.setSelected(true);
				rbMenuItem.setActionCommand("TOD:" + TOD_SUNRISE);
				rbMenuItem.addActionListener(this);
				menu.add(rbMenuItem);
				
				rbMenuItem = new JRadioButtonMenuItem("Sunset");
				todGroup.add(rbMenuItem);
				if(gameTime.checkDateTime() == TOD_SUNSET)
					rbMenuItem.setSelected(true);
				rbMenuItem.setActionCommand("TOD:" + TOD_SUNSET);
				rbMenuItem.addActionListener(this);
				menu.add(rbMenuItem);
				
				rbMenuItem = new JRadioButtonMenuItem("Night");
				todGroup.add(rbMenuItem);
				if(gameTime.checkDateTime() == TOD_NIGHT)
					rbMenuItem.setSelected(true);
				rbMenuItem.setActionCommand("TOD:" + TOD_NIGHT);
				rbMenuItem.addActionListener(this);
				menu.add(rbMenuItem);
				
				menu.addSeparator();
				
				subMenu = new JMenu("Change Border Skin");
				subMenu.setLayout(new GridLayout(0,5));
					for(int i = 0; i < tileSkins.length; i++){
						menuItem = new JMenuItem(new ImageIcon(tileSkins[gameTime.checkDateTime()][i]));
						menuItem.addActionListener(this);
						menuItem.setActionCommand("BORDER:" + Integer.toString(i));
						subMenu.add(menuItem);
					}
				menu.add(subMenu);
			add(menu);					
			
		}

		public void actionPerformed(ActionEvent e) {
			String[] command = e.getActionCommand().split("[:]");
			switch(command[0]){
			case "SAVE":
				try {
					LoadResources.saveWorld();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "LOAD":
				try {
					LoadResources.loadWorld(true);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "TEST":
				try {
					LoadResources.saveWorld();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				nextThread = "GAME";
				isRunning = false;
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
			case "TOD":
				gameTime.setTime(Integer.parseInt(command[1]));;
				break;
			case "DESTRUCT":
				toggleTileDestructible();
				break;
			case "DELETE":
				deleteTile();
				break;
			case "ENEMY":
				for (int x = marker.getFirstTileX(); x <= marker.getLastTileX(); x++) {
					for (int y = marker.getFirstTileY(); y <= marker.getLastTileY(); y++) {
				world.addEnemy(Integer.parseInt(command[1]), 
						(world.floorMap.TileSet[x][y].getX()), 
						(world.floorMap.TileSet[x][y].getY()));
					}
				}
				break;
			case "FRIEND":
				for (int x = marker.getFirstTileX(); x <= marker.getLastTileX(); x++) {
					for (int y = marker.getFirstTileY(); y <= marker.getLastTileY(); y++) {
				world.addFriendly(Integer.parseInt(command[1]), 
						(world.floorMap.TileSet[x][y].getX()), 
						(world.floorMap.TileSet[x][y].getY()));
					}
				}
				break;
			case "BORDER":
				world.setBorderSkin(Integer.parseInt(command[1]));
				break;
			case "START":
				world.ollie.setPos(marker.getFirstTileX() * tileWidth, marker.getFirstTileY() * tileHeight);
				world.setStart(world.ollie.getPos());
				break;
			}
		}
	}
	
	@SuppressWarnings("serial")
	private class EnemyPopupMenu extends JPopupMenu implements ActionListener{
		JMenuItem menuItem = null;	
		
		int enemyID;

		public EnemyPopupMenu(int enemyID){
			this.enemyID = enemyID;
			
			menuItem = new JMenuItem("Set Health (" + world.getEnemy(enemyID).getHealth() + ")");
			menuItem.setActionCommand("HEALTH");
			menuItem.addActionListener(this);
			add(menuItem);
			
			menuItem = new JMenuItem("Remove");
			menuItem.setActionCommand("REM");
			menuItem.addActionListener(this);
			add(menuItem);
		}

		public void actionPerformed(ActionEvent e) {
			String[] command = e.getActionCommand().split("[:]");
			switch(command[0]){
			case "REM":
				world.removeEnemy(enemyID);
				break;
			case "HEALTH":
				int i = Integer.parseInt(JOptionPane.showInputDialog(null, 
						"Enter new enemy health value (whole number only)", 
						"Enemy Health", 
						JOptionPane.PLAIN_MESSAGE, 
						new ImageIcon(enemySkins[gameTime.checkDateTime()][world.getEnemy(enemyID).getSkin()][0].getImage()),
						null,
						world.getEnemy(enemyID).getHealth()).toString());
				world.getEnemy(enemyID).setHealth(i);
				break;
			}
		}
	}
	
	@SuppressWarnings("serial")
	private class FriendlyPopupMenu extends JPopupMenu implements ActionListener{
		JMenuItem menuItem = null;	
		
		int friendlyID;

		public FriendlyPopupMenu(int friendlyID){
			this.friendlyID = friendlyID;
			
			menuItem = new JMenuItem("Set Health (" + world.getFriendly(friendlyID).getHealth() + ")");
			menuItem.setActionCommand("HEALTH");
			menuItem.addActionListener(this);
			add(menuItem);
			
			menuItem = new JMenuItem("Remove");
			menuItem.setActionCommand("REM");
			menuItem.addActionListener(this);
			add(menuItem);
		}

		public void actionPerformed(ActionEvent e) {
			String[] command = e.getActionCommand().split("[:]");
			switch(command[0]){
			case "REM":
				world.removeFriendly(friendlyID);
				break;
			case "HEALTH":
				int i = Integer.parseInt(JOptionPane.showInputDialog(null, 
						"Enter new friendly health value (whole number only)", 
						"Friendly Health", 
						JOptionPane.PLAIN_MESSAGE, 
						new ImageIcon(friendlySkins[gameTime.checkDateTime()][world.getFriendly(friendlyID).getSkin()][0].getImage()),
						null,
						world.getFriendly(friendlyID).getHealth()).toString());
				world.getFriendly(friendlyID).setHealth(i);
				break;
			}
		}
	}
}
