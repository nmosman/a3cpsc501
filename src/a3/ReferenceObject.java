package a3;

public class ReferenceObject {
	private SimpleObject simpleObj;
	public ReferenceObject() {}
	
	
	//not making a deep copy here! 
	public ReferenceObject(SimpleObject s)
	{
		simpleObj = s;
	}
	
}
