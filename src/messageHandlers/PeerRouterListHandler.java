package messageHandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import node.Router;

import wireformates.Message;
import wireformates.PeerRouterList;
import wireformates.RouterInfo;

import communications.Link;

// this class is responsible for handling the receiving PeerRouterList message and performs the process for sending PeerRouterList.
public class PeerRouterListHandler extends Handler{

	protected RouterInfo getRouterInfoMessage(String routerID){
		RouterInfo routerInfo = new RouterInfo();
		routerInfo.setHostName(node.getConnectionHostName(routerID));
		routerInfo.setPortNum(node.getConnectionPortNum(routerID));
		routerInfo.setRouterID(routerID);
		return routerInfo;
	}
	
	private void sendPeerRouterList(String remoteID, ArrayList<String> adjacencyRouters) throws  IOException{
		if (adjacencyRouters.size()==0)
			return;
		PeerRouterList peerRouterListMessage = new PeerRouterList();
		for (Iterator<String>  IDs = adjacencyRouters.iterator(); IDs.hasNext();){ 
			RouterInfo routerInfo = getRouterInfoMessage(IDs.next());
			peerRouterListMessage.addRouter(routerInfo);
		}
		node.sendMessage(peerRouterListMessage,remoteID);
	}
	
	public void sendPeerRouterList() throws InstantiationException, IllegalAccessException, IOException{
 		for (Iterator<String>  IDs =  node.getConnectionsIDList().iterator(); IDs.hasNext();){ 
 			String remoteID = IDs.next();
 			ArrayList<String> peerRouterList = node.getPeerList(remoteID);
 			sendPeerRouterList(remoteID, peerRouterList);
 		}
	}

	@Override
	public void perform(String remoteID) throws IOException {
		try {
			sendPeerRouterList();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
	}
	
	private void handlePeerRouterListMessage(Link sender, PeerRouterList message) throws IOException {
		for(int i=0;i< message.getNumberOfRouters();++i){
			String remoteID = message.getRouter(i).getRouterID();
			if(((Router)node).connect(message.getRouter(i).getHostName(),message.getRouter(i).getPortNum(),remoteID)){
				((Router)node).sendRegisterRequestMessage(remoteID);
			}
		}
	}

	@Override
	public void handle(Link sender, Message message)throws IOException {
		handlePeerRouterListMessage(sender,(PeerRouterList) message);
		
	}
}
