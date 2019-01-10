package thread;

import java.util.LinkedList;

import algorithm.AlgorithmContainer;
import algorithm.DataItem;

public class SingleWork implements Runnable {
	private DataItem work;
	private AlgorithmContainer AC;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(AC.getRunStatus()==0)//stop
			{
				return;
			}
			//System.out.println("SingleWork.begin work: address="+work.address+" size="+work.size+" processingtime="+work.processingtime);
			Thread.sleep(work.processingtime);
			if(AC.getRunStatus()==0)//stop double check
			{
				return;
			}
			//System.out.println("SingleWork.work done:   address="+work.address+" size="+work.size+" processingtime="+work.processingtime);			
			AC.getAlgorithmSelector().releaseWork(work);//in this function work could be changed			
			
			//--thread update UI remove partition
			new Thread(new UpdateAPUI(AC.getAnalyzePanel(),"removePartition",new int[]{work.address,work.size})).start();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//System.out.println("address="+work.address+" size="+work.size+" processingtime="+work.processingtime);
			e.printStackTrace();
		}
	}
	
	public SingleWork(DataItem work,AlgorithmContainer AC)
	{
		this.work=work;
		this.AC=AC;
	}
}
