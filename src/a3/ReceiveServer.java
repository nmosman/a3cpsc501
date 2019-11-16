package a3;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.lang.reflect.*;
import java.util.*;
import java.net.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ReceiveServer extends Thread {
	private Socket sock;
	private ServerSocket serverSock;
	
	
	public ReceiveServer(int port) {
		try {
			serverSock = new ServerSocket(port);
			serverSock.setSoTimeout(300000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		int port = 5557;
		try {
			Thread receiveThread = new ReceiveServer(port);
			receiveThread.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void run() {
		while(true)
		{
			try {
				System.out.println("Receive Server up and running...\n"
						+ "listening on port no: + " + serverSock.getLocalPort());
				
				
			sock = serverSock.accept();
			
			System.out.println("Now connected to: " + sock.getRemoteSocketAddress());
			
			File file = new File("testReceive.xml");
			//streams
			InputStream iStream = sock.getInputStream();
			FileOutputStream foStream = new FileOutputStream(file);
			
			
			byte[] fileBytes = new byte[1024*16];
			int bytesRead = 0;
			
			while((bytesRead = iStream.read(fileBytes)) > 0) {
				foStream.write(fileBytes, 0, bytesRead);
				break;
			    }
			
			System.out.println("File received");
			
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = (Document) saxBuilder.build(file);
			Object o = Deserializer.deserialize(doc);
			
			
			//now we get to see the object via our inspector class 
			//please assume this class to be perfect and don't mind
			//the lack of tabbing when recursing down objects :)
			
			System.out.println("\n----------------------------");
			Inspector inspector = new Inspector();
			inspector.inspect(o, false);
			
			System.out.println("\n\n\n");
			
			sock.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
}
