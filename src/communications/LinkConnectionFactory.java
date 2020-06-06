package communications;

import java.io.IOException;
import java.net.Socket;

// it is a factory class that responsible for creating instances of link, ListeningThread, and ReceivingThread 
// for every received and created connection .

public class LinkConnectionFactory {
	private Integer tmpRemoteID = 0;
	
	private static LinkConnectionFactory instance;
	
	private LinkConnectionFactory(){	
	}

	public  static  LinkConnectionFactory getInstance(){
		if (instance == null)
			instance = new LinkConnectionFactory();
	    return instance;
	}

	private String getTemporaryID(){
		++ tmpRemoteID;
		return tmpRemoteID.toString();
	}

	public Link buildLink(Socket socket,ConnectionManager connectionManager) throws IOException{
		Link connection 		= new Link(socket,connectionManager, getTemporaryID());
		Thread receivingThread 	= new ReceivingThread(connection);
		receivingThread.start();
		return connection;
	}
}
