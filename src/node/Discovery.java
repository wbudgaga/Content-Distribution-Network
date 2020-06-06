package node;

import graph.Edge;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;

import util.Graph;
import wireformates.*;
//this class represents the Discovery. It has all methods that the Discovery needs
//the handle() method(in superclass) will be automatically called once a message is received
//the method perform() (in superclass) is called when the Discovery wants sending a message
public class Discovery extends Node{
	private 	DiscoverTimer	timer;
	
	public Discovery(int port, long refresh_intereval) throws ClassNotFoundException{
		super(CDN.DISCOVERYID,port);
		timer = new DiscoverTimer(this, refresh_intereval);
	}
	
	public void shutdown(){
		if (stopped) 	return;
		System.out.println("The node,"+getId()+" , is shutting down ....");
		timer.stopRunning();
		connectionManager.closeAllConnections();
		stopListening();
	}	
	
	private int parseNumberOfLinksOfEveryRouter(String command){
		String 	numberOfLinksAsString = command.substring(9).trim();
		try {
			return Integer.parseInt(numberOfLinksAsString);
		} catch (Exception e) {
			return CDN.CR;
		}
	}
		
	public ArrayList<String> getPeerList(String remoteID) {
		return network.getVertexList(remoteID);
	}
	
	private void createNetworkNodes(){
		connectionManager.closeUnRegisteredNodes();
		resetNetwork();
		for (Iterator<String>  IDs = connectionManager.getConnectionsIDList().iterator(); IDs.hasNext();)
 			network.addVertex(IDs.next());
	}
	
	private void createCDN(int degree) throws InstantiationException, IllegalAccessException, IOException{
		if (CDNReady){
			errMessage("The CDN is already created!");
			return;
		}
		createNetworkNodes();
		if (!network.generateRegularGraph(degree,CDN.MIN_WEIGHT,CDN.MAX_WEIGHT)){
			return;
		}
		network.createCompressedList();
		perform(null,MessageType.PEER_ROUTER_LIST);
		
		CDNReady = true;
		timer.start();
		updateWeights();
	}
	
	public ArrayList<Edge> getEdges(){
		return network.getEdges();
	}
	
	protected void updateWeights() throws InstantiationException, IllegalAccessException, IOException{
		if (!CDNReady){
			errMessage("The CDN hasn't been created yet!");
			return;
		}
		Graph.updateWeights(network, CDN.MIN_WEIGHT, CDN.MAX_WEIGHT);
		perform(null,MessageType.LINK_WEIGHT_UPDATE);	
	}
	
	private void printEdgeInfo(Edge e){
		String node1Info = connectionManager.getCompleteAddress(e.getNode1());
		String node2Info = connectionManager.getCompleteAddress(e.getNode2());
		String addresses = String.format("%-34s %-40s %-40s",e.toString(), node1Info,node2Info);
		System.out.println(addresses);
	}
	
	private void listWeights(){
		if (!CDNReady){
			errMessage("The CDN hasn't been created yet!");
			return;
		}
		String header 	="==============================================================================================\n";
		header 			+= String.format("%-15s %-15s %-4s %-40s %-40s", "Router1", "Router2","Wt","Address1","Address2");
		header 			+="\n==============================================================================================";
		System.out.println(header);
		for (int i=0; i< network.getNumberOfEdges(); ++ i){
			printEdgeInfo(network.getEdge(i));
		}
	}
	
	protected void commandLineReanderMainLoop() throws InstantiationException, IllegalAccessException, IOException{
		String command;
		while (true){
			command = readCommand();
			if (command == null)							continue;
			if(command.compareTo("list-routers")	==0)	{connectionManager.listRouters(); 						continue;}
			if(command.contains("setup-cdn"))				{createCDN(parseNumberOfLinksOfEveryRouter(command));  	continue;}
			if(command.compareTo("list-weights")	==0)	{listWeights();  										continue;}
			if(command.compareTo("update-weights")	==0)	{updateWeights();  										continue;}
			if(command.compareTo("exit-cdn")		==0)	break;
			if(command.compareTo("quit")			==0)	break;
			errMessage("invalid command!");
		}
	}

	public void startup() throws InstantiationException, IllegalAccessException, IOException{
		if (!startListening())
			return;
		commandLineReanderMainLoop();
		shutdown();
	}
	
	public static void main(String args[]) throws InstantiationException, IllegalAccessException, IOException {
		Discovery discoveryNode;
		      
		if (args.length < 1) {
			errMessage("Discovery Node:  Usage:");
			errMessage("         java node.Discovery portnum [refresh_intereval");
		    return;
		}
		try{
			int port = Integer.parseInt(args[0]);
			long refresh_intereval = CDN.REFRESH_INTERVAL;
			if (args.length == 2)
				refresh_intereval =  Integer.parseInt(args[1]);
			
			discoveryNode = new Discovery(port, refresh_intereval);
			discoveryNode.startup();
		}catch(NumberFormatException e){
			errMessage("Discovery Node: the values of portnum and refresh_intereval must be inetger");
		} catch (ClassNotFoundException e) {
			errMessage("Some classes from wireformate or node.messageHandling package couldn't be loaded");
		}catch (SocketException e) {
			errMessage(e.getMessage());
		}
	}

}
