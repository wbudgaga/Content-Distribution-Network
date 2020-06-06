package wireformates;

import util.ByteStream;
import node.Node;

import communications.Link;
// this class is used by LinkWeightUpdate class. It can pack and unpack itself
public class LinkInfo extends Message{
	private RouterInfo 	router1;
	private RouterInfo 	router2;
	private int 		weight;
	
	public LinkInfo() {
		super(LINK_INFO);
	}

	private RouterInfo unpackRouterInfoObject(byte[] byteStream){
		RouterInfo routerInfo = new RouterInfo();
		byte [] routerInfoObjectBytes = readObjectBytes(byteStream);
		routerInfo.initiate(routerInfoObjectBytes);
		return routerInfo;
	}

	private void unpackRouter1(byte[] byteStream){
		setRouter1(unpackRouterInfoObject(byteStream));
	}
	
	private void unpackRouter2(byte[] byteStream){
		setRouter2(unpackRouterInfoObject(byteStream));
	}

	private void unpackWeight(byte[] byteStream){
		setWeight(unpackIntField(byteStream));
	}
	
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		unpackRouter1(byteStream);
		unpackRouter2(byteStream);
		unpackWeight(byteStream);
	}

	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	@Override
	public void handle(Link sender, Node receidver) {
	}

	private byte[] packRouter(RouterInfo router){
		byte[] routerBytes 		= router.packMessage();
		return ByteStream.addPacketHeader(routerBytes);
	}

	private byte[] packWeight(){
		return ByteStream.intToByteArray(getWeight());
	}

	@Override
	protected byte[] packMessageBody() {
		byte[] messageIDBytes 	= packMessageID();
		byte[] router1Bytes 	= packRouter(getRouter1());
		byte[] router2Bytes 	= packRouter(getRouter2());
		byte[] weightBytes 		= packWeight();
		byte[] messageBody1 	= ByteStream.join (messageIDBytes, router1Bytes);
		byte[] messageBody2 	= ByteStream.join (router2Bytes, weightBytes);
		return ByteStream.join(messageBody1, messageBody2);
	}

	@Override
	public String getMessageType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RouterInfo getRouter1() {
		return router1;
	}

	public void setRouter1(RouterInfo router1) {
		this.router1 = router1;
	}

	public RouterInfo getRouter2() {
		return router2;
	}

	public void setRouter2(RouterInfo router2) {
		this.router2 = router2;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}


}
