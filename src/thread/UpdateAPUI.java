package thread;

import panels.AnalyzePanel;

public class UpdateAPUI implements Runnable {

	private AnalyzePanel AP;
	private String Operation;
	int[] param;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//System.out.println("Update UI Operation"+Operation+" param[0]="+param[0]+" param[1]="+param[1]);
		if(AP.getRunStatus()==0)//stop
		{
			return;
		}
		switch(Operation)
		{
		case "setNextPartition":AP.setNextPartition(param[0],param[1]);break;
		case "setCurrentPartition":AP.setCurrentPartition(param[0],param[1]);break;
		case "removePartition":AP.removePartition(param[0],param[1]);break;
		default:System.out.println("Update UI Error!");
		}
	}
	
	public UpdateAPUI(AnalyzePanel AP,String Operation,int[] param)
	{
		this.AP=AP;
		this.Operation=Operation;
		this.param=param;
	}
}
