package messageHandlers;

import java.io.IOException;

import wireformates.Data;
import wireformates.Message;
import communications.Link;

// this class deals with messages of type: Data
// this class handles the received message and performing the process for sending message 
public class DataHandler extends Handler{
	
	private void  sendDataMessage(Data msg){
		for(int i = 0; i< msg.getNumberOfRemotes();++i){
			String remoteID = msg.getRemote(i);
			try {
				node.sendMessage(msg, remoteID);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void sendData(){
		Data msg = new Data();
		msg.setSender(node.getId());
		msg.setTracker(node.incTracker());
		msg.setRoutingPlan(node.getRoutingPlan());
		msg.prepareTheRountingProcess();
   		sendDataMessage(msg);
	}
	
	@Override
	public void perform(String remoteID) throws IOException {
		sendData();
	}

	private void handleDataMessage(Link sender, Data message) throws IOException {
		message.setSender(node.getId());
		message.prepareTheRountingProcess();
		node.setTracker(message.getTracker());
		System.out.println("Data received from "+ sender.getLinkId()+" (Tracker:"+node.getTracker()+")");
		System.out.println("-------------------------------------- ");
		sendDataMessage(message);	
	}

	@Override
	public void handle(Link sender, Message message) throws IOException {
		handleDataMessage(sender,(Data) message);
	}


}
