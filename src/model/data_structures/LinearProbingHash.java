package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;

public class LinearProbingHash<Key extends Comparable<Key>, Value> implements ILinearProbing<Key, Value> {

	/**
	 * Array that contains all the key of the map
	 */
	private Key[] keys;
	
	/**
	 * Array that contains all the values of the map
	 */
	private IQueue<Value>[] vals;
	
	/**
	 * Number of elements in the map
	 */
	private int n;
	
	/**
	 * Size of the map
	 */
	private int size;
	
	
	public LinearProbingHash(){
		size = 4;
		n = 0;
		keys = (Key[]) new Comparable[ size ];
		vals = (IQueue<Value>[]) new Object[ size ];
	}
	
	public LinearProbingHash(int capacity){
		size = capacity;
		n = 0;
		keys = (Key[]) new Comparable[ size ];
		vals = new IQueue[ size ];
	}
	
	public void put(Key key, Value value) {
		
		if( key == null || value == null )
			throw new IllegalArgumentException("The key or the value can't be null in put");
		
		if( chargeFactor() >= 0.5  )
			resize(2*size);
		
		int i;
		
		for( i = hash(key); keys[i] != null; i = (i+1) % size ){
			if( keys[i].equals(key) ){
				vals[i].enqueue(value);
				return;
			}
		}
		
		keys[i] = key;
		
		IQueue<Value> queueVal = new Queue<Value>();
		queueVal.enqueue(value);
		vals[i] = queueVal;
		n++;
	}
	
	private void put(Key key, IQueue<Value> value){
		if( key == null || value == null )
			throw new IllegalArgumentException("The key or the value can't be null in private put");
		
		if( chargeFactor() >= 0.5  )
			resize(2*size);
		
		int i;
		
		for( i = hash(key); keys[i] != null; i = (i+1) % size ){
			
		}
		
		keys[i] = key;
		vals[i] = value;
		n++;
	}

	
	public void resize(int capacity) {
		
		// code for searching a prime number upper than the capacity
		boolean primeFound = false;
		int j = capacity;
		for(; !primeFound; j++){
			primeFound = isPrime( j );
		}
		
		LinearProbingHash<Key, Value> temp = new LinearProbingHash<Key, Value>( j );
		
		for( int i = 0; i < size; i++ ){
			if( keys[i] != null ){
				temp.put(keys[i], vals[i]);
			}
		}
		
		keys = temp.keys;
		vals = temp.vals;
		size = temp.size;
	}
	
	private boolean isPrime( int number ){
		boolean isPrime = true;
		int x = 2;
		
		while( isPrime && x <= (number/2) ){
			if( number % x == 0 )
				isPrime = false;
			x++;
		}
		
		return isPrime;
	}
	
	public Iterator<Value> get(Key key) {
		
		if( key == null ) 
			throw new IllegalArgumentException("key can't be null in get");
		
		IQueue<Value> valueQueue = new Queue<>();
		
		for( int i = hash(key); keys[i] != null; i = (i+1) % size ){
			if( keys[i].equals(key) ){
				valueQueue = vals[i];
			}
		}
		
		return valueQueue.iterator();
	}
	
	public IQueue<Value>[] getValues(){
		return vals;
	}
	
	public int size() {
		return n;
	}

	
	public int hash(Key key) {
		return (key.hashCode() & 0x7fffffff) % size;
	}

	
	public boolean isEmpty() {
		return size <= 0;
	}

	private double chargeFactor(){
		return (double) n / (double) size;
	}
	
	public boolean contains(Key key) {
		if (key == null) 
			throw new IllegalArgumentException("argument to contains() is null");
        
		return get(key) != null;
	}

	
	public Iterator<Key> keys() {
		
		IQueue<Key> keyQueue = new Queue<Key>();
		
		for( int i = 0; i < keys.length; i++ )
			keyQueue.enqueue( keys[i] );
		
		return keyQueue.iterator();
	}
	
	public Iterator<Value> values(){
		
		IQueue<Value> valQueue = new Queue<Value>();
		
		for( int i = 0; i < vals.length; i++ ){
			Iterator<Value> valIterator = vals[i].iterator();
			while( valIterator.hasNext() )
				valQueue.enqueue( valIterator.next() );
		}
		
		return valQueue.iterator();
		
	}
	
	public Iterator<Value> deleteAndReturn(){
		IQueue<Value> valQueue = new Queue<Value>();
		
		for( int i = 0; i < vals.length; i++ ){
			IQueue<Value> actValue = vals[i];
			while( !actValue.isEmpty() )
				valQueue.enqueue( actValue.dequeue() );
			
			vals[i] = null;
		}
		
		resize(4);
		
		return valQueue.iterator();
	}

}
