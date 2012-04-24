import java.awt.Color;
import java.awt.event.KeyEvent;

public class Marker {
    private int dx;
    private int dy;
    private int x;
    private int y;
    private int level;
    private Color color;
    
    private long keyLastProcessed;

    public Marker() {
        level = 0;
        x = 0;
        y = 0;
        color = Color.GREEN;
    }
    
    public Marker(int x, int y, int level, Color color){
    	this.x = x;
    	this.y = y;
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
    
    public void setX(int x){
    	this.x = x;
    }

    public int getY() {
        return y;
    }
    
    public void setY(int y){
    	this.y = y;
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
        	if(x>0)
        		dx = -1;
        	break;
        case KeyEvent.VK_D: 
        	dx = 1;
        	break;
        case KeyEvent.VK_W: 
        	if(y>0)
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
