
public class Enemy extends CollisionDetection {

	private int dx;
	private int dy;
	private int id;
	private int speed;
	private long dLast;
	private AI ai;

	public Enemy(int id, int skin) {
		ai = new AI();
		this.skin = skin;
		x = 80;
		y = 120;
		width = Level1.enemySkins[skin].getWidth(null);
		height = Level1.enemySkins[skin].getHeight(null);
		this.id = id;
		setSpeed(2);
		speed = 1;	//Speed of Enemy
		dLast = System.currentTimeMillis() - 2000; //Do not set delay at start of game
	}

	public Enemy(int id, int skin, int x, int y) {
		ai = new AI();
		this.skin = skin;
		this.x = x;
		this.y = y;
		width = Level1.enemySkins[skin].getWidth(null);
		height = Level1.enemySkins[skin].getHeight(null);
		this.id = id;
		speed = 1;
		dLast = System.currentTimeMillis() - 2000;
	}

	public void move() {
		x += dx;
		y += dy;
		if (System.currentTimeMillis() - dLast > 2000) {	//Wait 2 seconds 
			ai.randomAIDirection();	//Run random AI Direction method
			dx = ai.returnx();		//Get x value from AI Class
			dy = ai.returny();		//Get y value from AI Class
			dLast = System.currentTimeMillis();
		}

		if (checkCollisions(getBounds(), true, true, true, id)) {	//Check Collision
			ai.randomAIDirection();	//Run random AI Direction method
			dx = ai.returnx();		//Get x value from AI Class
			dy = ai.returny();		//Get y value from AI Class
			dLast = System.currentTimeMillis() - 2000;
		}
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
