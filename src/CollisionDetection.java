import java.awt.Image;
import java.awt.Rectangle;

public class CollisionDetection {

	public int x, y;
	public Image image;

	public boolean checkCollisions(Rectangle r1, boolean checkEntity,
			boolean checkEnemy, boolean checkTile, int enemyID) { // Collision
																	// Detection
		boolean collision = false;
		Rectangle r2;

		// check collision with enemies
		if (checkEnemy) {
			for (int i = 0; i < Level1.enemy.size(); i++) {
				if (i != enemyID) {
					r2 = Level1.enemy.get(i).getBounds(); // Get bounds of enemy

					if (r1.intersects(r2)) { // Checks if entity collides with
												// an enemy
						collision = true;
					}
				}
			}
		}

		// check for tile collision
		if (checkTile) {
			for (int x = Level1.xOffset; x < Level1.SCREEN_TILES_WIDE
					+ Level1.xOffset; x++) {
				for (int y = Level1.yOffset; y < Level1.SCREEN_TILES_HIGH
						+ Level1.yOffset; y++) {
					if (Level1.world.wallMap.TileSet[x][y].isWall()
							&& Level1.world.wallMap.TileSet[x][y].isVisible()) { // no
																					// need
																					// to
																					// check
																					// for
																					// collision
																					// if
																					// it
																					// isn't
																					// a
																					// wall
						r2 = Level1.world.wallMap.TileSet[x][y].getBounds();

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

	public Rectangle getBounds() { // Get bounds for collision detection
		return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
	}
}
