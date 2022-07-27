import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;


public class Solver {
	/*
	 * This class organizes the solution of the peg puzzle.
	 * It is used to execute the following operations
	 * 
	 * 1. Turns the data from String to Integers. Creates the Starting board, the root
	 *    and gets every node to find its own children.
	 * 2. Contains two methods which are essentially the two basic algorithms "Depth first search" and "Best first Search".
	 * 3. If a solution is found, it writes it down to the output file
	 * 4. It also adds the time taken for the solution to be found, in the output file.
	 */
	
	
	/*
	 * The following Hash table is used for the best first algorithm. Every new unvisited node is added in this data structure.
	 * The Integer-key is the Value of the Node(The smaller - the better) and the Array_List<TreeNode> is a list which contains
	 * all the same valued nodes. The Hash - table is used because it minimizes the selection time of the node. In big PegSolitaire problems,
	 * the valuing time of the node takes inevitably quite some time. Which means, that the selecting time has to be minimized if we want the whole search method to be effective.
	 */
	private Hashtable<Integer,ArrayList<TreeNode>> map = new Hashtable<Integer,ArrayList<TreeNode>>(); 
	
	public Solver(int method, ArrayList<String> data, int numLines, int numCols) {
		if(method==0){ //method = 0: Stands for Depth first button was selected.
		long timeTaken =  depthFirst(data,numLines,numCols);
		writeTime("DepthFirstSolution.txt",timeTaken);
		}
		else{ //Else method = 1: So the best first search button was selected.
			long timeTaken = bestFirst(data,numLines,numCols);
			writeTime("Solution_Best.txt",timeTaken);
		} //Both depthFirst and best first return a "long" variable which is the time taken for the finding procedure.
	}		//The Solver's method write time, adds the time taken in the output file.

	
	private void writeTime(String string,long time) {
		try {
			
			PrintWriter writer = new PrintWriter(new FileWriter(string, true)); 
			
				writer.println();
				writer.print("Solution found after "+time+"ms");
				writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			FileChooserFrame.error_message("Problem with the output file");
		} catch (IOException e) {
			FileChooserFrame.error_message("Problem with the output file");
		}

		
	}


	private long depthFirst(ArrayList<String> data, int numLines, int numCols) {
		final long start = System.nanoTime();
		int[][] board = createBoard(data,numLines,numCols);
		
	/*int f = 0;*/ //This variable counts the the total number of nodes visited. It was mainly used during the testing procedure. The Solver has no use of it.
		
		TreeNode root = new TreeNode(null,board,-1,0,0); //Here we create the root of the tree.
		root.findChildren();				
		if(root.hasChildren()){			//If the root has children
				
			TreeNode currentNode = root.chooseRandomChild();	//we choose one random child											
			/*System.out.println(currentNode.printPath());*/
			currentNode.findChildren(); 		//Now the child finds all of its children.
			if(currentNode.isSolution()) writeDepthSolutionFile(currentNode); //If it has no children and is solution print the path. 
																   //This means that the peg puzzle is solved in the first move.
																	//If it has no children and is NOT solution, it gets in the while loop.
			
			while(!currentNode.isSolution()){					//This double while-loop is the "depth-first algorithm".
		//As long as our node is not a solution 
				while(currentNode.hasChildren()){
		//We search for its children.
					currentNode = currentNode.chooseRandomChild();//We choose one random child and then we ask from it to find its own children.
					currentNode.findChildren();				//If the solution exists, it is going to be on a terminal node.
															//So we try to reach on a terminal node.	
					
			/*f++;	*/			/*System.out.println(currentNode.printPath());	*/	//If lines : 69 - 90 - 101 : of the code are enabled. The node that is 
																					//currently tested, will be shown on the console!! f-variable holds the total number
				}																	//of nodes being tested.
				
			if(!currentNode.isSolution())		//If the terminal node we found is not a solution
			{	
				currentNode = currentNode.getParent();		//We go back to its parent.
				if(currentNode.getParent() == null && !currentNode.hasChildren()){
					FileChooserFrame.error_message("This peg puzzle has no solution.");break;}  //If there is no parent, it means that we've reached back to the root without
																								//having found a solution.
			}
			else{/*System.out.println("Nodes visited ="+f);	*/				//It also means that the root has no children left for examination, so the whole tree has been 
			System.out.println("SOLUTION FOUND");							//accessed, and there was no solution found.
			writeDepthSolutionFile(currentNode); 
			JOptionPane.showMessageDialog(null,"Solution found!");
			}	//If the current node is a solution we write it down to the file.
																							
			}																					
			
		}else{
			//These lines of code are accessed when the root has no children to begin with. So there are two cases of scenario
			if(root.isSolution()){	//The peg puzzle is solved anyway.
				FileChooserFrame.error_message("The peg puzzle is already solved.");
			}else{	//Or it is unsolvable.
				FileChooserFrame.error_message("The peg puzzle has no solution.");
			}
		}
		final long end = System.nanoTime();
		return (end - start) / 1000000; //return time in milliseconds
	}

	
	

