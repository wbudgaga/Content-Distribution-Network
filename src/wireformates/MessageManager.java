package wireformates;

public class MessageManager implements MessageType{
	private static MessageManager instance;

	private MessageFactory messageFactory;
	
	private MessageManager() throws ClassNotFoundException{
		messageFactory = MessageFactory.getInstance();
	}
	
	public  static  MessageManager getInstance() throws ClassNotFoundException {
		if (instance == null)
			instance = new MessageManager();
	    return instance;
	}

	public Message createMessageObject(int messageID) throws InstantiationException, IllegalAccessException{
		return messageFactory.createMessageObject(messageID);
	}
}
