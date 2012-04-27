import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Entity extends CollisionDetection {

	private String entity = "Images/entity.png";

	private int dx;
	private int dy;
	private int speed;

	public Entity() {
		ImageIcon ii = new ImageIcon(this.getClass().getResource(entity));
		image = ii.getImage();
		x = 40;
		y = 60;
		speed = 5;
		;
	}

	public Entity(int x, int y) {
		ImageIcon ii = new ImageIcon(this.getClass().getResource(entity));
		image = ii.getImage();
		this.x = x;
		this.y = y;
		speed = 5;
	}

	public void move() {
		x += dx;
		y += dy;

		if (checkCollisions(getBounds(), false, true, true, -1)) {
			x -= dx;
			y -= dy;
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

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			dx = -speed;
		}

		if (key == KeyEvent.VK_RIGHT) {
			dx = speed;
		}

		if (key == KeyEvent.VK_UP) {
			dy = -speed;
		}

		if (key == KeyEvent.VK_DOWN) {
			dy = speed;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			dx = 0;
		}

		if (key == KeyEvent.VK_RIGHT) {
			dx = 0;
		}

		if (key == KeyEvent.VK_UP) {
			dy = 0;
		}

		if (key == KeyEvent.VK_DOWN) {
			dy = 0;
		}
	}

	/*
	 * public void checkCollisions(){ //Rectangle r1 = Level1.getBounds();
	 * Rectangle r2 = getBounds(); //Rectangle r3 = en.getBounds();
	 * 
	 * if (r2.intersects()){ System.out.println("COLLISION!"); } }
	 */
}