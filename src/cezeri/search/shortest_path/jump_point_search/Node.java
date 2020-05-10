package cezeri.search.shortest_path.jump_point_search;


public class Node {
	public int x,y;
	float g,h,f;  //g = from start; h = to end, f = both together
	boolean pass;
	Node parent;
	
	public Node(int x,int y){
		this.x = x;
		this.y = y; 
		this.pass = true;
	}
	
	public void updateGHFP(float g, float h, Node parent){
		this.parent = parent;
		this.g = g;
		this.h = h;
		f = g+h;
	}
	
	public boolean setPass(boolean pass){
		this.pass = pass;
		return pass;
	}
}
