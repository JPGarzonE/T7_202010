package model.data_structures;

import java.util.ArrayList;

public class DataList<T> implements IDataList<T> {

	private DataNode<T> firstNode;
	
	private DataNode<T> lastNode;
	
	public int getSize(){
		
		DataNode<T> actualNode = firstNode;
		
		int size = 0;
		
		if( firstNode != null ){
			size++;
		
			while( actualNode.getNext() != null ){
				size++;
				actualNode = actualNode.getNext();
			}
		}
		return size;
	}

	public void addNode(T nodeInfo) {
		
		DataNode<T> node = new DataNode<T>(nodeInfo);
				
		if( lastNode != null ) {
			lastNode.setNext(node);
			lastNode = node;
		
		}else if( firstNode != null ){
			lastNode = node;
			firstNode.setNext(lastNode);
		}else{
			firstNode = node;
		}
	}

	public DataNode<T> getFirstNode() {
		return firstNode;
	}

	public DataNode<T> getLastNode() {
		return lastNode;
	}
	
}
