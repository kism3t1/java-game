import java.awt.Rectangle;

public class CollisionDetection extends JavaGame{

	public int x, y, width, height;
	public int skin;

	public boolean checkCollisions(Rectangle r1, boolean checkEntity,
			boolean checkEnemy, boolean checkTile, int enemyID) { // Collision
																	// Detection
		boolean collision = false;
		Rectangle r2;

		// check collision with enemies
		if (checkEnemy) {
			for (int i = 0; i < world.enemy.size(); i++) {
				if (i != enemyID) {
					r2 = world.enemy.get(i).getBounds(); // Get bounds of enemy

					if (r1.intersects(r2)) { // Checks if entity collides with
												// an enemy
						collision = true;
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
