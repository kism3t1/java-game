import java.awt.Rectangle;
import java.io.Serializable;

public class EntityFriendly extends JavaGame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7373033724067700601L;
	private int dx;
	private int dy;
	private int id;
	private int speed;
	private long dLast;
	private int x, y, width, height;
	private int skin;
	private int health;
	
	private EntityAIFriendly ai;

	public EntityFriendly(int id, int skin, int x, int y) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = entityFriendlySkins[skin].getWidth(null);
		height = entityFriendlySkins[skin].getHeight(null);
		this.id = id;
		speed = 1;
		health = 3;
		dLast = System.currentTimeMillis() - 800;
		ai = new EntityAIFriendly(id);
	}

	public void move() {
		x += dx;
		y += dy;
		if (System.currentTimeMillis() - dLast > 800) {	//Wait .8 second
			ai.checklocation();			//Check for friendly in proximity
			dx = ai.returnx();		//Get x value from AI Class
			dy = ai.returny();		//Get y value from AI Class
			dLast = System.currentTimeMillis();
		}

		if (collisionDetection.check(getBounds(), true, true, true, id) != CollisionDetection.CD_NULL) {	//Check Collision
			ai.randomAIDirection();	//Run random AI Direction method
			dx = ai.returnx();		//Get x value from AI Class
			dy = ai.returny();		//Get y value from AI Class
			dLast = System.currentTimeMillis() - 800;
		}
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getID()
	{
		return id;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getBounds() { // Get bounds for collision detection
		return new Rectangle(x, y, width, height);
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

}
