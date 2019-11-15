package a3;

import org.jdom2.*;
import java.util.*; 
import java.lang.reflect.*;
public class Serializer {
	

	public static Document serialize(Object obj)
	{
		
		Element root = new Element("serialized");
		Document doc = new Document(root);
	    IdentityHashMap hMap = new IdentityHashMap<>();
		
	    
	    
	    return serializeClass(obj, doc, hMap);
	}
	
	
	public static Document serializeClass(Object obj, Document doc, IdentityHashMap hMap)
	{
		// Element for class, fields, the actual value and reference 
		
		

		ArrayList<Element> contents = new ArrayList<>();
		ArrayList<Element> references = new ArrayList<>();
		ArrayList<Element> arrayVals = new ArrayList<>();
		
		Class clazz = obj.getClass();
		
		Element classElem;
		Element fieldElem;
		Element valElem;
		Element refElem;
		
		
		try {
			
			
			
			String id = Integer.toString(hMap.size());
			hMap.put(id, obj);
			classElem = new Element("object");
			classElem.setAttribute("class", clazz.getName());
			classElem.setAttribute("id", id);
			
			doc.getRootElement().addContent(classElem);
			
			//check to see if its an array first
			//then if it is, we need to check if its a primitive type or an array of objects
			if(clazz.isArray())
			{
				
				String lengthOfArray = String.valueOf(Array.getLength(obj));
				classElem.setAttribute("length", lengthOfArray);
				
				
				for(int i = 0; i < Array.getLength(obj); i++)
				{
					if(clazz.getComponentType().isPrimitive())
					{
						valElem = new Element("value");
						String val = String.valueOf(Array.get(obj, i));
						valElem.addContent(val);
						arrayVals.add(valElem);
						contents = arrayVals;
					}
					else
					{
					// dealing with reference types here
					
						id = Integer.toString(hMap.size());
						refElem = new Element("reference");
						
						refElem.addContent(id);
						
						references.add(refElem);
						contents = references; 
						
						//now we can recursively serialize on reference objects
						if(!hMap.containsKey(id))
						{
							serializeClass(Array.get(obj, i), doc, hMap);
						}
					}
				}
				
				//listed up array contents can be set to classElem
				classElem.setContent(contents);
			}
			
			// serialize all fields
			serializeFields(obj, doc, hMap, classElem);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return doc;
	}
	public static void serializeFields(Object obj, Document doc, IdentityHashMap hMap, Element classElem) throws IllegalArgumentException, IllegalAccessException
	{
		Class clazz = obj.getClass();
		Element fieldElem;
		Element valElem;
		Element refElem;
		
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields)
		{
			f.setAccessible(true);
			
			if(f.get(obj) != null)
			{

			
				System.out.println(f.getName());
				fieldElem = new Element("field");
				valElem = new Element("value");
				refElem = new Element("reference");
				String className = f.getDeclaringClass().getName();
				String fieldName = f.getName();
				
				fieldElem.setAttribute("name", fieldName);
				fieldElem.setAttribute("declaring class", className);
				
				//now we need to check if field is primitive or a reference
				
				Class fieldType = f.getType();
				
				if(fieldType.isPrimitive())
				{
					String fieldVal = f.get(obj).toString();
					valElem.addContent(fieldVal);
					fieldElem.setContent(valElem);
				}
				
				else
				{
					String id = Integer.toString(hMap.size());
					refElem.addContent(id);
					fieldElem.setContent(refElem);
					
					// run serialize class on reference object
					serializeClass(f.get(obj), doc, hMap);
				}
			}
			else
			{
				 fieldElem = new Element("null");
			}
			
			classElem.addContent(fieldElem);
			
		}
	}
}
