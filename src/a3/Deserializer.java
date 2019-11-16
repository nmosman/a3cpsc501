package a3;
import org.jdom2.*;
import java.lang.reflect.*;
import java.util.*;


//to prevent cannot set static final long.. error, we use a hack from
//https://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection


public class Deserializer {

	public static Object deserialize(Document doc)
	{
		
		Element root = doc.getRootElement();
		List listOfObjects = root.getChildren();
		
		HashMap hMap = new HashMap();
		
		Object o = null;
		try
		{
			
			deserializeHelper(listOfObjects, hMap);
			handleFields(listOfObjects, hMap);
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
	
	
	private static Object deserializeFieldElement(Class fieldType, Element elemVal)
	{
		Object objectVal = null;
		
		if(fieldType.equals(int.class)) {
			objectVal = Integer.valueOf(elemVal.getText());
		}
		else if(fieldType.equals(short.class)) {
			objectVal = Short.valueOf(elemVal.getText());
		}
		else if(fieldType.equals(byte.class)) {
			objectVal = Byte.valueOf(elemVal.getText());
		}
		else if(fieldType.equals(long.class)) {
			objectVal = Long.valueOf(elemVal.getText());
		}
		else if(fieldType.equals(float.class)) {
			objectVal = Float.valueOf(elemVal.getText());
		}
		else if(fieldType.equals(double.class)) {
			objectVal = Double.valueOf(elemVal.getText());
		}
		else if(fieldType.equals(boolean.class)) {
			String s = elemVal.getText();
			if(s.equals("true"))
				objectVal = Boolean.TRUE;
			else
				objectVal = Boolean.FALSE;
			
		}
		
		return objectVal;
	}
	private static void handleFields(List listOfObjects, HashMap hMap)
	{
		for(int i = 0; i < listOfObjects.size(); i++)
		{
			try {
				Element objElem = (Element) listOfObjects.get(i);
				
				Object objCreation = hMap.get(objElem.getAttributeValue("id"));
				
				//get all children
				
				List children = objElem.getChildren();
				
				//so we either have an array object where we set a value to each element in array
				//or an object that isn't an array, where we can just give values to all of the variables
				//in the object
				
				Class clazz = objCreation.getClass();
				System.out.println(clazz.getName());
				
				
				if(clazz.isArray())
				{
					Class arrayType = clazz.getComponentType();
					
					for(int k = 0; k < children.size(); k++)
					{
						Element arrayElemContent = (Element) children.get(k);
						String contentType = arrayElemContent.getName();
						Object content = null;
						if(contentType.equals("reference"))
						{
							content = hMap.get(arrayElemContent.getText());
						}
						
						else if(contentType.equals("value"))
						{
							content = deserializeFieldElement(arrayType, arrayElemContent);
						}
						
						else
						{
							content = arrayElemContent.getText();
						}
					
						Array.set(objCreation, k, content);
					
					}
				}
				
				else
				{
					// not an array type here
					for(int k = 0; k < children.size(); k++)
					{
						
						Element fieldElem = (Element) children.get(k);
						
						Class declaringClass = Class.forName(fieldElem.getAttributeValue("declaringclass"));
						String fieldName = fieldElem.getAttributeValue("name");
						Field field = declaringClass.getDeclaredField(fieldName);
						
						field.setAccessible(true);
						
						Field modifiersField = Field.class.getDeclaredField("modifiers");
						modifiersField.setAccessible(true);
						modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
						Class fieldType = field.getType();
						Element fieldElemContent = (Element) fieldElem.getChildren().get(0);
						
						Object content = null;
						System.out.println(field.getName());
						if(!fieldType.isPrimitive())
						{
							content = hMap.get(fieldElemContent.getText());
						}
						
						else {
							content = deserializeFieldElement(fieldType, fieldElemContent);
						}
						
						//else
						//{
							//content = fieldElemContent.getText();
						//}
			
						
						field.set(objCreation, content);
					}
				}
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}



