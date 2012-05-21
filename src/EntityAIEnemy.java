import java.io.Serializable;
import java.util.Random;

//This is the AI class for Enemies to use

public class EntityAIEnemy extends Halja implements Serializable {
	

	private static final long serialVersionUID = -123998419983546979L;
	private int dx;
	private int dy;
	private int speed;
	private int enemyX;
	private int enemyY;
	private int entityX;
	private int entityY;
	private int distanceX;
	private int distanceY;
	private int id;
	private boolean attack;
	private int animState = ANIM_STILL;
	
	public EntityAIEnemy(int id){
		this.id = id;
	}
	
	//The random direction algorithm
	
	public void randomAIDirection(){	
		speed = 1;	//Declare speed of enemy/NPC
		int Direction = (int) (Math.random() * 5); // Either return 0,1,2,3 for Right,left,down,up
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
	//VERY MUCH WORK IN PROGRESS!!!!!!!
	
	public void checklocation(){
		enemyX = world.getEnemy(id).getX();
		enemyY = world.getEnemy(id).getY();
		entityX = world.ollie.getX();
		entityY = world.ollie.getY();
		distanceX = Math.abs(enemyX - entityX);	//Calculate distance away from entity
		distanceY = Math.abs(enemyY - entityY);
		
		/*
		 * Check if enemy is less than 200 tiles away from enity - if so speed up and attack
		 * 
		 * If enemy if less than 100 tiles away speed up more and go get him!!!
		 * 
		 * Maybe there is a better way of doing this? with ! or &&
		 */
		
	if (distanceX < 200 && distanceY < 200){ 	//If enemy is less that 200 tiles away from entity
		if (distanceX < 100 && distanceY < 100){ //If it is closer then speed up
			speed = 4;
			attack();
			//System.out.println("Closest!");
		}else{
		speed = 2;	//Speed up enemy 
		attack();		//Run attack Method
		//System.out.println("I see you!");
		}
	}else{
		attack = false;
		randomAIDirection();
	}
}
	
	//Attack algorithm
	
	//If enemy is < 200 tiles away from entity check locations
	
	public void attack(){
		attack = true;
			if (enemyX < entityX && enemyY < entityY){	//If enemy is to the left of entity - go right
				dx = speed;
				dy = 0;		
				animState = ANIM_WALK_RIGHT;
			}
			else if (enemyX > entityX && enemyY > entityY){	//If enemy is to the right of entity - go left
				dx = -speed;
				dy = 0;
				animState = ANIM_WALK_LEFT;
			}
			else if (enemyY < entityY){	//If enemy is above the entity - go down
				dx = 0; 
				dy = speed;
				animState = ANIM_WALK_DOWN;
			}
			else if (enemyY > entityY){	//If enemy is below the entity - go up
				dx = 0;
				dy = -speed;				
				animState = ANIM_WALK_UP;
			}
			else{
				randomAIDirection();
			}
	}
	
	//Random number generator
	public int returnRandom(){
		Random rn = new Random(); //New random
		int maximum = 5000;	//maximum value
		int minimum = 800;	//minimum value
		int range = maximum - minimum + 1;	//Calculate the range from min & max
		int randomNum =  rn.nextInt(range) + minimum;
		return randomNum;
	}
	
	public int returnx(){	//Returns dx value
		return dx;
	}
	
	public int returny(){	//Returns dy value
		return dy;
	}
	
	//Return boolean value if attacking or not
	public boolean returnAttack(){
		return attack;
	}
	
	public int returnAnimState(){
		return animState;
	}

}
