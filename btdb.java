import java.util.*;
import java.io.*;

public class btdb
{
	public static void main(String[] args) throws IOException
	{
		//creates a BTree class and a ValuesRecord class
		 BTree btree = new BTree(args[0]);
		 //arr holds some data found in the filename (default is Data.bt) created by the BTree class
		 //the data arr contains hold all the ID and their equivalent offset
		 ArrayList<int[]> arr = btree.updateValuesRecord();
		 ValuesRecord values = new ValuesRecord(args[1], arr);
		 
		 //create Scanner
		 Scanner in  = new Scanner(System.in);
		 
		 //initialize inputs 
		 //split is used to get the command, the id, and the value associated to that id
		 //since the value can contain spaces, the rest of the elements after getting the first two contains the entire string for the value
		 //if no input is obtained for a specific field, the fields are given a default value which will make the program output "ERROR: invalid command."
		 String command = "";
		 String StringIDKey = "";
		 String keyValue = "";
		 int idKey = -1;
		 
		 String s = in.nextLine();
		 String[] inputSplit = s.split(" ");
		 
		 if(inputSplit.length >= 1)
		 {
			 command = inputSplit[0];
		 }
		 else
		 {
			 command = "INVALID COMMAND";
		 }
		 
		 if(inputSplit.length >= 2)
		 {
			 StringIDKey = inputSplit[1];
			 
			 if(integerConvertible(StringIDKey))
			 {
				 idKey = Integer.parseInt(StringIDKey);
			 }
		 }
		 else
		 {
			 StringIDKey = "NO ID";
		 }
		
		 if(inputSplit.length >= 3)
		 {
			 keyValue = "";
			 for(int i = 2; i < inputSplit.length - 1; i++)
			 {
				 keyValue += inputSplit[i] + " ";
			 }
			 keyValue += inputSplit[inputSplit.length - 1];
		 }
		 else
		 {
			 keyValue = "";
		 }
		 
		 //keeps receiving and processing inputs until the command "exit" is read
		 //the inputs are sent to the solve method which will process the required command
		 while(!command.equals("exit"))
		 {	 
			 solve(command, idKey, keyValue, btree, values);
			 
			 //receives input so that the program can check if the command is "exit" or not
			 s = in.nextLine();
			 inputSplit = s.split(" ");
			 
			 if(inputSplit.length >= 1)
			 {
				 command = inputSplit[0];
			 }
			 else
			 {
				 command = "INVALID COMMAND";
			 }
			 
			 if(inputSplit.length >= 2)
			 {
				 StringIDKey = inputSplit[1];
				 
				 if(integerConvertible(StringIDKey))
				 {
					 idKey = Integer.parseInt(StringIDKey);
				 }
				 else
				 {
					 idKey = -1;
				 }
			 }
			 else
			 {
				 StringIDKey = "NO ID";
				 idKey = -1;
			 }
			
			 if(inputSplit.length >= 3)
			 {
				 keyValue = "";
				 for(int i = 2; i < inputSplit.length - 1; i++)
				 {
					 keyValue += inputSplit[i] + " ";
				 }
				 keyValue += inputSplit[inputSplit.length - 1];
			 }
			 else
			 {
				 keyValue = "";
			 }
		 }
		 in.close();
    }
	
	//solves the command input
	//invalid commands are handled
	//repetition of ID is not allowed
	public static void solve(String command, int idKey, String keyValue, BTree btree, ValuesRecord values) throws IOException
	{
		//calls insert for values and btree
		//if id is already in values, then the insert command for btree is not executed
		//insert method for values also handles the output when the ID has been repeated
		if(command.equals("insert") && idKey != -1)
		{
			boolean proceed = values.isNew(idKey);
			values.insert(idKey, keyValue);
			
			if(proceed)
			{
				int offSet = values.getOffSet(idKey);
				btree.insert(idKey, offSet);
			}
		}
		
		//calls update method for values if the ID in the input is a valid ID
		else if(command.equals("update") && idKey != -1)
		{
			values.update(idKey, keyValue);
		}
		
		//calls select method for values if the ID in the input is a valid ID
		else if(command.equals("select") && idKey != -1)
		{
			values.select(idKey);
		}
		
		//closes the program
		else if(command.equals("exit"))
		{	
			btree.exit();
			values.exit();
		}
		
		//handles invalid commands
		else
		{
			System.out.println("ERROR: invalid command.");
		}
	}
	
	//checks if a given string can be converted to an integer
	public static boolean integerConvertible(String s)
	{
		 try 
		 { 
			 Integer.parseInt(s); 
		 } 
		 
		 catch(NumberFormatException e) 
		 { 
	        return false; 
		 }
		 catch(NullPointerException e) 
		 {
	        return false;
		 }
		 
		 return true;
	}
	
} 
