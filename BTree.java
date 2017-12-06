import java.util.*;
import java.io.*;

//holds the BTree formed from all the ID
public class BTree {
	
	private final int order = 3; //indicates the maximum number of ID that a node can hold
	private int Node_size;	//indicates the maximum number of long ints present per node
	private int max_bytes;
	private int recordCount;	//indicates the total number of records (number of nodes) 
	private int rootNum;	//indicates what record is the root node
	private int useRecord;
	private HashMap<Integer, BtreeNode> hmNode;	//returns the equivalent node given the record number

	private RandomAccessFile file;	//for creating the file for the BTree (default is Data.bt)
	
	public BTree(String strFile) throws IOException
	{
		File file = new File(strFile);
		hmNode = new HashMap<Integer, BtreeNode>();
		Node_size = ((order - 1)*3) + 2;
		max_bytes = Node_size*8;
		useRecord = 0;
		
		//creates the file if it does not exist
        if(!file.exists())
        {
            this.recordCount = 0;
            this.rootNum = 0;
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.file.writeLong(this.recordCount);
            this.file.writeLong(this.rootNum);
        }
        
        //reads the date from the file and updates hmNode for all the important information
        else
        {
            this.file = new RandomAccessFile(file,"rwd");
            this.file.seek(0);
            this.readData(hmNode);
        }	
	}
	
	//inserts a new ID in the BTree
	public void insert(int id, int offset) throws IOException
	{
		this.useRecord = -1;	//indicates the record number where the new ID will be inserted
		
		//if there are no other records present, useRecord is set to be record number 0 by default
		if(recordCount <= 1)
		{
			useRecord = 0;
		}
		//calls findRecord method to determine which record should be used
		else
		{
			findRecord(id, hmNode, rootNum);
		}
		
		//if the record is new, creates a new BtreeNode and updates hmNode
		if(!hmNode.containsKey(useRecord))
		{
			BtreeNode bN = new BtreeNode(order);
			hmNode.put(useRecord, bN); 
			recordCount++;
		}
		
		int insertIndex = hmNode.get(useRecord).insertLoc(id);	//determines where in BtreeNode should the new ID be inserted
		//moves the IDs in the BtreeNode to allow the insertion of the new ID in the proper place
		if(hmNode.get(useRecord).getValue(insertIndex) > id)
		{
			hmNode.get(useRecord).move(insertIndex);
		}
		hmNode.get(useRecord).insert(insertIndex, id, offset);	//inserts the new ID to the BtreeNode
		
		writeData(useRecord, hmNode);	//writes the important data in the BtreeNode to the file
		splitNode(useRecord, hmNode);	//checks if there is a need to undergo split
		
		printAllRecord(hmNode);	//prints all the records after every insertion. Used for checking and testing.
		
	}
	
	//splits the node similar to how BTree undergoes split
	public void splitNode(int useRecord, HashMap<Integer, BtreeNode> hmNode) throws IOException
	{
		int numCount = hmNode.get(useRecord).getRecordCount();	//determines the number of ID in the node

		//executes if the number of ID in the node exceeds the maximum capacity
		if(numCount >= order)
		{
			int[] all_data = hmNode.get(useRecord).getAllData();
			int middle = hmNode.get(useRecord).getMiddleKey();	//gets the middle ID. The middle ID is what will be transferred to the new root node
			
			//gives the appropriate data for the new root node
			int parentID = hmNode.get(useRecord).getValue(middle);
			int leftRecordNum = useRecord;
			int rightRecordNum = recordCount;
			
			//creates the new right node
			BtreeNode right = new BtreeNode(order);
			hmNode.put(rightRecordNum, right);
			recordCount++;
			
			int offVal = hmNode.get(useRecord).getValue(middle + 1); //gets the offset of the middle ID
			
			//executes if a new root node is created
			if(useRecord == rootNum)
			{
				BtreeNode newRoot = new BtreeNode(order);
				rootNum = recordCount;
				recordCount++;
				
				//updates the data of the root node accordingly
				newRoot.changeValue(1, leftRecordNum);
				newRoot.changeValue(2, parentID);
				newRoot.changeValue(3, offVal);
				newRoot.changeValue(4, rightRecordNum);
				hmNode.put(rootNum, newRoot);
				//writes the data of the new root node to the file
				writeData(rootNum, hmNode);
			}
			//executes if no new root node is created
			else
			{
				//updates the data of the parent node
				//inserts the new node in the parent node in the proper place
				int p = hmNode.get(useRecord).getValue(0);
				int ins = hmNode.get(p).insertLoc(parentID);
				if(hmNode.get(p).getValue(ins) > parentID)
				{
					hmNode.get(p).move(ins);
				}
				hmNode.get(p).insert(ins, parentID, offVal);
				
				hmNode.get(p).changeValue(ins - 1, leftRecordNum);
				hmNode.get(p).changeValue(ins + 2, rightRecordNum);
				//writes the data of the updated root node to the file
				writeData(p, hmNode);
			}
			
			//updates the data of the new right node accordingly 
			int index = 1;
			for(int i = middle + 2; i < all_data.length; i++)
			{
				hmNode.get(rightRecordNum).changeValue(index, all_data[i]);
				index++;
			}
			//writes the data of the new right node to the file
			writeData(rightRecordNum, hmNode);
			
			//updates the data of the node that has split (now the left node) accordingly
			hmNode.get(useRecord).split(rootNum);
			//writes the updated data of the left node to the file
			writeData(useRecord, hmNode);
			
			updateParentNode(rootNum, hmNode);
			
			int parentNum = hmNode.get(useRecord).getValue(0);
			//recursively checks if a new node needs to be split
			splitNode(parentNum, hmNode);
		}
	}
	
