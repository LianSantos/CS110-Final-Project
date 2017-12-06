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
		 System.out.println((int)values.getRecords());
		 Scanner in  = new Scanner(System.in);
		 int key = in.nextInt();
		 in.nextLine();
		 String str = in.nextLine();
		values.insert(key,str);
		System.out.println((int)values.getRecords());
		 
		 key = in.nextInt();
		 in.nextLine();
		 str = in.nextLine();
		 values.insert(key,str);
		 System.out.println((int)values.getRecords());
		 
		 key = in.nextInt();
		 System.out.println(values.getLine(key));
    }
	
} 







