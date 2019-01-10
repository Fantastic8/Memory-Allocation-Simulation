package algorithm;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import panels.AnalyzePanel;

public abstract class AlgorithmSelector <T extends List> {
	protected AnalyzePanel AP;
	protected AlgorithmContainer AC;
	protected T AvailableTable;
	protected LinkedList<DataItem> OccupiedTable;
	public abstract int[] createWork(int size,int processingtime);
	public abstract void releaseWork(DataItem dt);
	
	public T getAvaliableTable()
	{
		return AvailableTable;
	}
	
	public LinkedList<DataItem> getOccupiedTable()
	{
		return OccupiedTable;
	}
	
	public void printAvaliableTable()
	{
		System.out.println("----AvaliableTable----");
		for(int i=0;i<AvailableTable.size();i++)
		{
			System.out.println("AvaliableTabel <"+i+"> Address="+((DataItem)AvailableTable.get(i)).address+" Size="+((DataItem)AvailableTable.get(i)).size+" ProcessingTime="+((DataItem)AvailableTable.get(i)).processingtime);
		}
		System.out.println("====AvaliableTable====");
	}
	
	public void printLinkedAvailableTable()
	{
		System.out.println("----AvaliableTable----");
		//title
		for(int i=0;i<AvailableTable.size();i++)
		{
			System.out.print(String.format("%5d",(int)Math.pow(2, i+1)));
		}
		System.out.println(" ");
		//content
		for(int i=0;;i++)
		{
			boolean s=false;
			for(int j=0;j<AvailableTable.size();j++)
			{
				if(((LinkedList<DataItem>)AvailableTable.get(j)).size()<=i)
				{
					System.out.print("     ");
				}
				else
				{
					System.out.print(String.format("%5d",((LinkedList<DataItem>)AvailableTable.get(j)).get(i).address));
					s=true;
				}
			}
			System.out.println(" ");
			if(!s)
			{
				break;
			}
		}
		System.out.println("\n====AvaliableTable====");
	}
	
	public void printOccupiedTable()
	{
		System.out.println("----OccupiedTable----");
		for(int i=0;i<OccupiedTable.size();i++)
		{
			System.out.println("OccupiedTable <"+i+"> Address="+OccupiedTable.get(i).address+" Size="+OccupiedTable.get(i).size+" ProcessingTime="+OccupiedTable.get(i).processingtime);
		}
		System.out.println("====OccupiedTable====");
	}
	
	public void PrintTable()
	{
		printAvaliableTable();
		printOccupiedTable();
	}
}
