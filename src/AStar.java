import java.awt.Point;
import java.util.ArrayList;

//WIP: working from this article http://www.policyalmanac.org/games/aStarTutorial.htm

public class AStar extends Halja{
	
	/**
	 * Result returned by FindPath()
	 * 
	 * @param	FOUND		a path was found and can be retrieved via GetPath()
	 * @param	IMPOSSIBLE	no path is possible between the start and end point
	 * @param	MATCH		the start and end positions match; no path necessary
	 * @see		AStar#FindPath
	 * @author LosOjos
	 *
	 */
	public enum ReturnCode{
		FOUND,
		IMPOSSIBLE,
		MATCH
	}
	
	private static final int HV_COST = 10;		//cost of moving horizontally or vertically
	private static final int DIAG_COST = 1000;	//cost of moving diagonally (14 is norm, 1000 to prevent diagonal movement)
	
	private ArrayList<Node> openList = new ArrayList<Node>();	//nodes yet to be checked
	private ArrayList<Node> closedList = new ArrayList<Node>();	//nodes already checked
	
	private int onClosedList = -1;
	private int onOpenList = -1;
	
	private Node startNode = null;
	private Node targetNode = null;
	
	private Node curNode = null;
	
	private Point[] path;
	
	public boolean debug = true;
	
	public AStar(){}
	
	
	/**
	 * Calculates the path (if any) from startPos to targetPos.
	 * 
	 * @param	startPos	a Point containing the X & Y coordinates of the start position of the desired path
	 * @param	targetPos	a Point containing the X & Y coordinates of the target position on the desired path
	 * @return				an AStar.ReturnCode depending on the outcome of the search
	 * @see					ReturnCode
	 * @see					#GetPath
	 * @author LosOjos
	 */
	public ReturnCode FindPath(Point startPos, Point targetPos){
		//check whether path needs to be calculated
		if(startPos.x == targetPos.x && startPos.y == targetPos.y)	//already on target, no calculation neccessary
			return ReturnCode.MATCH;
		if(world.wallMap.TileSet[targetPos.x][targetPos.y] != null)	//target is a wall
			return ReturnCode.IMPOSSIBLE;
		
		//clear previous path info
		openList.clear();
		closedList.clear();
		openList.trimToSize();
		closedList.trimToSize();
		
		startNode = new Node(null, startPos.x, startPos.y);
		targetNode = new Node(null, targetPos.x, targetPos.y);
		
		openList.add(startNode);
		
		do{
			//find node with lowest cost on open list and check it next
			int lowestFCost = openList.get(0).getTotalCost();
			int lowestFIndex = 0;
			for(Node n : openList){
				if(n.getTotalCost() <= lowestFCost){
					lowestFCost = n.getTotalCost();
					lowestFIndex = openList.indexOf(n);
				}
			}
			curNode = openList.get(lowestFIndex);
			
			//drop current node from open list and add it to closed list
			openList.remove(curNode);
			closedList.add(curNode);
			
			//check 1 node radius for accessibility and add to open list if walkable
			for(int x = -1; x <= 1; x++){
				for(int y = -1; y <= 1; y++){
					//if(curNode.x + x > 0 && curNode.y + y > 0
					//		&& curNode.x + x < world.floorMap.getWidth()
					//		&& curNode.y + y < world.floorMap.getHeight()){	//don't check out of bounds
						if(world.wallMap.TileSet[curNode.x + x][curNode.y + y] == null){	//if no obstacle present and not already on closed list
							
							Node node = new Node(curNode, curNode.x + x, curNode.y + y);	//new node from parent
							
							//reset list checks
							onOpenList = -1;
							onClosedList = -1;
							
							//check if node is already on closed list
							onClosedList = isOnClosedList(node);
														
							//check if node is already on open list
							onOpenList = isOnOpenList(node);

							if(onClosedList == -1){	//if node isn't already on closed list
								if(onOpenList > -1){	//if node is already on the open list
									
									node = openList.get(onOpenList);	//get existing instance of node
									int newCost = 0;
									
									if(Math.abs(node.getX() - curNode.getX()) == 0 
											|| Math.abs(node.getY() - curNode.getY()) == 0){	//horizontal or vertical move
										newCost = curNode.getMoveCost() + HV_COST;
									}else{	//diagonal move
										newCost = curNode.getMoveCost() + DIAG_COST;
									}
									
									if(newCost < node.getMoveCost()){	//if it costs less to get to node from curNode, set curNode as node's parent
										node.setParent(curNode);
										node.setMoveCost(newCost);
									}
									
								}else{	//node not on open list, calculate values and add it
									//calculate move/g cost
									if(x == 0 || y == 0){	//horizontal or vertical move
										node.setMoveCost(node.parent.getMoveCost() + HV_COST);
									}else{	//diagonal move
										node.setMoveCost(node.parent.getMoveCost() + DIAG_COST);
									}

									//calculate heuristic/h using Manhattan method
									node.setHeuristic((Math.abs(targetNode.x - node.x) + Math.abs(targetNode.y - node.y)) * HV_COST);

									//add node to open list for checking later
									openList.add(node);
								}
							}else{

								//check if node is target i.e. path found
								if(node.x == targetNode.x && node.y == targetNode.y){
									targetNode.setParent(curNode);
									closedList.trimToSize();
									BuildPath();
									return ReturnCode.FOUND;
								}
								
						}
					}
				}
			}
		}while(!openList.isEmpty());
		
		return ReturnCode.IMPOSSIBLE;
	}
	
