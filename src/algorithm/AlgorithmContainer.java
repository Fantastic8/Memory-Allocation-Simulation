package algorithm;

import java.util.Date;
import java.util.LinkedList;

import panels.AnalyzePanel;
import randomseed.AverageRandom;
import randomseed.Fixed;
import randomseed.NormalDistributionRandom;
import randomseed.RandomProvider;
import thread.SingleWork;
import thread.UpdateAPUI;

public class AlgorithmContainer implements Runnable {
	private AnalyzePanel AP;
	private RandomProvider SizeRandomP;
	private RandomProvider ProcessingTimeRandomP;
	private AlgorithmSelector AlgorithmS;
	private String Algorithm;
	
	//params
	//private LinkedList<DataItem> Table;
	private int RunMode;//0-single step; 1-run till waits; 2-run till steps;
	private int RunStatus;//0-stop; 1-running; 2-pause;
	private int LoadTime;
	private int FormulaType;//0-average + average; 1-average + normal distribution; 2-normal distribution + average; 3-normal distribution + normal distribution;
	private int RunModeParam;
	
	//result
	private long inserttimes=0;
	private long waittimes=0;
	private long totalsize=0;
	private long totalprocessingtime=0;
	private double maxdebris;
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int size;
		int time;
		//printTable();
		RunStatus=1;//set RunStatus to running
		AP.setResultStatus("Running");//(Result) running
		AP.beginResultTime(new Date().getTime());//(Result) begin counting
		while(RunStatus!=0)
		{
			try {
				//next block
				size=SizeRandomP.nextInt();
				time=ProcessingTimeRandomP.nextInt();
				//System.out.println("AlgorithmContainer.Next Block: Random size="+size+" time="+time+" LoadTime="+LoadTime);
				if(RunMode==0&&size==-1&&time==-1)//single step
				{
					RunStatus=2;//pause
					checkPause();//pause
					continue;
				}
				checkPause();//pause
		//------thread update apanel next partition!!
				new Thread(new UpdateAPUI(AP,"setNextPartition",new int[]{size,time})).start();
				
				//load time
				Thread.sleep(LoadTime);
				
				checkPause();//double check pause
				
				//insert
				int address;
				int[] createresult;//0-address 1-size
				boolean isfirst=true;
				while(true)
				{
					createresult=AlgorithmS.createWork(size,time);
					address=createresult[0];
					//System.out.println("AlgorithmContainer.Next insert address="+address);
					if(address>=0)
					{
						totalsize+=size;//add real size
						size=createresult[1];//resize size into virtual size
						AP.setResultTotalSize(totalsize);//(result) total size
						totalprocessingtime+=time;
						AP.setResultTotalProcessingTime(totalprocessingtime);//(result) total processing time
						AP.setResultInsertTimes(++inserttimes);//(result) insert times +1
						break;
					}
					
//==================wait action
					if(isfirst)
					{
						isfirst=false;
						
						AP.setResultWaitTimes(++waittimes);//(result) wait times +1
						new Thread(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								AP.setResultMaxDebris(getDebris());//(result) max debris
							}}).start();
						
						if(RunMode==1)//run till waits
						{
							//System.out.println("Run Till Waits waits="+RunModeParam);
							RunModeParam--;							
							if(RunModeParam<=0)
							{
								AP.setResultStatus("Done");//(Result) Done
								AP.ACFinish();//notice finish
								return;
							}
						}
					}
					
					
					//wait
					Thread.sleep(100);
				}
				
		//------thread update apanel current partition
				new Thread(new UpdateAPUI(AP,"setCurrentPartition",new int[]{address,size})).start();
				
				if(RunMode==0&&size==-1&&time==-1)//single step
				{
					RunStatus=2;//pause
				}
				if(RunMode==2)//run till steps
				{
					RunModeParam--;
					//System.out.println("Run Till Steps steps="+RunModeParam);
					if(RunModeParam<=0)
					{
						AP.setResultStatus("Done");//(Result) done
						AP.ACFinish();//notice finish
						return;
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				AP.setResultStatus("Stop");//(Result) stop
				AP.ACFinish();//notice finish
				return;
			}
		}
		AP.setResultStatus("Stop");//(Result) stop
		AP.ACFinish();//notice finiSsh
	}
	
//get
	public int getRunStatus()
	{
		return RunStatus;
	}
	
	public double getDebris()
	{
		int occupiedsize=0;
		double debris;
		for(int i=0;i<AlgorithmS.getOccupiedTable().size();i++)
		{
			occupiedsize+=((DataItem)AlgorithmS.getOccupiedTable().get(i)).size;
		}
		debris=(1024-occupiedsize)/10.24;
		if(debris>maxdebris)
		{
			maxdebris=debris;
		}
		return maxdebris;
	}
	
	public AlgorithmSelector getAlgorithmSelector()
	{
		return AlgorithmS;
	}
	
	public AnalyzePanel getAnalyzePanel()
	{
		return AP;
	}
