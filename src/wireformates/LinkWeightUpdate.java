package wireformates;

import java.util.ArrayList;

import util.ByteStream;

import node.Node;

import communications.Link;
//this class represents a LinkWeightUpdate message. It is used to pack and unpack the LinkWeightUpdate message.
// it has a list of LinkInfo objects that are able to pack and unpack themselves.
//the method handle() calls the method handle() in receiver to handle the message
public class LinkWeightUpdate extends Message{
	private ArrayList<LinkInfo> linkInfoList = new ArrayList<LinkInfo>();
	
	public LinkWeightUpdate() {
		super(LINK_WEIGHT_UPDATE);
	}

	public void addLinkInfo(LinkInfo linkInfo){
		linkInfoList.add(linkInfo);
	}
	
	public LinkInfo getLinkInfo(int index){
		return linkInfoList.get(index);
	}

	public int getNumberOfLinks(){
		return linkInfoList.size();
	}

	@Override
	public void handle(Link sender, Node receidver) {
	 receidver.handle(sender, this);
	}

	private int unpackNumberOfLinks(byte[] byteStream){
		return unpackIntField(byteStream);
	}
	
	private void unpackLinkInfoObject(byte[] byteStream){
		LinkInfo linkInfo = new LinkInfo();
		byte [] linkInfoObjectBytes = readObjectBytes(byteStream);
		linkInfo.initiate(linkInfoObjectBytes);
		addLinkInfo(linkInfo);
	}
	
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		int numberOfLinks = unpackNumberOfLinks(byteStream);
		for (int i=0; i<numberOfLinks; ++i){
			unpackLinkInfoObject(byteStream);
		}
	}
	
	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}

	private byte[] packNumberOfLinks(){
		return ByteStream.intToByteArray(getNumberOfLinks());
	}

	@Override
	protected byte[] packMessageBody(){
		byte[] messageIDBytes = packMessageID();
		byte[] numberOfLinksBytes = packNumberOfLinks();
		byte[] messageBody = ByteStream.join(messageIDBytes, numberOfLinksBytes);
		for (int i=0; i<getNumberOfLinks(); ++i){
			LinkInfo linkInfo = linkInfoList.get(i);
			byte[] linkInfoBytes = linkInfo.packMessage();
			byte[] linkInfoBytesHeader = ByteStream.addPacketHeader(linkInfoBytes);
			messageBody = ByteStream.join(messageBody, linkInfoBytesHeader);
		}
		return messageBody;
	}

	@Override
	public String getMessageType() {
		// TODO Auto-generated method stub
		return null;
	}

}
