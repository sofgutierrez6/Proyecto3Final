package model.data_structures;

/**
 * Estructura de Datos Arreglo Dinamico 
 * Comentario de prueba
 */
public class ArregloDinamico<T extends Comparable<T>> implements IArregloDinamico<T> 
{
	/**
	 * Capacidad maxima del arreglo
	 */
	private int tamanoMax;
	/**
	 * Numero de elementos en el arreglo (de forma compacta desde la posicion 0)
	 */
	private int tamanoAct;
	/**
	 * Arreglo de elementos de tamaNo maximo
	 */
	private T elementos[ ];

	/**
	 * Construir un arreglo con la capacidad maxima inicial.
	 * @param max Capacidad maxima inicial
	 */
	public ArregloDinamico( int max )
	{
		elementos = (T [])new Comparable[max];
		tamanoMax = max;
		tamanoAct = 0;
	}

	public int darTamano() 
	{
		return tamanoAct;
	}

	public T darElemento(int i)
	{
		return elementos[i];
	}


	@Override
	public void agregar(T dato) {
		if ( tamanoAct == tamanoMax )
		{  // caso de arreglo lleno (aumentar tamaNo)
			tamanoMax = 2 * tamanoMax;
			T [ ] copia = elementos;
			elementos = (T [])new Comparable[tamanoMax];
			for ( int i = 0; i < tamanoAct; i++)
			{
				elementos[i] = copia[i];
			} 
			System.out.println("Arreglo lleno: " + tamanoAct + " - Arreglo duplicado: " + tamanoMax);
		}	
		elementos[tamanoAct] = dato;
		tamanoAct++;

	}
	public void set(int i, T dato)
	{
		if(i<tamanoAct||i==tamanoAct)
		{
			elementos[i]=dato;
		}
		else
		{
			System.out.println(i);
		}
		
	}
	@Override
	public T buscar(T dato) 
	{
		T t= dato;
		T resp=null;
		for(int i =0; i<elementos.length;i++)
		{
			if(elementos[i].compareTo(t)==0)
			{
				resp=elementos[i];
			}
		}
		return resp;
	}

	@Override
	public T eliminar(T dato) {
		 
		T[] copia=elementos;
		if(buscar(dato)!=null)
		{
			elementos = (T [])new Comparable[tamanoAct-1];
			for(int i=0; i<elementos.length; i++)
			{
				if(copia[i].compareTo(dato)==0)
				{//No hace nada
				}
				else
				{
					elementos[i]=copia[i];
				}

			}
		}
		return dato;
	}
	/**
	 * Intercambiar los datos de las posicion i y j
	 * @param i posicion del 1er elemento a intercambiar
	 * @param j posicion del 2o elemento a intercambiar
	 */
	public void exchange( int i, int j)
	{
		T copia=elementos[i];
		elementos[i]=elementos[j];
		elementos[j]=copia;
	}
}
