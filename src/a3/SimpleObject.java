package a3;

public class SimpleObject {
	private int val1;
	private double val2;
	public SimpleObject() {}
	
	public SimpleObject(int val1, double val2)
	{
		this.val1 = val1;
		this.val2 = val2;
	}
	
	public int getIntVal() {
		return val1;
	}
	
	public double getDoubleVal()
	{
		return val2;
	}
}
