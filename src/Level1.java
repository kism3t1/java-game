import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
		int [][]level1 = {	{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
							{1,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,3,0,0,0,0,0,3,0,0,3,0,0,0,0,0,0,0,0,0,1},
							{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,3,3,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,0,0,0,0,3,3,3,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,1,1,1,1,1,3,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,1,2,2,2,1,3,3,3,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,1,2,2,2,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0},
							{1,3,1,2,2,2,1,3,3,3,0,0,3,0,0,0,0,0,0,0,0,0},
							{1,0,1,1,1,1,1,0,0,0,0,3,0,3,0,0,0,0,0,0,0,1},
							{1,0,0,3,3,3,0,0,0,0,0,0,3,3,0,0,0,0,0,0,0,1},
							{1,0,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,0,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,1},
							{1,0,0,0,0,0,0,0,0,0,0,0,3,3,0,0,0,3,3,3,0,1},
							{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,0,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
							{1,3,3,3,3,0,0,0,0,0,0,0,3,3,0,0,0,0,0,0,0,1},
							{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
};
		
		private Timer timer;
	    private  Entity entity;
	    
	    public Level1 (){
	        addKeyListener(new TAdapter());
	        setFocusable(true);
	        setDoubleBuffered(true);

	        entity = new Entity();
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
			dirt = new ImageIcon(this.getClass().getResource("Images/dirt.png")).getImage();
			grass = new ImageIcon(this.getClass().getResource("Images/grass.png")).getImage();
			stone = new ImageIcon(this.getClass().getResource("Images/stone.png")).getImage();
			tree = new ImageIcon(this.getClass().getResource("Images/tree.png")).getImage();
			{
			/*	
                            for (int across = 0; across< level1.length; across++)
				{
					for (int vert = 0; vert< level1.length; vert++)
						
				if (level1 [across][vert]== 1){
					
					g2d.drawImage(stone, vert*32, across*32, this);
				}
				else if (level1 [across][vert] == 2){
			
					g2d.drawImage(dirt, vert*32,across*32, this);	
				}
				else if(level1 [across][vert] == 0){
					
					g2d.drawImage(grass, vert*32, across*32, this);	

				}
				else if(level1 [across][vert] == 3){
					
					g2d.drawImage(tree, vert*32, across*32, this);	

				}	
				}*/
                            
                            /*for(int x=0; x<22; x++){
                                for(int y=0; y<22; y++){
                                    g2d.drawImage(tileSkins[floorMap.TileSet[x][y].getSkin()], x*32, y*32, null);
                                }
                            }*/
                            
                            floorMap.draw(g2d, tileSkins, 0, 0, this);
				}
		}

		 public void paint(Graphics g) {
		        super.paint(g);

		        Graphics2D g2d = (Graphics2D)g;
		        g2d.drawImage(entity.getImage(), entity.getX(), entity.getY(), this);
		        
		        g.setColor(Color.BLACK);
		        //g.setFont(arg0)
		        g.drawString("X = "+entity.getX()+"Y = "+entity.getY(),20,20);
		        //Toolkit.getDefaultToolkit().sync();
		       //g.dispose();
		    }


		    public void actionPerformed(ActionEvent e) {
		    	entity.move();
		        repaint();  
		    }


		    private class TAdapter extends KeyAdapter {

		        public void keyReleased(KeyEvent e) {
		        	entity.keyReleased(e);
		        }

		        public void keyPressed(KeyEvent e) {
		        	entity.keyPressed(e);
		        }
		    }

	}
