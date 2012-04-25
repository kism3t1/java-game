
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Enemy {

    private String entity = "Images/entity.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private Image image;
    

    public Enemy() {
        ImageIcon ii = new ImageIcon(this.getClass().getResource(entity));
        image = ii.getImage();
        //width = image.getWidth(null); //dont think needed
        //height = image.getHeight(null);
        x = 100;
        y = 150;
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

    public Image getImage() {
        return image;
    }
    
    public Rectangle getBounds(){	//Get bounds for collision detection
    	return new Rectangle (x,y,32,32); //32, 32 size of tile
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }

        if (key == KeyEvent.VK_UP) {
            dy = -1;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = 1;
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
    
}