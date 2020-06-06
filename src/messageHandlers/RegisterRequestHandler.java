package messageHandlers;

import java.io.IOException;

import wireformates.*;
import communications.Link;

//this class deals with messages of type: RegisterRequest
//this class handles the RegisterRequest message by sending response and performing the process for sending RegisterRequest message 
public class RegisterRequestHandler extends RegistrationHandler{
		
	@Override
	public void perform(String remoteID) throws IOException {
		RegisterRequest msg = new RegisterRequest();
		node.sendMessage(fillRequestMessage(msg, remoteID),remoteID);
	}
	
	private void register(String remoteID, Link sender) throws IOException{
		if(!node.register(remoteID, sender)){
			sendFailResponse(MessageManager.REGISTER_RESPONSE, sender.getLinkId(),"Node with this ID is already registered");
			return;
		}
		sendSuccessResponse(MessageManager.REGISTER_RESPONSE,sender);
	}

	private void handleRegisterRequestMessage(Link sender, Request message) throws IOException {
		if (!checkIPAddressmatching(sender, message)){
			sendFailResponse(MessageManager.REGISTER_RESPONSE, sender.getLinkId(),"mismatch in the address that is "+
					"specified in the registration request and the IP address of the request");
			return;
		}
		sender.setPort(message.getPortNumber());
		register(message.getAssignedID(), sender);
	}
	
	@Override
	public void handle(Link sender, Message message) throws IOException {
		handleRegisterRequestMessage(sender, (Request) message);
	}
}
