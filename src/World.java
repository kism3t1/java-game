import java.io.Serializable;

@SuppressWarnings("serial")
public class World implements Serializable{

	public Map floorMap;
	public Map wallMap;
	
	private String title = "Default World";
	private int width;
	private int height;
	
	public World(String title, int width, int height){
		this.setTitle(title);
		this.setWidth(width);
		this.setHeight(height);
		
        floorMap = new Map(Level1.SCREEN_TILES_WIDE, Level1.SCREEN_TILES_HIGH, width, height, true, false);
        wallMap = new Map(Level1.SCREEN_TILES_WIDE, Level1.SCREEN_TILES_HIGH, width, height, false, true);
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
	
}
