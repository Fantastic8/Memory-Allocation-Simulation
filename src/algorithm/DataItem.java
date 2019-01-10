package algorithm;

public class DataItem
{
	public int size;
	public int address;
	public int processingtime;
	public DataItem(int address,int size,int processingtime)
	{
		this.size=size;
		this.address=address;
		this.processingtime=processingtime;
	}
	
	public DataItem(DataItem dt)
	{
		this.size=dt.size;
		this.address=dt.address;
		this.processingtime=dt.processingtime;
	}
}
