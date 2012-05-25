import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

public class Enemy extends Halja implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3429060072522433039L;
	private int dx;
	private int dy;
	private int id;
	private int speed;
	private long dLast;
	private int x, y, width, height;
	private int skin;
	private int health;
	private int theNumber;
	
	private int animState = ANIM_STILL;
	
	private EntityAIEnemy ai;

	public Enemy(int id, int skin, int x, int y) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = enemySkins[TOD_DAYTIME][skin][0].getWidth();
		height = enemySkins[TOD_DAYTIME][skin][0].getHeight();
		this.id = id;
		speed = 1;
		health = 3;
		//dLast = System.currentTimeMillis() - 500;
		ai = new EntityAIEnemy(id);
		
		animState = ANIM_STILL;
	}
	
	public Enemy(int id, int skin, int x, int y, int health) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = enemySkins[TOD_DAYTIME][skin][0].getWidth();
		height = enemySkins[TOD_DAYTIME][skin][0].getHeight();
		this.id = id;
		speed = 1;
		this.health = health;
		//dLast = System.currentTimeMillis() - 500;
		ai = new EntityAIEnemy(id);
		
		animState = ANIM_STILL;
	}
	
	public void move() {
		x += dx;
		y += dy;
		if (ai.returnAttack()){
			theNumber = 200;
		}else{
			theNumber = ai.returnRandom();
		}
		if (System.currentTimeMillis() - dLast > theNumber) {	//Wait a random int seconds
			ai.checklocation();			//Check for enemies in proximity
			dx = ai.returnx();		//Get x value from AI Class
			dy = ai.returny();		//Get y value from AI Class
			//System.out.println(theNumber);
			dLast = System.currentTimeMillis();
		}
//If collides then random direction
		if (collisionDetection.check(getBounds(), true, true, true, id) != CollisionDetection.CD_NULL) {	//Check Collision
			x -= dx;
			y -= dy;
			ai.randomAIDirection();	//Run random AI Direction method
			dx = ai.returnx();		//Get x value from AI Class
			dy = ai.returny();		//Get y value from AI Class
			//dLast = System.currentTimeMillis() - ai.returnRandom();
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
	
	public void draw(Graphics g){
		g.drawImage(enemySkins[gameTime.checkDateTime()][skin][ai.returnAnimState()].nextFrame(), x, y, null);
	}

}
