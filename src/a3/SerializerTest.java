package a3;

import static org.junit.Assert.*;

import java.util.List;

import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

public class SerializerTest {

	@Test
	public void testSerialize() {
		
		
		// Test with Simple Object 
		Object o = new SimpleObject(2, 3.0);
		Document doc = Serializer.serialize(o);
		
		assertNotNull(doc);
		
		// for developer to view 
		System.out.println(new XMLOutputter().outputString(doc));
		
		Element root = doc.getRootElement();
		assertEquals("serialized", root.getName());
		
		Element elem = root.getChildren().get(0);
		assertEquals(elem.getName(), "object");
		

		List children = elem.getChildren();;
		System.out.println(children);
		for(int i = 0; i < children.size(); i++)
		{
			Element childElem = (Element) children.get(i);
			
			System.out.println(" hey " + childElem.getValue());
			if(childElem.getAttributeValue("name").equals("val1"))
			{
				assertEquals("" + 2, childElem.getValue());
			}
			if(childElem.getAttributeValue("name").equals("val2"))
			{
				assertEquals("" + 3.0, childElem.getValue());
			}
			
		}
		
		
		// Test with SimpleArrayObject
		int[] arrayOfInts = {1, 2, 3, 4, 5};
		Object o2 = new SimpleArrayObject(arrayOfInts);
		
		doc = Serializer.serialize(o2);
		assertNotNull(doc);
		System.out.println(new XMLOutputter().outputString(doc));
		
		
		
	}

}
