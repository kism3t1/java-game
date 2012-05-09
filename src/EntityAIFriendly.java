import java.io.Serializable;

//This is the AI class for Friendly entities/mobs

public class EntityAIFriendly extends JavaGame implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3502850127885723374L;
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
	
	public EntityAIFriendly(int id){
		this.id = id;
	}
	
	//The random direction algorithm
	
	public void randomAIDirection(){	
		speed = 1;	//Declare speed of enemy/NPC
		int Direction = (int) (Math.random() * 4); // Either return 0,1,2,3 for Right,left,down,up
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
		}
	}
	
	//Chase after entity algorithm
	
	public void checklocation(){
			enemyX = world.getEnemy(id).getX();
			enemyY = world.getEnemy(id).getY();
			entityX = world.entity.getX();
			entityY = world.entity.getY();
			distanceY = enemyY - entityY;
		if (distanceX <= 200 && distanceY <= 200){ 	//If enemy is less that 200 tiles away from entity
			speed = 2;	//Speed up enemy they get angry
			attack();		//Run attack Method
		}else{
			randomAIDirection();
		}
	}
	
	/*
	 * Attack algorithm
	 * 
	 * Not really needed for the friendly mobs but kept in to maybe 
	 * edit to follow player if fed or something like that
	 */
	
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
