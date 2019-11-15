package a3;

import org.jdom2.*;
import java.util.*; 
import java.lang.reflect.*;
public class Serializer {
	

	public static Document serialize(Object obj)
	{
		
		Element root = new Element("serialized");
		Document doc = new Document(root);
	    IdentityHashMap objMap = new IdentityHashMap<>();
		
	    
	    
	    return null;
	}
	
	
	public static Document serializeClass(Object obj, Document doc, IdentityHashMap hMap)
	{
		
		return null;
	}
	public static Element serializeFields(Object obj)
	{
		Class clazz = obj.getClass();
		
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields)
		{
			f.setAccessible(true);
			Element el = new Element(f.getName());
			el.setAttribute("Modifier", String.valueOf(f.getModifiers()));
			el.setAttribute("Type", f.getType().getName() );

			try {
				el.setText(String.valueOf(f.get(obj)));

			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
	}

}
