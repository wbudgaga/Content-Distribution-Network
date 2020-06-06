package wireformates;

import node.Node;

import communications.Link;
//this class represents a DeRegisterResponse message. It is used to pack and unpack the DeRegisterResponse message by using the methods in superclass
//the method handle() calls the method handle() in receiver to handle the message
public class DeRegisterResponse extends Response{
	
	public DeRegisterResponse() {
		super(DEREGISTER_RESPONSE);
	}
	
	public String getMessageType() {
		return "DEREGISTER_RESPONSE";
	}

	@Override
	public void handle(Link sender, Node receidver) {
		receidver.handle(sender, this);
	}

}
