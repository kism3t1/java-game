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
	private int borderSkin;

	public World(String title, int width, int height, int borderSkin) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.borderSkin = borderSkin;

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

}
