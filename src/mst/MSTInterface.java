package mst;

import graph.Network;

public interface MSTInterface {
	
	public static final String	KRUSKAL 	= "MSTKruskal";	
//	public static final String	PRIM 		= "MSTPrim";
	
	public void init(Network graph);
	public void computeMST();
}
