package messageHandlers;

import java.io.IOException;

import communications.Link;

import wireformates.DeRegisterRequest;
import wireformates.Message;
import wireformates.MessageManager;

//this class deals with messages of type: DeRegisterRequest
//this class handles the DeRegisterRequest message by sending response and performing the process for sending DeRegisterRequest message 

public class DeRegisterRequestHandler extends RegistrationHandler{
	
	@Override
	public void perform(String remoteID) throws IOException {
		DeRegisterRequest msg = new DeRegisterRequest();
		node.sendMessage(fillRequestMessage(msg, remoteID),remoteID);
	}

	private void deRegister(Link sender) throws IOException{
		if(!node.deRegister(sender.getLinkId()))
			sendFailResponse(MessageManager.DEREGISTER_RESPONSE, sender.getLinkId(),"Node with this ID isn't registered");
	}

	private void handleDeRegisterRequestMessage(Link sender, DeRegisterRequest message) throws IOException {
		if (!checkIPAddressmatching(sender, message)){
			sendFailResponse(MessageManager.DEREGISTER_RESPONSE, sender.getLinkId(),"mismatch in the address that is "+
					"specified in the registration request and the IP address of the request");
			return;
		}
		deRegister(sender);
	}

	@Override
	public void handle(Link sender, Message message)throws  IOException {
		handleDeRegisterRequestMessage(sender, (DeRegisterRequest) message);
	}

}
