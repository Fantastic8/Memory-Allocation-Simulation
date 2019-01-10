package randomseed;

import java.util.LinkedList;

public class Fixed extends RandomProvider {
	private LinkedList<Integer> fixed;

	@Override
	public int nextInt() {
		// TODO Auto-generated method stub
		if(fixed.size()<=0)
		{
			//System.out.println("size="+fixed.size());
			return -1;
		}
		int next=fixed.get(0);
		fixed.remove(0);//remove first
		//System.out.println("after has next size="+fixed.size());
		return next;
	}
	
	public Fixed()
	{
		fixed=new LinkedList<Integer>();
	}

	@Override
	public void addNext(int next) {
		// TODO Auto-generated method stub
		fixed.addLast(next);
		//System.out.println("after add last size="+fixed.size());
	}
}
