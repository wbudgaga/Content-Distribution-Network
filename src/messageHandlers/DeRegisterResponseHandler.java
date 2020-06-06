package messageHandlers;

import java.io.IOException;

import node.CDN;

import wireformates.DeRegisterResponse;
import wireformates.Message;

import communications.Link;

//this class deals with messages of type: DeRegisterResponse
//this class handles the DeRegisterResponse message and performing nothing

public class DeRegisterResponseHandler extends Handler{

	private void handleDeRegisterResponseMessage(Link sender, DeRegisterResponse message) {
		System.out.println(message.getAdditionalInfo());
	}
	
	@Override
	public void handle(Link sender, Message message) {
		if(node.getId().compareTo(CDN.DISCOVERYID) == 0)//only the router receives and handles DeRegisterResponseMessage.
			return;
		
		handleDeRegisterResponseMessage(sender,(DeRegisterResponse) message);		
	}

	@Override
	public void perform(String remoteID) throws IOException {
	}

}
