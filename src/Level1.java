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
	
		Image dirt;
		Image grass;
		Image stone;
		Image tree;
                
		Map floorMap;
		Image[] tileSkins;
                
		int across;
		int vert;
		int x = 20;
		int xy = 320;
		int y = 20;
		int w = 1;
		
		private Timer timer;
	    private  Entity entity;
	    private Marker marker;
	    
	    private long keyLastProcessed;
	    
	    public Level1 (){
	        addKeyListener(new TAdapter());
	        setFocusable(true);
	        setDoubleBuffered(true);

	        entity = new Entity();
	        marker = new Marker();
            floorMap = new Map(22, 22, true);
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
				floorMap.draw(g2d, tileSkins, 0, 0, this);
			}
		}

		 public void paint(Graphics g) {
		        super.paint(g);

		        Graphics2D g2d = (Graphics2D)g;
		        g2d.drawImage(entity.getImage(), entity.getX(), entity.getY(), this);
		        
		        //draw tile marker
		        g.setColor(marker.getColor());
		        g2d.draw(new Rectangle2D.Double(marker.getX(), marker.getY(), marker.getWidth(), marker.getHeight()));
		        
		        g.setColor(Color.BLACK);
		        //g.setFont(arg0)
		        g.drawString("X = "+entity.getX()+"Y = "+entity.getY(),20,20);
		        g.drawString("Tile X,Y: " + (marker.getX()/marker.getWidth()) + "," + (marker.getY()/marker.getHeight()), 20, 40);
		        //Toolkit.getDefaultToolkit().sync();
		       //g.dispose();
		    }


		    public void actionPerformed(ActionEvent e) {
		    	entity.move();
		    	marker.move();
		        repaint();  
		    }
		    
		    private void nextTile(){
		    	if(System.currentTimeMillis()-keyLastProcessed>75){
		    		switch(marker.getLevel()){
		    		case 0: 
		    			int currentSkin = floorMap.TileSet[marker.getX()/32][marker.getY()/32].getSkin();
		    			int nextSkin = currentSkin + 1;
		    			if (nextSkin == tileSkins.length)
		    				nextSkin = 0;
		    			floorMap.TileSet[marker.getX()/32][marker.getY()/32].setSkin(nextSkin);
		    			break;
		    		case 1:
		    			//TODO: add tile altering routine for wall map
		    			break;
		    		}
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }
		    
		    private void previousTile(){
		    	if(System.currentTimeMillis()-keyLastProcessed>75){
		    		switch(marker.getLevel()){
		    		case 0: 
		    			int currentSkin = floorMap.TileSet[marker.getX()/32][marker.getY()/32].getSkin();
		    			int nextSkin = currentSkin - 1;
		    			if (nextSkin < 0)
		    				nextSkin = tileSkins.length-1;
		    			floorMap.TileSet[marker.getX()/32][marker.getY()/32].setSkin(nextSkin);
		    			break;
		    		case 1:
		    			//TODO: add tile altering routine for wall map
		    			break;
		    		}
		    		keyLastProcessed=System.currentTimeMillis();
		    	}
		    }


		    private class TAdapter extends KeyAdapter {

		        public void keyReleased(KeyEvent e) {
		        	int key = e.getKeyCode();
		        	
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
		        	}
		        }
		    }
		   

	}
