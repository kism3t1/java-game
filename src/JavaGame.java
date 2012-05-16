import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;


public class JavaGame {
	
	public static transient final int FRAME_DELAY = 20;	// frame delay in milliseconds (i.e. 1000/20 = 50 FPS)
	public static transient final int KEY_DELAY = 75; 	// set delay in milliseconds between key strokes
	
	public static transient String nextThread;
	
	// tile level identifiers
	public static transient final int LEVEL_FLOOR = 0;
	public static transient final int LEVEL_WALL = 1;
	
	//animation state identifiers
	public static transient final int STATE_NORMAL = 0;
	public static transient final int STATE_INJURED = 1;
	public static transient final int ANIM_STILL = 1;
	public static transient final int ANIM_WALK_LEFT = 2;
	public static transient final int ANIM_WALK_RIGHT = 3;
	public static transient final int ANIM_WALK_UP = 4;
	public static transient final int ANIM_WALK_DOWN = 5;
	
	//Time of Day identifiers
	public static final transient int TOD_DAYTIME = 0;
	public static final transient int TOD_SUNRISE = 1;
	public static final transient int TOD_SUNSET = 2;
	public static final transient int TOD_NIGHT = 3;
	
	//Time of day color casts
	public static final transient Color TOD_NIGHT_COLOR = new Color(0, 0, 50, 150);
	public static final transient Color TOD_SUNRISE_COLOR = new Color(255, 150, 0, 25);
	public static final transient Color TOD_SUNSET_COLOR = new Color(0, 0, 50, 75);
	
	//HUD icon ID's
	public static transient final int HUD_HEART = 0;
	public static transient final int HUD_SWORD = 1;

	public static transient World world;
	public static transient GameTime gameTime;
	public static transient SkyFade fadeSky;
	public static transient BufferedImage[][] tileSkins;
	
	public static transient BufferedImage[] skySkins;
	public static transient BufferedImage[] skyTransparency;
	
	public static transient BufferedImage[] HUDIcons;
	
	public static transient Animation[][][] enemySkins;
	public static transient Animation[][] entitySkins;
	public static transient BufferedImage[][] entityFriendlySkins;
	
	public static transient CollisionDetection collisionDetection = new CollisionDetection();

	// tile offset for scrolling
	public static transient int xOffset = 0;
	public static transient int yOffset = 0;
	public static transient int prevXOffset = 0;
	public static transient int prevYOffset = 0;

	// screen size info to aide scrolling
	public static transient int screenWidth = 0;
	public static transient int screenHeight = 0;
	public static transient int screenTilesWide = 0;
	public static transient int screenTilesHigh = 0;
	public static transient int tileWidth = 32;
	public static transient int tileHeight = 32;
	public static transient int guiWidth = 0;
	public static transient int guiHeight = 0;

	public static void main(String[] args) {
	//	/*
		
		if(System.getProperty("os.name").startsWith("Win"))
            System.setProperty("sun.java2d.d3d","true");
		else
            System.setProperty("sun.java2d.opengl", "true");
		
		System.setProperty("sun.java2d.translaccel", "true");
		
		JFrame frame = new JFrame();
		Canvas gui = new Canvas();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gui);
		
		frame.setTitle("Java-Game V0.1");
		frame.setVisible(true); // start AWT painting.
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setMinimumSize(frame.getSize());			//prevents window shrinking when moved
		frame.setResizable(true);
		
		//load resources in to memory
		Thread gThread = new Thread(new LoadResources());
		frame.setTitle("Loading");
		gThread.start();
		do{}while(gThread.isAlive());	//wait until resources are loaded to continue
		
		frame.setTitle("Java-Game V0.1");
		gThread = new Thread(new StartScreen(gui));
		
		fadeSky = new SkyFade();
		
		/* GameTime Section */
		/* new GameTime set to 400 so that the game starts just as the sun rises, while testing */
		gameTime = new GameTime(450);
		Thread pThread = new Thread(new GameTimeUpdater(gameTime));		
		
		
		gThread.start();
		
		
		Boolean isRunning = true;
		do{
			if(!gThread.isAlive()){
				String[] command = nextThread.split("[:]");
				switch(command[0]){
				case "MENU":
					gThread = new Thread(new StartScreen(gui));
					break;
				case "EDIT":
					gThread = new Thread(new EditorLoop(gui));
					
					break;
				case "GAME":
					gThread = new Thread(new GameLoop(gui));
					pThread = new Thread(new GameTimeUpdater(gameTime)); // Start the Game Time when game selected
					pThread.start();	//Starts the game Time thread
					break;
				}
				gThread.start();
			}
		}while(isRunning);
	}

}
