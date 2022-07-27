import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;



public class MethodChooserFrame extends JFrame implements ActionListener{

/*
 * This class is the frame from which the user selects the method-SearchAlgorithm which is going 
 * to solve the peg puzzle. It contains three buttons. i) "depth first solution" 
 * ii) "best First solution" iii) "Cancel button"
 */
	private static final long serialVersionUID = -2371363295539278394L;
	private JButton depthFirstButton;			//Buttons which select the solving algorithm.
	private JButton bestFirstSearchButton;
	private JButton cancelButton;
	private int spamFlagDepth;			//Those variables are used to prevent the user
	private int spamFlagBest;			//from solving the same pegSolitaire problem more than once.
	
	private ArrayList<String> data = new ArrayList<String>(); //Contains the data which have been received from the input file.
	private int numLines;
	private int numCols;
	
	public MethodChooserFrame(ArrayList<String> data, int numberOfLines, int numberOfColumns) {
	
		this.data=data;
		this.numCols=numberOfColumns;
		this.numLines=numberOfLines;
		this.spamFlagDepth=0;
		this.spamFlagBest=0;
		
		
		this.setBounds(200, 200, 320, 150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Choose algorithm");
		this.getContentPane().setLayout(null);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(50, 80, 200, 20);
		cancelButton.addActionListener(this);
		this.getContentPane().add(cancelButton);
		
		depthFirstButton = new JButton("Use Depth-first algorithm");
		depthFirstButton.setBounds(50, 50, 200, 20);
		depthFirstButton.addActionListener(this);
		this.getContentPane().add(depthFirstButton);
		
		bestFirstSearchButton = new JButton("Use Best First Search");
		bestFirstSearchButton.setBounds(50, 20, 200, 20);
		bestFirstSearchButton.addActionListener(this);
		this.getContentPane().add(bestFirstSearchButton);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource().equals(cancelButton)){
			this.dispose();
			new Initial_Screen();
		}
	
		if(e.getSource().equals(depthFirstButton)){
			this.spamFlagDepth++;
			if(spamFlagDepth==1){
				//Solve the PegSolitaire with depth-first algorithm
				new Solver(0,data,numLines,numCols);
				
			}
			else{
				FileChooserFrame.error_message("The PegSolitaire has already been solved with this method");
				
			}
		}
			
		if(e.getSource().equals(bestFirstSearchButton)){

			this.spamFlagBest++;
			if(spamFlagBest==1){
				//Solve the PegSolitaire using the best First Search algorithm				
				new Solver(1,data,numLines,numCols);
			}
			else{
				FileChooserFrame.error_message("The PegSolitaire has already been solved with this method");
			}
		}
			
		
	}

}
