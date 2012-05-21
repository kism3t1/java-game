import java.awt.Graphics;
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

	public ArrayList<Enemy> enemy;
	public ArrayList<EntityFriendly> friendly;
	public Player ollie;

	public World(String title, int width, int height, int borderSkin) {
		this.title = title;
		this.width = width + 2;			//we add 2 to width and height
		this.height = height + 2;		//to allow for the border

		enemy = new ArrayList<Enemy>();
		ollie = new Player();

		friendly = new ArrayList<EntityFriendly>();

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

	public void removeEnemy(int enemyID)
	{
		if(!enemy.isEmpty())
		{
			for(int i = 0; i < enemy.size(); i++)
			{
				if(enemy.get(i).getID() == enemyID)
					enemy.remove(i);
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
				for(int i = 0; i < enemy.size(); i++)
				{
					if(enemy.get(i).getID() == id)
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
		for(int i = 0; i < enemy.size(); i++){
			if(enemy.get(i).getID() == enemyID)
				return enemy.get(i);
		}
		return null;
	}

	public void moveEnemies()
	{
		for(int i = 0; i < enemy.size(); i++)
			enemy.get(i).move();
	}

	public void drawEnemies(Graphics g){
		for(int i = 0; i < enemy.size(); i++)
			enemy.get(i).draw(g);
	}

	/* Friendly entity part */


	public void addFriendly(int skin, int x, int y)
	{
		EntityFriendly e = new EntityFriendly(getNewFriendlyID(), skin, x, y);
		friendly.add(e);
	}

	public void removeFriendly(int FriendlyID)
	{
		if(!friendly.isEmpty())
		{
			for(int i = 0; i < friendly.size(); i++)
			{
				if(friendly.get(i).getID() == FriendlyID)
					friendly.remove(i);
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
				for(int i = 0; i < friendly.size(); i++)
				{
					if(friendly.get(i).getID() == id)
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
		for(int i = 0; i < friendly.size(); i++){
			if(friendly.get(i).getID() == FriendlyID)
				return friendly.get(i);
		}
		return null;
	}

	public void moveFriendlies()
	{
		for(int i = 0; i < friendly.size(); i++){
			friendly.get(i).move();
		}

	}
	public void drawFriendly(Graphics g){
		for(int i = 0; i < friendly.size(); i++)
			friendly.get(i).draw(g);
	}

}
