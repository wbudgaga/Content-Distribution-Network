package messageHandlers;

import java.io.IOException;

import wireformates.Message;
import node.Node;

import communications.Link;

// Every Handler class must implements this interface
public interface MessageHandler {
	public static final byte 	SUCCEESS		= 100;
	public static final byte 	FAIlURE			= 101;

	// it is very important that the order of names classes corresponds the order of message types in MessageType interface in wireformates package
	public static enum ClassName{
		RegisterRequestHandler,
		RegisterResponseHandler,
		DeRegisterRequestHandler,
		DeRegisterResponseHandler,
		PeerRouterListHandler,
		LinkWeightUpdateHandler,
		DataHandler;
		
		 public static String get(int i){
			 return values()[i].toString();
		 }
	}

	
	public void		handle(Link sender,Message message) throws IOException;//handles received message
	public void		perform(String remoteID) throws IOException;//performs the process for sending a message
	public void 	init(Node node); //assigns the reference of the node to instance variable, node.
}
