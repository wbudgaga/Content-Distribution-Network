package wireformates;

import node.Node;

import communications.Link;
//this class represents a RegisterResponse message. It is used to pack and unpack the RegisterResponse message by using the methods in superclass
//the method handle() calls the method handle() in receiver to handle the message
public class RegisterResponse 	extends Response{
	
	public RegisterResponse() {
		super(REGISTER_RESPONSE);
	}

	public String getMessageType() {
		return "REGISTER_RESPONSE";
	}

	@Override
	public void handle(Link sender, Node receidver) {
		receidver.handle(sender, this);
	}

}
