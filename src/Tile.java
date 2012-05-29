import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author LosOjos
 */

public class Tile extends Halja implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6973028747019539127L;
	// initialize Tile fields
	private boolean destructible;

	private int skin;
	private int health;

	private int x;
	private int y;

	public Tile(int x, int y) { // default constructor with default values
		skin = 0;
		health = 3;
		destructible = false;
		this.x = x;
		this.y = y;
	}

	public Tile(int x, int y, byte bSkin, byte bHealth, boolean isDestructible) { // customisable contructor
		skin = bSkin;
		health = bHealth;
		destructible = isDestructible;
		this.x = x;
		this.y = y;
	}
	
	public Tile(Point pos) { // default constructor with default values
		skin = 0;
		health = 3;
		destructible = false;
		x = pos.x;
		y = pos.y;
	}

	public Tile(Point pos, byte bSkin, byte bHealth, boolean isDestructible) { // customisable contructor
		skin = bSkin;
		health = bHealth;
		destructible = isDestructible;
		x = pos.x;
		y = pos.y;
	}

	public boolean isDestructible() { // returns destructible field
		return destructible;
	}

	public void setDestructible(boolean isDestructible) {
		destructible = isDestructible;
	}
	
	public void toggleDestructible() {
		destructible = !destructible;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int iHealth) {
		health = iHealth;
	}

	public int getSkin() { // returns skin field
		return skin;
	}

	public void setSkin(int iSkin) { // changes skin
		skin = iSkin;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Point getPos(){
		return new Point(x,y);
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int damage(byte bDamage) { // inflicts damage to tile
		if (destructible) {
			health -= bDamage;
		}
		return health;
	}

	public Rectangle getBounds() { // Get bounds for collision detection
		return new Rectangle(x, y, tileWidth, tileHeight); // 32, 32 size of tile
	}
}
