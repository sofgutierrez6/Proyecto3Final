package model.data_structures;

import java.util.Iterator;

public class TablaHash<K extends Comparable<K>, V> 
{
	
	// -----------------------------------------------------------------
		// Atributos
		// -----------------------------------------------------------------

		private int sizeArreglo;

		private int cantElementos;

		private float factorDeCarga;

		private Object[] arreglo;

		// -----------------------------------------------------------------
		// Constructores
		// -----------------------------------------------------------------
		public TablaHash() 
		{
			sizeArreglo = 2;
			cantElementos = 0;
			factorDeCarga = 0;
			arreglo =  new Object[sizeArreglo];
		}

		// -----------------------------------------------------------------
		// M�todos
		// -----------------------------------------------------------------

		public Object[] arreglo()
		{
			return arreglo;
		}
		public int hash(K key) 
		{
			return ((key.hashCode() & 0x7fffffff)%sizeArreglo);
		}

		
		public boolean put(K key, V value) 
		{
			boolean reemplazado = false;
			int index = hash(key);
			if(arreglo[index] == null)
			{
				arreglo[index] = new NodoTablaHash(key, value);
				cantElementos++;
			}
			else
			{
				NodoTablaHash actual = (NodoTablaHash)arreglo[index];
				NodoTablaHash anterior = null;
				while(actual!=null && !reemplazado)
				{
					if(actual.getKey().compareTo(key) == 0)
					{
						actual.changeValue(value);
						reemplazado = true;
					}
					else
					{
						anterior = actual;
						actual = actual.getNext();
					}
				}
				if(!reemplazado)
				{
					anterior.changeNext(new NodoTablaHash(key, value));
					cantElementos++;
				}
			}
			if(actualizarFactorDeCarga())
			{
				rehash();
			}
			return reemplazado;
		}

		public V delete(K key) 
		{
			V respuesta = null;
			int index = hash(key);
			NodoTablaHash actual = (NodoTablaHash)arreglo[index];
			NodoTablaHash anterior = null;
			boolean eliminado = false;
			while(actual != null && !eliminado)
			{
				if(actual.getKey().compareTo(key) == 0)
				{
					respuesta = actual.getValue();
					if(anterior==null)
					{
						arreglo[index] = actual.getNext();
					}
					else
					{
						anterior.changeNext(actual.getNext());
					}
					eliminado = true;
					cantElementos--;
				}
				anterior = actual;
				actual = actual.getNext();
			}
			return respuesta;
		}

		public V get(K key) 
		{
			V respuesta = null;
			int index = hash(key);
			boolean encontrado = false;
			NodoTablaHash actual = (NodoTablaHash)arreglo[index];
			while(actual != null && !encontrado)
			{
				if(actual.getKey().compareTo(key)==0)
				{
					respuesta = actual.getValue();
					encontrado = true;
				}
				actual = actual.getNext();
			}
			return respuesta;
		}
		
		public int getIndex(K key)
		{
			
			int index = hash(key);
			boolean encontrado = false;
			int respuesta = index;
			NodoTablaHash actual = (NodoTablaHash)arreglo[index];
			while(actual != null && !encontrado)
			{
				if(actual.getKey().compareTo(key)==0)
				{
					encontrado = true;
				}
				actual = actual.getNext();
				respuesta++;
			}
			return respuesta;
		}
		public NodoTablaHash get(int i)
		{
			if(i<arreglo.length)
			{
			 return ((TablaHash<K, V>.NodoTablaHash) arreglo[i]);
			}
			else
			{
				return null;
			}
		}

		public V getSecondForm(K key)
		{
			V respuesta = null;
			Iterator<K> iteratorKeys = new IteratorTablaHashSCKeys();
			Iterator<V> iteratorValues = new IteratorTablaHashSCValues();
			boolean encontrado = false;
			while(iteratorKeys.hasNext() && iteratorValues.hasNext() && !encontrado)
			{
				K actual = iteratorKeys.next();
				V actualV = iteratorValues.next();
				if(actual.compareTo(key)==0)
				{
					respuesta = actualV;
					encontrado = true;
				}
			}
			return respuesta;
		}

		
		public void rehash() 
		{
			sizeArreglo = sizeArreglo*2;
			while(!esPrimo(sizeArreglo))
			{
				sizeArreglo++;
			}
			Object[] aux = arreglo;
			arreglo = new Object[sizeArreglo];
			cantElementos=0;
			for (int i = 0; i < aux.length; i++) 
			{
				NodoTablaHash actual = (NodoTablaHash)aux[i];
				while(actual != null)
				{
					put(actual.getKey(), actual.getValue());
					actual = actual.getNext();
				}
			}
		}

		
		public Iterator<K> keys() 
		{
			return new IteratorTablaHashSCKeys();
		}

