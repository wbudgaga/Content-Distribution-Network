package mst;

import graph.Network;

public class MSTFactory {
	private static MSTFactory instance;  
	
	private MSTFactory(){
	}

	public  static  MSTFactory getInstance() throws ClassNotFoundException{
		if (instance == null)
			instance = new MSTFactory();
	    return instance;
	}

	public MSTInterface createMSTObject(String MST, Network network) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		@SuppressWarnings("unchecked")
		Class<MSTInterface> MST_Class = (Class<MSTInterface>) Class.forName("mst." + MST);
		MSTInterface MST_instance =(MSTInterface) MST_Class.newInstance();
		MST_instance.init(network);
		return MST_instance;
	}
}
