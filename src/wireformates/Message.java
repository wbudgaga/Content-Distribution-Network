package wireformates;

import util.ByteStream;
import communications.Link;

import node.Node;

// it has the methods that are used by all message classes. It has the basic operations to convert between primitive types and byte strem
public abstract class Message implements MessageType{
	private int id;
	
	protected int 	currentIndex;
	
	public  Message(int id){
		currentIndex = 0;
		this.id = id;
	}
	
	public int getMessageID() {
		return id;
	}
		
	protected byte[] readNextBytes(byte[] byteStream,int length){
		byte [] bytes = ByteStream.getBytes(byteStream,currentIndex,length);
		currentIndex +=length;
		return bytes;
	}

	protected String unpackStringField(byte[] byteStream){
		byte[] stringBytes = readObjectBytes(byteStream);
		return ByteStream.byteArrayToString(stringBytes);
	}

	protected int unpackIntField(byte[] byteStream){
		byte[] intBytes = readNextBytes(byteStream,4);
		return ByteStream.byteArrayToInt(intBytes);
	}

	protected byte unpackByteField(byte[] byteStream){
		byte[] byteFiled = readNextBytes(byteStream,1);
		return byteFiled[0];
	}
	
	protected byte[] packMessageID(){
		return ByteStream.intToByteArray(getMessageID());
	}

	public static final int unpackMessageID(byte[]  byteStream){
		byte[] messageIdBytes = ByteStream.getBytes(byteStream,0,4);
		return ByteStream.byteArrayToInt(messageIdBytes);
	}

	protected byte[] readObjectBytes(byte[] byteStream){
		byte[] objectLengthInBytes 	= readNextBytes(byteStream,4);
		int objectLength 			= ByteStream.byteArrayToInt(objectLengthInBytes);
		return readNextBytes(byteStream,objectLength);
	}

	public  byte[] packMessage(){
		return packMessageBody();
	}
	
	public abstract void   initiate(byte[]  byteStream);
	public abstract void   handle(Link sender,Node receidver);
	protected abstract byte[] packMessageBody();
	public abstract String getMessageType();
}
