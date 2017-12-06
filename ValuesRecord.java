import java.util.*;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;	

//holds the values for each of the ID, and their equivalent record number or offset
//the data is written in a file (default is Data.values)
public class ValuesRecord 
{
	private int cntRecords;	//indicates the number of records in the file
	private HashMap<Integer, Integer> idToRecordNum;	//holds every ID received from the input and returns its equivalent offset when called
	private RandomAccessFile file;	//file where the data will be stored
	private ArrayList<int[]> ID_list;	//holds the data received from BTree. This is used when continuing after exiting the program

	public ValuesRecord(String strFile, ArrayList<int[]> arr) throws IOException
	{
		idToRecordNum = new HashMap<Integer, Integer>();
		File file = new File(strFile);
		ID_list = arr;
		
		//creates a new file if it does not exist
        if(!file.exists()){
            this.cntRecords = 0;
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.file.writeLong(this.cntRecords);

            
        }
        //if the file exists, reads the data contained in the file
        else
        {
            this.file = new RandomAccessFile(file,"rwd");
            this.file.seek(0);
            this.cntRecords = (int) this.file.readLong();
            updateValues(ID_list); //updates the HashMap idToRecordNum 
        }	
        System.out.print("> ");
	}
	
	//returns the record number
	public long getRecords() throws IOException
	{
		this.file.seek(0);
		return this.file.readLong();
		
	}
	
	//returns the offset of a given ID
	public int getOffSet(int id)
	{
		return idToRecordNum.get(id);
	}
	
	//Given the ID, reads the its equivalent value in the file and returns that value
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
	
	//inserts a new ID and its equivalent value to the file 
	//idTorRecordNum is used so that the program does not need to keep reading from the file because it already contains important information found in the file
	//inserts the new data into the file
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
		    
		    System.out.print("< " + id + " inserted." + "\n" + "> ");
		}
		
		else
		{
			System.out.print("< ERROR: " + id + " already exists." + "\n" + "> ");
		}
	}
	
	//change the value of an existing ID
	//also changes the value written in the file for that ID
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
			
			System.out.print("< " +id + " updated." + "\n" + "> ");
		}
		
		else
		{
			System.out.print("< ERROR: " + id + " does not exist." + "\n" + "> ");
		}
	}
	
	//gets the value from the file given an existing ID
	public void select(int id) throws IOException
	{
		if(idToRecordNum.containsKey(id))
		{
			String s = this.getLine(id);
			System.out.print("< " + id + " => " + s + "\n" + "> ");
		}
		
		else
		{
			System.out.print("< ERROR: " + id + " does not exist." + "\n" + "> ");
		}
	}
	
	//closes the file
	public void exit() throws IOException
	{
		this.file.close();
	}
	
	//updates idToRecordNum so that it will contain all the ID and its equivalent offset 
	//ID_list is received from the file from BTree because it holds the all the ID and their equivalent offset
	public void updateValues(ArrayList<int[]> ID_list)
	{
		for(int i = 0; i < ID_list.size(); i++)
		{
			int[] temp = ID_list.get(i);
			
			for(int j = 0; j < temp.length; j += 2)
			{
				if(temp[j] != -1)
				{
					idToRecordNum.put(temp[j], temp[j+1]);
				}
			}
		}
	}
	
	//checks if the given ID is already present in the file or in idToRecordNum
	public boolean isNew(int id)
	{
		if(idToRecordNum.containsKey(id))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
}

