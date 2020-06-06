package messageHandlers;

import java.util.HashMap;
import java.util.Map;

import node.Node;
// this class uses reflection to create an instance of one message handle classes depending on messageID
public class MessageHandlerFactory {
	private static MessageHandlerFactory instance;
	private Map<Integer,Class<MessageHandler>> classList = new HashMap<Integer,Class<MessageHandler>>();  
	
	private void loadMessageHandlerClasses() throws ClassNotFoundException{
		MessageHandler.ClassName[] classIDs = MessageHandler.ClassName.values();
		for (int i=0;i<classIDs.length;++i){
			@SuppressWarnings("unchecked")
			Class<MessageHandler> messageHandlerClass = (Class<MessageHandler>) Class.forName("messageHandlers." + classIDs[i].toString());
			classList.put(new Integer(i), messageHandlerClass);
		}
	}
	
	
	private MessageHandlerFactory() throws ClassNotFoundException{	
		loadMessageHandlerClasses();
	}
	

	public  static  MessageHandlerFactory getInstance() throws ClassNotFoundException{
		if (instance == null)
			instance = new MessageHandlerFactory();
	    return instance;
	}

	public MessageHandler createMessageHandlerObject(int messageID, Node node) throws InstantiationException, IllegalAccessException{
		Class<MessageHandler> messageClass =  classList.get(new Integer(messageID));
		MessageHandler messageHandler =(MessageHandler) messageClass.newInstance();
		messageHandler.init(node);
		return messageHandler;
	}

}
