package communications;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import node.CDN;

import util.ByteStream;

//encapsulates the connection link information. each instance of this class is created by LinkConnectionFactory to represent one connection. 
// Byte stream data can be sent over this link.
// The method:
// sendData						: adds a message header and sends the whole message as byte stream on this link
// dataReceivedCallbackFunction	: will be called when byte stream data has been received on this link

public class Link {
	private Socket 				socket;
	private InputStream 		inStream;
	private OutputStream 		outStream;
	private String 				id;
	private int 				routerListingPort;
	private ConnectionManager 	connectionManager;
	private boolean 			registered = false;
	
	//Will be created by the LinkConnectionFactory
	protected Link(Socket socket, ConnectionManager connectionManager, String tmpID) throws IOException {  
		this.socket 			= socket;
		this.connectionManager 	= connectionManager;
		this.id 				= tmpID;
		this.socket.setSendBufferSize(CDN.BUFFERSIZE);
		this.socket.setReceiveBufferSize(CDN.BUFFERSIZE);
		getStreams();
	}
	
	protected void sendData(byte[] dataToBeSent) throws IOException {
		if (dataToBeSent.length + 4 > CDN.BUFFERSIZE)
			throw new SocketException("The message size("+(dataToBeSent.length + 4)+") exceeds the buffer size("+CDN.BUFFERSIZE+")");

		outStream.write(ByteStream.addPacketHeader(dataToBeSent)); // message header will be added by sending each message
	}
		
	protected void dataReceivedCallbackFunction(byte[] dataReceived) throws InstantiationException, IllegalAccessException{
		connectionManager.handleMassage(this,dataReceived);
	}
	
	protected void connectionCloseNotification() throws IOException{
		connectionManager.connectionCloseNotification(id);
	}
	
	protected void close(){
		try {
			this.socket.close();
		} catch (IOException e) {}
	}
	
///////////////////////////////////////////////////////////////////
//			Getters and setters methods							 //
///////////////////////////////////////////////////////////////////	
	 private void getStreams() throws IOException{
		 inStream 	= socket.getInputStream();
		 outStream 	= socket.getOutputStream();
	 }

	 protected void setLinkId(String linkId) {
		setRegistered(true);
		this.id = linkId;
	}
	
	public String getLinkId() {
		return this.id;
	}

	public int getPort() {
		return  routerListingPort;
	}
	public void setPort(int routerPort) {
		routerListingPort = routerPort;
	}

	public String getHostName() {
		return socket.getInetAddress().getHostName();
	}
	
	public String getHostAddress() {
		return socket.getInetAddress().getHostAddress();
	}
	
	protected String getCompleteAddress() {
		return getHostName().trim()+":"+getPort();
	}

	protected InputStream getInStream() {
		return inStream;
	}

	protected boolean isRegistered() {
		return registered;
	}

	protected void setRegistered(boolean registered) {
		this.registered = registered;
	}
	public static String getHeader(){

		String header 	="=====================================================================\n";
		header 			+= String.format("%-30s %-40s", "Router ID", "Address");
		header 			+="\n=====================================================================";
		return header;
	}

	public String toString(){
		return String.format("%-30s %-40s", getLinkId(),getCompleteAddress());
	}

}
