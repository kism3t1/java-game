import java.io.Serializable;

/*This is the AI class for Enemies to use at the moment.
 * Will be the base AI class that every entity will use before there own specialist AI
 */

public class AIBase extends Halja implements Serializable {
	

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
	
	public AIBase(int id){
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
			break;
		case 1: // LEFT
			dx = -speed;
			dy = 0;
			break;
		case 2: // DOWN
			dx = 0;
			dy = speed;
			break;
		case 3: // UP
			dx = 0;
			dy = -speed;
			break;
		case 4: //STOP
			dx = 0;
			dy = 0;
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
		if (distanceX <= 200 && distanceY <= 200){ 	//If enemy is less that 200 tiles away from entity
			speed = 2;	//Speed up enemy they get angry
			attack();		//Run attack Method
		}else{
			randomAIDirection();
		}
	}
	
	//Attack algorithm
	
	//If enemy is < 200 tiles away from entity check locations
	
	public void attack(){
			if (enemyX < entityX && enemyY < entityY){	//If enemy is to the left of entity - go right
				dx = speed;
				dy = 0;		
			}
			else if (enemyX > entityX && enemyY > entityY){	//If enemy is to the right of entity - go left
				dx = -speed;
				dy = 0;
			}
			else if (enemyY < entityY){	//If enemy is above the entity - go down
				dx = 0; 
				dy = speed;
			}
			else if (enemyY > entityY){	//If enemy is below the entity - go up
				dx = 0;
				dy = -speed;				
			}
			else{
				randomAIDirection();
			}
	}
	
	public int returnx(){	//Returns dx value
		return dx;
	}
	
	public int returny(){	//Returns dy value
		return dy;
	}

}
