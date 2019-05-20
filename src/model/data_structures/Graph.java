package model.data_structures;

import java.io.Serializable;
import java.util.Iterator;


public class Graph<K extends Comparable<K>, V, A extends Comparable<A>> implements Serializable
{
	// -----------------------------------------------------------------
	// Atributos
	// -----------------------------------------------------------------

	private HashTableSC<K, Vertex> vertices;

	private LinkedList<Arc> arcos;

	private int cantVertices;

	private int cantEnlaces;

	// -----------------------------------------------------------------
	// Contructores
	// -----------------------------------------------------------------
	public Graph()
	{
		vertices = new HashTableSC<K, Vertex>();
		arcos = new LinkedList<Arc>();
		cantVertices = 0;
		cantEnlaces = 0;
	}
	// -----------------------------------------------------------------
	// Mï¿½todos
	// -----------------------------------------------------------------

	//	public Vertice buscarVerticeMasCercano(double pLatitud, double pLongitud) {
	//		
	//	}

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
		Vertex nuevoVertice = new Vertex(idVertex, infoVertex);
		vertices.put(idVertex, nuevoVertice);
		//System.out.println(getVertice(idVertex)==null);System.out.print(" leyo");
		cantVertices++;
	}

	public void addVertexWithAdj(K idVertex, V infoVertex, ArregloDinamico<K> adj) 
	{
		Vertex nuevoVertice = new Vertex(idVertex, infoVertex, adj);
		vertices.put(idVertex, nuevoVertice);
		cantVertices++;
	
		//crear nodos a medida que se leen y luego si se vuelven a leer se les agrega atributos
		for(int i=0; i<adj.darTamano();i++)
		{
			K idAdj=adj.darElemento(i);
			//Se busca si el vertice ya fue creado anteriormente
			Vertex nuevoAdj=getVertice(idAdj);
			//Si no se crea uno con información vacía
			if(nuevoAdj==null)
			{
			 nuevoAdj = new Vertex(idAdj,null);
			}
			vertices.put(idAdj, nuevoAdj);
			
			//Se agrega a la lista de nodos adyacentes
			//nuevoVertice.addAdj(idAdj); //correccion ya se había agregado al inicializar el nodo
			
			//Se crea el arco
			Arc arc=new Arc(null, nuevoVertice, nuevoAdj);//Como identificar el arco con el grafo creado por ellos??
			//Se agrega a la lista de arcos del nodo
			nuevoVertice.getArcos().add(arc);
			//Se agrega a la lista de arcos del grafo
			arcos.add(arc);
			cantEnlaces++;
		}
	}

	public void addVertexSecondForm(K idVertex, V infoVertex, LinkedList<A> adj) 
	{
		Vertex nuevoVertice = new Vertex(idVertex, infoVertex, adj);
		vertices.put(idVertex, nuevoVertice);
		cantVertices++;
		NodeList<Arc> actAdj=  adj.getFirstNode();

		while(actAdj!=null && actAdj.getelem() != null)
		{
			arcos.add(actAdj.getelem());
			actAdj=actAdj.getNext();
			cantEnlaces++;
		}

	}


	public void addEdge(K idVertexInit, K idVertexFin, A infoArc) 
	{
		Vertex verticeInicio = getVertice(idVertexInit);
		Vertex verticeFin = getVertice(idVertexFin);
		Arc nuevoArco = new Arc(infoArc, verticeInicio, verticeFin);
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
		Vertex verticeInicio = getVertice(idVertexInit);
		//System.out.println(verticeInicio == null);
		Vertex verticeFin = getVertice(idVertexFin);
		if(verticeFin==null)
		{
			verticeFin=new Vertex(idVertexFin, null);
		}
		Arc nuevoArco = new Arc(infoArc, verticeInicio, verticeFin);
		verticeInicio.getArcos().add(nuevoArco);
		arcos.add(nuevoArco);
		
		cantEnlaces++;
	}

	public V getInfoVertex(K idVertex) 
	{
		V rta = null;
		Vertex temp = vertices.get(idVertex);
		if(temp!=null)
		{
			rta = temp.getInfo();
		}
		return rta;
	}

	public void setInfoVertex(K idVertex, V infoVertex) 
	{
		Vertex buscado = getVertice(idVertex);
		buscado.setInfoVertex(idVertex, infoVertex);
	}


	public A getInfoArc(K idVertexIni, K idVertexFin) 
	{
		A rta = null;
		Arc arcoBuscado = arcos.getObject(new Arc(null, new Vertex(idVertexIni, null), new Vertex(idVertexFin, null)));
		if(arcoBuscado!=null)
		{
			rta = arcoBuscado.getInfoArco();
		}
		return rta;
	}


	public void setInfoArc(K idVertexIni, K idVertexFin, A infoArc) 
	{
		Arc buscado = getArco(idVertexIni, idVertexFin);
		buscado.setInfoArc(infoArc);
	}

	public Iterable<K> adj(K idVertex) 
	{
		Vertex verticeInteres = getVertice(idVertex);
		LinkedList<Arc> arcosVertice = verticeInteres.getArcos();
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

	public Iterator<Vertex> iteratorVertices()
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
		Iterator<Vertex> iterator = iteratorVertices();
		while(iterator.hasNext())
		{
			iterator.next().desmarcar();
		}
	}

	public Vertex getVertice(K idVertex)
	{
		return vertices.get(idVertex);
	}

	public HashTableSC<K, Vertex> getVertices()
	{
		return vertices;
	}
	

	private Arc getArco(K idVertexIni, K idVertexFin)
	{
		Arc arcoBuscado = new Arc(null, new Vertex(idVertexIni, null), new Vertex(idVertexFin, null));
		return arcos.getObject(arcoBuscado);
	}

	// -----------------------------------------------------------------
	// Clases
	// -----------------------------------------------------------------
	public class Vertex implements Serializable
	{
		private K key;

		private V info;

		private LinkedList<Arc> arcos;

		private LinkedList<A> aList;

		private ArregloDinamico<K> adjNodes;

		private boolean marcado;

		public Vertex(K pKey, V pInfo)
		{
			key = pKey;
			info = pInfo;
			arcos = new LinkedList<Arc>();
			marcado = false;
		}

		public Vertex(K pKey, V pInfo, LinkedList<A> pAdj )
		{
			key = pKey;
			info = pInfo;
			aList = pAdj;
			arcos = new LinkedList<Arc>();
			marcado = false;
		}

		public Vertex(K pKey, V pInfo, ArregloDinamico<K> pAdjNodes )
		{
			key = pKey;
			info = pInfo;
			adjNodes = pAdjNodes;
			arcos = new LinkedList<Arc>();
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

		public LinkedList<Arc> getArcos()
		{
			return arcos;
		}

		public Arc getArco(A a)
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
					Arc actArco=(Arc)actual.getelem();
					if(actArco.getInfoArco().equals(a))
					{
						Arc resp=actArco;
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

	public class Arc implements Comparable<Arc>, Serializable
	{
		private A infoArco;

		private Vertex verticeInit;

		private Vertex verticeFin;

		public Arc(A pInfoArco, Vertex pVerticeInit, Vertex pVerticeFin)
		{
			infoArco = pInfoArco;
			verticeInit = pVerticeInit;
			verticeFin = pVerticeFin;
		}

		public A getInfoArco()
		{
			return infoArco;
		}

		public Vertex getVerticeInit()
		{
			return verticeInit;
		}

		public Vertex getVerticeFin()
		{
			return verticeFin;
		}

		public void setInfoArc(A pInfoArc)
		{
			infoArco = pInfoArc;
		}

		@Override
		public int compareTo(Arc o) 
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

		public IterableKeysAdjuntas(LinkedList<Arc> pArcos) 
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

		private Iterator<Arc> iteratorArcos;

		public IteratorKeysAdjuntas(LinkedList<Arc> pArcos) 
		{
			iteratorArcos = pArcos.iterator();
			Arc primerArco = iteratorArcos.next();
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
			Arc actual = iteratorArcos.next();
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

		private Iterator<Arc> iterator;

		public IteratorArcos() 
		{
			iterator = arcos.iterator();

			Arc primerArco = iterator.next();
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
			Arc actual = iterator.next();
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
