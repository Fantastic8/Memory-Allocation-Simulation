package randomseed;

import java.util.Random;

public class AverageRandom extends RandomProvider {
	private int max;
	private int min;
	private Random r;
	
	public AverageRandom(int min,int max)
	{
		r=new Random();
		this.max=max;
		this.min=min;
		//System.out.println("max= "+max+" min="+min);
	}
	@Override
	public int nextInt() {
		// TODO Auto-generated method stub
		if(max<=min)
		{
			return min;
		}
		return Math.abs(r.nextInt())%(max-min)+min;
	}
	// for single step below
	@Override
	public void addNext(int next) {
		// TODO Auto-generated method stub
		
	}

}
