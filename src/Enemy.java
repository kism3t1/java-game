
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Enemy {

    private String entity = "Images/entity.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private int speed;
    private Image image;

    public Enemy() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(entity));
        image = ii.getImage();
        x = 40;
        y = 60;
        setSpeed(5);
    }
    
    public Enemy(int x, int y) {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(entity));
        image = ii.getImage();
        this.x = x;
        this.y = y;
    }


    public void move() {
        x += dx;
        y += dy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
    	return new Rectangle (x,y,22,32); //22, 32 size of tile
    }

}