	private int isOnClosedList(Node node){
		for(Node n : closedList){
			if(n.getX() == node.getX() && n.getY() == node.getY()){
				return closedList.indexOf(n);
			}
		}
		return -1;
	}

	private int isOnOpenList(Node node){
		for(Node n : openList){
			if(n.getX() == node.getX() && n.getY() == node.getY()){
				return openList.indexOf(n);
			}
		}
		return -1;
	}
	
	private void BuildPath(){
		ArrayList<Point> tempPath = new ArrayList<Point>();
		ArrayList<Point> reversePath = new ArrayList<Point>();
		Node parentNode = targetNode;
		
		tempPath.add(new Point(targetNode.getX(), targetNode.getY()));
		
		do{
			tempPath.add(new Point(parentNode.getX(), parentNode.getY()));
			parentNode = parentNode.getParent();
		}while(parentNode.hasParent());
		
		for(int i = tempPath.size() - 1; i >=0; i--){
			reversePath.add(tempPath.get(i));
		}
		
		path = new Point[reversePath.size()];
		reversePath.trimToSize();
		reversePath.toArray(path);
	}
	/**
	 * Returns the path calculated by FindPath()
	 * 
	 * @return	an array of Points symbolising the calculated path
	 * @author LosOjos
	 * @see		#FindPath
	 */
	public Point[] GetPath(){
		return path;
	}
	
	private class Node{

		private Node parent = null;	//parent node
		
		private int moveCost = 0;	//move cost from start to here, aka 'g'
		private int heuristic = 0;	//estimated cost to move from here to end
		
		private int x = 0;	//x value in node map
		private int y = 0;	//y value in node map
		
		public Node(Node parent, int x, int y) {
			this.parent = parent;
			this.x = x;
			this.y = y;
		}
		
		public int getMoveCost() {
			return moveCost;
		}

		public void setMoveCost(int moveCost) {
			this.moveCost = moveCost;
		}

		public int getHeuristic() {
			return heuristic;
		}

		public void setHeuristic(int heuristic) {
			this.heuristic = heuristic;
		}

		public int getTotalCost() {
			return moveCost + heuristic;
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
		
		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		public boolean hasParent(){
			if(parent == null)
				return false;
			else
				return true;
		}
	}
}
