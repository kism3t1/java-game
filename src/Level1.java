import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

	
@SuppressWarnings("serial")
public class Level1 extends JPanel implements ActionListener{
                
		Map floorMap;
		Map wallMap;
		Image[] tileSkins;
		
		private Timer timer;
	    private  Entity entity;
	    private Marker marker;
	    
	    private long keyLastProcessed;
	    private int keyDelayMillis = 75;			//set delay in milliseconds between key strokes
	    
	    //tile offset for scrolling
	    private int xOffset = 0;
	    private int yOffset = 0;
	    
	    //level numbers for handling floor and wall editing (should make it easier to keep track)
	    private static final int LEVEL_FLOOR = 0;
	    private static final int LEVEL_WALL = 1;
	    
	    //screen size info to aide scrolling
	    private static final int SCREEN_TILES_WIDE = 21;
	    private static final int SCREEN_TILES_HIGH = 21;
	    
	    public Level1 (){
	        addKeyListener(new TAdapter());
	        setFocusable(true);
	        setDoubleBuffered(true);

	        entity = new Entity();
	        marker = new Marker();
            floorMap = new Map(SCREEN_TILES_WIDE, SCREEN_TILES_HIGH, 100, 100, true);
            wallMap = new Map(SCREEN_TILES_WIDE, SCREEN_TILES_HIGH, 100, 100, false);
            tileSkins = new Image[4];
            tileSkins[0] = new ImageIcon(this.getClass().getResource("Images/dirt.png")).getImage();
            tileSkins[1] = new ImageIcon(this.getClass().getResource("Images/grass.png")).getImage();
            tileSkins[2] = new ImageIcon(this.getClass().getResource("Images/stone.png")).getImage();
            tileSkins[3] = new ImageIcon(this.getClass().getResource("Images/tree.png")).getImage();

	        timer = new Timer(10, this);
	        timer.start();
	    }
		
		public void paintComponent (Graphics g){
			
			Graphics2D g2d = (Graphics2D) g;
			{        
				floorMap.draw(g2d, tileSkins, xOffset, yOffset, this);
				wallMap.draw(g2d, tileSkins, xOffset, yOffset, this);
			}
		}

		 public void paint(Graphics g) {
		        super.paint(g);

		        Graphics2D g2d = (Graphics2D)g;
		        g2d.drawImage(entity.getImage(), entity.getX(), entity.getY(), this);
		        
		        //scroll tiles if necessary
		        
		        if(marker.getX() > SCREEN_TILES_WIDE){
		        	xOffset += 1;
		        }
		        
		        if (marker.getX() < 0){
		        	if(xOffset>0)
		        		xOffset -= 1;
		        }
		        /*
		        if((marker.getY()/marker.getHeight()) > (SCREEN_TILES_HIGH + yOffset)){
		        	yOffset += 1;
		        }
		        if((marker.getY()/marker.getHeight()) == (SCREEN_TILES_HIGH - yOffset)){
		        	yOffset -= 1;
		        }
		        */
		        
		        //floorMap.draw(g2d, tileSkins, xOffset, yOffset, this);
				//wallMap.draw(g2d, tileSkins, xOffset, yOffset, this);
		        
		        //draw tile marker
		        g.setColor(marker.getColor());
		        g2d.draw(new Rectangle2D.Double((marker.getX() - xOffset) * 32,
		        		(marker.getY() - yOffset) * 32,
		        		32, 32));
		        
		        //write debug info
		        g.setColor(Color.BLACK);
		        g.drawString("X = "+entity.getX()+"Y = "+entity.getY(),20,20);
		        g.drawString("Tile X,Y: " + marker.getX() + "," + marker.getY(), 20, 40);
		    }


		    public void actionPerformed(ActionEvent e) {
		    	entity.move();
		    	marker.move();
		        repaint();  
		    }
		    
