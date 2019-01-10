package algorithm;

import java.util.LinkedList;

import panels.AnalyzePanel;
import thread.SingleWork;
import thread.UpdateAPUI;

public class FirstFit extends AlgorithmSelector<LinkedList<DataItem>>{
	public FirstFit(AnalyzePanel AP,AlgorithmContainer AC)
	{
		this.AC=AC;
		this.AP=AP;
		AvailableTable=new LinkedList<DataItem>();
		AvailableTable.add(new DataItem(0,1024,-1));//main
		OccupiedTable=new LinkedList<DataItem>();
		//PrintTable();
	}
	
	@Override
	public synchronized int[] createWork(int size,int processingtime)//return the address which was inserted
	{
		for(int i=0;i<AvailableTable.size();i++)//search in Available table
		{
			DataItem dt=AvailableTable.get(i);
			if(dt.size>size)
			{
				//split data item
				DataItem dtn=new DataItem(dt.address,size,processingtime);//occupied block
				//System.out.println("FirstFit.creatework.if dt.size>size: dt.address="+dt.address+" size="+size+"precessingtime="+processingtime);
				OccupiedTable.add(dtn);
				dt.size-=size;
				dt.address+=size;
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
		//System.out.println("FirstFit.releaseWork: dt.address="+dt.address+" dt.size="+dt.size+" dt.processingtime="+dt.processingtime);
		int index=OccupiedTable.indexOf(dt);
		if(index==-1)
		{
			System.out.println("Release Work Error!(DataItem Not Found)");
			return;
		}
		
		dt.processingtime=-1;
		
		//release work from occupiedtable
		OccupiedTable.remove(dt);
		
		//insert work into Availabletable inorder
		boolean isinsert=false;
		for(int i=0;i<AvailableTable.size();i++)
		{
			if(AvailableTable.get(i).address<=dt.address)
			{
				continue;
			}
			if(i==0)//insert first
			{
				if(dt.address+dt.size==AvailableTable.get(i).address)//emerge behind
				{
					AvailableTable.get(i).address=dt.address;
					AvailableTable.get(i).size+=dt.size;
					isinsert=true;
				}
				else
				{
					AvailableTable.add(0,dt);
					isinsert=true;
				}
			}
			else
			{
				if(AvailableTable.get(i-1).address+AvailableTable.get(i-1).size==dt.address)//emerge front
				{
					AvailableTable.get(i-1).size+=dt.size;
					dt=AvailableTable.get(i-1);//prepare for emerge behind
					isinsert=true;
				}
				if(dt.address+dt.size==AvailableTable.get(i).address)//emerge behind
				{
					AvailableTable.get(i).address=dt.address;
					AvailableTable.get(i).size+=dt.size;
					if(isinsert)//remove front
					{
						AvailableTable.remove(dt);
					}
					isinsert=true;
				}
				if(!isinsert)
				{
					AvailableTable.add(i,dt);
					isinsert=true;
				}
			}
			break;
		}
		//insert into last
		if(AvailableTable.size()==0)
		{
			AvailableTable.addLast(dt);
		}
		else
		{
			if(AvailableTable.getLast().address+AvailableTable.getLast().size==dt.address)//emerge front
			{
				AvailableTable.getLast().size+=dt.size;
				isinsert=true;
			}
			else if(!isinsert)
			{
				AvailableTable.addLast(dt);
				isinsert=true;
			}
		}
		//PrintTable();
	}
}
