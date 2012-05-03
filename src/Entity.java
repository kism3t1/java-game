import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.Serializable;

public class Entity extends JavaGame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7743962361732364015L;
	private int dx;
	private int dy;
	private int speed;
	public int x, y, width, height;
	public int skin;

	public Entity() {
		skin = 0;
		width = entitySkins[skin].getWidth(null);
		height = entitySkins[skin].getHeight(null);
		x = tileWidth * 2;		//default position
		y = tileHeight * 2;	//at tile 1,1
		speed = 5;
	}

	public Entity(int skin, int x, int y) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = entitySkins[skin].getWidth(null);
		height = entitySkins[skin].getHeight(null);
		speed = 5;
	}

	public void move() {
		x += dx;
		y += dy;

		if (collisionDetection.check(getBounds(), false, true, true, -1)) {
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
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSkin() {
		return skin;
	}

	public void setSkin(int skin) {
		this.skin = skin;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Rectangle getBounds() { // Get bounds for collision detection
		return new Rectangle(x, y, width, height);
	}

}