		    private void nextTile(){
		    	int currentSkin;
		    	int nextSkin;
		    	if(System.currentTimeMillis()-keyLastProcessed>keyDelayMillis){
		    		switch(marker.getLevel()){
		    		case LEVEL_FLOOR: 
		    			currentSkin = floorMap.TileSet[marker.getX()/32][marker.getY()/32].getSkin();
		    			nextSkin = currentSkin + 1;
		    			if (nextSkin == tileSkins.length)
		    				nextSkin = 0;
		    			floorMap.TileSet[marker.getX()/32][marker.getY()/32].setSkin(nextSkin);
		    			break;
		    		case LEVEL_WALL:
		    			currentSkin = wallMap.TileSet[marker.getX()/32][marker.getY()/32].getSkin();
		    			nextSkin = currentSkin + 1;
		    			if (nextSkin == tileSkins.length)
		    				nextSkin = 0;
		    			if (!wallMap.TileSet[marker.getX()/32][marker.getY()/32].isVisible())
		    				wallMap.TileSet[marker.getX()/32][marker.getY()/32].setVisible(true);
		    			wallMap.TileSet[marker.getX()/32][marker.getY()/32].setSkin(nextSkin);
		    			break;
		    		}
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }
		    
		    private void previousTile(){
		    	int currentSkin;
		    	int nextSkin;
		    	if(System.currentTimeMillis()-keyLastProcessed>keyDelayMillis){
		    		switch(marker.getLevel()){
		    		case LEVEL_FLOOR: 
		    			currentSkin = floorMap.TileSet[marker.getX()/32][marker.getY()/32].getSkin();
		    			nextSkin = currentSkin - 1;
		    			if (nextSkin < 0)
		    				nextSkin = tileSkins.length-1;
		    			floorMap.TileSet[marker.getX()/32][marker.getY()/32].setSkin(nextSkin);
		    			break;
		    		case LEVEL_WALL:
		    			currentSkin = wallMap.TileSet[marker.getX()/32][marker.getY()/32].getSkin();
		    			nextSkin = currentSkin - 1;
		    			if (nextSkin < 0 )
		    				nextSkin = tileSkins.length-1;
		    			if (!wallMap.TileSet[marker.getX()/32][marker.getY()/32].isVisible())
		    				wallMap.TileSet[marker.getX()/32][marker.getY()/32].setVisible(true);
		    			wallMap.TileSet[marker.getX()/32][marker.getY()/32].setSkin(nextSkin);
		    			break;
		    		}
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }
		    
		    private void hideTile(){
		    	if(System.currentTimeMillis()-keyLastProcessed>keyDelayMillis){
		    		switch(marker.getLevel()){
		    		case LEVEL_FLOOR: 
		    			floorMap.TileSet[marker.getX()/32][marker.getY()/32].toggleVisibility();
		    			break;
		    		case LEVEL_WALL:
		    			wallMap.TileSet[marker.getX()/32][marker.getY()/32].toggleVisibility();
		    			break;
		    		}
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }


		    private class TAdapter extends KeyAdapter {

		        public void keyReleased(KeyEvent e) {
		        
		        	entity.keyReleased(e);
		        	marker.keyReleased(e);
		        }

		        public void keyPressed(KeyEvent e) {
		        	int key = e.getKeyCode();
		        	
		        	entity.keyPressed(e);
		        	marker.keyPressed(e);
		        	
		        	switch(key){
		        	case KeyEvent.VK_Z:
		        		nextTile();
		        		break;
		        	case KeyEvent.VK_X:
		        		previousTile();
		        		break;
		        	case KeyEvent.VK_DELETE:
		        		hideTile();
		        		break;
		        	case KeyEvent.VK_PAGE_UP:
		        		marker.changeLevel(1);
		        		break;
		        	case KeyEvent.VK_PAGE_DOWN:
		        		marker.changeLevel(0);
		        		break;
		        	}
		        }
		    }
		   

	}
