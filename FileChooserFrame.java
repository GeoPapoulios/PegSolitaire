import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileChooserFrame {

	/*
	 * This frame appears when on initial_screen the user clicks on the 
	 * -Choose Peg Solitaire file- button. 
	 * This class will dispose the initial_screen only when a valid file is selected.
	 * The class is full of tests that forbid the user from giving as an input a file
	 * that is not of the following form
	 * 
	 * 6 5 
	 * 0 0 2 0 0
	 * 0 2 1 2 0
	 * 2 1 1 1 2
	 * 0 2 1 2 0
	 * 0 2 1 2 0
	 * 0 0 2 0 0
	 * 
	 * Where:
	 * 
	 * i)   The first two digits represent the number of lines and columns respectively.
	 * ii)  The next lines will be the problem itself and the only acceptable numbers are the following.
	 *      0 - Stands for : Out of Board
	 *      1 - Stands for : Peg
	 *      2 - Stands for : Empty
	 *      
	 * iii)The lines and the columns must agree with the given board in the next lines.
	 * iv) Any extra space or empty line is acceptable and is being ignored.
	 * v)  If any symbol, letter or forbidden digit is found among the data, an informing message appears on the user's display.
	 */
	
	
	
	
	private ArrayList<String> fileContent; //All the lines of the chosen file are enlisted here.
	private int numberOfLines;				//The dimensions of the PegSolitaire are stored
	private int numberOfColumns;			//in those two variables.
	
	public FileChooserFrame(Initial_Screen parent){
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(parent); //Open a dialog where the user can select a file.
		
		if (result == JFileChooser.APPROVE_OPTION) { //If a file is successfully selected
		    File selectedFile = fileChooser.getSelectedFile(); //we can have access to it over here.
		    
		    
		    if(hasCorrectForm(selectedFile)){
		    	//If the file has a correct form then, pass it to the frame that determines how it is going to be solved.
		    	new MethodChooserFrame(fileContent,numberOfLines,numberOfColumns);
		    	parent.dispose(); //Kill the starting frame.
		    }
		  
		}
	
		
		
	}


	private boolean hasCorrectForm(File file) {
		fileContent = new ArrayList<String>();
		
		try{ //Try reading the file.
			 InputStream inputStream = new FileInputStream(file); //New file input stream
			 Reader reader = new InputStreamReader(inputStream);  //New file reader
			 BufferedReader bufferedReader = new BufferedReader(reader); //Buffered Reader for efficiency.
			 
			 
			 String line;
			 line = bufferedReader.readLine();
			 while(line.trim().equals("")){
				 line = bufferedReader.readLine(); //Ignore empty lines
			 }
			 String[] parts = line.split(" ");  //Here we get the dimensions of the PegSolitaire.
			 if(parts.length!=2){				//We check if they are correctly defined.
				 error_message("Wrong input file format.(Dimensions wrongly defined or not defined)");
				 bufferedReader.close();
				 return false;
			 }
		
			 if( !(isDigit(parts[0]) && isDigit(parts[1])) )  //If there are no digits defining the dimensions, we inform the user.
			 {
				 error_message("Wrong input file format.(Dimensions wrongly defined, a character or a symbol was found)");
				 bufferedReader.close();
				 return false;
			 }
			 numberOfLines=Integer.parseInt(parts[0]);		//Then we get the dimensions 
			 numberOfColumns=Integer.parseInt(parts[1]);		//in their integer form.
			 System.out.println(numberOfLines+" "+numberOfColumns);
		
			 while ((line = bufferedReader.readLine()) != null) {	//We read the file, line by line and we add the lines to the arrayList.
		        if(!line.trim().equals("")) //Ignore empty lines.
				 fileContent.add(line);
		     }
			 
			 for(String str:fileContent)	//We make sure that the data have been put correctly inside of the "input" file. 
			 {
				if(!isDigit(str))
				{
					 error_message("A character or a symbol was found between the data");
					 bufferedReader.close();
					 return false;
				}
				if(!isAnAllowedDigit(str))
				{
					 error_message("A not allowed digit was found in the data (Only {0,1,2} are allowed)");
					 bufferedReader.close();
					 return false;
				}	 
			 }
			 
			 if(numberOfLines!=fileContent.size())	//We check the consistency of the lines and columns.
			 {
				error_message("The number of lines mismatches with the lines of data");
				bufferedReader.close();
				return false;
			 }
			 
			 if(!columnCheck(fileContent,numberOfColumns)){
				 error_message("The defined number of columns mismatches with the columns of data");
				 bufferedReader.close();
				 return false;
			 }
			 
			 			 
			 if(!fileContent.isEmpty())
			 System.out.println(fileContent); //In case we haven't opened a correct file, don't show anything.
			 
			 reader.close();
		
		}
		catch(IOException e){	error_message("The file is unable to be read");   return false;	} //If the file is corrupted or broken, inform the user.
		catch(NullPointerException e){ error_message("The file is Empty "); return false;} //If the lines variable gets no value, it means that the file is empty.
				
				
		return true;
	}

	private boolean isDigit(String string){  //Function which tells if the given string corresponds to a digit (or to a set of digits).
		String str = string.replaceAll(" ",""); //Remove the space character and check.
		for(int i = 0; i<str.length();i++){
			if(!Character.isDigit(str.charAt(i)))
				return false;
		}
			
		return true;
	}

	
	
	private boolean isAnAllowedDigit(String str) { //The allowed digits are {0,1,2}. This Function returns true if the given string 
													//contains no other digit than those three.
		 String[] parts = str.split(" ");
		 for(int i = 0 ; i < parts.length;i++){
			 if( Integer.parseInt(parts[i])!=0 && Integer.parseInt(parts[i])!=1 && Integer.parseInt(parts[i])!=2 ){
				 return false;
			 }
		 }
		
		return true;
	}

	
	private boolean columnCheck(ArrayList<String> list,int cols) { //Returns true if the number of columns in the list are equal to the declared number of columns.
		
		for(String str : list){
			 String[] parts = str.split(" ");
			 if(parts.length!=cols)
				 return false;
		}
		
		
		return true;
	}

	
	
	public static void error_message(String string) {
		JOptionPane.showMessageDialog(null,string,"Error detected", JOptionPane.WARNING_MESSAGE);		
		
	}
	
}
