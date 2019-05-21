package model.data_structures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


import model.vo.VOCC;
import model.data_structures.Graph.Arc;
import model.data_structures.Graph.Vertex;
import model.vo.VOWay;

public class Grafo <K extends Comparable<K>, V, A extends Comparable<A>> implements Serializable
{
	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------

	private TablaHash<K, Vertice> vertices;

	private LinkedList<Arco> arcos;

	private int cantVertices;

	private int cantEnlaces;

	/** Constante que representa el infinito para los nodos que no han sido marcados con BFS*/
	private static final int INFINITY = Integer.MAX_VALUE;

	/** 
	 * Arreglo que representa la existencia de un camino desde el nodo inicial hasta el nodo que est� en esa posici�n
	 * Ej. Si marked[v] es verdadero significa que ya existe un camino entre s(inicial) y v */
	private boolean[] marked;

	/**
	 * Arreglo en el que se encuentran los nodos anteriores en el camino m�s corto de s a v.
	 * Ej. edgeTo[v] = previous edge on shortest s-v path
	 */
	private K[] edgeTo;

	/**
	 * Arreglo en el que se encuentra la distancia m�s corta que hay desde el inicial hasta ese punto
	 * Ej. distTo[v] = number of edges shortest s-v path
	 */
	private int[] distTo;

	private K[] edges;
	// -----------------------------------------------------------------
	// Contructores
	// -----------------------------------------------------------------
	public Grafo()
	{
		vertices = new TablaHash<K, Vertice>();
		arcos = new LinkedList<Arco>();

		cantVertices = 0;
		cantEnlaces = 0;
	}

	@SuppressWarnings("unchecked")
	public K[] edges()
	{
		int n= arcos.getSize();
		edges=(K[])new Object[n];
		NodeList actual= arcos.getFirstNode();
		int i=0;
		while(actual!=null)
		{
			edges[i]=(K) actual.getelem();
			actual=actual.getNext();
			i++;
		}
		return edges;
	}
	public LinkedList<Arco> getArcos()
	{
		return arcos;
	}
	// -----------------------------------------------------------------
	// M�todos
	// -----------------------------------------------------------------

	// M�todos encargados de hacer el BFS -------------------------------------------------------------------------------------------------------------

	/**
	 * M�todo que se encarga de ejecutar el algoritmo de BFS desde un vertice inicial
	 * @param verticeInicial - El v�rtice desde el cual se quiere partir
	 * @param verticeDestino - El v�rtice al cual se quiere llegar
	 * @return Una Cola con los nodos que tiene el camino m�s corto entre los dos v�rtices
	 */
	@SuppressWarnings("unchecked")
	public Queue<K> breadthFirstSearch(K verticeInicial, K verticeDestino) {
		System.out.println("El tamaño del arreglo es: " + cantVertices);
		marked = new boolean[cantVertices];
		edgeTo =  (K[]) new Comparable[cantVertices];
		distTo = new int[cantVertices];
		bFS(verticeInicial);
		return pathTo(verticeDestino);
	}

	/**
	 * Hace el algoritmo de BFS para un v�rtice inicial que llega por par�metro
	 * @param verticeInicial
	 */
	private void bFS(K verticeInicial) {
		//Inicializa el queue necesario y asigna infinito a todos los vertices por visitar
		Queue<K> cola = new Queue<K>();
		for(int v = 0; v < cantVertices; v++) {
			distTo[v] = INFINITY;
		}
		// Establece el v�rtice inicial poni�ndole 0 como la distancia y lo marca como vistado
		distTo[vertices.getIndex(verticeInicial)-2] = 0;
		marked[vertices.getIndex(verticeInicial)-2] = true;
		cola.enqueue(verticeInicial);
		// Recorre todos los nodos hasta que la cola est� vac�a (esto significa que el algoritmo termin�)
		while(!cola.isEmpty()) {
			K vertice = cola.dequeue();
			//Recorre todos los v�rtices adyacentes del v�rtice actual
			for(K elemento : adyacentes(vertice)) {
				int indice = vertices.getIndex(elemento);
				if(!marked[indice-2]) {
					edgeTo[indice-2] = vertice;
					distTo[indice-2] = distTo[vertices.getIndex(vertice)-2] + 1;
					marked[indice-2] = true;
					cola.enqueue(elemento);
				}
			}
		}
	}

