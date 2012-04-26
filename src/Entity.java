
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Entity {

    private String entity = "Images/entity.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private int speed;
    private Image image;

    public Entity() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(entity));
        image = ii.getImage();
        x = 40;
        y = 60;
        speed = 5;;
    }
    
    public Entity(int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(entity));
        image = ii.getImage();
        this.x = x;
        this.y = y;
        speed = 5;
    }


    public void move() {
        x += dx;
        y += dy;
        
        if(checkCollisions()){
        	x -= dx;
        	y -= dy;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Image getImage() {
        return image;
    }
    
    public Rectangle getBounds(){	//Get bounds for collision detection
    	return new Rectangle (x,y, image.getWidth(null), image.getHeight(null)); //22, 32 size of tile
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -speed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = speed;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -speed;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = speed;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
    
    private boolean checkCollisions(){	//Collision Detection
    	boolean collision = false;
    	Rectangle r1 = getBounds();	//Get bounds of entity
    	Rectangle r2;
    	
    	//check collision with enemies
    	for(int i=0; i < Level1.enemy.size(); i++){
    		r2 = Level1.enemy.get(i).getBounds();	//Get bounds of enemy

    		if (r1.intersects(r2)){	//Checks if entity collides with an enemy
    			collision = true;
    		}
    	}
    	
    	//check for tile collision
    	for(int x = Level1.xOffset; x < Level1.SCREEN_TILES_WIDE + Level1.xOffset; x++){
    		for (int y = Level1.yOffset; y < Level1.SCREEN_TILES_HIGH + Level1.yOffset; y++){
    			if (Level1.world.wallMap.TileSet[x][y].isWall()
    					&& Level1.world.wallMap.TileSet[x][y].isVisible()){			//no need to check for collision if it isn't a wall
    				r2 = Level1.world.wallMap.TileSet[x][y].getBounds();

    				if (r1.intersects(r2)){		//Checks if entity collides with a tile
    					collision = true;
    				}
    			}
    		}
    	}
    	return collision;
    }
    
    /*public void checkCollisions(){
    	//Rectangle r1 = Level1.getBounds();
    	Rectangle r2 = getBounds();
    	//Rectangle r3 = en.getBounds();
    	
    	if (r2.intersects()){
    		System.out.println("COLLISION!");
    	}
    	}
    	*/
}