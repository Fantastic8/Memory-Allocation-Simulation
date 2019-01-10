package randomseed;

public abstract class RandomProvider {
	public abstract int nextInt();//for no single step
	public abstract void addNext(int next);//for single step
}
