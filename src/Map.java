/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 *
 * @author LosOjos
 */
public class Map {
    
    // initialize Map fields
    
    private int width;      //number of tiles wide
    private int height;     //number of tiles high
    
    Tile[][] TileSet;       //array to hold tiles and form level map
    
    public Map(int setWidth, int setHeight, boolean isVisible){        //Map constructor
        width = setWidth;
        height= setHeight;
        
        TileSet = new Tile[width][height];      //initialize TileSet to suit Map size
        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                TileSet[x][y] = new Tile();
                TileSet[x][y].setVisible(isVisible);
            }
        }
    }
    
    public int getWidth(){      //return Map width
        return width;
    }
    
    public int getHeight(){     //return Map height
        return height;
    }
    
    public void changeTile(int tileX, int tileY, byte bSkin, byte bHealth, boolean isDestructible, boolean isWall, boolean isVisible){
        TileSet[tileX][tileY].setSkin(bSkin);
        TileSet[tileX][tileY].setHealth(bHealth);
        TileSet[tileX][tileY].setDestructible(isDestructible);
        TileSet[tileX][tileY].setWall(isWall);
        TileSet[tileX][tileY].setVisible(isVisible);
    }
    
    public void draw(Graphics2D g, Image[] TileSkins, int xOffset, int yOffset, ImageObserver iObserver){
        for(int x=xOffset; x<width; x++){
            for(int y=yOffset; y<height; y++){
                if(TileSet[x][y].isVisible()){
                    g.drawImage(TileSkins[TileSet[x][y].getSkin()], x*32, y*32, iObserver);
                }
            }
        }
    }
}
