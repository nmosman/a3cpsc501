package a3;
import org.jdom2.*;
import java.lang.reflect.*;
import java.util.*;

public class Deserializer {

	public static Object deserialize(Document doc)
	{
		
		Element root = doc.getRootElement();
		List listOfObjects = root.getChildren();
		
		HashMap hMap = new HashMap();
		
		Object o = null;
		try
		{
			
			
			//retrieve main object which has id of 0 from hashMap
			o = hMap.get("0");
			
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return o;
	}
	
	private static void deserializeHelper(List listOfObjects, HashMap hMap)
	{
		for(int i = 0; i < listOfObjects.size(); i++)
		{
			try
			{
				Element objElem = (Element) listOfObjects.get(i);
				
				//
				Class clazz = Class.forName(objElem.getAttributeValue("class"));
				
				//checking type of object
				Object objCreation = null;
				
				if(clazz.isArray())
				{
					int length = Integer.parseInt(objElem.getAttributeValue("length"));
					Class arrayType = clazz.getComponentType();
					
					objCreation = Array.newInstance(arrayType, length);
				}
				else
				{
					// is not an array type object
					
					Constructor c = clazz.getDeclaredConstructor(null);
					c.setAccessible(true);
					
					objCreation = c.newInstance(null);
					
					
				}
				
				String id = objElem.getAttributeValue("id");
				hMap.put(id, objCreation);
				
			}
			
			catch(Exception e)
			{
				
			}
		}
	}
}
