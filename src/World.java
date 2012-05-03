import java.io.Serializable;
import java.util.ArrayList;


public class World implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2736137640198437766L;
	public Map floorMap;
	public Map wallMap;

	private String title = "Default World";
	private int width;
	private int height;
	private int borderSkin;
	
	public ArrayList<Enemy> enemy;
	public Entity entity;

	public World(String title, int width, int height, int borderSkin) {
		this.title = title;
		this.width = width + 2;			//we add 2 to width and height
		this.height = height + 2;		//to allow for the border
		this.borderSkin = borderSkin;
		
		enemy = new ArrayList<Enemy>();
		entity = (new Entity());

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
		this.borderSkin = borderSkin;
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

}
