import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class JavaGame {
	
	public static final int FRAME_DELAY = 20;	// frame delay in milliseconds (i.e. 1000/20 = 50 FPS)
	public static final int KEY_DELAY = 75; 	// set delay in milliseconds between key strokes
	
	// tile level identifiers
	public static final int LEVEL_FLOOR = 0;
	public static final int LEVEL_WALL = 1;

	public static World world;
	public static BufferedImage[] tileSkins;
	public static BufferedImage[] enemySkins;
	public static BufferedImage[] entitySkins;

	public static Entity entity;
	public static ArrayList<Enemy> enemy = new ArrayList<Enemy>();

	// tile offset for scrolling
	public static int xOffset = 0;
	public static int yOffset = 0;
	public static int prevXOffset = 0;
	public static int prevYOffset = 0;

	// screen size info to aide scrolling
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static int screenTilesWide = 0;
	public static int screenTilesHigh = 0;
	public static int tileWidth = 32;
	public static int tileHeight = 32;

	public static void main(String[] args) {
	//	/*
		JFrame frame = new JFrame();
		Canvas gui = new Canvas();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui);
		frame.setSize(710, 730);
		frame.setTitle("Java-Game V0.1 - Map Editor");
		frame.setResizable(true);
		frame.setVisible(true); // start AWT painting.
		
		Thread gThread = new Thread(new StartScreen(gui));
		
		gThread.start();
		
		Boolean isMenu = true;
		Boolean isRunning = true;
		do{
			if(!gThread.isAlive()){
				if(isMenu){
					gThread = new Thread(new EditorLoop(gui));
				}else{
					gThread = new Thread(new StartScreen(gui));
				}
				isMenu = !isMenu;
				gThread.start();
			}
		}while(isRunning);
	}

}
