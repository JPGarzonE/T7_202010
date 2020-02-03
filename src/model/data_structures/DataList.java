package model.data_structures;

public class DataList<T> implements IDataList<T> {

	private DataNode<T> firstNode;
	
	private DataNode<T> lastNode;
	
	public DataNode<T> getNode(int objectId) {
		return null;
	}
	
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

	public void addNode(DataNode<T> node) {
		if( lastNode != null ) {
			lastNode.setNext(node);
			lastNode = node;
		
		}else if( firstNode != null ){
			firstNode.setNext(node);
			lastNode = node;
		}else{
			firstNode = node;
		}
	}

	public DataNode<T> getFirstNode() {
		return firstNode;
	}

	public void setFirstNode(DataNode<T> firstNode) {
		this.firstNode = firstNode;
	}

	public DataNode<T> getLastNode() {
		return lastNode;
	}

	public void setLastNode(DataNode<T> lastNode) {
		this.lastNode = lastNode;
	}
	
}
