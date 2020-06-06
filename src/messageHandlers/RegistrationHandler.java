package messageHandlers;

import java.io.IOException;
import java.net.InetAddress;

import wireformates.*;

import communications.Link;

//this is a supper class of DeRegisterRequestHandler and RegisterRequestHandler classes
public abstract class RegistrationHandler extends Handler{

	protected boolean checkIPAddressmatching(Link sender, Request requestMessage) {
		if (sender.getHostAddress().compareTo(requestMessage.getIpAddress()) == 0 ||
				sender.getHostName().compareTo(requestMessage.getIpAddress()) == 0)
			return true;
		return false;
	}

	protected void sendSuccessResponse(int messageID,Link sender) throws IOException{
		Response responseMessage = (Response) node.createMessageObject(messageID);
		responseMessage.setStatusCode(SUCCEESS);
		responseMessage.setAdditionalInfo(node.getConfirmationRegisterResponseText());
		node.sendMessage(responseMessage, sender.getLinkId());
	}
		
	protected void sendFailResponse(int messageID, String senderID, String reasonMessage) throws IOException {
		Response responseMessage = (Response) node.createMessageObject(messageID);
		responseMessage.setStatusCode(FAIlURE);
		responseMessage.setAdditionalInfo(responseMessage.getMessageType()+ " FAILURE!: " + reasonMessage);
		node.sendMessage(responseMessage, senderID);
	}
	
	protected Message fillRequestMessage(Request requestMessage, String remoteID) throws IOException{
		requestMessage.setAssignedID(node.getId());
		requestMessage.setIpAddress(InetAddress.getLocalHost().getHostAddress());
		requestMessage.setPortNumber(node.getPort());
		return requestMessage;
	}

	
	public abstract void handle(Link sender, Message message) throws IOException ;
}
