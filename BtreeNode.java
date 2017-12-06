//holds important information for each node
//includes all the ID in that node, their equivalent offset, and the ID of the node's children
public class BtreeNode {
	
	private int order; //indicates the maximum number of keys or ID in the node
	private int Node_Size;	//maximum size to hold all the important information
	private int middle_key;	//indicates the index of the middle key
	
	private int recordCount;	//indicates the number of ID in the node
	
	private int[] data_values;	//holds all the important information of the node
	
	public BtreeNode(int ord)
	{
		order = ord;
		Node_Size = ((order - 1)*3) + 2;
		
		//determines the index of the middle key
		if(order % 2 != 0)
		{
			middle_key = (((order + 1)/2)*3) - 1;
		}
		else
		{
			middle_key = (((order)/2)*3) - 1;
		}
		
		recordCount = 0;
		data_values = new int[Node_Size + 3];
		
		//by default, sets all the values of data_values to -1
		for(int i = 0; i < data_values.length; i++)
		{
			data_values[i] = -1;
		}
	}
	
	//computes the total recordCount
	public void computeRecordCount()
	{
		recordCount = 0;
		
		for(int i = 2; i < data_values.length; i+=3)
		{
			if(data_values[i] != -1)
			{
				recordCount++;
			}
		}
	}
	
	//returns the recordCount
	public int getRecordCount()
	{
		computeRecordCount();
		return recordCount;
	}
	
	//returns the middle key
	public int getMiddleKey()
	{
		return middle_key;
	}
	
	//returns the array containing all the important data
	public int[] getAllData()
	{
		return data_values;
	}
	
	//returns a data in data_values given an index
	public int getValue(int index)
	{
		return data_values[index];
	}
	
	//changes a data in data_values indicated by the index
	public void changeValue(int index, int newVal)
	{
		data_values[index] = newVal;
	}
	
	//determines where in data_values should the new ID be inserted
	public int insertLoc(int id)
	{
		int location = -1;
		
		for(int i = 2; i < (Node_Size + 3); i += 3)
		{	
			if(this.getValue(i) == -1 || this.getValue(i) > id)
			{
				location = i;
				return location;
			}
		}
		
		return location;
	}
	
	//moves the subsequent data by one space to the right to make space for the new ID
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
	
	//inserts the ID and its equivalent offset to data_values
	public void insert(int insertIndex, int key, int offsetValue)
	{
		changeValue(insertIndex, key);
		changeValue(insertIndex + 1, offsetValue);
	}
	
	//handles what happens to data_values when a split happens
	public void split(int parentID)
	{
		for(int i = middle_key; i < (Node_Size + 3); i++)
		{
			this.changeValue(i, -1);
		}
		
		this.changeValue(0, parentID);
	}
	
	//returns data_values excluding the slots dedicated for the excess ID
	public int[] getData()
	{
		int[] dataWithNoSplitSpace = new int[Node_Size];
		
		for(int i = 0; i < data_values.length - 3; i++)
		{
			dataWithNoSplitSpace[i] = data_values[i];
		}
		
		return dataWithNoSplitSpace;
	}
	
	//returns all the ID and their equivalent offset
	public int[] getKeys(int record)
	{
		int[] d = new int[((Node_Size - 2)/3)*2];
		int index = 0;
		
		for(int i = 2; i < data_values.length - 3; i += 3)
		{
			d[index] = data_values[i];
			index++;
			d[index] = data_values[i+1];
			index++;
		}
		
		return d;
	}
	
	
	
	

}
