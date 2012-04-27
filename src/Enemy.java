
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
    private Thread tr;

    public Enemy() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(enemy));
        image = ii.getImage();
        x = 80;
        y = 120;
        setSpeed(2);
        speed = 1;
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
        randomDirection();
		//tr.sleep(500);
        
    }
    
    public void randomDirection(){
	int Direction = (int) Math.random() * 4;	//return 0,1,2,3
	switch(Direction){
	case 0: 
		dx = speed;	//RIGHT
		dy = 0;
	break;
	case 1:	//LEFT
		dx = -speed;
		dy = 0;
	break;
	case 2:	//DOWN
		dx = 0;
		dy = speed;
	break;
	case 3:	//UP
		dx = 0;
		dy = -speed;
	break;
	}
	System.out.println("dx " + Direction);
	System.out.println("dy " + Direction);
    }
    
    //public void randomDirection(){
    	//double speed = 2.5;
    	//double enemydirection = Math.random() * Math.PI;	//Skitty Enemy
    	//dx = (int) (speed * Math.cos(enemydirection));
    	//dy = (int) (speed * Math.sin(enemydirection));
    //}

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
