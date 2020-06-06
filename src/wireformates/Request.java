package wireformates;

import util.ByteStream;
import node.Node;

import communications.Link;
// it is a supper class of RegisterRequest and DeRegisterRequest 
public class Request extends Message{

		private String	ipAddress;
		private int		portNumber;
		private String	assignedID;
		
		public Request(int MessageID) {
			super(MessageID);
		}

		private void unpackIPAddress(byte[] byteStream){
			setIpAddress(unpackStringField(byteStream));
		}
		
		private void unpackPortNumber(byte[] byteStream){
			setPortNumber(unpackIntField(byteStream));
		}
		
		private void unpackAssignedID(byte[] byteStream){
			setAssignedID(unpackStringField(byteStream));
		}

		private void unpackMessage(byte[] byteStream){
			currentIndex = 4;
			unpackIPAddress(byteStream);
			unpackPortNumber(byteStream);
			unpackAssignedID(byteStream);
		}
		
		private byte[] packIPAddress(){
			return ByteStream.packString(getIpAddress());
		}

		private byte[] packAssignedID(){
			return ByteStream.packString(getAssignedID());
		}
		
		private byte[] packPortNumber(){
			return ByteStream.intToByteArray(getPortNumber());
		}

		protected byte[] packMessageBody(){
			byte[] messageIDBytes = packMessageID();
			byte[] ipAddressBytes = packIPAddress();
			byte[] portNumberBytes = packPortNumber();
			byte[] assignedIDBytes = packAssignedID();
			byte[] half1 = ByteStream.join (messageIDBytes, ipAddressBytes);
			byte[] half2 = ByteStream.join (portNumberBytes, assignedIDBytes);
			return ByteStream.join(half1, half2);
		}
		
		public  void   handle(Link sender, Node receidver){
		}
		
		@Override
		public void initiate(byte[] byteStream) {
			unpackMessage(byteStream);
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public int getPortNumber() {
			return portNumber;
		}

		public void setPortNumber(int portNumber) {
			this.portNumber = portNumber;
		}

		public String getAssignedID() {
			return assignedID;
		}

		public void setAssignedID(String assignedID) {
			this.assignedID = assignedID;
		}

		@Override
		public String getMessageType() {
			return null;
		}
}
