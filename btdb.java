import java.util.*;
import java.io.*;

public class btdb2 
{
	public static void main(String[] args) throws IOException
	{
		 BTree btree = new BTree(args[0]);
		 ValuesRecord values = new ValuesRecord(args[1]);
		 
		 Scanner in  = new Scanner(System.in);
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
		 
		 while(!command.equals("exit"))
		 {	 
			 solve(command, idKey, keyValue, btree, values);
			 
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
		 }
		 in.close();
    }
	
	public static void solve(String command, int idKey, String keyValue, BTree btree, ValuesRecord values) throws IOException
	{
		if(command.equals("insert"))
		{
			values.insert(idKey, keyValue);
			int offSet = values.getOffSet(idKey);
			btree.insert(idKey, offSet);
		}
		
		else if(command.equals("update"))
		{
			values.update(idKey, keyValue);
		}
		
		else if(command.equals("select"))
		{
			values.select(idKey);
		}
		
		else if(command.equals("exit"))
		{	
		}
		
		else
		{
			System.out.println("ERROR: invalid command.");
		}
	}
	
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
