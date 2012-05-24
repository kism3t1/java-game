/*
 * Item Class
 * 
 * At the moments just set if item is throwable
 */

public class Item {
	
	boolean thrown = false;
	int amount;
	
	public Item(int amount, boolean thrown) {
		this.thrown = thrown;
		this.amount = amount;
	}
	
}
