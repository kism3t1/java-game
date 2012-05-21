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


public class Inventory extends JPanel implements Runnable{

	private int inventoryChoice;
	Image inventory;
	boolean isPaused;
	
	private Canvas gui;
	private boolean isRunning;
	private long cycleTime;
	private KeyListener kl;
	
		public Inventory (Canvas gui){
			this.gui = gui;
			isRunning = true;
			
			inventoryChoice =1;		//Default selection
			
			kl = new TAdapter();
			gui.addKeyListener(kl);
			gui.setFocusable(true);
			gui.requestFocusInWindow();
			
	        inventory = new ImageIcon(this.getClass().getResource("/Images/inventory.png")).getImage();
		}
		
		@Override
		public void run() {
			cycleTime = System.currentTimeMillis();
			gui.createBufferStrategy(2);				//2 = double buffer
			BufferStrategy strategy = gui.getBufferStrategy();
			gui.setBackground(Color.DARK_GRAY);
			
			while(isRunning){
				updateGUI(strategy);
				syncFPS();
			}
			
			gui.removeKeyListener(kl);
		}

		
	    public void updateGUI(BufferStrategy strategy) {
	    	Graphics g = strategy.getDrawGraphics();
	    	g.setColor(Color.RED);
	    	g.drawImage(inventory, (gui.getWidth() /2) - (inventory.getWidth(null)/2), 50, null);
	    	g.drawString("TO DO: Pause game thread instead of stopping", gui.getWidth() /3, 300);
	    	g.drawString("TO DO: Set Layout, Menu Right - Items Left", gui.getWidth() /3, 330);
	    	g.drawString("TO DO: Use weaponSkin/weaponMenuText etc...", gui.getWidth() /3, 360);
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
	    
	    //Method to draw the yellow rectangle same as StartScreen
	    
	    public void selection(Graphics g, int x, int y, int width, int height){
	    	Graphics2D g2d = (Graphics2D) g;
	    	BasicStroke bs1 = new BasicStroke(5, BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
	        g2d.setStroke(bs1);
	        g2d.setPaint(Color.yellow);
	        g2d.drawRect(x, y, width, height);
	    }
		
	    //To highlight menu items the same as StartScreen
	
	private class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getExtendedKeyCode();
			
			if (key == KeyEvent.VK_E){
				Halja.nextThread = "GAME";
				isRunning = false;
			}
		}
		
		public void keyReleased(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_UP) {
				inventoryChoice -=1;
			}

			if (key == KeyEvent.VK_DOWN) {
				inventoryChoice +=1;
			}
				
		}
			
	}

}
