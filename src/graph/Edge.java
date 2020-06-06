package graph;

// this class represents a connection between two Nodes. it is used by Network class for some operations.
public class Edge {
	private String 	node1;
	private String 	node2;
	private int 	weight;

	public Edge(String node1, String node2){
		this.node1 = node1;
		this.node2 = node2;
		this.weight = 0;
	}
	
	public Edge(String node1, String node2, int weight){
		this(node1, node2);
		this.weight = weight;
	}


	public boolean isEqual(String v1, String v2){
		if ((node1.compareTo(v1)==0 && node2.compareTo(v2)==0) || (node1.compareTo(v2)==0 && node2.compareTo(v1)==0))
			return true;
		return false;
	}
	
	public String contains(String v){
		if (node1.compareTo(v)==0)
			return node2;	
		if (node2.compareTo(v)==0)
			return node1;
		return null;
	}
	
///////////////////////////////////////////////////////////////////
//			Getters and setters methods							 //
///////////////////////////////////////////////////////////////////	

	public String toString(){
		return String.format("%-15s %-15s %-4s", getNode1(),getNode2(),getWeight());
	}

	public String getNode1() {
		return node1;
	}

	public String getNode2() {
		return node2;
	}

	public int getWeight() {
		return weight;
	}

	public void setNode1(String node1) {
		this.node1 = node1;
	}

	public void setNode2(String node2) {
		this.node2 = node2;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

}
