package cezeri.search.shortest_path.jump_point_search;

import java.util.Iterator;
import java.util.LinkedList;
//This is actually just a priority queue, should be a heap when implemented for game use
//in order to improve runtime for adding an element

public class Heap {
	LinkedList<float[]> list;
	Iterator<float[]> listit;
	
	public Heap(){
		list = new LinkedList<float[]>();
	}
	
	public void add(float[] newXY){
		if (list.size()>0){
			listit = list.iterator();
			float[] tmp;
			int count = 0;
			while (true){
				tmp = listit.next();
				if (tmp[2]>newXY[2]){
					list.add(count, newXY);
					break;
				}
				else{
					count++;
				}
				if (!listit.hasNext()){
					list.add(count, newXY);
					break;
				}
			}
		}
		else{
			list.add(newXY);
		}
	}
	
	public float[] pop(){
		try{
			float[] tmp = list.pop();
			return tmp;
		}
		catch(Exception e){
			System.out.println("List is Empty!!");
			return null;
		}
	}
	
	public void printHeap(){
		listit = list.iterator();
		float[] tmp = listit.next();
		boolean flag = true;
		while (flag){
			System.out.println("Node f: "+tmp[2]);
			if (listit.hasNext()){
				tmp = listit.next();
			}
			else{
				flag = false;
			}
		}
		System.out.println("------");
	}
	
	public int getSize(){
		return list.size();
	}
}
