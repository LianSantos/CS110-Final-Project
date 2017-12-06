import java.util.*;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;	
import java.io.EOFException;
import java.io.UnsupportedEncodingException;
public class BTree
{
	private int cntNodes;
	private RandomAccessFile file;
	private int order;
	private int CurrentValuesRecords;
	private int recordSize;
	public BTree(String strFile,int cvr) throws IOException
	{
		File file = new File(strFile);
        if(!file.exists()){
			this.CurrentValuesRecords = 0;
			this.order = 7;
			this.recordSize = ((order*3)-1)*8;
            this.cntNodes = 1;
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.file.writeLong(this.cntNodes); //Writes number of nodes
			this.file.seek(8);
			this.file.writeLong(0); //Writes root node
        }else{
			this.order = 5;
            this.file = new RandomAccessFile(file,"rwd");
            this.file.seek(0);
            this.cntNodes = (int) this.file.readLong();
			this.CurrentValuesRecords = cvr;
        }	
	}
	public int getCheckNodes() throws IOException
	{
		this.file.seek(8);
		return (int)this.file.readLong();
		
	}
	public int getKeyValue(int key,int node) throws IOException // wrong change for btree
	{
		this.file.seek(32 + 24*key + recordSize*node );
		try
		{
			return (int)this.file.readLong();
		}
		catch(EOFException e)
		{
			return -1;
		}
		
	}
	public int getChild2(int key) throws IOException
	{
		try
		{
			return (int)this.file.readLong();
		}
		catch(EOFException e)
		{
			return -1;
		}
		
	}
	public void insert(int key, int valuekey,int nextNode) throws IOException
	{
		int CurrentIndex;
		if(nextNode==-1)
		{
			this.file.seek(8);//points to rootnode index
		    CurrentIndex = (int)this.file.readLong(); //Gets rootindex
		}
		this.file.seek(32+(recordSize)*CurrentIndex); //Points to current node
		Object[] current = new Object[order-1];
		for(int i = 1; i<order;i++)
		{
			try
			{
				this.file.seek((32+(recordSize)*CurrentIndex) + 24*(i-1) );
				current[i-1] = this.file.readLong();
			}
			catch(EOFException e)
			{
				current[i-1] = null;
			}
		}
		int ins = checkPlacement(current,key);
		
			if(ifFull(current) == false && getChild(CurrentIndex,placement) == -1)
			{
				moveRight(ins);
				this.file.seek(32+(recordSize)*CurrentIndex + 24*ins);
				this.file.writeLong(key);
			}
			if(ifFull(current) == true)
			{
				
			}
		
	}
	public int getChild(int currentNode,int placement)
	{
		this.seek(24+(recordSize *currentNode) + placement * 24)
		try
		{
			return (int)this.readLong();
		}
		catch(EOFException)
		{
			return -1
		}
	}
	public void split(Object[] currentnodearray, int toBeInserted, int insertedPlacement)
	{
		
	}
	public int findMiddle(Object[] array, int toBeInserted,int insertedPlacement)
	{
		long newToBeInserted = (long)toBeInserted;
		long newInsertedPlacement = (long)insertedPlacement;
		int middle;
		if((array.length + 1)%2 == 0)
		{
			middle = array.length/2 + 1;
			if(newInsertedPlacement > middle)
			{
				return array[middle-1];
			}
			else if(newInsertedPlacement < middle && newInsertedPlacement == middle - 1)
			{
				return newToBeInserted
			}
			else if(newInsertedPlacement == middle)
			{
				return toBeInserted;
			}
			else if(newInsertedPlacement<middle)
			{
				return array[middle-2];
			}
		}
		else
		{
			middle = array.length/2;
			if(newInsertedPlacement == middle)
			{
				return array[middle-1];
			}
			else if(newInsertedPlacement > middle && newInsertedPlacement==middle+1)
			{
				return newToBeInserted;
			}
			else if(newInsertedPlacement>middle)
			{
				return array[middle]
			}
			else if(newInsertedPlacement < middle)
			{
				return array[middle-1];
			}
		}
	}
	public void split(int currentNode,)
	{
		
	}
	public boolean ifFull(Object[] currentarray)
	{
		int len = currentarray.length;
		for(int i = 0;i<len;i++)
		{
			if(currentarray[i] == null)
			{
				return false;
			}
		}
		return true;
		
	}
	public void moveRight(int placement)  throws IOException
	{
		this.file.seek(8);//points to rootnode index
		int rootindex = (int)this.file.readLong();
		Object[] current = new Object[order - placement];
		for(int i = placement; i<order;i++)
		{
			try
			{
				this.file.seek((32+(recordSize)*rootindex) + 24*(i) );
				current[i-placement] = this.file.readLong();
			}
			catch(EOFException e)
			{
				current[i-placement] = null;
			}
		}
		for(int i = placement+1; i<order;i++)
		{
				this.file.seek((32+(recordSize)*rootindex) + 24*(i) );
				if(current[i-placement-1] != null)
				{
					this.file.writeLong((long)current[i-placement-1]);
				}
		}
	}
	public int checkPlacement(Object[] currentarray,int insertion)
	{
		int len = currentarray.length;
		long temp = insertion;
		for(int i = 0;i<len;i++)
		{
			if(currentarray[i] == null)
			{
				return i;
			}
			if(temp < (long)currentarray[i] && i == 0)
			{
				return i;
			}
			if(i != len - 1)
			{
				if (temp < (long)currentarray[i] && temp > (long)currentarray[i-1] && i != 0)
				{	
					return i;
				}
			}
		}
		return len;
	}
}
