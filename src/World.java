import java.io.Serializable;


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

	public World(String title, int width, int height) {
		this.setTitle(title);
		this.setWidth(width);
		this.setHeight(height);

		floorMap = new Map(width, height, true, false);
		wallMap = new Map(width, height, false, true);
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
