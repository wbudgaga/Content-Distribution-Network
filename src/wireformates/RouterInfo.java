package wireformates;

import util.ByteStream;

import node.Node;

import communications.Link;
//this class is used by PeerRouterList class. It can pack and unpack itself
public class RouterInfo extends Message{
	private String 	routerID;
	private String 	hostName;
	private int 	portNum;

	public RouterInfo() {
		super(ROUTER_INFO);
	}

	private void unpackRouterID(byte[] byteStream){
		setRouterID(unpackStringField(byteStream));
	}
	
	private void unpackHostName(byte[] byteStream){
		setHostName(unpackStringField(byteStream));
	}

	private void unpackPortNumber(byte[] byteStream){
		setPortNum(unpackIntField(byteStream));
	}
	
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		unpackRouterID(byteStream);
		unpackHostName(byteStream);
		unpackPortNumber(byteStream);
	}
	
	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	private byte[] packRouterID(){
		return ByteStream.packString(getRouterID());
	}

	private byte[] packHostName(){
		return ByteStream.packString(getHostName());
	}
	private byte[] packPortNumber(){
		return ByteStream.intToByteArray(getPortNum());
	}

	@Override
	protected byte[] packMessageBody() {
		byte[] messageIDBytes 	= packMessageID();
		byte[] routerIDBytes 	= packRouterID();
		byte[] hostNameIDBytes 	= packHostName();
		byte[] portNumberBytes 	= packPortNumber();
		byte[] half1 = ByteStream.join (messageIDBytes, routerIDBytes);
		byte[] half2 = ByteStream.join (hostNameIDBytes, portNumberBytes);
		return ByteStream.join(half1, half2);
	}

	@Override
	public void handle(Link sender, Node receidver) {		
	}

	@Override
	public String getMessageType() {
		return "ROUTER_INFO";
	}

	public String getRouterID() {
		return routerID;
	}

	public void setRouterID(String router_id) {
		this.routerID = router_id;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostname) {
		this.hostName = hostname;
	}

	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
}
