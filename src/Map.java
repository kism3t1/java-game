import java.awt.Graphics;
import java.io.Serializable;


public class Map extends Halja implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 570366463465046839L;
	// initialize Map fields

	private int width; // number of tiles wide
	private int height; // number of tiles high

	Tile[][] TileSet; // array to hold tiles and form level map

	public Map(int width, int height,
			boolean populate, int borderSkin) { // Map constructor
		this.width = width;
		this.height = height;

		TileSet = new Tile[width][height]; // initialize TileSet to suit Map
											// size
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				/*
				TileSet[x][y] = new Tile();
				TileSet[x][y].setVisible(isVisible);
				
				if(x == 0 || y == 0 || x == width-1 || y == height-1){
					TileSet[x][y].setSkin(borderSkin);
					TileSet[x][y].setVisible(true);
					TileSet[x][y].setDestructible(false);
				}
				*/
				
				if(populate)
					TileSet[x][y] = new Tile();
				
				if(x == 0 || y == 0 || x == width-1 || y == height-1){
					if(TileSet[x][y] == null)
						TileSet[x][y] = new Tile();
					
					TileSet[x][y].setSkin(borderSkin);
					TileSet[x][y].setDestructible(false);
				}
			}
		}
	}

	public int getWidth() { // return Map width
		return width;
	}

	public int getHeight() { // return Map height
		return height;
	}

	public void changeTile(int tileX, int tileY, byte bSkin, byte bHealth,
			boolean isDestructible, boolean isVisible) {
		
		if(TileSet[tileX][tileY] == null)
			TileSet[tileX][tileY] = new Tile();
		
		TileSet[tileX][tileY].setSkin(bSkin);
		TileSet[tileX][tileY].setHealth(bHealth);
		TileSet[tileX][tileY].setDestructible(isDestructible);
	}
	
	public void changeBorder(int borderSkin)
	{
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(x == 0 || y == 0 || x == width - 1 || y == height - 1){
					TileSet[x][y].setSkin(borderSkin);
					TileSet[x][y].setDestructible(false);
				}
			}
		}
	}
	
	public void draw(Graphics g, int cameraX, int cameraY)
	{
		int mapX = cameraX / tileWidth;
		int mapY = cameraY / tileHeight;
		
		int mapXoff = cameraX % tileWidth;
		int mapYoff = cameraY % tileHeight;
		
		for(int x = 0; x <= screenTilesWide+1; x++){
			for(int y = 0; y <= screenTilesHigh+1; y++){
				if(x + mapX >= 0 && x + mapX < TileSet.length && y + mapY >= 0 && y + mapY < TileSet[0].length)
				{
					if(TileSet[x + mapX][y + mapY] != null){
						TileSet[x + mapX][y + mapY].setPos((x * tileWidth) - mapXoff, (y * tileHeight) - mapYoff);
						g.drawImage(tileSkins[gameTime.checkDateTime()][TileSet[x + mapX][y + mapY].getSkin()], TileSet[x + mapX][y + mapY].getX(), TileSet[x + mapX][y + mapY].getY(), null);
					}
				}
			}
		}
	}

}
