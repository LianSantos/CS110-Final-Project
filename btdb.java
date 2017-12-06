import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
public class btdb 
{
	public static void main(String[] args) throws IOException
	{
		 ValuesRecords values = new ValuesRecords(args[1]);
		 BTree bt = new BTree(args[0],1);
		 Scanner in  = new Scanner(System.in);
		 int firstins = 1;
		 int secondins =2;
		 int findvalue = 0;
		/* values.insert(0,"Hello");
		 values.insert(1,"Hi");
		 values.insert(2,"Hillo");
		 System.out.println(values.getLine(0));
		 System.out.println(values.getLine(1));
		 System.out.println(values.getLine(2));
		 */
		 bt.insert(8,0);
		 System.out.println(bt.getKeyValue(0));
		 bt.insert(7,0);
		 System.out.println(bt.getKeyValue(0));
		 System.out.println(bt.getKeyValue(1));
		 //System.out.println(bt.getCheck());
		 System.out.println("Something Wrong");
		 
	}
	
} 







