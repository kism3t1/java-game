import java.awt.Color;
import java.awt.event.KeyEvent;

public class Marker {
    private int dx;
    private int dy;
    private int x;
    private int y;
    private int width;
    private int height;
    private int level;
    private Color color;
    
    private long keyLastProcessed;

    public Marker() {
        width = 32;
        height = 32;
        level = 0;
        x = 0;
        y = 0;
        color = Color.GREEN;
    }
    
    public Marker(int tileX, int tileY, int width, int height, int level, Color color){
    	x = tileX;
    	y = tileY;
    	this.width = width;
    	this.height = height;
    	this.level = level;
    	this.color = color;
    }


    public void move() {
    	if (System.currentTimeMillis() - keyLastProcessed > 75){
    		x += dx;
    		y += dy;
    		keyLastProcessed= System.currentTimeMillis();
    	}
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
    
    public int getHeight() {
    	return height;
    }
    
    public int getLevel() {
    	return level;
    }
    
    public Color getColor(){
    	return color;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        
        switch(key) {
        case KeyEvent.VK_A: 
        	dx = -1 * width;
        	break;
        case KeyEvent.VK_D: 
        	dx = 1 * width;
        	break;
        case KeyEvent.VK_W: 
        	dy = -1 * height;
        	break;
        case KeyEvent.VK_S: 
        	dy = 1 * height;
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
