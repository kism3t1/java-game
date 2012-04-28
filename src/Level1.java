import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Level1 extends JPanel implements ActionListener, MouseListener,
		MouseMotionListener, ComponentListener {

	public static World world;
	public static BufferedImage[] tileSkins;
	public static BufferedImage[] enemySkins;
	public static BufferedImage[] entitySkins;

	private Timer timer;
	private Entity entity;
	static public ArrayList<Enemy> enemy = new ArrayList<Enemy>();
	private Marker marker;

	private long keyLastProcessed;
	public static final int KEY_DELAY = 75; // set delay in milliseconds between
											// key strokes

	// tile offset for scrolling
	public static int xOffset = 0;
	public static int yOffset = 0;
	public static int prevXOffset = 0;
	public static int prevYOffset = 0;

	// environment options
	private boolean exclusiveLayer = false; // show only current editing layer
	private boolean shiftKey = false; // shift key pressed?
	private boolean mouseDragging = false; // is mouse being dragged?
	private int mouseButtonDown = 0; // hack for dragging

	// level numbers for handling floor and wall editing (should make it easier
	// to keep track)
	public static final int LEVEL_FLOOR = 0;
	public static final int LEVEL_WALL = 1;

	// screen size info to aide scrolling
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static int screenTilesWide = 0;
	public static int screenTilesHigh = 0;
	public static int tileWidth = 32;
	public static int tileHeight = 32;

	// global map info
	public static final int MAP_TILES_HIGH = 100;
	public static final int MAP_TILES_WIDE = 100;

	public Level1() {
		addKeyListener(new TAdapter());
		addMouseListener(this);
		addMouseMotionListener(this);
		addComponentListener(this);
		setFocusable(true);
		setDoubleBuffered(true);
		
		
		//load resources in to memory
		tileSkins = new BufferedImage[4];
		try{
			tileSkins[0] = optimizedImage("Images/dirt.png");
			tileSkins[1] = optimizedImage("Images/grass.png");
			tileSkins[2] = optimizedImage("Images/stone.png");
			tileSkins[3] = optimizedImage("Images/tree.png");
		}catch(IOException e){
			System.out.println("Error loading tileSkins");
		}
		
		enemySkins = new BufferedImage[2];
		try{
			enemySkins[0] = optimizedImage("Images/enemy.png");
			enemySkins[1] = optimizedImage("Images/eye.png");
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}
		
		entitySkins = new BufferedImage[1];
		try{
			entitySkins[0] = optimizedImage("Images/entity.png");
		}catch(IOException e){
			System.out.println("Error loading enemySkins");
		}
		//entitySkins[0] = new ImageIcon(this.getClass().getResource(
		//		"Images/entity.png")).getImage();

		
		//initialize world
		world = new World("Default World", MAP_TILES_WIDE, MAP_TILES_HIGH);

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

		
		timer = new Timer(10, this);
		timer.start();
	}

	public void paint(Graphics g) {
		super.paint(g);

		// initialize g2d and refresh screen
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.BLACK);
		g2d.setColor(Color.BLACK);
		g2d.clearRect(0, 0, getSize().width, getSize().height);

		// draw maps
		if (exclusiveLayer) {
			readCurrentMap().draw(g2d, xOffset, yOffset);
		} else {
			world.floorMap.draw(g2d, xOffset, yOffset);
			world.wallMap.draw(g2d, xOffset, yOffset);
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

		// draw entity
		g2d.drawImage(entitySkins[entity.getSkin()], entity.getX(), entity.getY(), this);

		// draw enemies
		for (int i = 0; i < enemy.size(); i++) {
			g2d.drawImage(enemySkins[enemy.get(i).getSkin()], enemy.get(i).getX(), enemy
					.get(i).getY(), this);
		}

		// draw tile marker
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

		g.setColor(marker.getColor());
		g2d.draw(new Rectangle2D.Double(marker.getScreenX(), marker
				.getScreenY(), 32 * (Math.abs(marker.getFirstTileX()
				- marker.getLastTileX()) + 1), 32 * (Math.abs(marker
				.getFirstTileY() - marker.getLastTileY()) + 1)));

		// write debug info
		g.setColor(Color.BLACK);
		g.drawString("X = " + entity.getX() + "Y = " + entity.getY(), 20, 20);
		g.drawString(
				"Tile X,Y:X,Y = " + marker.getFirstTileX() + ","
						+ marker.getFirstTileY() + ":" + marker.getLastTileX()
						+ "," + marker.getLastTileY(), 20, 40);
	}

	public void actionPerformed(ActionEvent e) {
		entity.move();
		marker.move(shiftKey);
		for (int i = 0; i < enemy.size(); i++) {
			enemy.get(i).move();
		}
		repaint();
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
			}
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
			repaint();
		}
	}

	// mouse control
	@Override
	public void mouseClicked(MouseEvent m) {
		switch (m.getButton()) {
		case MouseEvent.BUTTON1:
			for (int x = xOffset; x < xOffset + screenTilesWide; x++) {
				for (int y = yOffset; y < yOffset + screenTilesHigh; y++) {
					if (world.floorMap.TileSet[x][y].getBounds().contains(
							m.getPoint())) {
						marker.selectRange(new Point(x, y), new Point(x, y));
						return;
					}
				}
			}
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent m) {
		mouseButtonDown = m.getButton();
		switch (mouseButtonDown) {
		case MouseEvent.BUTTON1:
			for (int x = xOffset; x < xOffset + screenTilesWide; x++) {
				for (int y = yOffset; y < yOffset + screenTilesHigh; y++) {
					if (world.floorMap.TileSet[x][y].getBounds().contains(
							m.getPoint())) {
						marker.selectRange(new Point(x, y), new Point(x, y));
						return;
					}
				}
			}
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		switch (mouseButtonDown) {
		case MouseEvent.BUTTON1:
			for (int x = xOffset; x < xOffset + screenTilesWide; x++) {
				for (int y = yOffset; y < yOffset + screenTilesHigh; y++) {
					if (world.floorMap.TileSet[x][y].getBounds().contains(
							m.getPoint())) {
						marker.setSelectionEnd(new Point(x, y));
						mouseDragging = false;
						return;
					}
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
					if (world.floorMap.TileSet[x][y].getBounds().contains(
							m.getPoint())) {
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
		// TODO Auto-generated method stub
		screenTilesWide = this.getWidth() / tileWidth;
		screenTilesHigh = this.getHeight() / tileHeight;
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	//optimize graphics
	public BufferedImage optimizedImage(String resourceName) throws IOException
	{
		BufferedImage image = ImageIO.read(new File(this.getClass().getResource(resourceName).getPath()));
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

}
