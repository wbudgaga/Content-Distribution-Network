package communications;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import node.CDN;

import util.ByteStream;

// it is a thread class that is responsible for reading the incoming data from the connection.
// the message header will be removed for each received message
public class ReceivingThread extends Thread{
	private InputStream inStream;
	private Link 		connectionLink;
	
	public  ReceivingThread(Link connectionLink) {
		this.connectionLink = connectionLink;
		this.inStream 		= connectionLink.getInStream();
	}

	private byte[] readMessageBody(int bodyLength) throws SocketException, IOException{
		int totalBytesRcvd 		= 0;  // Total bytes received so far
		byte[] byteBuffer		= new byte[CDN.BUFFERSIZE];
		int bytesRcvd;           // Bytes received in last read
		
		if (bodyLength > CDN.BUFFERSIZE)
			throw new SocketException("The message size("+bodyLength+") exceeds the buffer size("+CDN.BUFFERSIZE+")");

		while (totalBytesRcvd < bodyLength) {
		      if ((bytesRcvd = inStream.read(byteBuffer, totalBytesRcvd, bodyLength - totalBytesRcvd)) == -1)
		    	  throw new SocketException("Connection close prematurely");
		      
		      totalBytesRcvd += bytesRcvd;
		}
	    return byteBuffer;
	}
	
	 private void receivingMessage() throws IOException, InstantiationException, IllegalAccessException{
		byte[] byteBuffer	= new byte[4];	 
		while (true){
			if (inStream.read(byteBuffer, 0,4)<4)
				throw new SocketException("Connection close prematurely");
			int bodyLength 	= ByteStream.byteArrayToInt(byteBuffer); // reading the message length and removing the message header
			connectionLink.dataReceivedCallbackFunction(readMessageBody(bodyLength));
		}
	}
	 
	public void run(){
		try {
			receivingMessage();
		} catch (IOException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		try {
			connectionLink.connectionCloseNotification();
		} catch (IOException e) {
		}
	}	
}
