import java.io.Serializable;
import java.util.Random;

/*This is the AI class for Friendly entities/mobs
 * 
 * The friendly will speed up and run away from the player
 */

public class EntityAIFriendly extends Halja implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3502850127885723374L;
	private int dx;
	private int dy;
	private int speed;
	private int friendlyX;
	private int friendlyY;
	private int entityX;
	private int entityY;
	private int distanceX;
	private int distanceY;
	private int id;
	private boolean runaway;
	private int animState = ANIM_STILL;
	
	public EntityAIFriendly(int id){
		this.id = id;
	}
	
	//The random direction algorithm
	
	public void randomAIDirection(){	
		speed = 1;	//Declare speed of enemy/NPC
		int Direction = (int) (Math.random() * 5); // Either return 0,1,2,3,4 for Right,left,down,up & stop
		switch (Direction) {
		case 0:	//RIGHT
			dx = speed; 
			dy = 0;
			animState = ANIM_WALK_RIGHT;
			break;
		case 1: // LEFT
			dx = -speed;
			dy = 0;
			animState = ANIM_WALK_LEFT;
			break;
		case 2: // DOWN
			dx = 0;
			dy = speed;
			animState = ANIM_WALK_DOWN;
			break;
		case 3: // UP
			dx = 0;
			dy = -speed;
			animState = ANIM_WALK_UP;
			break;
		case 4: //STOP
			dx = 0;
			dy = 0;
			animState = ANIM_STILL;
		}
	}
	
	//Chase after entity algorithm
	
	public void checklocation(){
			friendlyX = world.getFriendly(id).getX();
			friendlyY = world.getFriendly(id).getY();
			entityX = world.ollie.getX();
			entityY = world.ollie.getY();
			distanceX = Math.abs(friendlyX - entityX);	//Calculate distance away from entity
			distanceY = Math.abs(friendlyY - entityY);
		if (distanceX <= 80 && distanceY <= 80){ 	//If enemy is less that 80 tiles away from entity
			speed =2;	//Speed up friendly as they are very shy
			runAway();		//Run away Method
		}else{
			randomAIDirection();
		}
	}
	
	/*
	 * Attack algorithm
	 * 
	 * NOTE: May edit to follow player if fed or something like that
	 * At the moment instead of attacking they will run away...
	 */
	
	//If enemy is < 100 tiles away from entity check locations
	
	public void runAway(){
		runaway = true;
			if (friendlyX < entityX && friendlyY < entityY){	//If friendly is to the left of entity - go left
				dx = -speed;
				dy = 0;		
				animState = ANIM_WALK_LEFT;
			}
			else if (friendlyX > entityX && friendlyY > entityY){	//If friendly is to the right of entity - go right
				dx = speed;
				dy = 0;
				animState = ANIM_WALK_RIGHT;
			}
			else if (friendlyY < entityY){	//If friendly is above the entity - go up
				dx = 0; 
				dy = -speed;
				animState = ANIM_WALK_UP;
			}
			else if (friendlyY > entityY){	//If friendly is below the entity - go down
				dx = 0;
				dy = speed;				
				animState = ANIM_WALK_DOWN;
			}
			else{
				runaway = false;
				randomAIDirection();
			}
	}
	
	public int returnx(){	//Returns dx value
		return dx;
	}
	
	public int returny(){	//Returns dy value
		return dy;
	}
	
	public int retutnAnimState(){
		return animState;
	}
	
	public int returnRandom(){
		Random rn = new Random(); //New random
		int maximum = 5000;	//maximum value
		int minimum = 800;	//minimum value
		int range = maximum - minimum + 1;	//Calculate the range from min & max
		int randomNum =  rn.nextInt(range) + minimum;
		return randomNum;
	}
	
	public boolean returnRunAway(){
		return runaway;
	}

}