	public ArrayList<K> adyacentes(K pVertice) {
		ArrayList<K> lista = new ArrayList<K>();
		for(int i = 0; i < vertices.get(pVertice).getAdjNodes().darTamano(); i ++)
			lista.add(vertices.get(pVertice).getAdjNodes().darElemento(i));
		return lista;
	}

	/**
	 * Retorna si existe un camino hasta nodo indicado por par�metro
	 * @param pVertice
	 * @return verdadero si hay camino y falso de lo contrario
	 */
	public boolean hasPathTo(K pVertice) {
		//    	for(int i = 0; i < marked.length; i ++) {
		//    		System.out.println(i + " - " + marked[i]);
		//    	}
		return marked[vertices.getIndex(pVertice)-2];
	}

	/**
	 * Distancia desde el v�rtice inicial original al nodo dado por par�metro
	 * @param pV�rtice - 
	 * @return La cantidad de nodos visitados en el camino m�s corto desde el inicial. 
	 */
	public int distTo(K pVertice) {
		return distTo[vertices.getIndex(pVertice)-2];
	}

	/**
	 * Retorna el camino desde el v�rtice inicial hacia el v�rtice dado por par�metro.
	 * @param pVertice
	 * @return Un iterable con todos los nodos que est�n en el camino 
	 */
	public Queue<K> pathTo(K pVertice) {
		System.out.println(hasPathTo(pVertice));
		if (!hasPathTo(pVertice)) 
			return null;
		Queue<K> camino = new Queue<K>();
		K x;
		for (x = pVertice; distTo[vertices.getIndex(x)] != 0; x = edgeTo[vertices.getIndex(x)])
			camino.enqueue(x);
		camino.enqueue(x);
		return camino;
	}

	// ------------------------------------------------------------------------------------------------------------------------------------



	public int V() 
	{
		return cantVertices;
	}

	public int E() 
	{
		return cantEnlaces;
	}

	public void addVertex(K idVertex, V infoVertex) 
	{
		Vertice nuevoVertice = new Vertice(idVertex, infoVertex);
		vertices.put(idVertex, nuevoVertice);
		//System.out.println(getVertice(idVertex)==null);System.out.print(" leyo");
		cantVertices++;
	}

	public void addVertexWithAdj(K idVertex, V infoVertex, ArregloDinamico<K> adj) 
	{
		Vertice nuevoVertice = new Vertice(idVertex, infoVertex, adj);
		vertices.put(idVertex, nuevoVertice);
		cantVertices++;

		//crear nodos a medida que se leen y luego si se vuelven a leer se les agrega atributos
		for(int i=0; i<adj.darTamano();i++)
		{
			K idAdj=adj.darElemento(i);
			//Se busca si el vertice ya fue creado anteriormente
			Vertice nuevoAdj=getVertice(idAdj);
			//Si no se crea uno con informaci�n vac�a
			if(nuevoAdj==null)
			{
				nuevoAdj = new Vertice(idAdj,null);
			}
			vertices.put(idAdj, nuevoAdj);

			//Se agrega a la lista de nodos adyacentes
			//nuevoVertice.addAdj(idAdj); //correccion ya se hab�a agregado al inicializar el nodo

			//Se crea el arco
			Arco arc=new Arco(null, nuevoVertice, nuevoAdj);//Como identificar el arco con el grafo creado por ellos??
			//Se agrega a la lista de arcos del nodo
			nuevoVertice.getArcos().add(arc);
			//Se agrega a la lista de arcos del grafo
			arcos.add(arc);
			cantEnlaces++;
		}
	}

