package mst;

import graph.Network;

public interface MST {
	
	public static final String	KRUSKAL 	= "Kruskal";	

	public void init(Network graph);
	public void computeMST();
}
