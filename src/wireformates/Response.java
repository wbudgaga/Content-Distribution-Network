package wireformates;

import util.ByteStream;
import node.Node;

import communications.Link;
//it is a supper class of RegisterResponse and DegisterResponse 
public class Response extends Message{

	private byte statusCode;
	private String additionalInfo;
	
	public Response(int MessageID) {
		super(MessageID);
	}

	private void unpackStatusCode(byte[] byteStream){
		setStatusCode(unpackByteField(byteStream));
	}
	
	private void unpackAdditionalInfo(byte[] byteStream){
		setAdditionalInfo(unpackStringField(byteStream));
	}


	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		unpackStatusCode(byteStream);
		unpackAdditionalInfo(byteStream);
	}

	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	@Override
	public void handle(Link sender, Node receidver) {
	}

	private byte[] packAdditionalInfo(){
		return ByteStream.packString(getAdditionalInfo());
	}


	@Override
	public byte[] packMessageBody(){
		byte[] messageIDBytes = packMessageID();
		byte[] StatusCodeBytes = {getStatusCode()};
		byte[] AdditionalInfoBytes = packAdditionalInfo();
		byte[] half1 = ByteStream.join (messageIDBytes, StatusCodeBytes);
		return ByteStream.join (half1, AdditionalInfoBytes);
	}

	public byte getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(byte statusCode) {
		this.statusCode = statusCode;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	@Override
	public String getMessageType() {
		return null;
	}
}
