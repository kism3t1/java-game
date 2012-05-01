import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;


//Need to add KeyListener to select Start Game or Map Editor
//Add stroke to image when Up || Down is selected


@SuppressWarnings("serial")
public class StartScreen extends JPanel{
	
	private int choice;
	Image start;
	Image map;
	Image bg;
	
		public StartScreen (){
	        start = new ImageIcon(this.getClass().getResource("/Images/start.png")).getImage();
			map = new ImageIcon(this.getClass().getResource("/Images/map.png")).getImage();
			bg = new ImageIcon(this.getClass().getResource("/Images/background.png")).getImage();
		}

	    public void paint(Graphics g) {

	        Graphics2D g2d = (Graphics2D) g;
	        g2d.drawImage(bg, 0,0,710,730,null);
	        //g2d.drawString("Go on then....Choose", 200, 30);
	        g2d.drawImage(start, 200, 200, null);
	        g2d.drawImage(map, 200, 350, null);
	        select(g);
	    
	    }
	    
	    public void select(Graphics g){
	    	Graphics2D g2d = (Graphics2D) g;
	    	BasicStroke bs1 = new BasicStroke(8, BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
	        g2d.setStroke(bs1);
	        g2d.setPaint(Color.yellow);
	        g2d.drawRect(200, 350, 280, 50);
	    }
		
	
	public void init(){
		  JFrame frame = new JFrame("Java-Game V0.1 - StartScreen");
	        frame.add(new StartScreen());
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(710, 730);
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
		
	}
	
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		//if (key == KeyEvent.VK_LEFT) {
		//}

		//if (key == KeyEvent.VK_RIGHT) {
		//}

		if (key == KeyEvent.VK_UP) {
			//Highlight picture --
			//choice -=1;
			System.out.println("up");
		}

		if (key == KeyEvent.VK_DOWN) {
			//Highlight picture ++
			//choice +=1;
			System.out.println("down");
		}
		
		if ((key == KeyEvent.VK_SPACE) ||
				(key == KeyEvent.VK_ENTER)) {
			StartScreen.this.setVisible(false);
			JavaGame.init();
		}
	}

}
