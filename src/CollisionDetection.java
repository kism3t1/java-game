import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class CollisionDetection extends JavaGame{
	
	public static final int CD_ENEMY = 1;		//allow us to identify collision type
	public static final int CD_ENTITY = 2;
	public static final int CD_TILE = 3;
	public static final int CD_NULL = -1;		//no collision

	public int check(Rectangle r1, boolean checkEntity,
			boolean checkEnemy, boolean checkTile, int enemyID) { // Collision
																	// Detection
		Rectangle r2;

		// check collision with enemies
		if (checkEnemy) {
			if(!world.enemy.isEmpty()){
				for (int i = 0; i < world.enemy.size(); i++) {
					if (world.enemy.get(i).getID() != enemyID) {
						r2 = world.enemy.get(i).getBounds(); // Get bounds of enemy

						if (r1.intersects(r2)) { // Checks if entity collides with
							// an enemy
							return CD_ENEMY;
						}
					}
				}
			}
		}

		// check for tile collision
		if (checkTile) {
			for (int tx = xOffset; tx < screenTilesWide
					+ xOffset; tx++) {
				for (int ty = yOffset; ty < screenTilesHigh
						+ yOffset; ty++) {
					if (world.wallMap.TileSet[tx][ty].isVisible()) {
						r2 = world.wallMap.TileSet[tx][ty].getBounds();

						if (r1.intersects(r2)) { // Checks if entity collides
													// with a tile
							return CD_TILE;
						}
					}
				}
			}
		}
		return CD_NULL;
	}
	
	public boolean checkEnemies(Rectangle r1, int enemyID){
		Rectangle r2;
		if(!world.enemy.isEmpty()){
			for (int i = 0; i < world.enemy.size(); i++) {
				if (world.enemy.get(i).getID() != enemyID) {
					r2 = world.enemy.get(i).getBounds(); // Get bounds of enemy

					if (r1.intersects(r2)) { // Checks if entity collides with
						// an enemy
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkTiles(Rectangle r1)
	{
		Rectangle r2;
		for (int tx = xOffset; tx < screenTilesWide
				+ xOffset; tx++) {
			for (int ty = yOffset; ty < screenTilesHigh
					+ yOffset; ty++) {
				if (world.wallMap.TileSet[tx][ty].isVisible()) {
					r2 = world.wallMap.TileSet[tx][ty].getBounds();

					if (r1.intersects(r2)) { // Checks if entity collides
												// with a tile
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkFriendly(Rectangle r1, int friendlyID){
		Rectangle r2;
		if(!world.friendly.isEmpty()){
			for (int i = 0; i < world.friendly.size(); i++) {
				if (world.friendly.get(i).getID() != friendlyID) {
					r2 = world.friendly.get(i).getBounds(); // Get bounds of Friendly

					if (r1.intersects(r2)) { // Checks if entity collides with a friendly
						return true;
					}
				}
			}
		}
		return false;
	}

	public int getEnemyID(Point point)
	{
		int id;
		Rectangle r2;
		Rectangle r1 = new Rectangle(point, new Dimension(1,1));

		if(!world.enemy.isEmpty()){
			for (int i = 0; i < world.enemy.size(); i++) {
				r2 = world.enemy.get(i).getBounds(); // Get bounds of enemy

				if (r1.intersects(r2)) { 
					id = world.enemy.get(i).getID();
					return id;
				}

			}
			
		}

		return CD_NULL;
	}
	
	public int getFriendlyID(Point point)
	{
		int id;
		Rectangle r2;
		Rectangle r1 = new Rectangle(point, new Dimension(1,1));

		if(!world.friendly.isEmpty()){
			for (int i = 0; i < world.friendly.size(); i++) {
				r2 = world.friendly.get(i).getBounds(); // Get bounds of enemy

				if (r1.intersects(r2)) { 
					id = world.friendly.get(i).getID();
					return id;
				}

			}
			
		}

		return CD_NULL;
	}
}
