package mst;

import graph.Edge;
import graph.Network;

import java.util.ArrayList;
import java.util.Hashtable;

public class MSTKruskal implements MSTInterface{
	private ArrayList<Edge> sortedEdges;
	private Hashtable<String,String>vertexSet = new Hashtable<String,String>();
	private Hashtable<String,Integer>rank = new Hashtable<String,Integer>();
	
	private Network graph;
	
	public void init(Network graph){
		this.graph = graph;
	}
	
	private void createSet(){
		for(int i=0; i<graph.getVertices().size(); ++i){
			String v = graph.getVertex(i);
			rank.put(v,new Integer(0));
			vertexSet.put(v, v);
		}
	}
		
	private String findSet(String x){
		if (x!=vertexSet.get(x))
			return findSet(vertexSet.get(x));
		return x;
	}
	
	private void editMapValue(String key, String newValue){
		vertexSet.remove(key);
		vertexSet.put(key, newValue);
	}
	
	private void union(String x,String y){
			editMapValue(findSet(y),findSet(x));
	}
	
	private void sortEdgesInIncreasingOrder(){
		sortedEdges = new ArrayList<Edge>(graph.getEdges());
	
		for (int i=1; i<sortedEdges.size();++i){
			Edge key = sortedEdges.get(i);
			int j = i - 1;
			while (j>=0 && sortedEdges.get(j).getWeight()>key.getWeight()){
				sortedEdges.set(j + 1, sortedEdges.get(j));
				--j;
			}
			sortedEdges.set(j + 1,key);	
		}
	}
	
	private void findMSTEdges(){
		for (int i=0; i<sortedEdges.size();++i){
			Edge v = sortedEdges.get(i);
			if (findSet(v.getNode1())!= findSet(v.getNode2())){
				union(v.getNode1(),v.getNode2());
				graph.addToMST(v);
			}
		}
	}
		
	public void computeMST(){
		graph.cleanMST();
		createSet();
		sortEdgesInIncreasingOrder();
		findMSTEdges();
	}
	
}
