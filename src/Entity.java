import java.awt.Graphics;
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
	private int health;
	private int armour;
	private int state;
	private int frame_count = 0;
	private long frame_last;
	private boolean visible;

	public Entity() {
		skin = 0;
		health = 5;
		armour = 4;
		width = entitySkins[skin].getWidth(null);
		height = entitySkins[skin].getHeight(null);
		x = tileWidth * 2;		//default position
		y = tileHeight * 2;	//at tile 1,1
		speed = 5;
		state = STATE_NORMAL;
		visible = true;
	}

	public Entity(int skin, int x, int y) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		health = 5;
		armour = 4;
		width = entitySkins[skin].getWidth(null);
		height = entitySkins[skin].getHeight(null);
		speed = 5;
		state = STATE_NORMAL;
		visible = true;
	}

	public void move() {
		x += dx;
		y += dy;

		if(collisionDetection.checkTiles(getBounds()))
		{
			x -= dx;
			y -= dy;
		}

		if(collisionDetection.checkEnemies(getBounds(), -1))
		{
			if(state != STATE_INJURED)
				damage(1);
			System.out.println("Armour value is: " + armour);
		}
	}

	private void damage(int amount) {
		if (armour == 0){
			health -= amount;
		}else{
			armour -=amount;
		}
		state = STATE_INJURED;
		frame_count = 6;
		frame_last = System.currentTimeMillis() - 200;
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

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getArmour(){
		return armour;
	}
	
	public void setArmour(int armour){
		this.armour = armour;
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
	
	public void draw(Graphics g)
	{
		switch(state)
		{
		case STATE_NORMAL:
			visible = true;
			break;
		case STATE_INJURED:
			if(frame_last < System.currentTimeMillis() - 200)
			{
				if(frame_count > 0)
				{
					visible = !visible;
					frame_count -= 1;
					frame_last = System.currentTimeMillis();
				}else{
					state = STATE_NORMAL;
				}
			}
			break;
		default:
			visible = true;
			break;	
		}
		if(visible)
			g.drawImage(entitySkins[skin], x, y, null);
	}

}