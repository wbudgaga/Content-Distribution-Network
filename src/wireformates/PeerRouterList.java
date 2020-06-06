package wireformates;

import java.util.ArrayList;

import util.ByteStream;

import node.Node;

import communications.Link;
//this class represents a PeerRouterList message. It is used to pack and unpack the PeerRouterList message.
//it has a list of RouterInfo objects that are able to pack and unpack themselves.
//the method handle() calls the method handle() in receiver to handle the message
public class PeerRouterList extends Message{
	private ArrayList<RouterInfo> routerList = new ArrayList<RouterInfo>();

	public PeerRouterList() {
		super(PEER_ROUTER_LIST);
	}

	public void addRouter(RouterInfo routerInfo){
		routerList.add(routerInfo);
	}
	
	public RouterInfo getRouter(int index){
		return routerList.get(index);
	}

	public int getNumberOfRouters(){
		return routerList.size();
	}
	
	private int unpackNumberOfRouters(byte[] byteStream){
		return unpackIntField(byteStream);
	}

	private void unpackRouterInfoObject(byte[] byteStream){
		RouterInfo routerInfo = new RouterInfo();
		byte [] routerInfoObjectBytes = readObjectBytes(byteStream);
		routerInfo.initiate(routerInfoObjectBytes);
		addRouter(routerInfo);
	}
	
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		int numberOfRouters = unpackNumberOfRouters(byteStream);
		for (int i=0; i<numberOfRouters; ++i){
			unpackRouterInfoObject(byteStream);
		}
	}
	
	private byte[] packNumberOfRouters(){
		return ByteStream.intToByteArray(getNumberOfRouters());
	}

	@Override
	protected byte[] packMessageBody(){
		byte[] messageIDBytes = packMessageID();
		byte[] numberOfRoutersBytes = packNumberOfRouters();
		byte[] messageBody = ByteStream.join(messageIDBytes, numberOfRoutersBytes);
		for (int i=0; i<getNumberOfRouters(); ++i){
			RouterInfo routerInfo = routerList.get(i);
			byte[] m = routerInfo.packMessage();
			byte[] m1 = ByteStream.addPacketHeader(m);
			messageBody = ByteStream.join(messageBody, m1);
		}
		return messageBody;
	}
	
	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	@Override
	public void handle(Link sender, Node receidver) {
		receidver.handle(sender, this);
	}

	
	@Override
	public String getMessageType() {
		return "PEER_ROUTER_LIST";
	}

}
