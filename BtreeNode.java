
public class BtreeNode {
	
	private int recordCount;
	private int recordID;
	
	private int parent;
	
	private int child_1;
	private int key_1;
	private int offset_1;
	
	private int child_2;
	private int key_2;
	private int offset_2;
	
	private int child_3;
	private int key_3;
	private int offset_3;
	
	private int child_4;
	private int key_4;
	private int offset_4;
	
	private int child_5;
	
	private int key_5;
	private int offset_5;
	private int child_6;
	
	public BtreeNode()
	{
		recordCount = 0;
		
		parent = -1;
		
		child_1 = -1;
		key_1 = -1;
		offset_1 = -1;
		
		child_2 = -1;
		key_2 = -1;
		offset_2 = -1;
		
		child_3 = -1;
		key_3 = -1;
		offset_3 = -1;
		
		child_4 = -1;
		key_4 = -1;
		offset_4 = -1;
		
		child_5 = -1;
		
		child_6 = -1;
		key_5 = -1;
		offset_5 = -1;
	}
	
	public int getRecordID()
	{
		return recordID;
	}
	
	public void computeRecordCount()
	{
		recordCount = 0;
		
		if(key_1 != -1)
		{
			recordCount++;
		}
		
		if(key_2 != -1)
		{
			recordCount++;
		}
		
		if(key_3 != -1)
		{
			recordCount++;
		}
		
		if(key_4 != -1)
		{
			recordCount++;
		}
		
		if(key_5 != -1)
		{
			recordCount++;
		}
	}
	
	public int getRecordCount()
	{
		computeRecordCount();
		return recordCount;
	}
	
	public int getValue(int index)
	{
		if(index == 0)
		{
			return parent;
		}
		
		else if(index == 1)
		{
			return child_1;
		}
		else if(index == 2)
		{
			return key_1;
		}
		else if(index == 3)
		{
			return offset_1;
		}
		
		else if(index == 4)
		{
			return child_2;
		}
		else if(index == 5)
		{
			return key_2;
		}
		else if(index == 6)
		{
			return offset_2;
		}
		
		else if(index == 7)
		{
			return child_3;
		}
		else if(index == 8)
		{
			return key_3;
		}
		else if(index == 9)
		{
			return offset_3;
		}
		
		else if(index == 10)
		{
			return child_4;
		}
		else if(index == 11)
		{
			return key_4;
		}
		else if(index == 12)
		{
			return offset_4;
		}
		
		else if(index == 13)
		{
			return child_5;
		}
		
		else if(index == 14)
		{
			return key_5;
		}
		
		else if(index == 15)
		{
			return offset_5;
		}
		
		else
		{
			return child_6;
		}
	}
	
	public void changeValue(int index, int newVal)
	{
		if(index == 0)
		{
			parent = newVal;
		}
		
		else if(index == 1)
		{
			child_1 = newVal;
		}
		else if(index == 2)
		{
			key_1 = newVal;
		}
		else if(index == 3)
		{
			offset_1 = newVal;
		}
		
		else if(index == 4)
		{
			child_2 = newVal;
		}
		else if(index == 5)
		{
			key_2 = newVal;
		}
		else if(index == 6)
		{
			offset_2 = newVal;
		}
		
		else if(index == 7)
		{
			child_3 = newVal;
		}
		else if(index == 8)
		{
			key_3 = newVal;
		}
		else if(index == 9)
		{
			offset_3 = newVal;
		}
		
		else if(index == 10)
		{
			child_4 = newVal;
		}
		else if(index == 11)
		{
			key_4 = newVal;
		}
		else if(index == 12)
		{
			offset_4 = newVal;
		}
		
		else if(index == 13)
		{
			child_5 = newVal;
		}
		
		else if(index == 14)
		{
			key_5 = newVal;
		}
		
		else if(index == 15)
		{
			offset_5 = newVal;
		}
		
		else
		{
			child_6 = newVal;
		}
	}
	
	public int insertLoc(int id)
	{
		int location = -1;
		
		for(int i = 2; i < 17; i += 3)
		{	
			if(this.getValue(i) == -1 || this.getValue(i) > id)
			{
				location = i;
				return location;
			}
		}
		
		return location;
	}
	
	public void move(int insertIndex)
	{
		computeRecordCount();
		int counter = (recordCount*3) + 1;
		
		while(counter >= insertIndex)
		{
			int temp = this.getValue(counter);
			this.changeValue(counter + 3, temp);
			counter--;
		}
		
		for(int i = insertIndex; i < insertIndex + 3; i++)
		{
			this.changeValue(i, -1);
		}
	}
	
	public void insert(int insertIndex, int key, int offsetValue)
	{
		changeValue(insertIndex, key);
		changeValue(insertIndex + 1, offsetValue);
	}
	
	public void split(int parentID)
	{
		for(int i = 8; i < 17; i++)
		{
			this.changeValue(i, -1);
		}
		
		this.changeValue(0, parentID);
	}
	
	public int[] getData()
	{
		int[] allData = new int[14];
		
		allData[0] = parent;
		allData[1] = child_1;
		allData[2] = key_1;
		allData[3] = offset_1;
		allData[4] = child_2;
		allData[5] = key_2;
		allData[6] = offset_2;
		allData[7] = child_3;
		allData[8] = key_3;
		allData[9] = offset_3;
		allData[10] = child_4;
		allData[11] = key_4;
		allData[12] = offset_4;
		allData[13] = child_5;
		
		return allData;
	}
	
	
	
	

}
