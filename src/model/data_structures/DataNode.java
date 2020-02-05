package model.data_structures;

public class DataNode<T>{

	private DataNode<T> previous;
	
	private DataNode<T> next;
	
	private T nodeInfo;
	
	public DataNode(T nodeInfo){
		this.setNodeInfo(nodeInfo);
	}

	public DataNode<T> getPrevious() {
		return previous;
	}

	public DataNode<T> getNext() {
		return next;
	}

	public void setNext(DataNode<T> next) {
		this.next = next;
		next.previous = this;
	}

	public T getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(T nodeInfo) {
		this.nodeInfo = nodeInfo;
	}
	
}
