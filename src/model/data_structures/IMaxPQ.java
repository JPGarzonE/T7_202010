package model.data_structures;

public interface IMaxPQ<Key extends Comparable<Key>, Val> {

	public void insert(Key k, Val v);
	
	public Val max();
	
	public Val[] max(int n);
	
	public Val delMax();
	
	public boolean isEmpty();
	
	public int size();
	
}
