package wireformates;

import java.util.ArrayList;

import util.ByteStream;

import node.Node;

import communications.Link;
// this class represents a Data message. It is used to pack and unpack the Data message
// the method handle() calls the method handle() in receiver to handle the message
public class Data extends Message{
	private int 							tracker;
	private	String							senderID;
	private ArrayList<ArrayList<String>> 	routingPlan = new ArrayList<ArrayList<String>>();
	
	public void setRoutingPlan(ArrayList<ArrayList<String>> routingPlan) {
		this.routingPlan = routingPlan;
	}

	private ArrayList<String>				remoteList 	= new ArrayList<String>();
	
	private void unpackTracker(byte[] byteStream){
		setTracker(unpackIntField(byteStream));
	}

	private void unpackSenderID(byte[] byteStream){
		setSender(unpackStringField(byteStream));
	}

	private void unpackPath(byte[] byteStream ){
		ArrayList<String> path = new ArrayList<String>();
		int numberOfRoutersInThePath = unpackIntField(byteStream);
		for (int i=0; i<numberOfRoutersInThePath; ++i){
			path.add(unpackStringField(byteStream));
		}
		if(!path.isEmpty())
			addPath(path);
	}

	private void unpackTheRountingPlan(byte[] byteStream){
		int numberOfPaths = unpackIntField(byteStream);
		for (int i=0; i<numberOfPaths; ++i){
			unpackPath(byteStream);
		}
	}
	
	private void unpackMessage(byte[] byteStream){
		currentIndex = 4;
		unpackTracker(byteStream);
		unpackSenderID(byteStream);
		unpackTheRountingPlan(byteStream);
	}
	
	@Override
	public void initiate(byte[] byteStream) {
		unpackMessage(byteStream);
	}
	

	private byte[] packTracker(){
		return ByteStream.intToByteArray(getTracker());
	}
	
	private byte[] packRouterID(String routerID){
		return ByteStream.packString(routerID);
	}

	private byte[] packSenderID(){
		return ByteStream.packString(getSender());
	}
	
	private byte[] packPath(ArrayList<String> path){
		byte[] thePathInBytes = ByteStream.intToByteArray(path.size()); // convert the number of routers in this path into byte array
		for (int i=0; i<path.size(); ++i){
			thePathInBytes = ByteStream.join( thePathInBytes, packRouterID(path.get(i)) );
		}
		return thePathInBytes;
	}


	private byte[] packTheRountingPlan(){
		byte[] theRountingPlaInBytes = ByteStream.intToByteArray(getNumberOfPaths()); // convert the number of paths into byte array
		for (int i=0; i<getNumberOfPaths(); ++i){
			theRountingPlaInBytes = ByteStream.join(theRountingPlaInBytes, packPath(getPath(i)) );
		}
		return theRountingPlaInBytes;
	}

	@Override
	protected byte[] packMessageBody(){
		byte[] messageIDBytes 		= packMessageID();
		byte[] trackerBytes 		= packTracker();
		byte[] senderIDBytes 		= packSenderID();
		byte[] rountingPlanBytes 	= packTheRountingPlan();
		
		byte[] messageBody	 		= ByteStream.join(messageIDBytes, trackerBytes);
		messageBody			 		= ByteStream.join(messageBody, senderIDBytes);
		
		return ByteStream.join(messageBody, rountingPlanBytes);
	}


	public void addPath(ArrayList<String> path){
		routingPlan.add(path);
	}
	
	public void addRouter(String routerID, int pathIndex){
		getPath(pathIndex).add(routerID);
	}
	
	public ArrayList<String> getPath(int index){
		return routingPlan.get(index);
	}

	public int getNumberOfRouters(int pathIndex){
		return getPath(pathIndex).size();
	}
	
	public int getNumberOfPaths(){
		return routingPlan.size();
	}

	public int getNumberOfRemotes(){
		return remoteList.size();
	}

	public String getRemote(int index){
		return remoteList.get(index);
	}

	public void pop(int pathIndex){
		ArrayList<String> path = routingPlan.get(pathIndex);
		if (path.get(0).compareTo(senderID)==0){
			path.remove(0);
			if (!path.isEmpty())
				remoteList.add(path.get(0));
		}
	}
	
	public void prepareTheRountingProcess(){
		for (int i=0; i<getNumberOfPaths(); ++i){
			pop(i);
		}
	}
		
	public Data() {
		super(DATA);
	}

	@Override
	public void handle(Link sender, Node receiver) {
		receiver.handle(sender, this);
	}

	@Override
	public String getMessageType() {
		return null;
	}
	
	public int getTracker() {
		return tracker;
	}
	
	public void setTracker(int tracker) {
		this.tracker = tracker;
	}
	
	public String getSender() {
		return senderID;
	}

	public void setSender(String sender) {
		this.senderID = sender;
	}

}
