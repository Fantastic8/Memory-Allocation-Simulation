package algorithm;

import java.util.ArrayList;
import java.util.LinkedList;

import panels.AnalyzePanel;
import thread.SingleWork;
import thread.UpdateAPUI;

public class BuddySystem extends AlgorithmSelector<ArrayList<LinkedList<DataItem>>> {

	public BuddySystem(AnalyzePanel AP,AlgorithmContainer AC)
	{
		this.AP=AP;
		this.AC=AC;
		AvailableTable=new ArrayList<LinkedList<DataItem>>();
		OccupiedTable=new LinkedList<DataItem>();
		//add sub list
		for(int i=0,max=1024;max>1;max=max/2,i++)
		{
			AvailableTable.add(new LinkedList<DataItem>());
		}
		AvailableTable.get(AvailableTable.size()-1).add(new DataItem(0,1024,-1));//add first
		//System.out.println("Initialized");
		//printLinkedAvailableTable();
	}
	
	@Override
	public synchronized int[] createWork(int size, int processingtime) {
		// TODO Auto-generated method stub
		for(int i=0;i<AvailableTable.size();i++)
		{
			if((int)Math.pow(2, i+1)<size)
			{
				continue;
			}
			//2^i < size <= 2^(i+1)
			//distribute AvailableTable(i)
			int j=i;
			for(;AvailableTable.get(j).size()<=0;j++)//request
			{
				if(j>=AvailableTable.size()-1)//no enough space
				{
					//printLinkedAvailableTable();
					//printOccupiedTable();
					return new int[]{-1,size};
				}
			}
			DataItem dt=AvailableTable.get(j).pollFirst();
			for(;i<j;j--)//distribute space
			{
				//split
				DataItem dtbehind=new DataItem(dt.address+dt.size/2,dt.size/2,-1);
				dt.size=dt.size/2;
				AvailableTable.get(j-1).add(dtbehind);
			}
			//distribute work
			//DataItem dt=AvailableTable.get(i).pollFirst();
			if(dt!=null)
			{
				dt.processingtime=processingtime;
				OccupiedTable.add(dt);
				//start work process
				SingleWork sw=new SingleWork(dt,AC);
				new Thread(sw).start();
				//printLinkedAvailableTable();
				//printOccupiedTable();
				return new int[]{dt.address,dt.size};
			}
			else
			{
				//printLinkedAvailableTable();
				//printOccupiedTable();
				return new int[]{-3,size};
			}
		}
		//printLinkedAvailableTable();
		//printOccupiedTable();
		return new int[]{-2,size};
		
	}

	@Override
	public synchronized void releaseWork(DataItem tdt) {
		// TODO Auto-generated method stub
		//delete from table
		int index=OccupiedTable.indexOf(tdt);
		if(index==-1)
		{
			System.out.println("Release Work Error!(DataItem Not Found)");
			return;
		}
		
		tdt.processingtime=-1;
		
		//release work from occupiedtable
		OccupiedTable.remove(tdt);
		
		DataItem dt=new DataItem(tdt);//temp var
		//insert into availableTable
		//calculate index
		int i=0,caltsize=dt.size;
		for(;caltsize>1;i++)
		{
			caltsize=caltsize/2;
		}
		i--;
		//return to availableTable(i) linkedlist
		for(int aindex=i;;aindex++)
		{
			boolean isemerge=false;
			//check emerge
			for(DataItem d:AvailableTable.get(aindex))
			{
				//if((d.address+d.size==dt.address)||(dt.address+dt.size==d.address))//emerge buddy
				//{
				if(d.address==dt.address+(int)Math.pow(2, aindex+1)*((dt.address%((int)Math.pow(2, aindex+2))==0)?1:-1))//emerge buddy
				{
					//remove from this linkedlist
					AvailableTable.get(aindex).remove(d);
					dt.address=d.address<dt.address?d.address:dt.address;
					dt.size*=2;
					isemerge=true;
					break;
				}
			}
			if(!isemerge)
			{
				//insert
				AvailableTable.get(aindex).add(dt);
				break;
			}
		}
		//System.out.println("Release Work Done ");
		//printLinkedAvailableTable();
		//printOccupiedTable();
	}
}
