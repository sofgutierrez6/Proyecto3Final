package model.data_structures;

import java.util.Comparator;
import model.data_structures.ArregloDinamico;

public class MinPQ <T extends Comparable<T>>{

	private int size;
	private int tamanioMax;
	private T max;
	private ArregloDinamico<T> heap;
	private Comparator<T> comparador;
	public MinPQ(int n, Comparator<T> comparador)
	{
		size=0;
		tamanioMax=n+1;
		heap= new ArregloDinamico<T>(tamanioMax);
		max=heap.darElemento(1);
		this.comparador=comparador;
	}
	/**
	 * Retorna número de elementos presentes en la cola de prioridad
	 * @return
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * Agrega un elemento a la cola. Si el elemento ya existe y tiene una prioridad diferente, el elemento debe actualizarse en la cola de prioridad.
	 * @param elemento
	 */
	public void agregar(T elemento)
	{
		heap.agregar(elemento);
		size++;
		swim(size-2);
	}

	/**
	 * Saca/atiende el elemento máximo en la cola y lo retorna; null en caso de cola vacía
	 * @return
	 */
	public T delMax ()
	{
		T copiaMax=max();
		heap.eliminar(copiaMax);
		size--;
		return copiaMax;
	}

	/**
	 * Obtener el elemento máximo (sin sacarlo de la Cola); null en caso de cola vacía
	 * @return
	 */
	public T max()
	{
		max=heap.darElemento(1);
		return max;
	}

	/**
	 * Retorna si la cola está vacía o no
	 * @return
	 */
	public boolean esVacia ()
	{
		return size==0;
	}

	/**
	 * Comparar 2 objetos usando la comparacion "natural" de su clase
	 * @param v primer objeto de comparacion
	 * @param w segundo objeto de comparacion
	 * @return true si v es menor que w usando el metodo compareTo. false en caso contrario.
	 */
	private boolean more(T v, T w)
	{		
		boolean resp=false;
		if(comparador.compare(v,w)>0)
		{
			resp =true;
		}
		return resp;
	}
	/**
	 * swim
	 * @param k
	 */
	private void swim(int k)
	{
		while (k > 1 && more(heap.darElemento(k/2), heap.darElemento(k)))
		{
			heap.exchange(k/2, k);
			k = k/2;
		}
	}

	/**
	 * sink
	 * @param k
	 */
	private void sink(int k)
	{
		while (2*k <= size)
		{
			int j = 2*k;
			if (j < size && more(heap.darElemento(j), heap.darElemento(j+1))) j++;
			if (!more(heap.darElemento(k), heap.darElemento(j))) break;
			heap.exchange(k, j);
			k = j;
		}
	}
	public boolean contains(T x)
	{
		for(int i= 0; i<heap.darTamano();i++)
		{
			if(heap.darElemento(i).compareTo(x)==0)
			{
				return true;
			}
		}
		return false;
	}

}
