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
        firstTileX = 0;
        firstTileY = 0;
        screenX = 0;
        screenY = 0;
        color = Color.GREEN;
    }
    
    public Marker(int tileX, int tileY, int level, Color color){
    	firstTileX = tileX;
    	firstTileY = tileY;
    	this.level = level;
    	this.color = color;
    }


    public void move() {
    	if (System.currentTimeMillis() - keyLastProcessed > Level1.KEY_DELAY){
    		//check marker is within bounds of tileset
    		if (firstTileX + dx >= 0 && firstTileX + dx < Level1.MAP_TILES_WIDE){			
    			
    			//calculate scrolling offset
    			if (firstTileX + dx >= Level1.SCREEN_TILES_WIDE + Level1.xOffset || 
    					(firstTileX + dx < Level1.xOffset && Level1.xOffset > 0))
    				Level1.xOffset += dx;
    			
    			firstTileX += dx;
    		}
    		
    		//check marker is within bounds of tileset
    		if (firstTileY + dy >= 0 && firstTileY + dy < Level1.MAP_TILES_HIGH){
    	
    			//calculate scrolling offset
    			if (firstTileY + dy >= Level1.SCREEN_TILES_HIGH + Level1.yOffset || 
    					(firstTileY + dy < Level1.yOffset && Level1.yOffset > 0))
    				Level1.yOffset += dy;
    			
    			firstTileY += dy;
    		}
    			
    		calculateScreenPos();
    		
    		keyLastProcessed= System.currentTimeMillis();
    	}
    }
    
    public void selectRange(int startTileX, int startTileY, int lastTileX, int lastTileY){
    	this.firstTileX = startTileX;
    	this.firstTileY = startTileY;
    }

    public int getFirstTileX() {
        return firstTileX;
    }
    
    public void setFirstTileX(int tileX){
    	this.firstTileX = tileX;
    }

    public int getFirstTileY() {
        return firstTileY;
    }
    
    public void setFirstTileY(int tileY){
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
	
	public void setSelectionStart(Point p){
		firstTileX = (int)p.getX();
		firstTileY = (int)p.getY();
		calculateScreenPos();
	}
	
	public void setSelectionEnd(Point p){
		lastTileX = (int)p.getX();
		lastTileY = (int)p.getY();
	}

	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public int getLevel() {
    	return level;
    }
    
    public Color getColor(){
    	return color;
    }
    
    public void changeLevel(int level){
    	this.level = level;
    	
    	switch(level){
    	case 0:								//floor
    		color = Color.GREEN;
    		break;
    	case 1:								//wall
    		color = Color.RED;
    		break;
    	default:							//default to floor if other value set
    		this.level = 0;
    		color = Color.GREEN;
    		break;
    	}
    }
    
    private void calculateScreenPos(){
    	//calculate screen position of marker    		
		screenX = (firstTileX - Level1.xOffset) * 32;
		screenY = (firstTileY - Level1.yOffset) * 32;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        
        switch(key) {
        case KeyEvent.VK_A: 
        	if(firstTileX>0)
        		dx = -1;
        	break;
        case KeyEvent.VK_D: 
        	dx = 1;
        	break;
        case KeyEvent.VK_W: 
        	if(firstTileY>0)
        		dy = -1;
        	break;
        case KeyEvent.VK_S: 
        	dy = 1;
        	break;
        }
    }

    public void keyReleased(KeyEvent e) {
    	
        int key = e.getKeyCode();

        switch(key) {
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
    }
}
