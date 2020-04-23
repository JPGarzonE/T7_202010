package model.data_structures;

import java.util.Comparator;

public interface IMaxPQ<Val> {

	public void insert(Val v);
	
	public Val max();
	
	public Val[] max(int n);
	
	public Val delMax();
	
	public boolean isEmpty();
	
	public void changeComparator(Comparator<Val> comp);
	
	public int size();
	
}
