import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.io.Serializable;

public class Player extends Halja implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7743962361732364015L;
	
	private int dx;
	private int dy;
	private int direction;
	private int speed;
	public int x, y, width, height;
	public int skin;
	private int health;
	private int armour;
	
	private int state;
	private int animState = ANIM_STILL;
	private int frame_count = 0;
	private long frame_last;
	private boolean visible;
	
	private AffineTransform wpnTransform = new AffineTransform();
	private int wpnRotation = -45;
	private boolean attacking = false;
	private int attack_frame = 0;

	public Player() {
		skin = 0;
		health = 5;
		armour = 4;
		width = entitySkins[TOD_DAYTIME][skin].getWidth();
		height = entitySkins[TOD_DAYTIME][skin].getHeight();
		x = tileWidth * 2;		//default position
		y = tileHeight * 2;	//at tile 1,1
		speed = 5;
		state = STATE_NORMAL;
		animState = ANIM_STILL;
		visible = true;
	}

	public Player(int skin, int x, int y) {
		this.skin = skin;
		this.x = x;
		this.y = y;
		health = 5;
		armour = 4;
		width = entitySkins[TOD_DAYTIME][skin].getWidth();
		height = entitySkins[TOD_DAYTIME][skin].getHeight();
		speed = 5;
		state = STATE_NORMAL;
		animState = ANIM_STILL;
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
	
	public Point getPos() {
		return new Point(x, y);
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void keyPressed(KeyEvent e) {

		switch(e.getKeyCode()){
		case KeyEvent.VK_A:
			dx = -speed;
			dy = 0;
			setDirection(DIR_LEFT);
			animState = ANIM_WALK_LEFT;
			break;
		case KeyEvent.VK_D:
			dx = speed;
			dy = 0;
			setDirection(DIR_RIGHT);
			animState = ANIM_WALK_RIGHT;
			break;
		case KeyEvent.VK_W:
			dy = -speed;
			dx = 0;
			setDirection(DIR_UP);
			animState = ANIM_WALK_UP;
			break;
		case KeyEvent.VK_S:
			dy = speed;
			dx = 0;
			setDirection(DIR_DOWN);
			animState = ANIM_WALK_DOWN;
			break;
		case KeyEvent.VK_CONTROL:
			if(!attacking){
				attack_frame = 6;
				attacking = true;
				state = STATE_ATTACKING;
			}
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_CONTROL:
			break;
		default:
			dx = 0;
			dy = 0;
			animState = ANIM_STILL;
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

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
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
		Graphics2D g2d = (Graphics2D) g;
		switch(state)		//is player injured? Adjust visibility if so
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
		
	if(attacking){
		if(attack_frame > 0)
		{
			wpnRotation += 20;
			switch(direction){
			case DIR_UP:
				wpnTransform.setToTranslation(x, y - 13);
				wpnTransform.rotate(Math.toRadians(wpnRotation), weaponSkin[0][eqpWeapon].getWidth() / 2, weaponSkin[0][eqpWeapon].getHeight());
				break;
			case DIR_DOWN:
				wpnTransform.setToTranslation(x, y + 13);
				wpnTransform.translate(0, -weaponSkin[0][eqpWeapon].getHeight(null));
				wpnTransform.rotate(Math.toRadians(wpnRotation), weaponSkin[0][eqpWeapon].getWidth() / 2, weaponSkin[0][eqpWeapon].getHeight());
				break;
			case DIR_RIGHT:
				wpnTransform.setToTranslation(x + 3, y - 3);
				wpnTransform.rotate(Math.toRadians(wpnRotation), weaponSkin[0][eqpWeapon].getWidth() / 2, weaponSkin[0][eqpWeapon].getHeight());
				break;
			case DIR_LEFT:
				wpnTransform.setToTranslation(x - 3, y - 3);
				wpnTransform.translate(-weaponSkin[0][eqpWeapon].getWidth(null), 0);
				wpnTransform.rotate(Math.toRadians(wpnRotation), weaponSkin[0][eqpWeapon].getWidth() / 2, weaponSkin[0][eqpWeapon].getHeight());
			}
			
			attack_frame -= 1;
		}else{
			attacking = false;
			wpnRotation = -45;
			state = STATE_NORMAL;
		}
	}
		
		if(visible){
			g.drawImage(entitySkins[gameTime.checkDateTime()][animState].nextFrame(), x, y, null);
			
			if(attacking)
				g2d.drawImage(weaponSkin[gameTime.checkDateTime()][eqpWeapon], wpnTransform, null);
		}
	}

}