	public void addVertexSecondForm(K idVertex, V infoVertex, LinkedList<A> adj) 
	{
		Vertice nuevoVertice = new Vertice(idVertex, infoVertex, adj);
		vertices.put(idVertex, nuevoVertice);
		cantVertices++;
		NodeList<Arco> actAdj=  adj.getFirstNode();

		while(actAdj!=null && actAdj.getelem() != null)
		{
			arcos.add(actAdj.getelem());
			actAdj=actAdj.getNext();
			cantEnlaces++;
		}

	}


	public void addEdge(K idVertexInit, K idVertexFin, A infoArc) 
	{
		Vertice verticeInicio = getVertice(idVertexInit);
		Vertice verticeFin = getVertice(idVertexFin);
		Arco nuevoArco = new Arco(infoArc, verticeInicio, verticeFin);
		if(verticeInicio.getArco(infoArc)==null)
		{
			verticeInicio.getArcos().add(nuevoArco);
			//verticeFin.getArcos().add(new Arco(infoArc, verticeFin, verticeInicio));
			//arcos.addNotRepeated(nuevoArco);
			arcos.add(nuevoArco);
			cantEnlaces++;
		}
	}

	public void addEdgeSecondForm(K idVertexInit, K idVertexFin, A infoArc) 
	{
		Vertice verticeInicio = getVertice(idVertexInit);
		//System.out.println(verticeInicio == null);
		Vertice verticeFin = getVertice(idVertexFin);
		if(verticeFin==null)
		{
			verticeFin=new Vertice(idVertexFin, null);
		}
		Arco nuevoArco = new Arco(infoArc, verticeInicio, verticeFin);
		verticeInicio.getArcos().add(nuevoArco);
		arcos.add(nuevoArco);

		cantEnlaces++;
	}

	public V getInfoVertex(K idVertex) 
	{
		V rta = null;
		Vertice temp = vertices.get(idVertex);
		if(temp!=null)
		{
			rta = temp.getInfo();
		}
		return rta;
	}

	public void setInfoVertex(K idVertex, V infoVertex) 
	{
		Vertice buscado = getVertice(idVertex);
		buscado.setInfoVertex(idVertex, infoVertex);
	}


	public A getInfoArc(K idVertexIni, K idVertexFin) 
	{
		A rta = null;
		Arco arcoBuscado = arcos.getObject(new Arco(null, new Vertice(idVertexIni, null), new Vertice(idVertexFin, null)));
		if(arcoBuscado!=null)
		{
			rta = arcoBuscado.getInfoArco();
		}
		return rta;
	}


	public void setInfoArc(K idVertexIni, K idVertexFin, A infoArc) 
	{
		Arco buscado = getArco(idVertexIni, idVertexFin);
		buscado.setInfoArc(infoArc);
	}

	public Iterable<K> adj(K idVertex) 
	{
		Vertice verticeInteres = getVertice(idVertex);
		LinkedList<Arco> arcosVertice = verticeInteres.getArcos();
		return new IterableKeysAdjuntas(arcosVertice);
	}

	public Iterator<K> iteratorLlaves()
	{
		return vertices.keys();
	}

	public Iterator<V> iteratorValores()
	{
		return new IteratorValores();
	}

	public Iterator<A> iteratorArcos()
	{
		return new IteratorArcos();
	}

	public Iterator<Vertice> iteratorVertices()
	{
		return vertices.values();
	}

	public void marcar(K idVertex)
	{
		getVertice(idVertex).marcar();
	}

	public boolean estaMarcado(K idVertex)
	{
		return getVertice(idVertex).estaMarcado();
	}

	public void desmarcarVertices()
	{
		Iterator<Vertice> iterator = iteratorVertices();
		while(iterator.hasNext())
		{
			iterator.next().desmarcar();
		}
	}

	public Vertice getVertice(K idVertex)
	{
		return vertices.get(idVertex);
	}

	public TablaHash<K, Vertice> getVertices()
	{
		return vertices;
	}
	private Vertice getVerticeSecondForm(K idVertex)
	{
		return vertices.getSecondForm(idVertex);
	}

	private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

