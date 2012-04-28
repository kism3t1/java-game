//This is the AI class for Enemies & other bot entities to use

public class AI{
	
	private int dx;
	private int dy;
	private int speed;
	
	//The random direction algorithm
	
	public void randomAIDirection(){	
		speed = 1;	//Declare speed of enemy/bot
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
	
	public int returnx(){	//Returns dx value
		return dx;
	}
	
	public int returny(){	//Returns dy value
		return dy;
	}
	

}
