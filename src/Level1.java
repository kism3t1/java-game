import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

	
@SuppressWarnings("serial")
public class Level1 extends JPanel implements ActionListener{
                
		//Map floorMap;
		//Map wallMap;
		World world;
		Image[] tileSkins;
		
		private Timer timer;
	    private  Entity entity;
	    private Marker marker;
	    private Enemy enemy;
	    //private int width;
	    //private int height;
	    
	    
	    private long keyLastProcessed;
	    public static final int KEY_DELAY = 75;			//set delay in milliseconds between key strokes
	    
	    //tile offset for scrolling
	    public static int xOffset = 0;
	    public static int yOffset = 0;
	    
	    //level numbers for handling floor and wall editing (should make it easier to keep track)
	    private static final int LEVEL_FLOOR = 0;
	    private static final int LEVEL_WALL = 1;
	    
	    //screen size info to aide scrolling
	    public static final int SCREEN_TILES_WIDE = 22;
	    public static final int SCREEN_TILES_HIGH = 22;
	    
	    //global map info
	    public static final int MAP_TILES_HIGH = 100;
	    public static final int MAP_TILES_WIDE = 100;
	    
	    public Level1 (){
	        addKeyListener(new TAdapter());
	        setFocusable(true);
	        setDoubleBuffered(true);

	        entity = new Entity();
	        marker = new Marker();
	        enemy = new Enemy();
	        world = new World("Default World", MAP_TILES_WIDE, MAP_TILES_HIGH);
            //floorMap = new Map(SCREEN_TILES_WIDE, SCREEN_TILES_HIGH, MAP_TILES_HIGH, MAP_TILES_WIDE, true);
            //wallMap = new Map(SCREEN_TILES_WIDE, SCREEN_TILES_HIGH, MAP_TILES_HIGH, MAP_TILES_WIDE, false);
            tileSkins = new Image[4];
            tileSkins[0] = new ImageIcon(this.getClass().getResource("Images/dirt.png")).getImage();
            tileSkins[1] = new ImageIcon(this.getClass().getResource("Images/grass.png")).getImage();
            tileSkins[2] = new ImageIcon(this.getClass().getResource("Images/stone.png")).getImage();
            tileSkins[3] = new ImageIcon(this.getClass().getResource("Images/tree.png")).getImage();
           // width = tileSkins[4].getWidth(null); //for collision detection // dont think needed
           // height = tileSkins[4].getHeight(null); //for collision detection
            		
            		
	        timer = new Timer(10, this);
	        timer.start();
	    }
		

		 public void paint(Graphics g) {
		        super.paint(g);
		        
		        //initialize g2d and refresh screen
		        Graphics2D g2d = (Graphics2D)g;
		        g2d.setBackground(Color.BLACK);
		        g2d.setColor(Color.BLACK);
		        g2d.clearRect(0, 0, getSize().width, getSize().height);
		        		               
		        //draw maps
		        world.floorMap.draw(g2d, tileSkins, xOffset, yOffset, this);
				world.wallMap.draw(g2d, tileSkins, xOffset, yOffset, this);
		        
				//draw entity
				g2d.drawImage(entity.getImage(), entity.getX() - (xOffset * 32), entity.getY() - (yOffset * 32), this);
				g2d.drawImage(enemy.getImage(), enemy.getX() - (xOffset * 32), enemy.getY() - (yOffset * 32), this);
				
		        //draw tile marker
		        g.setColor(marker.getColor());
		        g2d.draw(new Rectangle2D.Double(marker.getScreenX(), marker.getScreenY(), 32, 32));
		        
		        //write debug info
		        g.setColor(Color.BLACK);
		        g.drawString("X = "+entity.getX()+"Y = "+entity.getY(),20,20);
		        g.drawString("Tile X,Y: " + marker.getTileX() + "," + marker.getTileY(), 20, 40);
		    }


		    public void actionPerformed(ActionEvent e) {
		    	entity.move();
		    	marker.move();
		        repaint();  
		    	//entity.checkCollisions();
		    }
		    
