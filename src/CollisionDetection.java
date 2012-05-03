import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public class CollisionDetection extends JavaGame{

	public boolean check(Rectangle r1, boolean checkEntity,
			boolean checkEnemy, boolean checkTile, int enemyID) { // Collision
																	// Detection
		boolean collision = false;
		Rectangle r2;

		// check collision with enemies
		if (checkEnemy) {
			if(!world.enemy.isEmpty()){
				for (int i = 0; i < world.enemy.size(); i++) {
					if (world.enemy.get(i).getID() != enemyID) {
						r2 = world.enemy.get(i).getBounds(); // Get bounds of enemy

						if (r1.intersects(r2)) { // Checks if entity collides with
							// an enemy
							collision = true;
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
							collision = true;
						}
					}
				}
			}
		}
		return collision;
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

		return -1;
	}
}
