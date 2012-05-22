import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class InventoryMenu extends Halja implements Runnable{
	
	//constant ID's for menues
	private static final int MENU_LEFT = 0;
	private static final int MENU_RIGHT = 1;
	
	private static final int SUBMENU_WEAPONS = 0;
	private static final int SUBMENU_ARMOUR = 1;
	private static final int SUBMENU_POTIONS = 2;
	private static final int SUBMENU_ITEMS = 3;
	
	//menu properties
	private static final Color BG_COLOR = new Color(0, 0, 50); 
	private static final Color SELECT_COLOR = new Color(0,0,200);
	private static final int BUTTON_HEIGHT = 50;
	
	private Canvas gui = null;
	private long cycleTime;
	private Rectangle rectBG = null;
	private boolean isRunning;
	private KeyListener kl = null;
	
	private Menu[] menu;
	private SubMenu[] subMenu;
	
	private int currentMenu = MENU_LEFT;
	private int currentMenuItem = 0;
	private int currentMenuCount;
	
	public InventoryMenu(Canvas gui){
		this.gui = gui;
		isRunning = true;
		
		kl = new TAdapter();
		gui.addKeyListener(kl);
		
		rectBG = new Rectangle(gui.getWidth(), gui.getHeight());
		
		menu = new Menu[2];
		menu[MENU_LEFT] = new Menu(0, 0, (int) (rectBG.getWidth() * 0.25f), (int)rectBG.getHeight());
		menu[MENU_RIGHT] = new Menu(menu[MENU_LEFT].getWidth(), 0, (int)rectBG.getWidth() - menu[MENU_LEFT].getWidth(), (int)rectBG.getHeight());
		
		subMenu = new SubMenu[4];
		subMenu[SUBMENU_WEAPONS] = new SubMenu("Weapons");
		subMenu[SUBMENU_ARMOUR] = new SubMenu("Armour");
		subMenu[SUBMENU_POTIONS] = new SubMenu("Potions");
		subMenu[SUBMENU_ITEMS] = new SubMenu("Items");
		
		for(int i = 0; i < weaponMenuText.length; i++){
			subMenu[SUBMENU_WEAPONS].item.add(new MenuItem(weaponMenuText[i], "Stats go here", weaponSkin[TOD_DAYTIME][i]));
		}
		
		subMenu[SUBMENU_ARMOUR].item.add(new MenuItem("Armour","Dummy Item",null));
		
		subMenu[SUBMENU_POTIONS].item.add(new MenuItem("Potion","Dummy Item",null));
		
		subMenu[SUBMENU_ITEMS].item.add(new MenuItem("Item","Dummy Item",null));
		
		//sort subMenu
		for(int i=0; i<subMenu.length; i++){
			subMenu[i].setX(menu[MENU_LEFT].getX());
			subMenu[i].setY(menu[MENU_LEFT].getY() + (i * BUTTON_HEIGHT));
			subMenu[i].setWidth(menu[MENU_LEFT].getWidth());
			subMenu[i].setHeight(BUTTON_HEIGHT);
			subMenu[i].sort();
		}
		
		//activate weapon menu on start
		subMenu[SUBMENU_WEAPONS].setActive(true);
		
		//initialise placeholder variable
		currentMenuCount = subMenu.length - 1;
		currentMenuItem = SUBMENU_WEAPONS;
	}

	@Override
	public void run() {
		cycleTime = System.currentTimeMillis();
		BufferStrategy strategy = gui.getBufferStrategy();
		
		do{
			updateGameState();			//calculates any necessary changes to game play objects

			updateGUI(strategy);		//redraws GUI

			syncFPS();					//tries to keep game at target FPS
		}while(isRunning);
				
		gui.removeKeyListener(kl);
		latch.countDown();
	}

	private void updateGameState() {
		if(currentMenu == MENU_LEFT){
			for(int i = 0; i < subMenu.length; i++){
				if(currentMenuItem == i){
					subMenu[i].setActive(true);
				}else{
					subMenu[i].setActive(false);
				}
			}
			currentMenuCount = subMenu.length - 1;
		}
		
		if(currentMenu == MENU_RIGHT){
			for(int i=0; i < subMenu.length; i++){
				if(subMenu[i].isActive()){
					subMenu[i].update();
					currentMenuCount = subMenu[i].item.size() - 1;
				}
			}
		}
	}

	private void updateGUI(BufferStrategy strategy){
		Graphics g = strategy.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, gui.getWidth(), gui.getHeight());
		
		//draw menu outlines
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, (int)rectBG.getWidth(), (int)rectBG.getHeight());
		for(int i =0; i < menu.length; i++)
			menu[i].draw(g);
		
		//draw sub menues
		for(int i=0; i < subMenu.length; i++)
			subMenu[i].draw(g);
		
		g.dispose();
		strategy.show();	
	}
	
	private void syncFPS() {
		cycleTime = cycleTime + FRAME_DELAY;
		long difference = cycleTime - System.currentTimeMillis();
		try {
			Thread.sleep(Math.max(0, difference));
		}catch(InterruptedException e){
			e.printStackTrace();
		}		
	}
	
	private class TAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e){
			int key = e.getKeyCode();
			switch(key){
			case KeyEvent.VK_ESCAPE:
				isRunning = false;
				break;
			case KeyEvent.VK_UP:
				if(currentMenuItem > 0){
					currentMenuItem -= 1;
				}else{
					currentMenuItem = currentMenuCount;
				}
				break;
			case KeyEvent.VK_DOWN:
				if(currentMenuItem < currentMenuCount){
					currentMenuItem += 1;
				}else{
					currentMenuItem = 0;
				}
				break;
			case KeyEvent.VK_LEFT:
				currentMenu = MENU_LEFT;
				break;
			case KeyEvent.VK_RIGHT:
				currentMenu = MENU_RIGHT;
				break;
			}
		}
	}
	
	private class Menu{
		private int width;
		private int height;
		private int x;
		private int y;
		
		public Menu(int x, int y, int width, int height){
			this.setX(x);
			this.setY(y);
			this.setWidth(width);
			this.setHeight(height);
		}
		
		public void draw(Graphics g){
			g.draw3DRect(x, y, width, height, true);
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
	}
	
	private class SubMenu{
		private String title = null;
		private int x, y, width, height;
		private ArrayList<MenuItem> item = new ArrayList<MenuItem>();
		private boolean active = false;
		
		public SubMenu(String title){
			this.setTitle(title);
		}

		public SubMenu(String title, int x, int y, int width, int height){
			this.title = title;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public void draw(Graphics g){
			if(active && currentMenu == MENU_LEFT){
				g.setColor(SELECT_COLOR);
			}else{
				g.setColor(BG_COLOR);
			}
			g.fill3DRect(x, y, width, height, true);
			
			g.setColor(Color.WHITE);
			g.drawString(title, x + 2, y + (BUTTON_HEIGHT / 2));
			
			if(active){
				for(int i=0; i<item.size(); i++){
					item.get(i).draw(g);
				}
			}
		}
		
		public void sort() {
			for(int i = 0; i < item.size(); i++){
				item.get(i).setX(menu[MENU_RIGHT].getX());
				item.get(i).setY(menu[MENU_RIGHT].getY() + (i * BUTTON_HEIGHT));
				item.get(i).setWidth(menu[MENU_RIGHT].getWidth());
				item.get(i).setHeight(BUTTON_HEIGHT);
			}
		}
		
		public void update() {
			for(int i = 0; i < item.size(); i++){
				if(currentMenuItem == i){
					item.get(i).setActive(true);
				}else{
					item.get(i).setActive(false);
				}
			}
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
			if(item.size() > 0){
				item.get(0).setActive(true);
				
				if(item.size()>1){
					for(int i=1; i<item.size(); i++)
						item.get(i).setActive(false);
				}
			}
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}
		
	}
	
	private class MenuItem{
		private String title, description;
		private BufferedImage icon;
		private int x, y, width, height;
		private boolean active = false;
		
		public MenuItem(String title, String description, BufferedImage icon){
			this.title = title;
			this.description = description;
			this.icon = icon;
		}

		public void draw(Graphics g) {
			if(active && currentMenu == MENU_RIGHT){
				g.setColor(SELECT_COLOR);
			}else{
				g.setColor(BG_COLOR);
			}
			g.fill3DRect(x, y, width, height, true);
			if(icon != null)
				g.drawImage(icon, x+2, y+2, null);
			
			g.setColor(Color.WHITE);
			g.drawString(title, x + 35, y + (BUTTON_HEIGHT / 2) - 10);
			g.drawString(description, x + 35, y + (BUTTON_HEIGHT / 2) + 10);
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}
	}
}
