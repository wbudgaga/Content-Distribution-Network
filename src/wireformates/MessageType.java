package wireformates;
// contains the messages types and their equivalent classes' names
// it is very important to keep the order the same in both lists 
public interface MessageType {
	public static final int REGISTER_REQUEST 		= 0;
	public static final int REGISTER_RESPONSE 		= 1;
	public static final int DEREGISTER_REQUEST 		= 2;
	public static final int DEREGISTER_RESPONSE 	= 3;
	public static final int PEER_ROUTER_LIST 		= 4;
	public static final int LINK_WEIGHT_UPDATE		= 5;
	
	public static final int DATA					= 6;
	public static final int LINK_INFO				= 7;
	public static final int ROUTER_INFO				= 8;
		
	public static enum ClassName{
		RegisterRequest,
		RegisterResponse,
		DeRegisterRequest,
		DeRegisterResponse,
		PeerRouterList,
		LinkWeightUpdate,
		Data,
		LinkInfo,
		RouterInfo;

		 public static String get(int i){
			 return values()[i].toString();
		 }
	}

	
}
