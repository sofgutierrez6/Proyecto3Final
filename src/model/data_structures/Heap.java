package model.data_structures;

import java.util.Comparator;

public class Heap <T extends Comparable<T>>
{
	
	private T[] pq;
	private int N = 0;
	private int tamanoMax;
	private Comparator<T> comparador;
	
	public Heap(int max )
	{
		tamanoMax = max;
		pq = (T[]) new Comparable[ max + 1];
		
	}
	
	public Heap(int max,Comparator<T> comparador  )
	{
		tamanoMax = max;
		pq = (T[]) new Comparable[ max + 1];
		this.comparador=comparador;
	}
	
	public boolean estaVacia()
	{
		return N == 0;
	}

	public void agregar( T elemento)
	{
		
		if ( N == tamanoMax )
		{
			tamanoMax = 2 * tamanoMax;
			T [] copia = pq;
			pq = ( T[]) new Comparable[ tamanoMax];
			
			for ( int i = 0; i < N ; i++)
			{
				pq[i] = copia[i];
			}
 		}
		
		pq[++N] = elemento;
		swim(N);
	}
	
	public T delMax()
	{
		T max = pq[1];
		exchange(1 , N--);
		pq[N +1] = null;
		sink(1);
		return max;
		
	}

	private boolean less(int i , int j)
	{
		T first = pq[i];
		T last = pq[j];
		return comparador.compare(first,last) < 0;
		
		//return ((Comparable<?>) pq[i]).compareTo( (Comparable<?>) pq[j]) < 0;
		
	}
	
	private void exchange( int i , int j )
	{
		T ta = pq[i];
		pq[i] = pq[j];
		pq[j] = ta;
	}
	
	private void swim(int k)
	{
		while( k > 1 && less( k/2 , k ))
		{
			exchange(k/2, k);
			k = k/2;
		}
	}
	
	private void sink(int k)
	{
	
		while ( 2*k <= N  )
		{
			int j = 2*k;
			if ( j < N && less( j , j + 1))  j++;
			
			if( !less(k , j )) break;
			
			exchange(k, j);
			k = j;
			
		}
	}
	
	public T max()
	{
		return pq[1];
	}
	
	
	public int darNumeroElementos()
	{
		return N;
	}
}
