package messageHandlers;

import graph.Edge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import node.Router;

import wireformates.LinkInfo;
import wireformates.LinkWeightUpdate;
import wireformates.Message;
import wireformates.RouterInfo;

import communications.Link;

//this class is responsible for handling the receiving LinkWeightUpdate message and performs the process for sending LinkWeightUpdate message.

public class LinkWeightUpdateHandler extends Handler{
	
	private LinkInfo getLinkInfoMessage(Edge edge, PeerRouterListHandler handler){
		RouterInfo router1 = handler.getRouterInfoMessage(edge.getNode1());
		RouterInfo router2 = handler.getRouterInfoMessage(edge.getNode2());
		LinkInfo linkInfo = new LinkInfo();
		linkInfo.setRouter1(router1);
		linkInfo.setRouter2(router2);
		linkInfo.setWeight(edge.getWeight());
		return linkInfo;
	}

	private void sendLinkWeightUpdate(LinkWeightUpdate linkWeightUpdateMessage) throws IOException{	
		for (Iterator<String>  IDs =  node.getConnectionsIDList().iterator(); IDs.hasNext();){ 
 			String remoteID = IDs.next();
 			node.sendMessage(linkWeightUpdateMessage,remoteID);
		}
	}

	private void updateWeights() throws IOException{
		LinkWeightUpdate linkWeightUpdateMessage = new LinkWeightUpdate();
		PeerRouterListHandler peerRouterListHandler = new PeerRouterListHandler();
		peerRouterListHandler.init(node);
		ArrayList<Edge> edges = node.getEdges();
 		for (int i = 0; i < edges.size(); ++i){ 
 			LinkInfo linkInfo = getLinkInfoMessage(edges.get(i),peerRouterListHandler);
 			linkWeightUpdateMessage.addLinkInfo(linkInfo);
 		}
 		sendLinkWeightUpdate(linkWeightUpdateMessage);
	}

	@Override
	public void perform(String remoteID) throws IOException {
		updateWeights();
	}

	private void handleLinkWeightUpdateMessage(Link sender, LinkWeightUpdate message) throws IOException {
		((Router)node).resetNetwork();
		for(int i=0;i< message.getNumberOfLinks();++i){
			String router1ID 	= message.getLinkInfo(i).getRouter1().getRouterID();
			String router2ID 	= message.getLinkInfo(i).getRouter2().getRouterID();
			int weight 			= message.getLinkInfo(i).getWeight();
			((Router)node).addEdge(router1ID, router2ID, weight);
		}
		((Router)node).updateNetwork();
	}

	@Override
	public void handle(Link sender, Message message)
			throws IOException {
		handleLinkWeightUpdateMessage(sender,(LinkWeightUpdate) message);
	}
}
