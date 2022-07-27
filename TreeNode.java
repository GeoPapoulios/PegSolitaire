import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class TreeNode {

	

	private TreeNode parent;
	private ArrayList<TreeNode> children = new ArrayList<TreeNode>();
	private int[][] board;
	private int boardLines;
	private int boardColumns;
	private String move;   //This strings holds the move which was made, and led us from the parent's board statement to the current board statement.
	private int findChildrenFlag;	//This flag guarantees that every node searches for its children one and only one time.
	private int value;//Value of the heuristic function. The node with the smallest value will be the chosen one.
	private int pegsLeft;
	
	public TreeNode(TreeNode parent, int[][] board, int direction, int x, int y){
		this.parent=parent;
		this.board=board;
		this.boardLines=board.length;
		this.boardColumns=board[0].length;
		this.setMove(direction,x,y);
		this.findChildrenFlag=0;
		
		if(parent == null){ //If this is the root, we get how many pegs exist at the starting state of the board.
			this.pegsLeft=0;
			for(int i = 0 ;i<board.length;i++){
				for(int j = 0 ; j < board[i].length;j++){
					if(board[i][j]==1) this.pegsLeft++;
				}
			}
		}else{
			this.pegsLeft=(parent.getPegsLeft()-1); //If it is not, then the pegsLeft are going to be equal to one fewer peg than its previous statement.
		}
		
	}
	
	

	public void findChildren(){
	  if(findChildrenFlag==0){
		for(int i=0;i<boardLines;i++){
			for(int j = 0 ; j<boardColumns;j++){
				if(this.board[i][j]==1)
				{ 
					boolean[] allowedDirections = findAllowedDirections(i,j); 
					//We pass down to this boolean array the possible directions of the current peg.
					//0 stands for up
					//1 stands for down
					//2 stands for left
					//3 stands for right
					//If a direction is possible, that cell of the allowedDirections will be true.
					//e.g. if "down" direction is possible, then the allowedDirections[1] will become true.
					for(int k=0;k<4;k++){
						if(allowedDirections[k])
						{
							//System.out.println("I am the spot ("+i+","+j+") I can move "+k+" way.");
							int[][] newBoard = move(k,i,j); //move method = Moves the (i,j) peg to k direction and returns the new board after that move.
							TreeNode child = new TreeNode(this,newBoard,k,i,j); 
							children.add(child);
						}
					}
					
				}
			}
		}	
	
	  }this.findChildrenFlag++; 
	
	}
	
	
	
	
	


	private boolean[] findAllowedDirections(int x, int y) {
		
		boolean[] directions = new boolean[4];  //We create the array with the directions
		for(int i=0;i<4;i++)
			directions[i]=false; //In the beginning all directions are not valid.
		//Then we check which directions are allowed and we inform the array.
		
		try{
		if(board[x-1][y]==1 &&  board[x-2][y]==2){
			directions[0]=true; //Up is allowed
		}
		}catch(IndexOutOfBoundsException e){}
		
		try{
		if(board[x+1][y]==1 &&  board[x+2][y]==2){
			directions[1]=true; //Down is allowed
		}
		}catch(IndexOutOfBoundsException e){}
		
		try{
			if(board[x][y-1]==1 &&  board[x][y-2]==2){
			directions[2]=true; //Left is allowed
		}
			
		}catch(IndexOutOfBoundsException e){}
		
		try{
		if(board[x][y+1]==1 &&  board[x][y+2]==2){
			directions[3]=true;	//Right is allowed
		}
			
		}catch(IndexOutOfBoundsException e){}
		
		
		return directions;
	}
	
	
	
	private int[][] move(int direction, int x, int y) {
		int[][]newBoard = new int[boardLines][boardColumns];
		for(int i=0; i<board.length; i++)
			  for(int j=0; j<board[i].length; j++)
			    newBoard[i][j]=board[i][j];
	
		
		switch(direction) {
		   case 0 :
			   //Move Up
			   newBoard[x][y]=2;
			   newBoard[x-1][y]=2;
			   newBoard[x-2][y]=1;
			   return newBoard;
		   case 1:
		      //Move Down
			   newBoard[x][y]=2;
			   newBoard[x+1][y]=2;
			   newBoard[x+2][y]=1;
			   return newBoard; 
		   
		   case 2:
			   //Move Left

			   newBoard[x][y]=2;
			   newBoard[x][y-1]=2;
			   newBoard[x][y-2]=1;
			   return newBoard;
		  
		   case 3:
		      // Move Right
			   newBoard[x][y]=2;
			   newBoard[x][y+1]=2;
			   newBoard[x][y+2]=1;
			   return newBoard;
			  
			default: return newBoard; //If something goes wrong.
		}
				
		
	}

	public boolean hasChildren(){
		if(children.size()>0)
			return true;		
		return false;
		
	}

	public TreeNode chooseRandomChild() {
		int numberOfChildren = children.size();
		Random random = new Random();
		
		return children.remove(random.nextInt(numberOfChildren));
		
		
	}
		
	
	public void setValue(){
		boolean firstPegFound=false; //This variable becomes true when the first peg is found.
								//Because we search for pegs by row, with the following double "for"
								//the first peg found is going to be the one in the minimum indexed line.
								//So when we take by using lineHasPegs the maximum indexed line with pegs,
								//their difference is going to be the height of the rectangle which encloses them.
		int isolatedPegs=0;
		
		int maxVertical = 0; //Index of the "Highest" peg
		int minVertical = 0;//Index of the "lowest" peg
		
		int minHor=boardColumns;	
		int maxHor=0;
		
		for(int i = 0; i < board.length;i++){
			boolean lineHasPegs=false;
			
			
			for(int j = 0 ; j < board[i].length; j++){
				if(board[i][j]==1){ 
					
						if(minHor>j) 
							minHor=j;
						if(maxHor<j)
							maxHor=j;
						
						
					if(isIsolated(i,j)) isolatedPegs++;
					
					if(!firstPegFound) {
						 minVertical = i;
						 firstPegFound=true;
					}
					if(!lineHasPegs)
					{
						lineHasPegs=true;
						maxVertical = i;
					}
					
					
				}
			}
		}
		 
		
		this.findChildren();
		int verticalSurface = maxVertical-minVertical;
		
		int horizontalSurface = maxHor-minHor;
		
		this.value =isolatedPegs+verticalSurface+horizontalSurface; //The smaller the better.
	}
	
	//This method returns true only when the peg has no other pegs at the up,down,left and right cell.
 
	private boolean isIsolated(int i, int j) {
		try{
			if(board[i][j+1]==1) return false;
		}catch(IndexOutOfBoundsException e ){}
		
		try{
			if(board[i+1][j]==1) return false;
		}catch(IndexOutOfBoundsException e ){}
		
		try{
			if(board[i][j-1]==1) return false;
		}catch(IndexOutOfBoundsException e ){}
		
		try{
			if(board[i-1][j]==1) return false;
		}catch(IndexOutOfBoundsException e ){}
		
		return true;
	}
	
	public int getValue(){		
		return this.value;
	}

	
	
	
	//This method sets the move that was made to the parent node's board and caused the current node to be created.
	public void setMove(int direction, int i, int j) {
		 int x = i+1; //We add number one here because this string will be used as an output. So the board is being indexed starting from one, not from zero.
		 int y = j+1;
		switch(direction) {
		   case 0 :
			   //I Moved Upwards
			   move=x+" "+y+" "+(x-2)+" "+y;
			   break;
		   case 1:
		      //I Moved Downwards
			   move=x+" "+y+" "+(x+2)+" "+y;
			   break;  
		   case 2:
			   //I Moved to the Left
			   move=x+" "+y+" "+x+" "+(y-2);
			   break; 
		   case 3:
		      //I Moved to the Right
			   move=x+" "+y+" "+x+" "+(y+2);
			   break;		  
			default: move=x+" "+y+" "+x+" "+y; //If something goes wrong OR if we have the root node.
		}
				
		
	}

	public boolean isSolution(){
		//If a node is a solution it is going to have only one peg.
		return (this.pegsLeft==1);
	}
		

	public void printBestPath(String fileName) {
		TreeNode currentNode = this;
		Stack<String> stack = new Stack<String>();
		
		while(currentNode.getParent()!=null){
			String string =currentNode.getMove();
			stack.add(string);
			currentNode = currentNode.getParent();
		}
			
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName+".txt", "UTF-8");
			writer.println(stack.size());
			while(!stack.isEmpty())
				writer.println(stack.pop());
				writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
	
			e.printStackTrace();
		}

		
		
		
	}


	public int getPegsLeft() {
		return pegsLeft;
	}



	public ArrayList<String> printPath() {
		ArrayList<String> path = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		TreeNode currentNode=this;
		while(currentNode.getParent()!=null){
			stack.push(currentNode.getMove());
			currentNode=currentNode.getParent();
		}
		while(!stack.isEmpty())
			path.add(stack.pop());
		return path;
	}

	public String getMove() {
		return move;
	}

	public TreeNode getParent() {
		return parent;
	}

	public ArrayList<TreeNode> getChildren() {
		return children;
	}



}