		public Iterator<V> values()
		{
			return new IteratorTablaHashSCValues();
		}

		public int size()
		{
			return cantElementos;
		}

		private boolean actualizarFactorDeCarga()
		{
			boolean excedio = false;
			factorDeCarga = cantElementos/sizeArreglo;
			if(factorDeCarga>5)
			{
				excedio = true;
			}
			return excedio;
		}

		private boolean esPrimo(int pNumero)
		{
			boolean esPrimo = true;
			for(int i=2; i<=Math.sqrt(pNumero) && esPrimo;i++)
			{
				if(pNumero%i == 0)
				{
					esPrimo = false;
				}
			}
			return esPrimo;
		}	

		public class NodoTablaHash
		{
			// -----------------------------------------------------------------
			// Atributos
			// -----------------------------------------------------------------
			private K key;

			private V value;

			private NodoTablaHash next;

			// -----------------------------------------------------------------
			// Constructor
			// -----------------------------------------------------------------
			public NodoTablaHash(K pKey, V pValue) 
			{
				key = pKey;
				value = pValue;
				next = null;
			}

			// -----------------------------------------------------------------
			// M�todos
			// -----------------------------------------------------------------
			public K getKey()
			{
				return key;
			}

			public V getValue()
			{
				return value;
			}

			public NodoTablaHash getNext()
			{
				return next;
			}

			public void changeValue(V pValue)
			{
				value = pValue;
			}

			public void changeNext(NodoTablaHash pNext)
			{
				next = pNext;
			}
		}

		public class IteratorTablaHashSCKeys implements Iterator<K>
		{
			// -----------------------------------------------------------------
			// Atributos
			// -----------------------------------------------------------------			
			private NodoTablaHash proximo;

			private int index;

			// -----------------------------------------------------------------
			// Constructor
			// -----------------------------------------------------------------
			public IteratorTablaHashSCKeys() 
			{
				proximo = (NodoTablaHash)arreglo[0];
				index = 1;
			}

			// -----------------------------------------------------------------
			// M�todos
			// -----------------------------------------------------------------

			@Override
			public boolean hasNext() 
			{
				return proximo != null;
			}

			@Override
			public K next() 
			{
				K respuesta = null;
				if(proximo != null)
				{
					respuesta = proximo.getKey();
					if(proximo.getNext()!=null)
					{
						proximo = proximo.getNext();
					}
					else if(arreglo.length!=index)
					{
						proximo = (NodoTablaHash)arreglo[index];
						index++;
					}
					else
					{
						proximo=null;
					}
				}
				return respuesta;
			}
		}

		private class IteratorTablaHashSCValues implements Iterator<V>
		{
			// -----------------------------------------------------------------
			// Atributos
			// -----------------------------------------------------------------			
			private NodoTablaHash proximo;

			private int index;

			// -----------------------------------------------------------------
			// Constructor
			// -----------------------------------------------------------------
			public IteratorTablaHashSCValues() 
			{
				proximo = (NodoTablaHash)arreglo[0];
				index = 1;
			}

			// -----------------------------------------------------------------
			// M�todos
			// -----------------------------------------------------------------

			@Override
			public boolean hasNext() 
			{
				return proximo != null;
			}

			@Override
			public V next() 
			{
				V respuesta = null;
				if(proximo != null)
				{
					respuesta = proximo.getValue();
					if(proximo.getNext()!=null)
					{
						proximo = proximo.getNext();
					}
					else if(arreglo.length!=index)
					{
						proximo = (NodoTablaHash)arreglo[index];
						index++;
					}
					else
					{
						proximo=null;
					}
				}
				return respuesta;
			}
		}

}
