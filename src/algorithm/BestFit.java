package algorithm;

import java.util.LinkedList;

import panels.AnalyzePanel;
import thread.SingleWork;
import thread.UpdateAPUI;

public class BestFit extends AlgorithmSelector<LinkedList<DataItem>> {
	
	public BestFit(AnalyzePanel AP,AlgorithmContainer AC)
	{
		this.AP=AP;
		this.AC=AC;
		this.AvailableTable=new LinkedList<DataItem>();
		AvailableTable.add(new DataItem(0,1024,-1));//main
		OccupiedTable=new LinkedList<DataItem>();
	}

	@Override
	public synchronized int[] createWork(int size, int processingtime) {
		// TODO Auto-generated method stub
		for(int i=0;i<AvailableTable.size();i++)//search in avaliable table
		{
			DataItem dt=AvailableTable.get(i);
			if(dt.size>size)
			{
				//split data item
				DataItem dtn=new DataItem(dt.address,size,processingtime);//occupied block
				OccupiedTable.add(dtn);
				dt.size-=size;
				dt.address+=size;
				
				//sort available table in order of size
				AvailableTable.remove(dt);
				InsertIntoAvailableTableOrderBySize(dt);
				
				//start work process
				SingleWork sw=new SingleWork(dtn,AC);
				new Thread(sw).start();
				
				//PrintTable();
				return new int[]{dtn.address,size};
			}
			else if(dt.size==size)
			{
				dt.processingtime=processingtime;
				AvailableTable.remove(dt);
				OccupiedTable.add(dt);
				//start work process
				SingleWork sw=new SingleWork(dt,AC);
				new Thread(sw).start();
				
				//PrintTable();
				return new int[]{dt.address,size};
			}
		}
		return new int[]{-1,size};
	}
	
	@Override
	public synchronized void releaseWork(DataItem dt) {
		// TODO Auto-generated method stub
		//delete from table
		//System.out.println("BestFit.releaseWork: dt.address="+dt.address+" dt.size="+dt.size+" dt.processingtime="+dt.processingtime);
		int index=OccupiedTable.indexOf(dt);
		if(index==-1)
		{
			System.out.println("Release Work Error!(DataItem Not Found)");
			return;
		}
		
		dt.processingtime=-1;
		
		//release work from occupiedtable
		OccupiedTable.remove(dt);
		
		//insert work into AvailableTable in order of size
		boolean isemerge=false;
		for(int i=0;i<AvailableTable.size();i++)
		{
			if(AvailableTable.get(i).address+AvailableTable.get(i).size==dt.address)//front emerge
			{
				AvailableTable.get(i).size+=dt.size;
				dt=AvailableTable.get(i);//prepare for emerge behind
				
				//sort operation
				AvailableTable.remove(dt);//remove
				InsertIntoAvailableTableOrderBySize(dt);//insert
				
				isemerge=true;
				break;
			}
		}
		
		for(int i=0;i<AvailableTable.size();i++)
		{
			if(dt.address+dt.size==AvailableTable.get(i).address)//behind emerge
			{
				DataItem dtbehind=AvailableTable.get(i);
				dtbehind.address=dt.address;
				dtbehind.size+=dt.size;
				if(isemerge)//remove front
				{
					AvailableTable.remove(dt);
				}
				
				//sort operation
				dt=dtbehind;
				AvailableTable.remove(dt);//remove
				InsertIntoAvailableTableOrderBySize(dt);//insert
				isemerge=true;
				break;
			}
		}
		if(!isemerge)//no emerge
		{
			InsertIntoAvailableTableOrderBySize(dt);
		}
		
		//PrintTable();
	}
	
	public void InsertIntoAvailableTableOrderBySize(DataItem dt)
	{
		for(int i=0;i<AvailableTable.size();i++)
		{
			if(AvailableTable.get(i).size<=dt.size)
			{
				continue;
			}
			AvailableTable.add(i,dt);
			return;
		}
		AvailableTable.addLast(dt);
	}
}
