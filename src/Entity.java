import java.awt.event.KeyEvent;
import java.io.Serializable;

public class Entity extends CollisionDetection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7743962361732364015L;
	private int dx;
	private int dy;
	private int speed;

	public Entity() {
		skin = 0;
		width = JavaGame.entitySkins[skin].getWidth(null);
		height = JavaGame.entitySkins[skin].getHeight(null);
		x = JavaGame.tileWidth;		//default position
		y = JavaGame.tileHeight;	//at tile 1,1
		speed = 5;
	}

	public Entity(int skin, int x, int y) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = JavaGame.entitySkins[skin].getWidth(null);
		height = JavaGame.entitySkins[skin].getHeight(null);
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

}