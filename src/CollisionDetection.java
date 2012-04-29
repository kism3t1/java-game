import java.awt.Rectangle;

public class CollisionDetection {

	public int x, y, width, height;
	public int skin;

	public boolean checkCollisions(Rectangle r1, boolean checkEntity,
			boolean checkEnemy, boolean checkTile, int enemyID) { // Collision
																	// Detection
		boolean collision = false;
		Rectangle r2;

		// check collision with enemies
		if (checkEnemy) {
			for (int i = 0; i < JavaGame.enemy.size(); i++) {
				if (i != enemyID) {
					r2 = JavaGame.enemy.get(i).getBounds(); // Get bounds of enemy

					if (r1.intersects(r2)) { // Checks if entity collides with
												// an enemy
						collision = true;
					}
				}
			}
		}

		// check for tile collision
		if (checkTile) {
			for (int x = JavaGame.xOffset; x < JavaGame.screenTilesWide
					+ JavaGame.xOffset; x++) {
				for (int y = JavaGame.yOffset; y < JavaGame.screenTilesHigh
						+ JavaGame.yOffset; y++) {
					if (JavaGame.world.wallMap.TileSet[x][y].isWall()
							&& JavaGame.world.wallMap.TileSet[x][y].isVisible()) { // no
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
						r2 = JavaGame.world.wallMap.TileSet[x][y].getBounds();

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
		return new Rectangle(x, y, width, height);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