	public static double distance(double startLat, double startLong, double endLat, double endLong)
	{

		double dLat  = Math.toRadians((endLat - startLat));
		double dLong = Math.toRadians((endLong - startLong));

		startLat = Math.toRadians(startLat);
		endLat   = Math.toRadians(endLat);

		double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c; // <-- d
	}

	public static double haversin(double val) {
		return Math.pow(Math.sin(val / 2), 2);
	}


	private Arco getArco(K idVertexIni, K idVertexFin)
	{
		Arco arcoBuscado = new Arco(null, new Vertice(idVertexIni, null), new Vertice(idVertexFin, null));
		return arcos.getObject(arcoBuscado);
	}


	private int count;
	private int[] id;
	private ArregloDinamico<VOCC> ccs;


	public ArregloDinamico<VOCC> cc()
	{
		id = new int[V()];
		Vertice actual=null;
		ccs= new ArregloDinamico<VOCC>(5);
		Queue cola;
		for(int i=0; i<V();i++)
		{
			//Recorrer arreglo vertices
			if(getVertices().get(i)!=null)
			{
				actual=getVertices().get(i).getValue();
			}
			if(actual!=null && !actual.marcado)
			{
				cola= new Queue();
				cola = dfs(actual, cola);
				ccs.agregar(new VOCC(cola));
				count ++;
			}
		}
		return ccs;
	}

	public int count()
	{
		return count;
	}

	public Queue<Vertice> dfs(Vertice v, Queue<Vertice> cola)
	{
		v.marcar();

		id[getVertices().getIndex(v.getKey())]=count;

		cola.enqueue(v);


		for(int i=0; i<v.adjNodes.darTamano();i++)
		{
			Vertice adjActual=(Grafo<K, V, A>.Vertice) v.adjNodes.darElemento(i);
			if(!adjActual.marcado)
			{
				dfs(adjActual, cola);
			}
		}
		return cola;
	}

	public int[] id()
	{
		return id;
	}


	


	// -----------------------------------------------------------------
	// Clases
	// -----------------------------------------------------------------
	public class Vertice implements Serializable
	{

		private K key;

		private V info;

		private Vertice next;

		private LinkedList<Arco> arcos;

		private LinkedList<A> aList;

		private ArregloDinamico<K> adjNodes;

		private boolean marcado;

		private K cameFrom;

		public Vertice(K pKey, V pInfo)
		{
			key = pKey;
			info = pInfo;
			arcos = new LinkedList<Arco>();
			marcado = false;
		}

		public Vertice(K pKey, V pInfo, LinkedList<A> pAdj )
		{
			key = pKey;
			info = pInfo;
			aList = pAdj;
			arcos = new LinkedList<Arco>();
			marcado = false;
		}

		public Vertice(K pKey, V pInfo, ArregloDinamico<K> pAdjNodes )
		{
			key = pKey;
			info = pInfo;
			adjNodes = pAdjNodes;
			arcos = new LinkedList<Arco>();
			marcado = false;
		}

		public void addAdj(K idVertexAdj) 
		{
			adjNodes.agregar(idVertexAdj);
		}

		public K getKey()
		{
			return key;
		}

		public V getInfo()
		{
			return info;
		}

		public LinkedList<Arco> getArcos()
		{
			return arcos;
		}

		public ArregloDinamico<K> getAdjNodes()
		{
			return adjNodes;
		}

		public K cameFrom()
		{
			return cameFrom;
		}

		public void setCameFrom(K came)
		{
			cameFrom= came;
		}
		public void setNext(Vertice next)
		{
			this.next=next;
		}

		public Vertice getNext()
		{
			return next;
		}
		public Arco getArco(A a)
		{

			if(arcos==null||arcos.getSize()==0)
			{
				return null;
			}
			else
			{
				NodeList actual= arcos.getFirstNode();
				while(actual!=null)
				{
					Arco actArco=(Arco)actual.getelem();
					if(actArco.getInfoArco().equals(a))
					{
						Arco resp=actArco;
						return resp;
					}
					actual=actual.getNext();
				}
			}
			return null;
		}
		public void setInfoVertex(K pKey, V pInfo)
		{
			key = pKey;
			info = pInfo;
		}

