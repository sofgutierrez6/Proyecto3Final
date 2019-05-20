package model.data_structures;

import java.util.Iterator;

import model.data_structures.TablaHash.IteratorTablaHashSCKeys;

public class HashTableSC <Key, Value>{

	 private static final int INIT_CAPACITY = 4;

	    private int n;                                // number of key-value pairs
	    private int m;                                // hash table size
	    private SequentialSearchSt<Key, Value>[] st;  // array of linked-list symbol tables
	    private SequentialSearchSt<Key, Value> lista; 

	    /**
	     * Initializes an empty symbol table.
	     */
	    public HashTableSC() {
	        this(INIT_CAPACITY);
	    } 

	    /**
	     * Initializes an empty symbol table with {@code m} chains.
	     * @param m the initial number of chains
	     */
	    public HashTableSC(int m) {
	        this.m = m;
	        lista=new SequentialSearchSt<Key, Value>();
	        st = (SequentialSearchSt<Key, Value>[]) new SequentialSearchSt[m];
	        for (int i = 0; i < m; i++)
	            st[i] = new SequentialSearchSt<Key, Value>();
	    } 

	    // resize the hash table to have the given number of chains,
	    // rehashing all of the keys
	    private void resize(int chains) {
	    	HashTableSC<Key, Value> temp = new HashTableSC<Key, Value>(chains);
	        for (int i = 0; i < m; i++) {
	            for (Key key : st[i].keys()) {
	                temp.put(key, st[i].get(key));
	            }
	        }
	        this.m  = temp.m;
	        this.n  = temp.n;
	        this.st = temp.st;
	    }

	    // hash value between 0 and m-1
	    private int hash(Key key) {
	        return (key.hashCode() & 0x7fffffff) % m;
	    } 

	    /**
	     * Returns the number of key-value pairs in this symbol table.
	     *
	     * @return the number of key-value pairs in this symbol table
	     */
	    public int size() {
	        return n;
	    } 

	    /**
	     * Returns true if this symbol table is empty.
	     *
	     * @return {@code true} if this symbol table is empty;
	     *         {@code false} otherwise
	     */
	    public boolean isEmpty() {
	        return size() == 0;
	    }

	    /**
	     * Returns true if this symbol table contains the specified key.
	     *
	     * @param  key the key
	     * @return {@code true} if this symbol table contains {@code key};
	     *         {@code false} otherwise
	     * @throws IllegalArgumentException if {@code key} is {@code null}
	     */
	    public boolean contains(Key key) {
	        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
	        return get(key) != null;
	    } 

	    /**
	     * Returns the value associated with the specified key in this symbol table.
	     *
	     * @param  key the key
	     * @return the value associated with {@code key} in the symbol table;
	     *         {@code null} if no such value
	     * @throws IllegalArgumentException if {@code key} is {@code null}
	     */
	    public Value get(Key key) {
	        if (key == null) throw new IllegalArgumentException("argument to get() is null");
	        int i = hash(key);
	        return st[i].get(key);
	    } 

	    /**
	     * Inserts the specified key-value pair into the symbol table, overwriting the old 
	     * value with the new value if the symbol table already contains the specified key.
	     * Deletes the specified key (and its associated value) from this symbol table
	     * if the specified value is {@code null}.
	     *
	     * @param  key the key
	     * @param  val the value
	     * @throws IllegalArgumentException if {@code key} is {@code null}
	     */
	    public void put(Key key, Value val) {
	        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
	        if (val == null) {
	            delete(key);
	            return;
	        }

	        // double table size if average length of list >= 10
	        if (n >= 10*m) resize(2*m);

	        int i = hash(key);
	        if (!st[i].contains(key)) n++;
	        st[i].put(key, val);
	        //lista.put(key, val);
	    } 

	    public SequentialSearchSt<Key, Value> getList()
	    {
	    	return lista;
	    }
	    
	    /**
	     * Removes the specified key and its associated value from this symbol table     
	     * (if the key is in this symbol table).    
	     *
	     * @param  key the key
	     * @throws IllegalArgumentException if {@code key} is {@code null}
	     */
	    public void delete(Key key) {
	        if (key == null) throw new IllegalArgumentException("argument to delete() is null");

	        int i = hash(key);
	        if (st[i].contains(key)) n--;
	        st[i].delete(key);

	        // halve table size if average length of list <= 2
	        if (m > INIT_CAPACITY && n <= 2*m) resize(m/2);
	    } 

	    // return keys in symbol table in a Queue
	    public Queue<Key> keysQueue() {
	        Queue<Key> queue = new Queue<Key>();
	        for (int i = 0; i < m; i++) 
	        {
	            for (Key key : st[i].keys())
	            {
	                queue.enqueue(key);
	            	System.out.println(i);
	            }
	        }
	        return queue;
	    } 
	    
	    public Iterator<Key> keys() 
		{
			return new IteratorTablaHashSCKeys();
		}
	    public class IteratorTablaHashSCKeys implements Iterator<Key>
		{
	    	Key next;
	    	
	    	public IteratorTablaHashSCKeys() 
			{
	    		//next=keysQueue().dequeue();
			}
			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return !keysQueue().isEmpty();
			}

			@Override
			public Key next() {
				// TODO Auto-generated method stub
				return keysQueue().dequeue();
			}
	    	
		}

	    public Iterator<Value> values() 
		{
			return new IteratorTablaHashSCValues();
		}
	    public class IteratorTablaHashSCValues implements Iterator<Value>
		{
	    	Value next;
	    	private Queue<Key> q;
	    	public IteratorTablaHashSCValues() 
			{
	    		q=keysQueue();	
			}
			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return !q.isEmpty();
			}

			@Override
			public Value next() {
				// TODO Auto-generated method stub
				
				if(!q.isEmpty())
				{
					Key key=(Key) q.dequeue();
					int i = hash(key);
		    		next=st[i].get(key);
				}	
							
		        return next;
				
			}
	    	
		}
}
