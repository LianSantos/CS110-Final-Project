import java.util.*;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;	
import java.io.UnsupportedEncodingException;
public class ValuesRecord 
{
	private int cntRecords;
	private HashMap<Integer, Integer> idToRecordNum;
	private RandomAccessFile file;

	public ValuesRecord(String strFile) throws IOException
	{
		idToRecordNum = new HashMap<Integer, Integer>();
		File file = new File(strFile);
        if(!file.exists()){
            this.cntRecords = 0;
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.file.writeLong(this.cntRecords);
        }else{
            this.file = new RandomAccessFile(file,"rwd");
            this.file.seek(0);
            this.cntRecords = (int) this.file.readLong();
        }	
	}
	
	public long getRecords() throws IOException
	{
		this.file.seek(0);
		return this.file.readLong();
		
	}
	
	public int getOffSet(int id)
	{
		return idToRecordNum.get(id);
	}
	
	public String getLine(int id) throws IOException
	{
		int key = idToRecordNum.get(id);
		this.file.seek(8 + ((key)*256));
		int len = this.file.readShort();
		byte[] array = new byte[len];
		this.file.seek(8+((key*256)+16));
		this.file.read(array);
		String str = new String(array,"UTF8");
		return str;
	}
	
	public  void insert(int id, String str) throws IOException
	{
		if(!idToRecordNum.containsKey(id))
		{
			idToRecordNum.put(id, cntRecords);
			int key = idToRecordNum.get(id);
			this.file.seek(8 + (key)*256);
			byte[] array = str.getBytes("UTF-8");
			this.file.writeShort(array.length);
			this.file.seek(8+((key*256)+16));
			this.file.write(array);
			this.file.seek(0);
		    cntRecords += 1;
		    this.file.writeLong(this.cntRecords);
		    
		    System.out.println(id + " inserted.");
		}
		
		else
		{
			System.out.println("ERROR: " + id + " already exists.");
		}
	}
	
	public void update(int id, String value) throws IOException
	{
		if(idToRecordNum.containsKey(id))
		{
			int key = idToRecordNum.get(id);
			this.file.seek(8 + (key)*256);
			byte[] array = value.getBytes("UTF-8");
			this.file.writeShort(array.length);
			this.file.seek(8 + (key*256) + 16);
			this.file.write(array);
			
			System.out.println(id + " updated.");
		}
		
		else
		{
			System.out.println("ERROR: " + id + " does not exist.");
		}
	}
	
	public void select(int id) throws IOException
	{
		if(idToRecordNum.containsKey(id))
		{
			String s = this.getLine(id);
			System.out.println(id + " => " + s);
		}
		
		else
		{
			System.out.println("ERROR: " + id + " does not exist.");
		}
	}
	
}