//Action
	public synchronized void Pause()
	{
		RunStatus=2;
	}
	
	public synchronized void Continue()
	{
		RunStatus=1;
	}
	
	public synchronized void Continue(int sizefixed,int processingtimefixed)//single step
	{
		RunStatus=1;
		SizeRandomP.addNext(sizefixed);
		ProcessingTimeRandomP.addNext(processingtimefixed);
	}
	
	public synchronized void Stop()
	{
		RunStatus=0;
	}
	
	public void checkPause()
	{
		if(RunStatus!=1)
		{
			AP.setResultStatus("Pause");//(Result) running
			while(RunStatus!=1)//puase loop
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					AP.setResultStatus("Stop");//(Result) running
					AP.ACFinish();//notice finish
					return;
				}
			}
		}
		
		AP.setResultStatus("Running");//(Result) running
	}
	
//constructor
	//single step
	public AlgorithmContainer(AnalyzePanel AP,String Algorithm,int RunMode,int LoadTime,int sizefixed, int processingtimefixed)//formula type 3: normal distribution + normal distribution
	{
		/*
		 * 
		 * RunMode
		 * 0:single step
		 * 1:run till waits
		 * 2:run till steps
		 * 
		 */
		//config
		this.AP=AP;
		this.Algorithm=Algorithm;
		this.LoadTime=LoadTime;
		this.RunMode=RunMode;
		this.RunStatus=0;
		//System.out.println("formula type="+FormulaType);
		if(RunMode==0)
		{
			SizeRandomP=new Fixed();
			ProcessingTimeRandomP=new Fixed();
			SizeRandomP.addNext(sizefixed);
			ProcessingTimeRandomP.addNext(processingtimefixed);
		}
		else
		{
			System.out.println("Algorithm Container Error!");
		}
		//algorithm
		switch(Algorithm)
		{
		case "First Fit":AlgorithmS=new FirstFit(AP,AlgorithmContainer.this);break;
		case "Next Fit":AlgorithmS=new NextFit(AP,AlgorithmContainer.this);break;
		case "Best Fit":AlgorithmS=new BestFit(AP,AlgorithmContainer.this);break;
		case "Worst Fit":AlgorithmS=new WorstFit(AP,AlgorithmContainer.this);break;
		case "Buddy System":AlgorithmS=new BuddySystem(AP,AlgorithmContainer.this);break;
		default:System.out.println("Algorithm Missing!");
		}
		//System.out.println("Algorithm="+Algorithm+" RunMode="+RunMode+" LoadTime="+LoadTime+" sizefixed="+sizefixed+"processingtimfixed"+processingtimefixed);
	}
	
	//not single step
	public AlgorithmContainer(AnalyzePanel AP,String Algorithm,int RunMode,int RunModeparam,int LoadTime,int FormulaType,int SizeParam1,int SizeParam2,int ProcessingTimeParam1,int ProcessingTimeParam2)//formula type 3: normal distribution + normal distribution
	{
		/*
		 * 
		 * RunMode
		 * 0:single step
		 * 1:run till waits
		 * 2:run till steps
		 * 
		 *Formula Type
		 *    Size      Processing Time
		 * 0: average + average
		 * 1: average + normal distribution
		 * 2: normal distribution + average
		 * 3: normal distribution + normal distribution
		 * 
		 * 
		 */
		//config
		this.AP=AP;
		this.Algorithm=Algorithm;
		this.LoadTime=LoadTime;
		this.RunMode=RunMode;
		this.RunModeParam=RunModeparam;
		this.RunStatus=0;
		this.FormulaType=FormulaType;
		//System.out.println("formula type="+FormulaType);
		if(FormulaType<2)//size - average
		{
			SizeRandomP=new AverageRandom(SizeParam1,SizeParam2);
		}
		else//size - normal distribution
		{
			SizeRandomP=new NormalDistributionRandom(SizeParam1,SizeParam2,1024);
		}
		
		if(FormulaType%2==0)//processing time - average
		{
			ProcessingTimeRandomP=new AverageRandom(ProcessingTimeParam1,ProcessingTimeParam2);
		}
		else//processing time - normal distribution
		{
			ProcessingTimeRandomP=new NormalDistributionRandom(ProcessingTimeParam1,ProcessingTimeParam2,5000);
		}
		//algorithm
		switch(Algorithm)
		{
		case "First Fit":AlgorithmS=new FirstFit(AP,AlgorithmContainer.this);break;
		case "Next Fit":AlgorithmS=new NextFit(AP,AlgorithmContainer.this);break;
		case "Best Fit":AlgorithmS=new BestFit(AP,AlgorithmContainer.this);break;
		case "Worst Fit":AlgorithmS=new WorstFit(AP,AlgorithmContainer.this);break;
		case "Buddy System":AlgorithmS=new BuddySystem(AP,AlgorithmContainer.this);break;
		default:System.out.println("Algorithm Missing!");
		}
	}
}
