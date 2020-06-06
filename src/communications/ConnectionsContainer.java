package communications;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

//it is singleton class that is responsible for managing the connections
// And performing operations on collection of connections.

public class ConnectionsContainer {
	private static 	ConnectionsContainer 	instance;
	private 		Hashtable<String,Link>	links = new Hashtable<String,Link>();
	
	private ConnectionsContainer(){}
	
	protected  static  ConnectionsContainer getInstance(){
		if (instance == null)
			instance = new ConnectionsContainer();
	    return instance;
	}
	
	protected Link getConnectionByAddress(String hostAddress,int  connectingPort){
		ArrayList<Link> connections = new ArrayList<Link>  (links.values());
		for(int i=0; i< connections.size() ; ++i){
  			Link link = connections.get(i);
   			if (connectingPort == link.getPort())
   				if (hostAddress.compareTo(link.getHostName())==0 || hostAddress.compareTo(link.getHostAddress())==0 )
   					return link;
		}
		return null;
	}

	protected boolean closeConnection(String connectionID){
		Link connection = links.remove(connectionID);
		if (connection != null){
			connection.close();
			System.out.println("Connection to the node "+ connectionID + " has been disconnected!");
			return true;
		}
		return false;
	}

	protected void closeAllConnections(){
		ArrayList<String> connectionsIDList = getConnectionsIDList();
  		for (Iterator<String>  IDs = connectionsIDList.iterator(); IDs.hasNext();) 
  			closeConnection(IDs.next());
   	}

	protected ArrayList<String> getConnectionsIDList(){
		ArrayList<String> connectionsIDList = new ArrayList<String>(links.keySet());
   		return connectionsIDList;
   	}

	private ArrayList<String> getUnRegisteredNodes(){
		ArrayList<String> unRegisteredNodesIDs = new ArrayList<String>();
  		for (Iterator<Link>  linkList = links.values().iterator(); linkList.hasNext();) {
  			Link node = linkList.next();
   			if(!node.isRegistered())
   				unRegisteredNodesIDs.add(node.getLinkId());
  		}
   		return unRegisteredNodesIDs;
   	}

	protected void closeUnRegisteredNodes(){
   		for (Iterator<String>  linkList = getUnRegisteredNodes().iterator(); linkList.hasNext();) {
   			closeConnection(linkList.next());
   		}
   	}

	protected int getTheNumberOfRegisteredNodes(){
		return links.size() - getUnRegisteredNodes().size();
   	}
	
	protected Link getConnectionByID(String id){
		return links.get(id);
	}
	
	protected boolean updateConnectionID(String newID, Link connection){
		if (connection == null)
			return false;
		
		if (!add(newID,connection))
			return false;

		links.remove(connection.getLinkId());
		connection.setLinkId(newID);
		return true;
	}

	protected boolean add(String connectionID, Link connection){
		if (links.containsKey(connectionID))
			return false;
		return links.put(connectionID,connection)==null? true: false;
	}
	
	protected void listConnections(){
     	System.out.println(Link.getHeader());
   		for (Iterator<Link>  linkList = links.values().iterator(); linkList.hasNext();) 
   			System.out.println(linkList.next().toString());
	}
}
