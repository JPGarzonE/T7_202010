package model.data_structures;

public interface IDataList<T> {
	
	public int getSize();
	
	public DataNode<T> getFirstNode();
	
	public DataNode<T> getLastNode();
	
	public void addNode( T nodeInfo );
	
}