	private long bestFirst(ArrayList<String> data, int numLines, int numCols) {
		final long start = System.nanoTime();
		int[][] board = createBoard(data,numLines,numCols);
/*int f = 0;	*/
		TreeNode root = new TreeNode(null,board,-1,0,0); //Here we create the root of the tree.
		root.setValue();	//Root gets a value and has its children found.
		for(int i = 0; i<root.getChildren().size();i++){
			TreeNode node = root.getChildren().get(i);	//The root node adds its children in the 
			node.setValue();							//allNodes list (The children are also valued).
	
			
		if(map.get(node.getValue())==null){ 
			map.put(node.getValue(),new ArrayList<TreeNode>());
			map.get(node.getValue()).add(node);
		}else{
			map.get(node.getValue()).add(node);
		}
		
		} 
		TreeNode currentNode = bestValuedNode();//If the root has no children, bestValuedNode() returns null
	try{												//So we need a try catch block here, so we can inform the user 
														//If we have taken as an input an already solved peg puzzle(Or an initially unsolvable PegPuzzle).
		/*System.out.println(currentNode.printPath() +" "+currentNode.getValue()); f++;*/
		while(!currentNode.isSolution()){
		//After we have chosen the best child, we check if it is a solution, so we can get out of the loop and return the output.
			
			for(int i = 0; i<currentNode.getChildren().size();i++){ //If it is not, we get its children.
				TreeNode node = currentNode.getChildren().get(i);	//We give them values and put them into the allNodes list.
				node.setValue();
		
				if(map.get(node.getValue())==null){
					map.put(node.getValue(),new ArrayList<TreeNode>());
					map.get(node.getValue()).add(node);
				}else{
					map.get(node.getValue()).add(node);
				}
			}	/*f++; *//*f counts the total number of visited nodes. If you want to track the whole
			 			   procedure, you enable the comments in lines: 146,167, 173, 127 and this one.*/
			
						
				currentNode=bestValuedNode();   //Then, we get the new best child and repeat the process until we find the solution or until we have no nodes left to examine.
				System.gc(); 			//In big problems the memory wont be enough, so in this line we clear the nodes already visited.
				
				/*System.out.println(currentNode.printPath() +" "+currentNode.getValue());*/ //Prints the current node visited AND its Value.
		}
		
		if(currentNode.isSolution()){
			System.out.println("SOLUTION FOUND !!!!"); 
			currentNode.printBestPath("Solution_Best");
			/*System.out.println("Visited "+f);*/
			JOptionPane.showMessageDialog(null,"Solution found!");
			} 
		else FileChooserFrame.error_message("The PegPuzzle has no solution");  //When we find the solution we write it down to the output file "Solution_Best".
	}
	catch(NullPointerException e){
		if(root.isSolution())	//We reach this part of code if we have an already solved puzzle OR when in the while loop we get NullPonterException, which means that
								//Our Map has gone empty! So we ran out of nodes and the peg puzzle has no solution.
			FileChooserFrame.error_message("The PegPuzzle is already solved");
		else
			FileChooserFrame.error_message("The PegPuzzle has no solution");
	}
	final long end = System.nanoTime();
	return (end - start) / 1000000;
	}
	
	
	private TreeNode bestValuedNode(){
		int i = 1;
		while(!map.isEmpty()){ 	//Find the least valued node. (Map will be empty only if no node is added to it) in
								//in any other case it is going to contain the pointers to the array list even if the lists are going to be empty.
			if(map.get(i)!=null && !map.get(i).isEmpty()){
				Random random = new Random();
				return map.get(i).remove(random.nextInt(map.get(i).size())); //We have a guarantee that a random least valued node will be removed and "return" to the solver.
			} i++; if(i == 350) break;	//If the lists are all empty, then break and return null.
		}								//We can never have a value equal to 350.
		return null;
	}	


	private void writeDepthSolutionFile(TreeNode solutionNode) {
		TreeNode currentNode = solutionNode;
		Stack<String> stack = new Stack<String>();
		
		while(currentNode.getParent()!=null){
			String string =currentNode.getMove();
			stack.add(string);
			currentNode = currentNode.getParent();
		}
			
	
		try {
			PrintWriter writer = new PrintWriter("DepthFirstSolution.txt", "UTF-8");
			writer.println(stack.size());
			while(!stack.isEmpty())
				writer.println(stack.pop());
				writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
	
			e.printStackTrace();
		}
		
	}

	private int[][] createBoard( ArrayList<String> data, int numLines, int numCols) {
		int i=0;	
		int[][] board = new int[numLines][numCols];
			for(String nums: data){
				 String[] parts = nums.split(" ");
				 for(int j=0;j<parts.length;j++){
					 board[i][j]=Integer.parseInt(parts[j]);
				 }
				 i++;
			}
			return board;
	}

}
