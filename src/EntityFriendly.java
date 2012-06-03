import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public class EntityFriendly extends Halja implements Serializable {

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
	private int state;
	private int frame_count = 0;
	private long frame_last;
	private boolean visible;
	private int theNumber;
	
	private EntityAIFriendly ai;

	public EntityFriendly(int id, int skin, int x, int y) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = friendlySkins[TOD_DAYTIME][skin][0].getWidth();
		height = friendlySkins[TOD_DAYTIME][skin][0].getHeight();
		this.id = id;
		speed = 1;
		health = 3;
		state = STATE_NORMAL;
		visible = true;
		dLast = System.currentTimeMillis() - 800;
		ai = new EntityAIFriendly(id);
	}
	
	public EntityFriendly(int id, int skin, int x, int y, int health) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = friendlySkins[TOD_DAYTIME][skin][0].getWidth();
		height = friendlySkins[TOD_DAYTIME][skin][0].getHeight();
		this.id = id;
		speed = 1;
		this.health = health;
		state = STATE_NORMAL;
		visible = true;
		dLast = System.currentTimeMillis() - 800;
		ai = new EntityAIFriendly(id);
	}

	public void move() {
		x += dx;
		y += dy;
		if (ai.returnRunAway()){
			theNumber = 500;
		}else{
			theNumber = ai.returnRandom();
		}
		if (System.currentTimeMillis() - dLast > theNumber) {	//Wait
			ai.checklocation();			//Check for friendly in proximity
			dx = ai.returnx();		//Get x value from AI Class
			dy = ai.returny();		//Get y value from AI Class
			dLast = System.currentTimeMillis();
		}

		if (collisionDetection.check(getBounds(), true, true, true, id) != CollisionDetection.CD_NULL) {	//Check Collision
			x -= dx;
			y -= dy;
			ai.randomAIDirection();	//Run random AI Direction method
			dx = ai.returnx();		//Get x value from AI Class
			dy = ai.returny();		//Get y value from AI Class
			dLast = System.currentTimeMillis() - 800;
		}
		if(collisionDetection.checkFriendly(world.ollie.getBounds(), -1))
		{
			world.ollie.setHealth(world.ollie.getHealth()+1);
			if(state != STATE_INJURED)
				damage(1);
		}
	}
	
	private void damage(int amount) {
		health += amount;
		state = STATE_INJURED;
		frame_count = 6;
		//frame_last = System.currentTimeMillis() - 200;
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
			g.drawImage(friendlySkins[gameTime.checkDateTime()][skin][ai.retutnAnimState()].nextFrame(), x, y, null);
	}

}
