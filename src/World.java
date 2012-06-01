import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;


public class World extends Halja implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2736137640198437766L;
	public Map floorMap;
	public Map wallMap;

	private String title = "Default World";
	private int width;
	private int height;
	
	private int start_x = tileWidth * 2;
	private int start_y = tileHeight * 2;
	
	private int[][] enemyArray;
	private int[][] friendlyArray;
	
	public transient ArrayList<Enemy> enemy;
	public transient ArrayList<EntityFriendly> friendly;
	public transient Player ollie;

	public World(String title, int width, int height, int borderSkin) {
		this.title = title;
		this.width = width + 2;			//we add 2 to width and height
		this.height = height + 2;		//to allow for the border

		ollie = new Player();
		friendly = new ArrayList<EntityFriendly>();
		enemy = new ArrayList<Enemy>();
		
		floorMap = new Map(width, height, true, borderSkin);	
		wallMap = new Map(width, height, false, borderSkin);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public void setBorderSkin(int borderSkin)
	{
		wallMap.changeBorder(borderSkin);
	}

	public void addEnemy(int skin, int x, int y)
	{
		Enemy e = new Enemy(getNewID(), skin, x, y);
		enemy.add(e);
	}
	
	public void addEnemy(int skin, int x, int y, int health)
	{
		Enemy e = new Enemy(getNewID(), skin, x, y, health);
		enemy.add(e);
	}

	public void removeEnemy(int enemyID)
	{
		if(!enemy.isEmpty())
		{
			for(Enemy e : enemy)
			{
				if(e.getID() == enemyID){
					enemy.remove(e);
					enemy.trimToSize();
					return;
				}
			}
		}
	}

	private int getNewID() {
		int id = 1;
		boolean searching = true;
		boolean unique = true;

		if(!enemy.isEmpty())
		{
			do
			{
				for(Enemy e : enemy)
				{
					if(e.getID() == id)
					{
						unique = false;
						break;
					}
				}

				if(unique)
				{
					searching = false;
				}
				else
				{
					id++;
					unique = true;
				}

			}while(searching);
		}
		return id;
	}

	public Enemy getEnemy(int enemyID)
	{
		for(Enemy e : enemy){
			if(e.getID() == enemyID)
				return e;
		}
		return null;
	}

	public void moveEnemies()
	{
		for(Enemy e : enemy)
			e.move();
	}

	public void drawEnemies(Graphics g){
		for(Enemy e : enemy)
			e.draw(g);
	}

	/* Friendly entity part */


	public void addFriendly(int skin, int x, int y)
	{
		EntityFriendly f = new EntityFriendly(getNewFriendlyID(), skin, x, y);
		friendly.add(f);
	}
	
	public void addFriendly(int skin, int x, int y, int health)
	{
		EntityFriendly f = new EntityFriendly(getNewFriendlyID(), skin, x, y, health);
		friendly.add(f);
	}

	public void removeFriendly(int FriendlyID)
	{
		if(!friendly.isEmpty())
		{
			for(EntityFriendly f : friendly)
			{
				if(f.getID() == FriendlyID){
					friendly.remove(f);
					friendly.trimToSize();
					return;
				}
			}
		}
	}

	private int getNewFriendlyID() {
		int id = 1;
		boolean searching = true;
		boolean unique = true;

		if(!friendly.isEmpty())
		{
			do
			{
				for(EntityFriendly f : friendly)
				{
					if(f.getID() == id)
					{
						unique = false;
						break;
					}
				}

				if(unique)
				{
					searching = false;
				}
				else
				{
					id++;
					unique = true;
				}

			}while(searching);
		}
		return id;
	}

	public EntityFriendly getFriendly(int FriendlyID)
	{
		for(EntityFriendly f : friendly){
			if(f.getID() == FriendlyID)
				return f;
		}
		return null;
	}

	public void moveFriendlies()
	{
		for(EntityFriendly f : friendly){
			f.move();
		}

	}
	public void drawFriendly(Graphics g){
		for(EntityFriendly f : friendly)
			f.draw(g);
	}
	
	public void setStart(int x, int y){
		start_x = x;
		start_y = y;
	}
	
	public void setStart(Point pos){
		start_x = pos.x;
		start_y = pos.y;
	}
	
	public boolean PrepareSave(){
		try{
			enemy.trimToSize();
			enemyArray = new int[enemy.size()][4];
			for(int x = 0; x < enemyArray.length; x++){
				enemyArray[x][0] = enemy.get(x).getSkin();
				enemyArray[x][1] = enemy.get(x).getX();
				enemyArray[x][2] = enemy.get(x).getY();
				enemyArray[x][3] = enemy.get(x).getHealth();
			}

			friendly.trimToSize();
			friendlyArray = new int[friendly.size()][4];
			for(int x = 0; x < friendlyArray.length; x++){
				friendlyArray[x][0] = friendly.get(x).getSkin();
				friendlyArray[x][1] = friendly.get(x).getX();
				friendlyArray[x][2] = friendly.get(x).getY();
				friendlyArray[x][3] = friendly.get(x).getHealth();
			}
		}catch(Exception e){
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	public void Initialise(){
		cameraX = 0;
		cameraY = 0;
		
		if(ollie != null)
			ollie = null;
		ollie = new Player(start_x, start_y);
		
		if(friendly != null)
			friendly = null;
		friendly = new ArrayList<EntityFriendly>();
		
		if(enemy != null)
			enemy = null;
		enemy = new ArrayList<Enemy>();
		
		enemy.clear();
		enemy.trimToSize();
		friendly.clear();
		friendly.trimToSize();
		
		for(int x = 0; x < enemyArray.length; x++)
			addEnemy(enemyArray[x][0], enemyArray[x][1], enemyArray[x][2], enemyArray[x][3]);
		
		for(int x = 0; x < friendlyArray.length; x++)
			addFriendly(friendlyArray[x][0], friendlyArray[x][1], friendlyArray[x][2], friendlyArray[x][3]);
	}

}