		public boolean estaMarcado()
		{
			return marcado;
		}

		public void marcar()
		{
			marcado = true;
		}

		public void desmarcar()
		{
			marcado = false;
		}
	}

	public class Arco implements Comparable<Arco>, Serializable
	{
		private A infoArco;

		private double distancia;

		private Vertice verticeInit;

		private Vertice verticeFin;

		public void setDistancia(double d)
		{
			distancia=d;
		}
		public double dist()
		{
			return distancia;
		}
		public Arco(A pInfoArco, Vertice pVerticeInit, Vertice pVerticeFin)
		{
			infoArco = pInfoArco;
			verticeInit = pVerticeInit;
			verticeFin = pVerticeFin;
		}

		public A getInfoArco()
		{
			return infoArco;
		}

		public Vertice getVerticeInit()
		{
			return verticeInit;
		}

		public Vertice getVerticeFin()
		{
			return verticeFin;
		}

		public void setInfoArc(A pInfoArc)
		{
			infoArco = pInfoArc;
		}

		@Override
		public int compareTo(Arco o) 
		{
			int respuesta = -1;
			if(verticeInit.getKey().compareTo(o.getVerticeInit().getKey())==0
					&& verticeFin.getKey().compareTo(o.getVerticeFin().getKey())==0)
			{
				respuesta = 0;
			}
			return respuesta;
		}
	}

	private class IterableKeysAdjuntas implements Iterable<K>
	{
		private Iterator<K> iterator;

		public IterableKeysAdjuntas(LinkedList<Arco> pArcos) 
		{
			iterator = new IteratorKeysAdjuntas(pArcos);
		}

		@Override
		public Iterator<K> iterator() 
		{
			return iterator;
		}
	}

	private class IteratorKeysAdjuntas implements Iterator<K>
	{		
		private K proximo;

		private Iterator<Arco> iteratorArcos;

		public IteratorKeysAdjuntas(LinkedList<Arco> pArcos) 
		{
			iteratorArcos = pArcos.iterator();
			Arco primerArco = iteratorArcos.next();
			if(primerArco != null)
			{
				proximo = primerArco.getVerticeFin().getKey();
			}
			else
			{
				proximo = null;
			}
		}

		@Override
		public boolean hasNext() 
		{
			return proximo!=null;
		}

		@Override
		public K next() 
		{
			K siguiente = proximo;
			Arco actual = iteratorArcos.next();
			if(actual != null)
			{
				proximo = actual.getVerticeFin().getKey();
			}
			else
			{
				proximo = null;
			}
			return siguiente;
		}
	}

	private class IteratorArcos implements Iterator<A>
	{
		private A proximo;

		private Iterator<Arco> iterator;

		public IteratorArcos() 
		{
			iterator = arcos.iterator();

			Arco primerArco = iterator.next();
			if(primerArco != null)
			{
				proximo = primerArco.getInfoArco();
			}
			else
			{
				proximo = null;
			}
		}		

		@Override
		public boolean hasNext() 
		{
			return proximo!=null;
		}

		@Override
		public A next() 
		{
			A siguiente = proximo;
			Arco actual = iterator.next();
			if(actual != null)
			{
				proximo = actual.getInfoArco();
			}
			else
			{
				proximo = null;
			}
			return siguiente;
		}
	}

	private class IteratorValores implements Iterator<V>
	{
		private V proximo;

		private Iterator<K> iteratorKeys;


		public IteratorValores() 
		{
			iteratorKeys = iteratorLlaves();
			proximo = getInfoVertex(iteratorKeys.next());
		}

		@Override
		public boolean hasNext() 
		{
			return proximo!=null;
		}

		@Override
		public V next() 
		{
			V siguiente = proximo;
			K sigLlave = iteratorKeys.next();
			if(sigLlave!=null)
			{
				proximo = getInfoVertex(sigLlave);
			}
			else
			{
				proximo = null;
			}
			return siguiente;
		}
	}
}

