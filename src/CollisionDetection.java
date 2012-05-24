import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class CollisionDetection extends Halja{
	
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
				for (Enemy e : world.enemy) {
					if (e.getID() != enemyID) {
						r2 = e.getBounds(); // Get bounds of enemy

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
			Point[] tileRange = getTileRange(r1);
			if(tileRange != null){
				for (int tx = tileRange[0].x; tx < tileRange[1].x; tx++) {
					for (int ty = tileRange[0].y; ty < tileRange[1].y; ty++) {
						if (world.wallMap.TileSet[tx][ty] != null) {
							r2 = world.wallMap.TileSet[tx][ty].getBounds();

							if (r1.intersects(r2)) { // Checks if entity collides
								// with a tile
								return CD_TILE;
							}
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
			for (Enemy e : world.enemy) {
				if (e.getID() != enemyID) {
					r2 = e.getBounds(); // Get bounds of enemy

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
		Point[] tileRange = getTileRange(r1);
		if(tileRange != null){
			for (int tx = tileRange[0].x; tx < tileRange[1].x; tx++) {
				for (int ty = tileRange[0].y; ty < tileRange[1].y; ty++) {
					if (world.wallMap.TileSet[tx][ty] != null) {
						r2 = world.wallMap.TileSet[tx][ty].getBounds();

						if (r1.intersects(r2)) { // Checks if entity collides
							// with a tile
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean checkFriendly(Rectangle r1, int friendlyID){
		Rectangle r2;
		if(!world.friendly.isEmpty()){
			for (EntityFriendly f : world.friendly) {
				if (f.getID() != friendlyID) {
					r2 = f.getBounds(); // Get bounds of Friendly

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
			for (Enemy e : world.enemy) {
				r2 = e.getBounds(); // Get bounds of enemy

				if (r1.intersects(r2)) { 
					id = e.getID();
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
			for (EntityFriendly f : world.friendly) {
				r2 = f.getBounds(); // Get bounds of enemy

				if (r1.intersects(r2)) { 
					id = f.getID();
					return id;
				}

			}
			
		}

		return CD_NULL;
	}
	
	public Point[] getTileRange(Rectangle r){
		for(int x = 0; x < world.floorMap.TileSet.length; x++){
			for(int y = 0; y < world.floorMap.TileSet[0].length; y++){
				if(r.intersects(world.floorMap.TileSet[x][y].getBounds())){
					int startX = x;
					int startY = y;
					
					if(startX > 0)
						startX -= 1;
					if(startY > 0)
						startY -= 1;
					if (startX < 0)
						startX = 0;
					if(startY < 0)
						startY = 0;
					
					int endX = x + 1;
					int endY = y + 1;
					
					if(r.width < tileWidth){
						endX += 1;
					}else{
						endX += Math.abs(r.width / tileWidth);
					}
					
					if(r.height < tileHeight){
						endY += 1;
					}else{
						endY += Math.abs(r.height / tileHeight);
					}
					
					Point[] point = new Point[]{
						new Point(startX, startY),
						new Point(endX, endY)
						};
					return point;
				}
			}
		}
		
		return null;
	}
}