		    private void nextTile(){
		    	int currentSkin;
		    	int nextSkin;
		    	if(System.currentTimeMillis()-keyLastProcessed>KEY_DELAY){
		    		switch(marker.getLevel()){
		    		case LEVEL_FLOOR: 
		    			currentSkin = world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].getSkin();
		    			nextSkin = currentSkin + 1;
		    			if (nextSkin == tileSkins.length)
		    				nextSkin = 0;
		    			if (!world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].isVisible())
		    				world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].setVisible(true);
		    			world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].setSkin(nextSkin);
		    			break;
		    		case LEVEL_WALL:
		    			currentSkin = world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].getSkin();
		    			nextSkin = currentSkin + 1;
		    			if (nextSkin == tileSkins.length)
		    				nextSkin = 0;
		    			if (!world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].isVisible())
		    				world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].setVisible(true);
		    			world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].setSkin(nextSkin);
		    			break;
		    		}
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }
		    
		    private void previousTile(){
		    	int currentSkin;
		    	int nextSkin;
		    	if(System.currentTimeMillis()-keyLastProcessed>KEY_DELAY){
		    		switch(marker.getLevel()){
		    		case LEVEL_FLOOR: 
		    			currentSkin = world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].getSkin();
		    			nextSkin = currentSkin - 1;
		    			if (nextSkin < 0)
		    				nextSkin = tileSkins.length-1;
		    			if (!world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].isVisible())
		    				world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].setVisible(true);
		    			world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].setSkin(nextSkin);
		    			break;
		    		case LEVEL_WALL:
		    			currentSkin = world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].getSkin();
		    			nextSkin = currentSkin - 1;
		    			if (nextSkin < 0 )
		    				nextSkin = tileSkins.length-1;
		    			if (!world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].isVisible())
		    				world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].setVisible(true);
		    			world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].setSkin(nextSkin);
		    			break;
		    		}
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }
		    
		    private void hideTile(){
		    	if(System.currentTimeMillis()-keyLastProcessed>KEY_DELAY){
		    		switch(marker.getLevel()){
		    		case LEVEL_FLOOR: 
		    			world.floorMap.TileSet[marker.getTileX()][marker.getTileY()].toggleVisibility();
		    			break;
		    		case LEVEL_WALL:
		    			world.wallMap.TileSet[marker.getTileX()][marker.getTileY()].toggleVisibility();
		    			break;
		    		}
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }
		    
		    public void checkCollisions(){	//Collision Detection
		    	Rectangle r1 = enemy.getBounds();	//Get bounds if enemy
		    	Rectangle r2 = getBounds();			//Get bounds of supposedly tiles?....
		    	Rectangle r3 = entity.getBounds();	//Get bounds of entity
		    	
		    	if (r2.intersects(r3) || r3.intersects(r1)){	//Checks if entity collides with either a tile or an enemy
		    		System.out.println("COLLISION!");			//Temporary prints out COLLISION
		    	}
		    	}


		    private class TAdapter extends KeyAdapter {

		        public void keyReleased(KeyEvent e) {
		        
		        	entity.keyReleased(e);
		        	marker.keyReleased(e);
		        	//entity.checkCollisions();
		        }

		        public void keyPressed(KeyEvent e) {
		        	int key = e.getKeyCode();
		        	
		        	entity.keyPressed(e);
		        	marker.keyPressed(e);
		            checkCollisions();

		        	
		        	switch(key){
		        	case KeyEvent.VK_Z:						//next tile
		        		nextTile();
		        		break;
		        	case KeyEvent.VK_X:						//previous tile
		        		previousTile();
		        		break;
		        	case KeyEvent.VK_DELETE:				//toggle tile visibility		
		        		hideTile();
		        		break;
		        	case KeyEvent.VK_PAGE_UP:				//edit wall map
		        		marker.changeLevel(LEVEL_WALL);
		        		break;
		        	case KeyEvent.VK_PAGE_DOWN:				//edit floor map
		        		marker.changeLevel(LEVEL_FLOOR);
		        		break;
		        	case KeyEvent.VK_F11:					//load world
		        		try {
							loadWorld();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		        		break;
		        	case KeyEvent.VK_F12:					//save world
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
		    
		    //file handling
		    public void saveWorld() throws IOException{
		    	int n = JOptionPane.showConfirmDialog(null, "Save current world?", "Save Dialog", JOptionPane.YES_NO_OPTION);
		    	if(n == JOptionPane.YES_OPTION){
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
		    
		    public void loadWorld() throws IOException, ClassNotFoundException{
		    	int n = JOptionPane.showConfirmDialog(null, "Load previously saved world?", "Load Dialog", JOptionPane.YES_NO_OPTION);
		    	if(n == JOptionPane.YES_OPTION){
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
		    public Rectangle getBounds(){	//Get bounds for collision detection
		    	return new Rectangle (getX(),getY(),32,32);
		    }

	}
