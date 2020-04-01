package model.data_structures;

public interface IMaxPQ<Val> {

	public void insert(Val v);
	
	public Val max();
	
	public Val[] max(int n);
	
	public Val delMax();
	
	public boolean isEmpty();
	
	public int size();
	
}
