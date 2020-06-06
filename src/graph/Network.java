package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.Graph;

//this class is used by Router and Discovery classes to represent the CDN network

public class Network {
	private ArrayList<String> 				vertices 		= new ArrayList<String>();
	private ArrayList<Edge> 				edges 			= new ArrayList<Edge>();
	private ArrayList<Edge> 				mst 			= new ArrayList<Edge>();
	private ArrayList<ArrayList<String>> 	pathsList 		= new ArrayList<ArrayList<String>>();
	private Map<String,ArrayList<String>> 	adjacencyList 	= new HashMap<String,ArrayList<String>>();
	private String							root;
	
	private void init(){
		adjacencyList.clear();
		for(int i =0 ; i < edges.size() ; ++ i){
			adjacencyList.put(edges.get(i).getNode1(), new ArrayList<String>() );
			adjacencyList.put(edges.get(i).getNode2(), new ArrayList<String>() );
		}
	}
	
	private void add(String v1, String v2){
		ArrayList<String> vertexList = adjacencyList.get(v1);
		vertexList.add(v2);
	}
	
	public void createCompressedList(){
		init();
		for(int i =0 ; i < edges.size() ; ++ i){
			add(edges.get(i).getNode1(), edges.get(i).getNode2());
		}
	}
		
	public  boolean generateRegularGraph(int degree, int min, int max){
		Graph graph = new Graph(this,min, max);
		if (!graph.generateRegularGraph(degree)){
			System.out.println("invalid degree");
			return false;
		}
		return true;
	}
	
	
	public boolean updateMSTPaths(){
		if (mst.isEmpty())
			return false;
		pathsList.clear();
		Graph graph = new Graph(this);
		graph.createPathsList(root);
		return true;
	}

	
	public boolean update(){
		if (mst.isEmpty())
			return false;
		return updateMSTPaths();
	}

	public void cleanMST(){
		mst.clear();
	}
	
	public void addToMST(Edge e){
		mst.add(e);
	}
	
	public ArrayList<Edge> getMST(){
		return mst;
	}
	
	public void addPath(ArrayList<String> path) {
		this.pathsList.add(path);
	}

	public int findVertex(String v){
		return vertices.indexOf(v);
	}
	
	public boolean addVertex(String v){
		if (findVertex(v)==-1){
			vertices.add(v);
			return true;
		}
		return false;
	}

	public int findEdge(String v1, String v2){
		for(int i=0;i<edges.size();++i){
			Edge e = this.getEdge(i);
			if (e.isEqual(v1, v2))
				return i;
		}	
		return -1;
	}
		
	public boolean addEdge(String v, String u, int weight){
		if (findEdge(v,u)!=-1)
			return false;
		
		edges.add(new Edge(v,u,weight));
		addVertex(v);
		addVertex(u);
		
		return true;
	}
	
	public boolean addEdge(String v, int weight){
		return addEdge(root , v, weight);
	}
	
	public boolean addEdge(Edge e){
		return addEdge(e.getNode1(), e.getNode2(), e.getWeight());
	}
	
	public void deleteEdges(){
		edges.clear();
	}
	
///////////////////////////////////////////////////////////////////
//			Getters and setters methods							 //
///////////////////////////////////////////////////////////////////	
	public  Network(String root){	
		this.root = root;
	}

	public ArrayList<String> getVertices() {
		return vertices;
	}
	
	public void setVertices(ArrayList<String> vertexList) {
		vertices = vertexList;
	}

	public ArrayList<Edge> getEdges() {
		return edges;
	}
	
	public void setEdges(ArrayList<Edge> edgeList) {
		edges = edgeList;
	}

	public int getNumberOfPaths(){
		return pathsList.size();
	}

	public ArrayList<String> getPath(int index) {
		if (index<getNumberOfPaths())
			return pathsList.get(index);
		return null;
	}

	public int getDegree(String v){
		int count = 0;
		for(int i=0;i<vertices.size();++i){
			String u = vertices.get(i);
			if (findEdge(v,u)!=-1)
				++count;
		}
		return count;
	}
	
	public Edge getEdge(int index){
		if (index>= edges.size())
			return null;
		return edges.get(index);
	}
	
	public int getNumberOfEdges(){
		return edges.size();
	}
	
	public String getVertex(int index){
		if (index>= vertices.size())
			return null;
		return vertices.get(index);
	}
	
	public int getNumberOfVertices(){
		return vertices.size();
	}
	
	public ArrayList<String> getVertexList(String v){
		return adjacencyList.get(v);
	}

}
