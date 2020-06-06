package messageHandlers;

import java.io.IOException;

import wireformates.Message;

import communications.Link;

import node.Node;
// all message handles must extends this class.
public abstract class Handler implements MessageHandler{
	protected Node node;
	
	public void init(Node node){
		this.node = node;
	}

	@Override
	public abstract void handle(Link sender, Message message) throws IOException ;
}
