package algorithm;

import java.util.LinkedList;

import panels.AnalyzePanel;
import thread.SingleWork;
import thread.UpdateAPUI;

public class NextFit extends AlgorithmSelector<LinkedList<DataItem>>{
	private DataItem nextitem;
	public NextFit(AnalyzePanel AP,AlgorithmContainer AC)
	{
		this.AC=AC;
		this.AP=AP;
		this.AvailableTable=new LinkedList<DataItem>();
		AvailableTable.add(new DataItem(0,1024,-1));//main
		OccupiedTable=new LinkedList<DataItem>();
		
		//initialize next item
		nextitem=AvailableTable.getFirst();
	}
	
	@Override
	public synchronized int[] createWork(int size, int processingtime) {
		// TODO Auto-generated method stub
		int lenth=AvailableTable.size();
		int start=AvailableTable.indexOf(nextitem);//begin with next blank
		if(start==-1)//no enough avaliable space
		{
			//System.out.println("NextFit.creatework");
			//PrintTable();
			return new int[]{-1,size};
		}
		for(int i=start,num=0;num<lenth;i=(i+1)%lenth,num++)
		{
			DataItem dt=AvailableTable.get(i);
			if(dt.size>size)
			{
				//split data item
				DataItem dtn=new DataItem(dt.address,size,processingtime);
				OccupiedTable.add(dtn);
				dt.size-=size;
				dt.address+=size;
				nextitem=dt;//set next item pointer
				//start work process
				SingleWork sw=new SingleWork(dtn,AC);
				new Thread(sw).start();
				
				//System.out.println("NextFit.creatework");
				//PrintTable();
				
				return new int[]{dtn.address,size};
			}
			else if(dt.size==size)
			{
				dt.processingtime=processingtime;
				AvailableTable.remove(dt);
				OccupiedTable.add(dt);
				//set next pointer
				if(AvailableTable.size()==0)
				{
					nextitem=null;
				}
				else
				{
					nextitem=AvailableTable.get((i+1)%AvailableTable.size());
				}
				//start work process
				SingleWork sw=new SingleWork(dt,AC);
				new Thread(sw).start();
				
				//System.out.println("NextFit.creatework");
				//PrintTable();
				return new int[]{dt.address,size};
			}
		}
		//System.out.println("NextFit.creatework");
		//PrintTable();
		return new int[]{-1,size};//no enough avaliable space
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
		
		//insert work into AvailableTable inorder
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
					AvailableTable.addFirst(dt);
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
						//update next item
						if(dt.equals(nextitem))
						{
							nextitem=AvailableTable.get(i);
						}
						
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
