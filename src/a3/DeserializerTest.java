package a3;

import static org.junit.Assert.*;
import java.util.*;
import org.jdom2.*;
import java.lang.reflect.*;
import org.junit.Test;

public class DeserializerTest {

	@Test
	public void testDeserialize() {
		
		//Testing with Simple Object
		SimpleObject s = new SimpleObject(2, 3.0);
		
		Document doc = Serializer.serialize(s);
		
		// check if its not empty
		Object o = Deserializer.deserialize(doc);
		assertNotNull(o);
		
		Class simpleObjClass = s.getClass();
		Class docObjClass = o.getClass();
		
		Field[] simpleObjFields = simpleObjClass.getDeclaredFields();
		Field[] docObjFields = docObjClass.getDeclaredFields();
		
		assertEquals(simpleObjFields.length, docObjFields.length);
		
		for(int i = 0; i < docObjFields.length; i++)
		{
			try {
				
			
				Field sField = simpleObjFields[i];
				Field dField = docObjFields[i];
				
				sField.setAccessible(true);
				dField.setAccessible(true);
				
				assertTrue(dField.getType().isPrimitive());
				assertTrue(dField.getName().equals(sField.getName()));
				
				Object sVal = sField.get(s);
				Object dVal = dField.get(o);
				
				assertTrue(sVal.toString().equals(dVal.toString()));
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// testing with an array object
		int[] arr = {1,2,3};
		SimpleArrayObject sa = new SimpleArrayObject(arr);
		
		Document doc2 = Serializer.serialize(sa);
		// check if its not empty
		Object o2 = Deserializer.deserialize(doc2);
		
		assertNotNull(o2);
		
	}
	
	
	

			
	
	

}
