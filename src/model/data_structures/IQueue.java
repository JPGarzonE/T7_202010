package model.data_structures;

public interface IQueue<T extends Comparable<T>> {
	
	public void enqueue( T nodeInfo );
	
	public T dequeue();
	
	public int size();
	
	public boolean isEmpty();
	
	public DataNode<T> getFront();
	
}
