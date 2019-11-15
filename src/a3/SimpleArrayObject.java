package a3;

public class SimpleArrayObject {

	private int[] arrayInt; 
	public SimpleArrayObject() {}
	public SimpleArrayObject(int [] arr) {
		arrayInt = new int[arr.length];
		for(int i = 0; i < arr.length; i++)
		{
			arrayInt[i] = arr[i];
		}
	}
	
	public int[] getArray()
	{
		return arrayInt;
	}
}
