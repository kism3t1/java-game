import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Enemy extends CollisionDetection {

	private String enemy = "Images/enemy.png";

	private int dx;
	private int dy;
	private int id;
	private int speed;
	private long dLast;

	public Enemy(int id) {
		ImageIcon ii = new ImageIcon(this.getClass().getResource(enemy));
		image = ii.getImage();
		x = 80;
		y = 120;
		this.id = id;
		setSpeed(2);
		speed = 1;	//Speed of Enemy
		dLast = System.currentTimeMillis() - 2000; //Do not set delay at start of game
	}

	public Enemy(int id, int x, int y) {
		ImageIcon ii = new ImageIcon(this.getClass().getResource(enemy));
		image = ii.getImage();
		this.x = x;
		this.y = y;
		this.id = id;
		speed = 1;
		dLast = System.currentTimeMillis() - 2000;
	}

	public void move() {
		x += dx;
		y += dy;
		if (System.currentTimeMillis() - dLast > 2000) {	//Wait 2 seconds 
			randomDirection();								
			dLast = System.currentTimeMillis();
		}

		if (checkCollisions(getBounds(), true, true, true, id)) {	//Check Collision
			randomDirection();
			dLast = System.currentTimeMillis() - 2000;
		}
	}
// AI ROUTINE!
	
	public void randomDirection() {					//Random Direction for AI
		int Direction = (int) (Math.random() * 4); // Either return 0,1,2,3 for Right,left,down,up
		switch (Direction) {
		case 0:	//RIGHT
			dx = speed; 
			dy = 0;
			break;
		case 1: // LEFT
			dx = -speed;
			dy = 0;
			break;
		case 2: // DOWN
			dx = 0;
			dy = speed;
			break;
		case 3: // UP
			dx = 0;
			dy = -speed;
			break;
		}
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

	public Rectangle getBounds() { // Get bounds for collision detection
		return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
	}

}
