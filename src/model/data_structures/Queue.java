package model.data_structures;

public class Queue<T extends Comparable<T>> implements IQueue<T> {

	private DataNode<T> front;
	
	private DataNode<T> back;
	
	private int size;
	
	public Queue(){
		size = 0;
		front = null;
		back = null;
	}
	
	public void enqueue(T nodeInfo) {
		
		DataNode<T> dataNode = new DataNode<T>( nodeInfo );
		
		if( front == null ){
			front = dataNode;
		}
		else if( back == null ){
			back = dataNode;
			front.setNext(back);
		}
		else{
			back.setNext(dataNode);
			back = dataNode;
		}
		
		size++;
	}

	
	public T dequeue() {
		
		DataNode<T> nodeToDequeue = front;
		
		if( nodeToDequeue != null )
			front = nodeToDequeue.next();
		
		size--;
		
		return nodeToDequeue != null ? nodeToDequeue.getNodeInfo() : null;
	}

	
	public int size() {
		return size;
	}

	
	public boolean isEmpty() {
		
		return front == null ? true : false;
	}

	
	public DataNode<T> getFront() {
		return front;
	}

}
