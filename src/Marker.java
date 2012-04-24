import java.awt.Color;
import java.awt.event.KeyEvent;

public class Marker {
    private int dx;
    private int dy;
    private int tileX;
    private int tileY;
    private int screenX;
    private int screenY;
    private int level;
    private Color color;
    
    private long keyLastProcessed;

    public Marker() {
        level = 0;
        tileX = 0;
        tileY = 0;
        screenX = 0;
        screenY = 0;
        color = Color.GREEN;
    }
    
    public Marker(int tileX, int tileY, int level, Color color){
    	this.tileX = tileX;
    	this.tileY = tileY;
    	this.level = level;
    	this.color = color;
    }


    public void move() {
    	if (System.currentTimeMillis() - keyLastProcessed > 75){
    		if (tileX + dx >= 0 && tileX + dx < Level1.MAP_TILES_WIDE)
    			tileX += dx;
    		
    		if (tileY + dy >= 0 && tileY + dy < Level1.MAP_TILES_HIGH)
    			tileY += dy;
    		
    		if(tileX * 32 < Level1.SCREEN_TILES_WIDE * 32)
    			screenX = tileX * 32;
    		
    		if(tileY * 32 < Level1.SCREEN_TILES_HIGH * 32)
    			screenY = tileY * 32;
    		
    		keyLastProcessed= System.currentTimeMillis();
    	}
    }

    public int getTileX() {
        return tileX;
    }
    
    public void setTileX(int tileX){
    	this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }
    
    public void setTileY(int tileY){
    	this.tileY = tileY;
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

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        
        switch(key) {
        case KeyEvent.VK_A: 
        	if(tileX>0)
        		dx = -1;
        	break;
        case KeyEvent.VK_D: 
        	dx = 1;
        	break;
        case KeyEvent.VK_W: 
        	if(tileY>0)
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
        
        keyLastProcessed= System.currentTimeMillis();
    }
}
