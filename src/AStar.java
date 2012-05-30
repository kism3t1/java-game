import java.util.ArrayList;

//WIP: working from this article http://www.policyalmanac.org/games/aStarTutorial.htm

public class AStar {
	private static final int HV_COST = 10;		//cost of moving horizontally or vertically
	private static final int DIAG_COST = 14;	//cost of moving diagonally
	
	private ArrayList<Node> openList = new ArrayList<Node>();	//nodes yet to be checked
	private ArrayList<Node> closedList = new ArrayList<Node>();	//nodes already checked
	
	private class Node{
		private Node parent = null;	//parent node
		
		private int moveCost = 0;	//move cost from start to here, aka 'g'
		private int heuristic = 0;	//estimated cost to move from here to end
		private int totalCost = 0;	//total of above costs for scoring
		
		private int x = 0;	//x value in node map
		private int y = 0;	//y value in node map
	}
}
