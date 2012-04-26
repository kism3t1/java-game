
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Enemy {

    private String enemy = "Images/enemy.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private int speed;
    private Image image;

    public Enemy() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(enemy));
        image = ii.getImage();
        x = 80;
        y = 120;
        setSpeed(2);
        speed = 2;
    }
    
    public Enemy(int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(enemy));
        image = ii.getImage();
        this.x = x;
        this.y = y;
    }


    public void move() {
        x += dx;
        y += dy;
    	//dx = +1;
        
        randomDirection();
        
    }
    
    public void randomDirection(){
    	//double speed = 2.5;
    	/*		BRAIN FREEZE!!! so commented out until my brain starts working again
    	 * 		I am sure there is some working theory here somewhere...
    	 * 
    	int dxDirection = (int) Math.random() * 2;	//return 0,1
    	int dxdirection = 1;
    	switch(dxDirection){
    	case 0: dxdirection = 1;
    	break;
    	case 1: dxdirection  = +1;
    	break;
    	}
    	System.out.print("dx" + dxdirection);
    	
    	int dyDirection = (int) Math.random() * 2;	//return 0,1
    	int dydirection = 1;
    	switch(dyDirection){
    	case 0: dydirection = 1;
    	break;
    	case 1: dydirection = +1;
    	break;
    	}
    	System.out.print("dy" + dydirection);
    	
    	dx = (dxdirection);
    	System.out.print(dy);
    	dy = (dydirection);
    	System.out.print(dx);
    	*/
    	double enemydirection = Math.random() *2 * Math.PI;	//Skitty Enemy
    	dx = (int) (speed * Math.cos(enemydirection));
    	dy = (int) (speed * Math.sin(enemydirection));
    }
    
public static void wait (int n){
        
        long t0, t1;
        t0 =  System.currentTimeMillis();
        do{
            t1 = System.currentTimeMillis();
        }
        while ((t1 - t0) < (n * 1000));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Image getImage() {
        return image;
    }
    
    public Rectangle getBounds(){	//Get bounds for collision detection
    	return new Rectangle (x, y, image.getWidth(null), image.getHeight(null)); 
    }

}
