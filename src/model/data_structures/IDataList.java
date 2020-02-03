package model.data_structures;

public interface IDataList<T> {

	public DataNode<T> getNode( int objectId );
	
	public int getSize();
	
	public void addNode( DataNode<T> node );
	
}
