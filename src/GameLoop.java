import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class GameLoop implements Runnable, MouseListener,
MouseMotionListener{
	private boolean isRunning;
	private Canvas gui;
	private long cycleTime;

	public GameLoop(Canvas gui){
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

		try {
			loadWorld(false);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


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

		JavaGame.entity.move();

		//move enemies
		for (int i = 0; i < JavaGame.enemy.size(); i++) {
			JavaGame.enemy.get(i).move();
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
		// draw entity
		g.drawImage(JavaGame.entitySkins[JavaGame.entity.getSkin()], JavaGame.entity.getX(), JavaGame.entity.getY(), null);
		
		
		// draw enemies
		for (int i = 0; i < JavaGame.enemy.size(); i++) {
			g.drawImage(JavaGame.enemySkins[JavaGame.enemy.get(i).getSkin()], JavaGame.enemy.get(i).getX(), JavaGame.enemy.get(i).getY(), null);
		}

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

			JavaGame.entity.keyReleased(e);

		}

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			JavaGame.entity.keyPressed(e);

			switch (key) {
			case KeyEvent.VK_ESCAPE:	//kill thread and exit to menu
				JavaGame.nextThread = "MENU";
				isRunning = false;
				break;
			case KeyEvent.VK_W:
				if(JavaGame.yOffset > 0)
					JavaGame.yOffset -= 1;
				break;
			case KeyEvent.VK_S:
				if(JavaGame.yOffset < JavaGame.world.getHeight() - JavaGame.screenTilesHigh)
					JavaGame.yOffset += 1;
				break;
			case KeyEvent.VK_A:
				if(JavaGame.xOffset > 0)
					JavaGame.xOffset -= 1;
				break;
			case KeyEvent.VK_D:
				if(JavaGame.xOffset < JavaGame.world.getWidth() - JavaGame.screenTilesWide)
					JavaGame.xOffset += 1;
				break;
			}
		}
	}
}
