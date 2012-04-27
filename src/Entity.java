import java.awt.event.KeyEvent;

public class Entity extends CollisionDetection {

	private int dx;
	private int dy;
	private int speed;

	public Entity() {
		skin = 0;
		x = 40;
		y = 60;
		width = Level1.entitySkins[skin].getWidth(null);
		height = Level1.entitySkins[skin].getHeight(null);
		speed = 5;
	}

	public Entity(int skin, int x, int y) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = Level1.entitySkins[skin].getWidth(null);
		height = Level1.entitySkins[skin].getHeight(null);
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