	public void updateParentNode(int record, HashMap<Integer, BtreeNode> hmNode) throws IOException
	{
		for(int i = 1; i < Node_size; i += 3)
		{
			int child = hmNode.get(record).getValue(i);
			if(child != -1)
			{
				hmNode.get(child).changeValue(0, record);
				writeData(child, hmNode);
				updateParentNode(child, hmNode);
			}
		}
	}
	
	//determines what record number should the new ID be inserted
	public void findRecord(int id, HashMap<Integer, BtreeNode> hmNode, int rec)
	{
		int[] data = hmNode.get(rec).getData();
		int numChild = 0; 
		
		for(int i = 2; i < data.length; i += 3)
		{
			if(data[i] > id)
			{
				int Child_record = hmNode.get(rec).getValue(i - 1);
				if(Child_record != -1)
				{
					rec = Child_record;
					
					data = hmNode.get(rec).getData();
					for(int k = data.length - 1; k >= 1; k -= 3)
					{
						if(data[k] == -1)
						{
							numChild++;
						}
						else
						{
							numChild = 0;
							break;
						}
					}
					
					if(numChild == this.order)
					{
						this.useRecord = rec;
					}
					
					findRecord(id, hmNode, rec);
					break;
				}
			}
		}
		
		int lastKeyIndex = -1;
		
		for(int j = data.length - 3; j >= 2; j -= 3)
		{
			if(data[j] != -1)
			{
				lastKeyIndex = j;
				break;
			}
		}
		
		if(data[lastKeyIndex] < id)
		{
			int child = hmNode.get(rec).getValue(lastKeyIndex + 2);
			
			if(child != -1)
			{
				rec = child;
				
				data = hmNode.get(rec).getData();
				for(int k = data.length - 1; k >= 1; k -= 3)
				{
					if(data[k] == -1)
					{
						numChild++;
					}
					else
					{
						numChild = 0;
						break;
					}
				}
				
				if(numChild == this.order)
				{
					this.useRecord = rec;
				}
				
				findRecord(id, hmNode, rec);
			}
		}
	}
	
	//writes the important data in the file created
	public void writeData(int record, HashMap<Integer, BtreeNode> hmNode) throws IOException
	{
		int[] allData = hmNode.get(record).getData();
		
		this.file.seek(0);
	    this.file.writeLong(this.recordCount);
	    this.file.seek(8);
	    this.file.writeLong(this.rootNum);
	    
		this.file.seek(16 + (record)*max_bytes);
		
		for(int elem: allData)
		{
			this.file.writeLong(elem);;
		}
	}
	
	//reads the data from the file to update important information to hmNode
	public void readData(HashMap<Integer, BtreeNode> hmNode) throws IOException
	{
		this.file.seek(0);
		recordCount = (int) this.file.readLong();
		this.file.seek(8);
		rootNum = (int) this.file.readLong();
		
		int currentRec = 0;
		
		while(true)
		{
			try
			{
				if(!hmNode.containsKey(currentRec))
				{
					BtreeNode btN = new BtreeNode(order);
					int[] data = btN.getData();
					
					for(int i = 0; i < data.length; i++)
					{
						int elem = (int) this.file.readLong();
						btN.changeValue(i, elem);
					}
					
					hmNode.put(currentRec, btN);
				}
				currentRec++;
			}
			
			catch(EOFException ex)
			{
				break;
			}
		}
		
	}
	
	//creates an array which will be sent to ValuesRecord that contains information of all the ID and their equivalent offset
	public ArrayList<int[]> updateValuesRecord()
	{
		ArrayList<int[]> arr = new ArrayList<int[]>();
		
		for(int i = 0; i < recordCount; i++)
		{
			 arr.add(hmNode.get(i).getKeys(i));
		}
		
		return arr;
	}
	
	//closes the file
	public void exit() throws IOException
	{
		this.file.close();
	}
	
	//prints all the records
	//for testing purposes
	public void printAllRecord(HashMap<Integer, BtreeNode> hmNode)
	{
		String s = "";
		for(int i = 0; i < recordCount; i++)
		{
			if(hmNode.containsKey(i))
			{
				BtreeNode n = hmNode.get(i);
				int[] d = n.getData();
				
				s = s + "Record# " + i + ": (";
				for(int j = 0; j < d.length - 1; j++)
				{
					s = s + d[j] + ", ";
				}
				s = s + d[d.length - 1] + ")" + "\n";
			}
		}
		System.out.print(s + "\n" + "> ");
	}
}
