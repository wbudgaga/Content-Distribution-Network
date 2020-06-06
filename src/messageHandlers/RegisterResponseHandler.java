package messageHandlers;

import java.io.IOException;

import node.CDN;

import wireformates.Message;
import wireformates.RegisterResponse;

import communications.Link;

//this class deals with messages of type: RegisterResponse
//this class handles the RegisterResponse message and performing nothing 
public class RegisterResponseHandler  extends Handler{

	private void handleRegisterResponseMessage(Link sender, RegisterResponse message) {
		System.out.println(message.getAdditionalInfo());

		if (message.getStatusCode()== FAIlURE)
			node.closeUnRegisteredNodes();
	}
	
	@Override
	public void handle(Link sender, Message message) {
		if(node.getId().compareTo(CDN.DISCOVERYID) == 0)//only the router receives and handles RegisterResponseMessage.
			return;
		
		handleRegisterResponseMessage(sender,(RegisterResponse) message);		
	}

	@Override
	public void perform(String remoteID) throws IOException {		
	}

}
