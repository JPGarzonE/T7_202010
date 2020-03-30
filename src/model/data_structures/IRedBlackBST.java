package model.data_structures;

public interface IRedBlackBST<Key extends Comparable<Key>, Val> {

	public void put(Key k, Val v);
	
	public Val get(Key k);
	
	public void delete(Key k);
	
	public boolean contains(Key k);
	
	public int size();
	
	public Key floor(Key k);
	
	public Key ceiling(Key k);
	
	public int rank(Key k);
	
	public Key select(int i);
	
	public Iterable<Key> keys();
	
	public Iterable<Key> keys(Key min, Key max);
	
}
