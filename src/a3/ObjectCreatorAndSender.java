package a3;

import java.util.*;
import org.jdom2.*;

public class ObjectCreatorAndSender {

	
	public static void main(String[] args)
	{
		Document doc;
		String hostname = "127.0.0.1";
		int portNumber = 8888;
	
		
		
		
		System.out.println("Welcome to the Object Creator Tool v.0.0.1!\n\n"
				+ "For creating a Simple Object, press (1)\n"
				+ "For creating a Reference Object, press (2)\n"
				+ "For creating an Array of Primitives, press (3)\n"
				+ "For creating an Array of Reference Types, press (4)\n"
				+ "For creating a Collection of Objects, press (5)\n"
				+ "Or, press (0) to quit the tool");
		
		//Create objects
		
		
		Scanner input = new Scanner(System.in);
		while(!input.hasNextInt())
		{
			input.next();
			System.out.println("Not a valid choice!");
		}
		int choice = input.nextInt();
		
		//Serialize objects
		Serializer serializer = new Serializer();
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
	//}
	}
}
