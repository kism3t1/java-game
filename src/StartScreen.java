import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


//Need to add KeyListener to select Start Game or Map Editor
//Add stroke to image when Up || Down is selected


@SuppressWarnings("serial")
public class StartScreen extends JPanel implements Runnable{
	
	private int menuChoice;
	Image start;
	Image map;
	Image bg;
	
	private Canvas gui;
	private boolean isRunning;
	private long cycleTime;
	private KeyListener kl;
	
		public StartScreen (Canvas gui){
			this.gui = gui;
			isRunning = true;
			
			menuChoice =1;		//Default to Play Game
			
			kl = new TAdapter();
			gui.addKeyListener(kl);
			gui.setFocusable(true);
			gui.requestFocusInWindow();
			//do{}while(!gui.requestFocusInWindow());
			
	        start = new ImageIcon(this.getClass().getResource("/Images/start.png")).getImage();
			map = new ImageIcon(this.getClass().getResource("/Images/map.png")).getImage();
			bg = new ImageIcon(this.getClass().getResource("/Images/background.png")).getImage();
		}
		
		@Override
		public void run() {
			cycleTime = System.currentTimeMillis();
			gui.createBufferStrategy(2);				//2 = double buffer
			BufferStrategy strategy = gui.getBufferStrategy();
			
			while(isRunning){
				updateGUI(strategy);
				syncFPS();
			}
			
			gui.removeKeyListener(kl);
		}

		//Draw routine & check menuChoice
		
	    public void updateGUI(BufferStrategy strategy) {
	    	Graphics g = strategy.getDrawGraphics();

	        g.drawImage(bg, 0,0,710,730,null);
	        g.drawImage(start, 200, 200, null);
	        g.drawImage(map, 200, 350, null);
	        if (menuChoice == 1){
	        	selectGame(g);
	        }else{
	        	selectEditor(g);
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
	    
	    public void selectGame(Graphics g){
	    	Graphics2D g2d = (Graphics2D) g;
	    	BasicStroke bs1 = new BasicStroke(5, BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
	        g2d.setStroke(bs1);
	        g2d.setPaint(Color.yellow);
	        g2d.drawRect(200, 200, 280, 50);
	    }
	    
	    public void selectEditor(Graphics g){
	    	Graphics2D g2d = (Graphics2D) g;
	    	BasicStroke bs1 = new BasicStroke(5, BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
	        g2d.setStroke(bs1);
	        g2d.setPaint(Color.yellow);
	        g2d.drawRect(200, 350, 280, 50);
	    }
		
	    //Highlight menu items
	
	private class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {}
		
		public void keyReleased(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_UP) {
				menuChoice =1;
				//System.out.println("up");
			}

			if (key == KeyEvent.VK_DOWN) {
				menuChoice =2;
				//System.out.println("down");
			}
			
			if ((key == KeyEvent.VK_SPACE) ||
					(key == KeyEvent.VK_ENTER)) {
				if (menuChoice == 1){
				//isRunning = false;
					System.out.println("Sorry no game yet...");
				}
				else if(menuChoice == 2){
					isRunning = false;
				}
				
			}
		}
		
		
	}

}
