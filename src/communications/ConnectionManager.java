package communications;

//Manages the connections. it hides all the connections details. All connection operation can be  only performed through this class.

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import wireformates.Message;
import wireformates.MessageFactory;

import node.Node;

public class ConnectionManager {
	private ListeningThread 		listeningThread ;
	private LinkConnectionFactory 	linkFactory;
	private MessageFactory 			messageFactory;
	private ConnectionsContainer 	connections;
	private Node 					localNode;
	
	public ConnectionManager(Node localNode) throws ClassNotFoundException{
		this.localNode 		= localNode;
		linkFactory 		= LinkConnectionFactory .getInstance();
		messageFactory 		= MessageFactory.getInstance();
		connections 		= ConnectionsContainer.getInstance();
	}
	
	private boolean isConnected(String hostAddress,int  connectingPort){
		Link link = connections.getConnectionByAddress(hostAddress, connectingPort);
   		if (link == null)
   			return false;
		return true;
	}
	
	public boolean connect(String hostAddress,int  connectingPort, String remoteID) throws IOException{
		if (isConnected(hostAddress, connectingPort))
			return true;
		
		Socket socket 	= new Socket(hostAddress, connectingPort);
		Link connection = addConnection(socket);
		if (connection == null)
			return false;
		connection.setPort(connectingPort);
		connections.updateConnectionID(remoteID, connection);
		return true;
	}
		
	public Link addConnection(Socket connectionSocket) throws IOException{
		Link connectionLink = linkFactory.buildLink(connectionSocket,this);
		connections.add(connectionLink.getLinkId(), connectionLink);
		return connectionLink;
	}
	
	public void listRouters(){
		connections.listConnections();
	}
	
	public ArrayList<String> getConnectionsIDList(){
   		return connections.getConnectionsIDList();
   	}
	
	public void closeUnRegisteredNodes(){
		connections.closeUnRegisteredNodes();
	}
	
	public boolean deRegister(String connectionID) throws IOException{
		return connections.closeConnection(connectionID);
	}
	
	public boolean register(String id, Link  connection){
		return connections.updateConnectionID(id, connection);
	}
	
	public void handleMassage(Link sender, byte[] byteBuffer) throws InstantiationException, IllegalAccessException{
		Message msg = messageFactory.createMessage(byteBuffer);
		msg.handle(sender, localNode);
	}
	
	public String getCompleteAddress(String connectionID){
		Link connection = connections.getConnectionByID(connectionID);
		return connection.getCompleteAddress();
	}

	private void startListeningThread(){
		// Create the thread supplying it with the server object
		Thread thread = new Thread(listeningThread);
		// Start the thread
		thread.start();
		System.out.println("Listening on port "+ localNode.getPort());
	}
	
	public void startListening(int listeningPort) throws IOException{
		listeningThread = new ListeningThread(listeningPort,this);
		startListeningThread();		
	}
	
	public void listenerStopNotification(){
		System.out.println("This node is stopped listening!");
		localNode.shutdown();
	}
	
	public void connectionCloseNotification(String connectionID){
		connections.closeConnection(connectionID);
		localNode.connectionCloseNotification(connectionID);
	}

	public void closeAllConnections(){
		connections.closeAllConnections();
   	}

	public void stopListening(){
		if (listeningThread!= null)
			listeningThread.stop();
	}
	
	public int getTheNumberOfRegisteredNodes(){
		return connections.getTheNumberOfRegisteredNodes();
	}
	
	public String getConnectionIPAddress(String remoteID){
		Link connection = connections.getConnectionByID(remoteID);
		if (connection == null)
			return "";
		return connection.getHostAddress();
	}
	
	public String getConnectionHostName(String remoteID){
		Link connection = connections.getConnectionByID(remoteID);
		if (connection != null)
			return connection.getHostName();
		connectionCloseNotification(remoteID);
		return "";
	}
	
	public int getConnectionPortNum(String remoteID){
		Link connection = connections.getConnectionByID(remoteID);
		if (connection != null)
			return connection.getPort();
		connectionCloseNotification(remoteID);	
		return 0;
		
	}

	public void sendMessage(Message msg, String remoteID) throws IOException {		
		Link link = connections.getConnectionByID(remoteID);
		if (link != null){
			link.sendData(msg.packMessage());
			return;
		}
		connectionCloseNotification(remoteID);
	}

	// send message to all connections
	public void sendMessage(Message msg) throws IOException {	
		ArrayList<String> connectionsIDList = connections.getConnectionsIDList();
  		for (Iterator<String>  IDs = connectionsIDList.iterator(); IDs.hasNext();) {
   			String id = IDs.next();
   			sendMessage(msg,id);
		}
	}
}
