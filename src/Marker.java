import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;

public class Marker {
	private int dx;
	private int dy;
	private int firstTileX;
	private int firstTileY;
	private int lastTileX;
	private int lastTileY;
	private int screenX;
	private int screenY;
	private int level;
	private Color color;

	private long keyLastProcessed;

	public Marker() {
		level = 0;
		firstTileX = 1;
		firstTileY = 1;
		lastTileX = 1;
		lastTileY = 1;
		screenX = 0;
		screenY = 0;
		color = Color.GREEN;
	}

	public Marker(int tileX, int tileY, int level, Color color) {
		firstTileX = tileX;
		firstTileY = tileY;
		lastTileX = tileX;
		lastTileY = tileY;
		this.level = level;
		this.color = color;
	}

	public void move(boolean shiftKey) {
		if (System.currentTimeMillis() - keyLastProcessed > Halja.KEY_DELAY) {
			// check marker is within bounds of tileset
			if (firstTileX + dx >= 1 && lastTileX + dx <= Halja.world.getWidth() - 2) {

				// calculate scrolling offset
				if (lastTileX + dx >= Halja.screenTilesWide + Halja.xOffset
						|| (firstTileX + dx <= Halja.xOffset && Halja.xOffset > 0))
					Halja.xOffset += dx;

				if (!shiftKey)
					firstTileX += dx;
				lastTileX += dx;
			}

			// check marker is within bounds of tileset
			if (firstTileY + dy >= 1 && lastTileY + dy <= Halja.world.getHeight() - 2) {

				// calculate scrolling offset
				if (lastTileY + dy >= Halja.screenTilesHigh + Halja.yOffset
						|| (firstTileY + dy <= Halja.yOffset && Halja.yOffset > 0))
					Halja.yOffset += dy;

				if (!shiftKey)
					firstTileY += dy;
				lastTileY += dy;
			}

			keyLastProcessed = System.currentTimeMillis();
		}
	}

	public void selectRange(int startTileX, int startTileY, int lastTileX,
			int lastTileY) {
		this.firstTileX = startTileX;
		this.firstTileY = startTileY;
		this.lastTileX = lastTileX;
		this.lastTileY = lastTileY;
	}

	public void selectRange(Point startTile, Point endTile) {
		this.firstTileX = (int) startTile.getX();
		this.firstTileY = (int) startTile.getY();
		this.lastTileX = (int) endTile.getX();
		this.lastTileY = (int) endTile.getY();
	}

	public int getFirstTileX() {
		return firstTileX;
	}

	public void setFirstTileX(int tileX) {
		this.firstTileX = tileX;
	}

	public int getFirstTileY() {
		return firstTileY;
	}

	public void setFirstTileY(int tileY) {
		this.firstTileY = tileY;
	}

	public int getLastTileX() {
		return lastTileX;
	}

	public void setLastTileX(int lastTileX) {
		this.lastTileX = lastTileX;
	}

	public int getLastTileY() {
		return lastTileY;
	}

	public void setLastTileY(int lastTileY) {
		this.lastTileY = lastTileY;
	}

	public Point getFirstTile() {
		return new Point(firstTileX, firstTileY);
	}

	public Point getLastTile() {
		return new Point(lastTileX, lastTileY);
	}

	public void setSelectionStart(Point p) {
		firstTileX = (int) p.getX();
		firstTileY = (int) p.getY();
	}

	public void setSelectionStart(int tileX, int tileY) {
		firstTileX = tileX;
		firstTileY = tileY;
	}

	public void setSelectionEnd(Point p) {
		lastTileX = (int) p.getX();
		lastTileY = (int) p.getY();
	}

	public void setSelectionEnd(int tileX, int tileY) {
		lastTileX = tileX;
		lastTileY = tileY;
	}

	public int getScreenX() {
		calculateScreenPos();
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		calculateScreenPos();
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public int getLevel() {
		return level;
	}

	public Color getColor() {
		return color;
	}

	public void changeLevel(int level) {
		this.level = level;

		switch (level) {
		case Halja.LEVEL_FLOOR: // floor
			color = Color.GREEN;
			break;
		case Halja.LEVEL_WALL: // wall
			color = Color.RED;
			break;
		default: // default to floor if other value set
			this.level = Halja.LEVEL_FLOOR;
			color = Color.GREEN;
			break;
		}
	}

	private void calculateScreenPos() {
		if (firstTileX < lastTileX) {
			screenX = (firstTileX - Halja.xOffset) * 32;
		} else {
			screenX = (lastTileX - Halja.xOffset) * 32;
		}

		if (firstTileY < lastTileY) {
			screenY = (firstTileY - Halja.yOffset) * 32;
		} else {
			screenY = (lastTileY - Halja.yOffset) * 32;
		}
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_A:
			dx = -1;
			break;
		case KeyEvent.VK_D:
			dx = 1;
			break;
		case KeyEvent.VK_W:

			dy = -1;
			break;
		case KeyEvent.VK_S:
			dy = 1;
			break;
		}
	}

	public void keyReleased(KeyEvent e) {

		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_A:
			dx = 0;
			break;
		case KeyEvent.VK_D:
			dx = 0;
			break;
		case KeyEvent.VK_W:
			dy = 0;
			break;
		case KeyEvent.VK_S:
			dy = 0;
			break;
		}

		keyLastProcessed = System.currentTimeMillis() - Halja.KEY_DELAY;
	}
}
