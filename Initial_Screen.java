import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Initial_Screen extends JFrame implements ActionListener{
	
	/*
	 * This class is a JFrame that represents the initial screen of the program.
	 * It has two buttons. 1 - File chooser button - on click the user
	 * chooses from windows explorer the input file which contains the 
	 * peg solitaire problem.
	 * 2 - Cancel button - closes the program. 
	 * */
	
	
	
	private JButton fileChooserButton;
	private JButton cancelButton;
	
	private static final long serialVersionUID = -2660756208129792065L;

	public Initial_Screen() {
		initialize();
	}

	private void initialize() {
	
		this.setBounds(100, 100, 338, 119);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Starting Screen");
		this.getContentPane().setLayout(null);
		
		fileChooserButton = new JButton("Choose the PegSolitaire file");
		fileChooserButton.setBounds(49, 11, 237, 23);
		this.getContentPane().add(fileChooserButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setBounds(49, 42, 237, 23);		
		this.getContentPane().add(cancelButton);
		cancelButton.addActionListener(this);
		fileChooserButton.addActionListener(this);
		this.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(cancelButton))
			this.dispose();
		if(e.getSource().equals(fileChooserButton)){
			
			new FileChooserFrame(this);
		}
	}
}
