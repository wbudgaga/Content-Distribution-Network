package node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import mst.MSTInterface;
import mst.MSTFactory;

import wireformates.*;

// this class represents the Router. It has all methods that the Router needs
// the handle() method(in superclass) will be automatically called once a message is received
// the method perform() (in superclass) is called when the Router wants sending a message
public class Router extends  Node{
	private 		String 			discoveryHost;
	private 		int 			discoveryPort;
	private 		int 			tracker			=	0;

	public Router(String id, int port, String discoveryHost,  int discoveryPort) throws ClassNotFoundException{
		super(id, port);
		this.discoveryHost = discoveryHost;
		this.discoveryPort = discoveryPort;
	}
	
	public void shutdown(){
		if (stopped) return;
		System.out.println("The node,"+getId()+" , is shutting down ....");
		stopListening();
		System.exit(0);
	}
	
	public void connectionCloseNotification(String connectionID){
		if(connectionID.compareTo(CDN.DISCOVERYID)==0 ||
				connectionManager.getTheNumberOfRegisteredNodes() ==0)
			shutdown();
	}
			
	public void addEdge(String node1, String node2, int weight){
		network.addEdge(node1, node2, weight);
	}
	
	public void updateNetwork(){
		try {
			MSTInterface mst = MSTFactory.getInstance().createMSTObject(CDN.MST_ALGORITHM, network);
	  		mst.computeMST();
	   		network.updateMSTPaths();
	   		CDNReady = true;
	   		return;
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		}
		errMessage("There is a problem by creating MST instance. Check mst package");
 	}
	
	
	public ArrayList<ArrayList<String>> getRoutingPlan(){
		ArrayList<ArrayList<String>> 	routingPlan = new ArrayList<ArrayList<String>>();
		for(int i = 0; i< network.getNumberOfPaths(); ++ i){
			routingPlan.add(new ArrayList<String>(network.getPath(i)));
		}
		return routingPlan;
	}
	
	public boolean connect(String remoteIPAddress, int remotePort, String remoteID) throws IOException{
		return connectionManager.connect(remoteIPAddress, remotePort,remoteID);
	}
	
	private boolean connectDiscoveryNode(){
		try {
			connect(discoveryHost, discoveryPort,CDN.DISCOVERYID);
		} catch (IOException e) {
			errMessage("Connection with the discovery node couldn't be established");
			return false;
		}
		return true;
	}
			
	public String getConfirmationRegisterResponseText(){
		return "Registration was successful.";
	}

	public void sendRegisterRequestMessage(String remoteID) throws IOException{
		perform(remoteID,MessageManager.REGISTER_REQUEST);
	}

	public void sendDeRegisterRequestMessage(String remoteID) throws IOException{
		perform(remoteID,MessageManager.DEREGISTER_REQUEST);
	}

	public void sendDeRegisterRequestMessage() throws IOException{
		for (Iterator<String>  IDs 	= getConnectionsIDList().iterator(); IDs.hasNext();){
			String remoteID 		= IDs.next();
			sendDeRegisterRequestMessage(remoteID);
		}
 	}
	
	private void sendData() throws IOException{
		if (!CDNReady){
			errMessage("The MST isn't computed yet");
			return;
		}
		perform(null,MessageManager.DATA);
	}

	private void printPath(ArrayList<String> path){
		int i = 0;
		for(; i< path.size()-1; ++ i){
			int index = network.findEdge(path.get(i), path.get(i+1));
			int weight = network.getEdge(index).getWeight();
			System.out.print(path.get(i)+"--"+weight+"--");
		}
		System.out.print(path.get(i));
	}
	
	private void printMST(){
		if (!CDNReady){
			errMessage("The MST isn't computed yet");
			return;
		}
		for(int i = 0; i< network.getNumberOfPaths(); ++ i){
			printPath (network.getPath(i));
			System.out.println();
		}
   		System.out.println("=====================================================================");
	}
	
	public void setTracker(int tracker) {
		this.tracker = tracker;
	}

	public int getTracker() {
		return tracker;
	}
	
	public int incTracker() {
		return ++tracker;
	}

	protected void commandLineReanderMainLoop() throws IOException{
		String command;
		while (true){
			command = readCommand();
			if (command == null)						continue;
			if(command.compareTo("list-routers")==0)	{connectionManager.listRouters();				continue;}
			if(command.compareTo("print-MST")	==0)	{printMST();									continue;}
			if(command.compareTo("send-data")	==0)	{sendData();									continue;}
			if(command.compareTo("exit-cdn")	==0)	{sendDeRegisterRequestMessage(CDN.DISCOVERYID);	continue;}
			if(command.compareTo("quit")		==0)	break;
			errMessage("invalid command!");
		}
	}

	public void startup() throws IOException{
		if (!connectDiscoveryNode())
			return;

		if (!startListening()){
			shutdown();
			return;
		}
		sendRegisterRequestMessage(CDN.DISCOVERYID);
		commandLineReanderMainLoop();
		shutdown();
	}
	
	public static void main(String args[]) throws IOException {
		Router routerNode;
		      
		if (args.length != 4) {
			errMessage("Router Node:  Usage:");
			errMessage("         java node.Router portnum assignedid discovery-host discovery-port");
		    return;
		}
		try{
			int 	port 			= Integer.parseInt(args[0]);
			String 	id 				= args[1];
			String 	discoveryHost 	= args[2];
			int 	discoveryPort 	=  Integer.parseInt(args[3]);
			routerNode = new Router(id, port, discoveryHost, discoveryPort);
			routerNode.startup();
		}catch(NumberFormatException e){
			errMessage("Router Node: the values of the port must be inetger");
		} catch (ClassNotFoundException e) {
			errMessage("Some classes from wireformate or node.messageHandling package couldn't be loaded");
		}
		
	}
}
