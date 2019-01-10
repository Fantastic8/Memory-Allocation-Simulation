package randomseed;

import java.util.Random;

public class NormalDistributionRandom extends RandomProvider {

	private int param1;
	private int param2;
	private int max;
	private Random r;
	
	public NormalDistributionRandom(int p1,int p2,int max)
	{
		r=new Random();
		this.param1=p1;
		this.param2=p2;
		this.max=max;
	}
	@Override
	public int nextInt() {
		// TODO Auto-generated method stub
		return Math.abs((int)(param2*r.nextGaussian()+param1))%max;
	}
	//for single step below
	@Override
	public void addNext(int next) {
		// TODO Auto-generated method stub
		
	}

}
