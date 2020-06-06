package util;

import graph.Edge;
import graph.Network;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

// this class is used by network class to create random regular graph
public class Graph{
	private Network	network;
	private	int min;
	private int max;
	
	public Graph(Network network){
		this.network = network;
	}
	public Graph(Network network, int min, int max){
		this(network);
		this.min = min;
		this.max = max;
	}
	
	public  boolean isVAlidDegree(int degree, int	numberOfVertices){
		if ((numberOfVertices>2 && degree <2) || degree >= numberOfVertices)
			return false;
		float m = 0.5f * numberOfVertices * degree;
		return m == (int) m;
	}
	
	public  static int getRandomNumber(int min, int max){
		int range = max - min + 1;
		Random randomGenerater = new Random();
		int generatedNumber = (int) (range * randomGenerater.nextDouble());
		return generatedNumber + min;
	}
	
	public  static void updateWeights(Network network,  int min, int max){
		for (int i = 0; i < network.getNumberOfEdges(); ++i){
			int weight = getRandomNumber(min, max);
			network.getEdge(i).setWeight(weight);
		}
	}
	
	private  String getMin(ArrayList<String> vertices){
		String v = vertices.get(0);
		for(int i=1;i<vertices.size();++i){
			String u = vertices.get(i);
			if (network.getDegree(u)<network.getDegree(v))
				v = u;
		}
		return v;
	}
	
	private  void makeEdges(String v, ArrayList<String> vertices, int degree){
		while( network.getDegree(v)<degree){
				String u = getMin(vertices);
				int weight = getRandomNumber(min, max);
				network.addEdge(new Edge(v,u,weight));
		}
	}
		
		
	public  boolean generateRegularGraph(int degree){
		if (!isVAlidDegree(degree, network.getNumberOfVertices()))
			return false;
		network.deleteEdges();
		ArrayList<String> vertices = new ArrayList<String>(network.getVertices());
		
		while ( vertices.size()>0 ){
			String v1 = vertices.remove( getRandomNumber(0, vertices.size()-1));
			makeEdges(v1,vertices, degree);
		}
		return true;
	}
	
	private Edge findEdgeContains (ArrayList<Edge> copyOfEdges, String v){
		for(int i =0 ; i < copyOfEdges.size(); ++i)
			if (copyOfEdges.get(i).contains(v)!=null)
				return copyOfEdges.get(i);
			
		return null;
	}
	

	public void findPath(ArrayList<Edge> copyOfEdges,  ArrayList<String> path, Stack<String> stack){
		String v = (String) stack.pop();
		Edge e = findEdgeContains(copyOfEdges, v);
		path.add(v);
		if(e != null){
			String u = e.contains(v);
			copyOfEdges.remove(e);
			stack.push(u);
			findPath(copyOfEdges,path,stack);
			stack.push(u);
		}		
	}
	
	public void findPaths(ArrayList<Edge> 	copyOfEdges, Stack<String> stack){
		if (copyOfEdges.isEmpty() || stack.isEmpty())
			return;
			
		ArrayList<String> path = new ArrayList<String>();
		String v = stack.peek();
		findPath(copyOfEdges,path, stack);
		if(path.size()>1){
			stack.push(v);
			network.addPath(path);
		}
		findPaths(copyOfEdges, stack);
	}
	
	public void createPathsList(String stratVertex){
		ArrayList<Edge> 	copyOfEdges = new ArrayList<Edge>(network.getMST());
		ArrayList<String> 	vertexList 	= new ArrayList<String>(network.getVertices());
		Stack<String> stack = createStack(vertexList);
		stack.push(stratVertex);
		findPaths(copyOfEdges, stack);
	}
	
	private Stack<String> createStack(ArrayList<String> vertexList){
		Stack<String> stack = new Stack<String>();
		for (int i = 0; i< vertexList.size(); ++i)
			stack.push(vertexList.get(i));
		return stack;
	}

}
