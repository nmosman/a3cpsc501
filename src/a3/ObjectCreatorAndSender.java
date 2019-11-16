package a3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ObjectCreatorAndSender {

	private static ArrayList<Object> createdObjects = new ArrayList<Object>();
	
	public static SimpleObject makeSimpleObject()
	{
		
		SimpleObject s = null;
		System.out.println("Making a Simple Object(int val1, double val2)\nFirst number must be an int"
				+ "\nsecond must be a double!");
		try {
			Scanner input = new Scanner(System.in);
			
			while(!input.hasNextInt())
			{
				input.next();
				System.out.println("Please enter in a valid integer for val1");
			}
			
			int val1 = input.nextInt();
			
			System.out.println("Now enter in double for val2");
			
			while(!input.hasNextDouble())
			{
				input.next();
				System.out.println("Please enter in a valid doublefor val2");
			}
			
			double val2 = input.nextDouble();
			
			s = new SimpleObject(val1, val2);
			
			System.out.println("Simple Object succesfully created!");
			
		
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return s;
	}
	
	
	
	public static ReferenceObject makeReferenceObject()
	{
		ReferenceObject r = null;
		
		// Use the simple object method to let user create type of simple object
		
		System.out.println("Making a ReferenceObject with a SimpleObject inside\n"
				+ "You will be prompted to enter values for this SimpleObject!");
		
		SimpleObject s = makeSimpleObject();
		
		r = new ReferenceObject(s);
		
		System.out.println("ReferenceObject succesfully created!");
		
		return r;
	}
	
	
	public static SimpleArrayObject makeSimpleArrayObject()
	{
		SimpleArrayObject s = null;
		
		System.out.println("Now we create an object containing an array of ints!");
		System.out.println("But first, how many integers will you be entering in?");
		
		
		try
		{
			Scanner input = new Scanner(System.in);
			
			while(!input.hasNextInt())
			{
				input.next();
				System.out.println("Please enter in a valid integer for array length!");
			}
			
			int arrayLength = input.nextInt();
			
			int [] intArray = new int[arrayLength];
			
			System.out.println("Great, now enter in your values:");
			//Now let's get the integer values for each element of array
			for(int i = 0; i < arrayLength; i++)
			{
				while(!input.hasNextInt())
				{
					input.next();
					System.out.println("Please enter in a valid integer for the " + i + " th element!");
				}
				intArray[i] = input.nextInt();
			}
			
			s = new SimpleArrayObject(intArray);
			
			System.out.println("Array of Primitives (Ints) succesfully created!");
			
		}
		
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return s;
		
	}
	
	public static ReferenceArrayObject makeReferenceArrayObject()
	{
		ReferenceArrayObject r = null;
		
		System.out.println("Creating an array of SimpleObjects for our example of an array of references!");
		System.out.println("But first, how many objects would you like to enter?");
		
		
		try
		{
			Scanner input = new Scanner(System.in);
			
			while(!input.hasNextInt())
			{
				input.next();
				System.out.println("Please enter in a valid integer for array length!");
			}
			
			int arrayLength = input.nextInt();
			
			Object [] objArray = new Object[arrayLength];
			
			System.out.println("Great, now we'll create a simple object " + arrayLength + " times");
			//Now let's get the integer values for each element of array
			for(int i = 0; i < arrayLength; i++)
			{
				
				objArray[i] = makeSimpleObject();
			}
			
			r = new ReferenceArrayObject(objArray);
			
			System.out.println("Array of Primitives (Ints) succesfully created!");
			
		}
		
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return r;
	}
	
	
	public static CollectionObject makeCollectionObject() {
		CollectionObject c = null;
		ArrayList<SimpleObject> soList = new ArrayList<>();
		System.out.println("Now creating a collection of Simple Objects using ArrayLists!");
		
		System.out.println("But first, how many objects would you like to enter?");
		
		
		try
		{
			Scanner input = new Scanner(System.in);
			
			while(!input.hasNextInt())
			{
				input.next();
				System.out.println("Please enter in a valid integer for number of objects to enter!");
			}
			
			int numOfObjs = input.nextInt();
			
		
			
			System.out.println("Great, now we'll create a simple object " + numOfObjs + " times");
			//Now let's get the integer values for each element of array
			for(int i = 0; i < numOfObjs; i++)
			{
				soList.add(makeSimpleObject());
	
			}
			
			c = new CollectionObject(soList);
			
			System.out.println("Collection of Simple Objects succesfully created!");
			
		}
		
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		
		return c;
	}
	
	public static File getXMLFormat(Document doc)
	{
		
		File file = null;
		
		try {
			file = new File("testSend.xml");
			XMLOutputter xmlOut = new XMLOutputter();
			xmlOut.setFormat(Format.getPrettyFormat());
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			xmlOut.output(doc, bufferedWriter);
			bufferedWriter.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return file;
	}
	public static void serializeAndSend(String hostName, int port)
	{
		
		
		try {
			File file;
		
			for(Object o : createdObjects)
			{
				System.out.println("Attempting to connect to " + hostName + "on port no: " + port);
				
				Socket sock = new Socket(hostName, port);	
				
				System.out.println("Now connected to server " + sock.getRemoteSocketAddress());
				OutputStream oStream = sock.getOutputStream();
				
				Class clazz = o.getClass();
				System.out.println("\n\nHit return key to proceed with serializing " + clazz.getName());
				Scanner input = new Scanner(System.in);
				
				input.nextLine();
				
				System.out.println("Now serializing the object!");
				Document doc = Serializer.serialize(o);
				
				System.out.println("Converting the serialized doc into an XML format");
				file = getXMLFormat(doc);
			
				System.out.println("Now attempting to send XML file...");
				
				FileInputStream fiStream = new FileInputStream(file);
				
				byte[] bytes = new byte[1024*16];
				
				int bytesRead = 0;
				
				while((bytesRead = fiStream.read(bytes)) > 0)
				{
					oStream.write(bytes, 0 , bytesRead);
				}
				
				// essentially sending one xml file per object
				fiStream.close();
				oStream.close();
				sock.close();
			}
			
		}
		
		catch(Exception e)
		{
			
		}
	}
	public static void main(String[] args)
	{
		Document doc;
		String hostName = "127.0.0.1";
		int portNumber = 8888;
	
		boolean running = true;
		
		while(running)
		{

			System.out.println("Welcome to the Object Creator Tool v.0.0.1!\n\n"
					+ "For creating a Simple Object, press (1)\n"
					+ "For creating a Reference Object, press (2)\n"
					+ "For creating an Array of Primitives, press (3)\n"
					+ "For creating an Array of Reference Types, press (4)\n"
					+ "For creating a Collection of Objects, press (5)\n"
					+ "To see the objects created so far, press (6)\n"
					+ "To serialize and send what you've created, press (7)\n"
	
					+ "Or, press (0) to quit the tool");
			
			//Create objects
			
			
			Scanner input = new Scanner(System.in);
			while(!input.hasNextInt())
			{
				input.next();
				System.out.println("Not a valid choice!");
			}
			int choice = input.nextInt();
			
			
			
			switch(choice)
			{
			case 0:
				System.out.println("Ending Object Creator Tool...");
				System.exit(0);
				
			case 1:
				SimpleObject s = makeSimpleObject();
				createdObjects.add(s);
				break;
			case 2:
				ReferenceObject r = makeReferenceObject();
				createdObjects.add(r);
				break;
			case 3:
				SimpleArrayObject sa = makeSimpleArrayObject();
				createdObjects.add(sa);
				break;
				
			case 4:
				ReferenceArrayObject ra = makeReferenceArrayObject();
				createdObjects.add(ra);
				break;
				
			case 5:
				CollectionObject c = makeCollectionObject();
				createdObjects.add(c);
				break;
				
			case 6: 
				System.out.println(createdObjects);
				break;
			case 7:
				running = false;
				break;
			default:
				System.out.println("Choose a menu choice between 0-7");
				break;
			}
			//Serialize objects
			serializeAndSend(hostName, portNumber);
			//doc = serializer.serialize(objects);
			
			//Open a socket and send the data
			/*
			try
			{
			
				//Open a socket
				Socket socket = new Socket(hostname, portNumber);
				OutputStream socketOut = socket.getOutputStream();
				
				//Transmit the data
				XMLOutputter xmlOut = new XMLOutputter();
				
				xmlOut.output(doc, socketOut);

				//Close the socket
				socket.close();			
			}
			catch(IOException e)
			{
				System.out.println(e);
			}
			*/
		}
		
		
	//}
	}
}
