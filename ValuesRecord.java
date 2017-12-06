import java.util.*;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;	
import java.io.UnsupportedEncodingException;
public class ValuesRecords 
{
	private int cntRecords;
	private RandomAccessFile file;

	public ValuesRecords(String strFile) throws IOException
	{
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
	public String getLine(int key) throws IOException
	{
		this.file.seek(8 + ((key)*256));
		int len = this.file.readShort();
		byte[] array = new byte[len];
		this.file.seek(8+((key*256)+2));
		this.file.read(array);
		String str = new String(array,"UTF8");
		return str;
	}
	public  void insert(int key, String str) throws IOException
	{
		
		this.file.seek(8 + (key)*256);
		byte[] array = str.getBytes("UTF-8");
		this.file.writeShort(array.length);
		this.file.seek(8+((key*256)+2));
		this.file.write(array);
		this.file.seek(0);
	    cntRecords += 1;
	    this.file.writeLong(this.cntRecords);
	}
	
}
