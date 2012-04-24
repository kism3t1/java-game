/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author LosOjos
 */
public class Tile {
    
    // initialize Tile fields
    private boolean destructible;
    private boolean wall;
    private boolean visible;
    
    private byte skin;
    private byte health;
    
    private Image iSkin;
    
    public Tile(){      //default constructor with default values
        skin = 0;
        health = 3;
        destructible = false;
        wall = false;
        visible = true;
    }
    
    public Tile(byte bSkin, byte bHealth, boolean isDestructible, boolean isWall, boolean isVisible){     //customisable contructor
        skin = bSkin;
        health = bHealth;
        destructible = isDestructible;
        wall = isWall;
        visible = isVisible;
    }
    
    
    public boolean isDestructible(){    //returns destructible field
        return destructible;
    }
    
    public void setDestructible(boolean isDestructible){
        destructible = isDestructible;
    }
    
    public boolean isWall(){    //returns wall field
        return wall;
    }
    
    public void setWall(boolean isWall){
        wall = isWall;
    }
    
    public boolean isVisible(){     //returns visible field
        return visible;
    }
    
    public void setVisible(boolean isVisible){
        visible = isVisible;
    }
    
    public byte getHealth(){
        return health;
    }
    
    public void setHealth(byte bHealth){
        health = bHealth;
    }
    
    public byte getSkin(){       //returns skin field
        return skin;
    }
    
    public void setSkin(byte bSkin){    //changes skin
        skin = bSkin;
    }
    
    public byte damage(byte bDamage){       //inflicts damage to tile
        if(destructible){
            health -= bDamage;
        }
        return health;
    }
}
