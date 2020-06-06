package wireformates;

import node.Node;

import communications.Link;

//this class represents a DeRegisterRequest message. It is used to pack and unpack the DeRegisterRequest message by using the methods in superclass
//the method handle() calls the method handle() in receiver to handle the message
public class DeRegisterRequest extends Request{
	public DeRegisterRequest() {
		super(DEREGISTER_REQUEST);
	}
	
	public String getMessageType() {
		return "DEREGISTER_REQUEST";
	}

	public  void   handle(Link sender, Node receidver){
		receidver.handle(sender, this);
	}

}
