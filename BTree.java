import java.util.*;
import java.io.*;

public class BTree {
	
	private int recordCount;
	private int rootNum;
	private HashMap<Integer, BtreeNode> hmNode;
	private HashMap<Integer, Integer> idToRecordNum;
	private RandomAccessFile file;
	
	public BTree(String strFile) throws IOException
	{
		File file = new File(strFile);
		hmNode = new HashMap<Integer, BtreeNode>();
		idToRecordNum = new HashMap<Integer, Integer>();
		
        if(!file.exists())
        {
            this.recordCount = 0;
            this.rootNum = 0;
            this.file = new RandomAccessFile(file, "rwd");
            this.file.seek(0);
            this.file.writeLong(this.recordCount);
            this.file.writeLong(this.rootNum);
        }
        
        else
        {
            this.file = new RandomAccessFile(file,"rwd");
            this.file.seek(0);
            this.recordCount = (int) this.file.readLong();
            this.rootNum = (int) this.file.readLong();
        }	
	}
	
	public void insert(int id, int offset) throws IOException
	{
		int useRecord = -1;
		
		if(!idToRecordNum.containsKey(id))
		{
			if(recordCount <= 1)
			{
				useRecord = 0;
			}
			else
			{
				useRecord = findRecord(id, hmNode, rootNum);
			}
			
			if(!hmNode.containsKey(useRecord))
			{
				BtreeNode bN = new BtreeNode();
				hmNode.put(useRecord, bN); 
				recordCount++;
			}
			
			int insertIndex = hmNode.get(useRecord).insertLoc(id);
			hmNode.get(useRecord).move(insertIndex);
			hmNode.get(useRecord).insert(insertIndex, id, offset);
			splitNode(useRecord, hmNode);
		}
	}
	
	public void splitNode(int useRecord, HashMap<Integer, BtreeNode> hmNode)
	{
		int numCount = hmNode.get(useRecord).getRecordCount();
		if(numCount >= 5)
		{
			int parentID = hmNode.get(useRecord).getValue(8);
			int leftRecordNum = useRecord;
			
			BtreeNode right = new BtreeNode();
			int rightRecordNum = recordCount;
			hmNode.put(rightRecordNum, right);
			recordCount++;
			
			int offVal = hmNode.get(useRecord).getValue(9);
			
			if(useRecord == rootNum)
			{
				BtreeNode newRoot = new BtreeNode();
				recordCount++;
				
				newRoot.changeValue(1, leftRecordNum);
				newRoot.changeValue(2, parentID);
				newRoot.changeValue(3, offVal);
				newRoot.changeValue(4, rightRecordNum);
				rootNum = recordCount;
				hmNode.put(rootNum, newRoot);
			}
			else
			{
				int ins = hmNode.get(rootNum).insertLoc(parentID);
				hmNode.get(rootNum).move(ins);
				hmNode.get(rootNum).insert(ins, parentID, offVal);
				
				hmNode.get(rootNum).changeValue(ins - 1, leftRecordNum);
				hmNode.get(rootNum).changeValue(ins + 2, rightRecordNum);
			}
			
			int rightChild0 = hmNode.get(useRecord).getValue(10);
			int rightID_1 = hmNode.get(useRecord).getValue(11);
			int rightID_2 = hmNode.get(useRecord).getValue(14);
			int rightOff_1 = hmNode.get(useRecord).getValue(12);
			int rightOff_2 = hmNode.get(useRecord).getValue(15);
			int rightChild1 = hmNode.get(useRecord).getValue(13);
			int rightChild2 = hmNode.get(useRecord).getValue(16);
			
			hmNode.get(rightRecordNum).changeValue(0, rootNum);
			hmNode.get(rightRecordNum).changeValue(1, rightChild0);
			hmNode.get(rightRecordNum).changeValue(2, rightID_1);
			hmNode.get(rightRecordNum).changeValue(3, rightOff_1);
			hmNode.get(rightRecordNum).changeValue(4, rightChild1);
			hmNode.get(rightRecordNum).changeValue(5, rightID_2);
			hmNode.get(rightRecordNum).changeValue(6, rightOff_2);
			hmNode.get(rightRecordNum).changeValue(7, rightChild2);
			
			hmNode.get(useRecord).split(rootNum);
			
			splitNode(rootNum, hmNode);
		}
	}
	
	public int findRecord(int id, HashMap<Integer, BtreeNode> hmNode, int record)
	{
		int[] arr = hmNode.get(record).allKeys();
		
		for(int i = 0; i < arr.length; i++)
		{
			if(arr[i] > id)
			{
				int Child_record = hmNode.get(record).getValue(i*3 - 2);
				if(Child_record != -1)
				{
					findRecord(id, hmNode, Child_record);
				}
				else
				{
					return record;
				}
			}
		}
		
		return record;
	}

}
