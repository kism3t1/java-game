import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

	
@SuppressWarnings("serial")
public class Level1 extends JPanel implements ActionListener, MouseListener{
                
		public static World world;
		private Image[] tileSkins;
		
		private Timer timer;
	    private Entity entity;
	    
	    static public ArrayList<Enemy> enemy = new ArrayList<Enemy>();
	    private Marker marker;  
	    
	    private long keyLastProcessed;
	    public static final int KEY_DELAY = 75;			//set delay in milliseconds between key strokes
	    
	    //tile offset for scrolling
	    public static int xOffset = 0;
	    public static int yOffset = 0;
	    
	    //environment options
	    private boolean exclusiveLayer = false;			//show only current editing layer
	    
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
	        addMouseListener(this);
	        setFocusable(true);
	        setDoubleBuffered(true);

	        world = new World("Default World", MAP_TILES_WIDE, MAP_TILES_HIGH);
	        entity = new Entity();
	        marker = new Marker();
	        
	        for (int i=0; i < 10; i++){			//create 10 enemies at random positions on map
	        	enemy.add(new Enemy(32 + (int)(Math.random() * (world.floorMap.getWidth() * 32) - 32), 
	        			32 + (int)(Math.random() * (world.floorMap.getHeight() * 32) - 32)));
	        }
	        
            tileSkins = new Image[4];
            tileSkins[0] = new ImageIcon(this.getClass().getResource("Images/dirt.png")).getImage();
            tileSkins[1] = new ImageIcon(this.getClass().getResource("Images/grass.png")).getImage();
            tileSkins[2] = new ImageIcon(this.getClass().getResource("Images/stone.png")).getImage();
            tileSkins[3] = new ImageIcon(this.getClass().getResource("Images/tree.png")).getImage();         		
            		
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
		        if(exclusiveLayer){
		        	switch(marker.getLevel()){
		        	case LEVEL_FLOOR:
		        		world.floorMap.draw(g2d, tileSkins, xOffset, yOffset, this);
		        		break;
		        	case LEVEL_WALL:
		        		world.wallMap.draw(g2d, tileSkins, xOffset, yOffset, this);
		        		break;
		        	}
		        }else{
		        	world.floorMap.draw(g2d, tileSkins, xOffset, yOffset, this);
		        	world.wallMap.draw(g2d, tileSkins, xOffset, yOffset, this);
		        }
		        
				//draw entity
				g2d.drawImage(entity.getImage(), entity.getX() - (xOffset * 32), entity.getY() - (yOffset * 32), this);
				
				//draw enemies
				for(int i = 0; i < enemy.size(); i++){
					g2d.drawImage(enemy.get(i).getImage(), enemy.get(i).getX() - (xOffset * 32), enemy.get(i).getY() - (yOffset * 32), this);
				}
				
				
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
		    
		    private void toggleExclusiveLayer(){
		    	if(System.currentTimeMillis()-keyLastProcessed>KEY_DELAY){
		    		exclusiveLayer = !exclusiveLayer;
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }
		    
		    public void checkCollisions(){	//Collision Detection
		    	Rectangle r1 = entity.getBounds();	//Get bounds of entity
		    	Rectangle r2;
		    	
		    	//check collision with enemies
		    	for(int i=0; i < enemy.size(); i++){
		    		r2 = enemy.get(i).getBounds();	//Get bounds if enemy

		    		if (r1.intersects(r2)){	//Checks if entity collides with an enemy
		    			System.out.println("ENEMY COLLISION!");			//Temporary prints out ENEMY COLLISION
		    		}
		    	}
		    	
		    	//check for tile collision
		    	for(int x = 0; x < world.getWidth(); x++){
		    		for (int y = 0; y < world.getHeight(); y++){
		    			if (world.wallMap.TileSet[x][y].isWall()
		    					&& world.wallMap.TileSet[x][y].isVisible()){			//no need to check for collision if it isn't a wall
		    				r2 = world.wallMap.TileSet[x][y].getBounds();

		    				if (r1.intersects(r2)){								//Checks if entity collides with a tile
		    					System.out.println("TILE COLLISION!");			//Temporary prints out ENEMY COLLISION
		    				}
		    			}
		    		}
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
		        	case KeyEvent.VK_F8:					//toggle exclusive layer mode
		        		toggleExclusiveLayer();
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


			@Override
			public void mouseClicked(MouseEvent m) {
				// TODO Auto-generated method stub
				for (int x = xOffset; x < xOffset + SCREEN_TILES_WIDE; x++){
					for (int y = yOffset; y < yOffset + SCREEN_TILES_HIGH; y++){
						if(world.floorMap.TileSet[x][y].getBounds().contains(m.getPoint())){
							marker.setTileX(x);
							marker.setTileY(y);
						}
					}
				}
			}
			


			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

	}
