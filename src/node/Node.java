package node;

import graph.Edge;
import graph.Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import messageHandlers.MessageHandler;
import messageHandlers.MessageHandlerFactory;

import wireformates.*;

import communications.ConnectionManager;
import communications.Link;

// this class is the superclass of Router and Discovery and implements the shared methods.
// Since in this application the Router and Discovery are considered as Nodes, this class has all methods the both have
//     (some of the methods are empty because they are implemented only by one of them) 
public class Node{
	private 	String 				localId;
	private 	int 				localListingPort;
	private 	BufferedReader 		bufferedReader;	//needed to read commands from command line
	protected	ConnectionManager 	connectionManager;
	protected 	Network			 	network;
	protected 	boolean 			stopped				=	false;
	protected 	boolean				CDNReady 			= 	false;
	
	public Node(String id, int port) throws ClassNotFoundException{
		bufferedReader 	= new BufferedReader(new InputStreamReader(System.in));
		this.setId(id);
		this.setPort(port);
		connectionManager 	= new ConnectionManager(this);
	}

	public void resetNetwork(){
		network 			= new Network(getId());
	}
	
	public Message createMessageObject(int messageID) {
		try {
			return MessageManager.getInstance().createMessageObject(messageID);
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		}
		System.err.println("Instance of one of message classes couldn't be created. check the corresponding classes");
		return null;
	}
	 
	protected MessageHandler getMessageHandlerInstance(int messageID){
		try {
			return MessageHandlerFactory.getInstance().createMessageHandlerObject(messageID, this);
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		}
		System.err.println("Instance of one of message handler classes couldn't be created. check the corresponding class");
		return null;
	}

	public void handle(Link sender, Message message){
		try {
			MessageHandler mHandler = getMessageHandlerInstance(message.getMessageID());
			mHandler.handle(sender, message);
		} catch (IOException e) {
			System.err.println("The response message couldn't be send ");
		}
	}
	
	protected void perform(String remoteID, int messageID)throws IOException{
		MessageHandler handler = getMessageHandlerInstance(messageID);
		if (handler==null)
			return;
		handler.perform(remoteID);
	}
	
	protected boolean startListening(){
		try {
			connectionManager.startListening(localListingPort);
		} catch (IOException e) {
			errMessage("It couldn't listen on the port "+getPort());
			return false;
		}
		return true;
	}
	
	protected void stopListening(){
		try {
			System.in.close();
		} catch (IOException e) {}
		connectionManager.stopListening();
		stopped = true;
	}
	
	protected String readCommand(){
		try {
			return bufferedReader.readLine();
		} catch (IOException e) {}
		return "quit";
	}
	
	public String getConnectionHostName(String remoteID){
		return connectionManager.getConnectionHostName(remoteID);
	}
	
	public int getConnectionPortNum(String remoteID){
		return connectionManager.getConnectionPortNum(remoteID);
	}

	public ArrayList<String> getConnectionsIDList(){
		return connectionManager.getConnectionsIDList();
	}

	public void sendMessage(Message message, String senderID) throws IOException{
		connectionManager.sendMessage(message, senderID);
	}
	
	public boolean register(String senderID, Link sender){
		return connectionManager.register(senderID, sender);
	}

	public void closeUnRegisteredNodes(){
		connectionManager.closeUnRegisteredNodes();
	}

	public boolean deRegister(String remoteID) throws IOException{
		return connectionManager.deRegister(remoteID);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
	
///////////////////////////////////////////////////////////////////
// Methods that are overwrote by either the Router or Discovery	 //
///////////////////////////////////////////////////////////////////		
	public ArrayList<Edge> getEdges(){return null;}
	public ArrayList<String> getPeerList(String remoteID) {return null;}
	public void connectionCloseNotification(String connectionID){}
	public void shutdown() {}
	public int incTracker() {return 0;}
	public ArrayList<ArrayList<String>> getRoutingPlan(){return null;}
	public void setTracker(int tracker) {}
	public int getTracker() {return 0;}

///////////////////////////////////////////////////////////////////
//			Getters and setters methods							 //
///////////////////////////////////////////////////////////////////	
	public String getConfirmationRegisterResponseText(){
		return "Registration request successful. The number of routers currently constituting the CDN is ("+
				connectionManager.getTheNumberOfRegisteredNodes()+")";
	}

	protected static void errMessage(String msg){
		System.err.println(msg);
	}
	
	public int getPort() {
		return localListingPort;
	}

	private void setPort(int port) {
		this.localListingPort = port;
	}

	public String getId() {
		return localId;
	}

	private void setId(String id) {
		this.localId = id;
	}
}
