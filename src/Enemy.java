
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Enemy {

    private String enemy = "Images/enemy.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private int speed;
    private long dLast;
    private Image image;

    public Enemy() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(enemy));
        image = ii.getImage();
        x = 80;
        y = 120;
        setSpeed(2);
        speed = 1;
        dLast = System.currentTimeMillis()-2000;
    }
    
    public Enemy(int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(enemy));
        image = ii.getImage();
        this.x = x;
        this.y = y;
        speed = 1;
        dLast = System.currentTimeMillis()-2000;
    }

    public void move() {
        x += dx;
        y += dy;
        if (System.currentTimeMillis()-dLast > 2000){
        randomDirection();
        dLast = System.currentTimeMillis();
        }
        /*if(checkCollisions()){
        	randomDirection();
        	dLast = System.currentTimeMillis()-2000;
        }
        */
		//tr.sleep(500);
        
    }
    
    public void randomDirection(){
	int Direction = (int) (Math.random() * 4);	//return 0,1,2,3
	switch(Direction){
	case 0: 
		dx = speed;	//RIGHT
		dy = 0;
	break;
	case 1:	//LEFT
		dx = -speed;
		dy = 0;
	break;
	case 2:	//DOWN
		dx = 0;
		dy = speed;
	break;
	case 3:	//UP
		dx = 0;
		dy = -speed;
	break;
	}
	//System.out.println("dx " + Direction);
	//System.out.println("dy " + Direction);
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
    	return new Rectangle (x, y, image.getWidth(null), image.getHeight(null)); 
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

}
