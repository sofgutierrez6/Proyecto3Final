package controller;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import Mapa.Mapa;
import model.data_structures.ArregloDinamico;
import model.data_structures.BST;
import model.data_structures.ComparadorXAccidentes;
import model.data_structures.Grafo;
import model.data_structures.Grafo.Arco;
import model.data_structures.Grafo.Vertice;
import model.data_structures.Graph;
import model.data_structures.Graph.Arc;
import model.data_structures.Graph.Vertex;
import model.data_structures.Heap;
import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.LinkedList;
import model.data_structures.MaxColaPrioridad;
import model.data_structures.MinPQ;
import model.data_structures.NodeList;
import model.data_structures.Queue;
import model.data_structures.RedBlackBST;
import model.data_structures.SequentialSearchSt;
import model.data_structures.TablaHash;
import model.data_structures.TablaHash.NodoTablaHash;
import model.vo.Counter;
import model.vo.LocationVO;
import model.vo.VOCC;
import model.vo.VODaylyStatistic;
import model.vo.VOGeographicLocation;
import model.vo.VOIntersections;
import model.vo.VOMovingViolations;
import model.vo.VOViolationCode;
import model.vo.VOWay;
import view.MovingViolationsManagerView;

public class Controller {

	// Atributos ------------------------------------------------------------------

	/** Vista del controlador */
	private MovingViolationsManagerView view;

	/** Grafo from XLM donde se almacena toda la red vial de Washington con arcos de tipo highway */
	private static Grafo grafo;

	/** Grafo from JSON donde se almacena la red vial del downtown de Washington con arcos de tipo highway */
	private static Grafo grafoJson;

	/** Grafo from JSON donde se almacena toda la red vial de Washington con arcos de tipo highway */
	private static Grafo<Long,VOIntersections,Long> grafoJson2;

	private static Graph<Long,VOIntersections,Long> grafoPrueba;

	private LinkedList<Long> idsNodos;

	private Mapa mapa;

	ComparadorXAccidentes comparador;
	// Constructor -------------------------------------------------------------------

	/**
	 * Construye el controlador 
	 */
	public Controller() 
	{
		view = new MovingViolationsManagerView();

		//Grafo generado leyendo XML
		//grafo= new Grafo<Long,VOIntersections,VOWay>();

		//Para el centro de la ciudad cargando el grafo generado 
		grafoJson = new Grafo<Long,VOIntersections,VOWay>();

		//Para la ciudad completa cargando el grafo dado
		grafoJson2 = new Grafo<Long,VOIntersections,Long>();

		//
		grafoPrueba = new Graph<Long,VOIntersections,Long>();

		idsNodos= new LinkedList<Long>();

		//mapa = new Mapa("Información Washington D.C");
		comparador= new ComparadorXAccidentes();
	}

	// Métodos -----------------------------------------------------------------------------

	/**
	 * Corre el programa con los argumentos que le entraron por parámetro al main
	 * @param args
	 */
	public void run(String args[]) {

		long startTime;
		long endTime;
		long duration;

		Scanner sc = new Scanner(System.in);
		boolean fin=false;
		//Counter contador = new Counter();
		// Mantiene el programa corriendo hasta que el usuario decida temrinarlo. 
		Controller controller = new Controller();

		while(!fin) {

			int idVertice1 = 0;
			int idVertice2 = 0;

			String id1 = "0";
			String id2 = "0";

			view.printMenu();

			int option = sc.nextInt();



			//Recorre las posibles opciones que ingresa el usuario al ejecutar el programa.
			switch(option){
			case 0:

				String RutaArchivo = "";
				view.printMessage("Escoger el grafo a cargar: (1) Downtown  o (2)Ciudad Completa.");

				int ruta = sc.nextInt();

				if(ruta == 1)
				{
					RutaArchivo = "./data//WashingtonGraph.json"; //la ruta del archivo de Downtown
					startTime = System.currentTimeMillis();
					controller.loadGraphFromJson(RutaArchivo);
					endTime = System.currentTimeMillis();
					System.out.println("Información del grafo:");
					System.out.println("Número de nodos: " + grafoJson.V() + ", Número de arcos: " + grafoJson.E());
					mapa = new Mapa("Mapa del Downtown de Washington");
				}

				else if(ruta == 2)
				{
					RutaArchivo = "./data/jsonPrueba.json"; //ruta del archivo de la ciudad completa
					startTime = System.currentTimeMillis();
					controller.loadJSON(RutaArchivo);
					endTime = System.currentTimeMillis();
					//System.out.println(grafoJson2.getVertices().arreglo().length);
					System.out.println("Información del grafo:");
					System.out.println("Número de nodos: " + grafoJson2.V() + ", Número de arcos: " + grafoJson2.E());
					//mapa = new Mapa("Mapa de Washington D.C");
					//System.out.println("Número de nodos: " + grafoPrueba.V() + ", Número de arcos: " + grafoPrueba.E());
					//System.out.println("Tama�o hash: "+grafoPrueba.getVertices().getList().size());

					//Iterator iterador = grafo.getVertices().keys();

				}

				else
				{
					startTime = System.currentTimeMillis();
					endTime = System.currentTimeMillis();
					System.out.println("error");
				}


				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				// TODO Informar el total de vértices y el total de arcos que definen el grafo cargado

				break;

			case 1:
				//System.out.println("Cola: "+grafoPrueba.getVertices().keysQueue().size());
				view.printMessage("Ingrese El id del primer vertice (Ej. 611328751): ");
				id1 = sc.next();
				view.printMessage("Ingrese El id del segundo vertice (Ej. 611286570): ");
				id2 = sc.next();

				//System.out.println("Antes: "+grafoJson2.V());
				startTime = System.currentTimeMillis();
				controller.caminoCostoMinimoA1(id1, id2);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/* 
				TODO Consola: Mostrar el camino a seguir con sus vértices (Id, Ubicación Geográfica),
				el costo mínimo (menor cantidad de infracciones), y la distancia estimada (en Km).

				TODO Google Maps: Mostrar el camino resultante en Google Maps 
				(incluyendo la ubicación de inicio y la ubicación de destino).
				 */
				break;

			case 2:
				view.printMessage("2A. Consultar los N v�rtices con mayor n�mero de infracciones. Ingrese el valor de N: ");
				int n = sc.nextInt();


				startTime = System.currentTimeMillis();
				mayorNumeroVerticesA2(n);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/* 
				TODO Consola: Mostrar la informacion de los n vertices 
				(su identificador, su ubicación (latitud, longitud), y el total de infracciones) 
				Mostra el número de componentes conectadas (subgrafos) y los  identificadores de sus vertices 

				TODO Google Maps: Marcar la localización de los vértices resultantes en un mapa en
				Google Maps usando un color 1. Destacar la componente conectada más grande (con
				más vértices) usando un color 2. 
				 */
				break;

			case 3:			

				view.printMessage("Ingrese El id del primer vertice (Ej. 901839): ");
				idVertice1 = sc.nextInt();
				view.printMessage("Ingrese El id del segundo vertice (Ej. 901839): ");
				idVertice2 = sc.nextInt();


				startTime = System.currentTimeMillis();
				caminoLongitudMinimoaB1(idVertice1, idVertice2);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");

				/*
				   TODO Consola: Mostrar  el camino a seguir, informando
					el total de vértices, sus vértices (Id, Ubicación Geográfica) y la distancia estimada (en Km).

				   TODO Google Maps: Mostre el camino resultante en Google Maps (incluyendo la
					ubicación de inicio y la ubicación de destino).
				 */
				break;

			case 4:		
				double lonMin;
				double lonMax;
				view.printMessage("Ingrese la longitud minima (Ej. -87,806): ");
				lonMin = sc.nextDouble();
				view.printMessage("Ingrese la longitud m�xima (Ej. -87,806): ");
				lonMax = sc.nextDouble();

				view.printMessage("Ingrese la latitud minima (Ej. 44,806): ");
				double latMin = sc.nextDouble();
				view.printMessage("Ingrese la latitud m�xima (Ej. 44,806): ");
				double latMax = sc.nextDouble();

				view.printMessage("Ingrese el n�mero de columnas");
				int columnas = sc.nextInt();
				view.printMessage("Ingrese el n�mero de filas");
				int filas = sc.nextInt();


				startTime = System.currentTimeMillis();
				definirCuadriculaB2(lonMin,lonMax,latMin,latMax,columnas,filas);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar el número de vértices en el grafo
					resultado de la aproximación. Mostar el identificador y la ubicación geográfica de cada
					uno de estos vértices. 

				   TODO Google Maps: Marcar las ubicaciones de los vértices resultantes de la
					aproximación de la cuadrícula en Google Maps.
				 */
				break;

			case 5:

				startTime = System.currentTimeMillis();
				arbolMSTKruskalC1();
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar los vértices (identificadores), los arcos incluidos (Id vértice inicial e Id vértice
					final), y el costo total (distancia en Km) del árbol.

				   TODO Google Maps: Mostrar el árbol generado resultante en Google Maps: sus vértices y sus arcos.
				 */

				break;

			case 6:

				startTime = System.currentTimeMillis();
				arbolMSTPrimC2();
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar los vértices (identificadores), los arcos incluidos (Id vértice inicial e Id vértice
				 	final), y el costo total (distancia en Km) del árbol.

				   TODO Google Maps: Mostrar el árbol generado resultante en Google Maps: sus vértices y sus arcos.
				 */
				break;

			case 7:

				startTime = System.currentTimeMillis();
				caminoCostoMinimoDijkstraC3();
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar de cada camino resultante: su secuencia de vértices (identificadores) y su costo (distancia en Km).

				   TODO Google Maps: Mostrar los caminos de costo mínimo en Google Maps: sus vértices
					y sus arcos. Destaque el camino más largo (en distancia) usando un color diferente
				 */
				break;

			case 8:
				view.printMessage("Ingrese El id del primer vertice (Ej. 901839): ");
				id1 = sc.next();
				view.printMessage("Ingrese El id del segundo vertice (Ej. 901839): ");
				id2 = sc.next();

				startTime = System.currentTimeMillis();
				caminoMasCortoC4(id1, id2);
				endTime = System.currentTimeMillis();
				duration = endTime - startTime;
				view.printMessage("Tiempo del requerimiento: " + duration + " milisegundos");
				/*
				   TODO Consola: Mostrar del camino resultante: su secuencia de vértices (identificadores), 
				   el total de infracciones y la distancia calculada (en Km).

				   TODO Google Maps: Mostrar  el camino resultante en Google Maps: sus vértices y sus arcos.	  */
				break;

			case 9: 	
				fin = true;
				sc.close();
				break;
			case 10:
				/*grafo = contador.load(args);
				System.out.println();
				System.out.println("Carga del grafo con la información del archivo .XML:");
				System.out.println("Información del grafo:");
				System.out.println("Número de nodos: " + grafo.V() + ", Número de arcos: " + grafo.E());
				try {
					System.out.println("Lectura de documento con las infracciones de los archivos. CSV:");
					System.out.println("Información de la carga:");
					cargarInfracciones();
					System.out.println();
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}*/
				//Lectura XML
				//grafo = contador.load(args);
				System.out.println();
				System.out.println("Carga del grafo con la informaci�n del archivo .XML:");
				System.out.println("Informaci�n del grafo:");
				System.out.println("N�mero de nodos: " + grafo.V() + ", N�mero de arcos: " + grafo.E());
				break;
			}
		}

	}


	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia


	public void loadGraphFromJson(String ruta) 
	{
		int numCargados=0;
		int linea=0;
		JsonParser parser = new JsonParser();
		try 
		{
			Reader reader = Files.newBufferedReader(Paths.get(ruta));
			JsonArray arreglo = (JsonArray)parser.parse(new FileReader(ruta));
			System.out.println("El numero de vertices es: "+ arreglo.size());
			String var ="";
			for(int i=0; arreglo != null && i < arreglo.size(); i++)
			{
				//if(i==0){System.out.println("Entra for");}

				JsonObject objeto = (JsonObject)arreglo.get(i);
				//	System.out.println("convierte  jsonobject");
				//------------------------------------
				//------ Lectura de atributos de la interseccion
				//------------------------------------
				long ID=0;
				JsonElement elementoID = objeto.get("ID");
				//System.out.println("convierte  id");
				if(elementoID!=null && !elementoID.isJsonNull())
				{
					ID=elementoID.getAsLong();
					//System.out.println("ID: " +ID);
				}

				double LAT=0;
				JsonElement elementoLAT = objeto.get("LAT");
				if(elementoLAT!=null && !elementoLAT.isJsonNull())
				{
					LAT=elementoLAT.getAsDouble();
					//System.out.println("LAT: "+LAT);
				}

				double LON=0;
				JsonElement elementoLON = objeto.get("LON");
				if(elementoLON!=null && !elementoLON.isJsonNull())
				{
					LON=elementoLON.getAsDouble();
					//System.out.println("LON: "+LON);
				}
				//TODO agregar infracciones
				VOIntersections nuevaInter= new VOIntersections(ID, LAT, LON, null);
				//Agregar vertice al grafo
				grafoJson.addVertex(nuevaInter.getId(), nuevaInter);

				LinkedList<VOWay>adj=new LinkedList<VOWay>();
				boolean cargoArreglo=objeto.get("ADJ").isJsonArray();
				//System.out.println(cargoArreglo);
				if(cargoArreglo)
				{
					JsonArray JAdj=(JsonArray) objeto.get("ADJ").getAsJsonArray();
					//System.out.println("convirtio arreglo: "+JAdj.size());
					//Pasar Adj a linked List
					for(int j=0; JAdj != null && j < JAdj.size(); j++)
					{
						JsonObject objetoAdj = (JsonObject)JAdj.get(j);
						//System.out.println(objetoAdj.toString() + (objetoAdj.isJsonNull())+"1");
						long IDAdj=0;

						JsonElement  elementoIDAdj = objetoAdj.get("ID_ARC");
						//System.out.println(elementoIDAdj2.isJsonNull()+"2");
						if(elementoIDAdj!=null && !elementoIDAdj.isJsonNull())
						{
							IDAdj=elementoIDAdj.getAsLong();
							//System.out.println("IDAdj: "+ IDAdj);
						}

						Long NODO1=(long) 0.0;
						JsonElement elementoNODO1 = objetoAdj.get("NODO1");
						if(elementoNODO1!=null && !elementoNODO1.isJsonNull())
						{
							NODO1=elementoNODO1.getAsLong();
							//System.out.println("NODO1: "+NODO1);
						}

						Long NODO2=(long) 0.0;
						JsonElement elementoNODO2 = objetoAdj.get("NODO2");
						if(elementoNODO2!=null && !elementoNODO2.isJsonNull())
						{
							NODO2=elementoNODO2.getAsLong();
							//System.out.println("NODO2: "+NODO2);
						}
						// se crea un nuevo VOWay
						VOWay nuevoVOWay = new VOWay(IDAdj,NODO1,NODO2);
						adj.add(nuevoVOWay);
						//verificar que nodo1 y/o nodo 2 existen dentro del grafo, despues si agregar el vertice.
						//if(!grafoJson.contieneNodo(NODO1)){ grafoJson.agregarNodo(new Nodo (NODO1))}, same para el 2;
						grafoJson.addEdgeSecondForm(NODO1, NODO2, IDAdj);
					}
				}



				//System.out.println(numCargados);
				numCargados++;
			}
		}
		catch (Exception e)
		{
			System.out.println("Alcanz� a cargar "+numCargados);
			System.out.println(e.getStackTrace().toString());
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Cargar el Grafo No Dirigido de la malla vial: Downtown o Ciudad Completa
	 * @param rutaArchivo 
	 */
	public void loadJSON(String rutaArchivo) 
	{
		int numCargados=0;
		JsonParser parser = new JsonParser();
		try 
		{
			Reader reader = Files.newBufferedReader(Paths.get(rutaArchivo));
			JsonArray arreglo = (JsonArray)parser.parse(new FileReader(rutaArchivo));
			System.out.println("El numero de vertices del json es: "+ arreglo.size());
			int numeroArcos=0;
			VOIntersections[] list= new VOIntersections[10];
			for(int i=0; arreglo != null && i < arreglo.size(); i++)
			{
				//System.out.println("Entra for");
				JsonObject objeto = (JsonObject)arreglo.get(i);
				//------------------------------------
				//------ Lectura de atributos de la interseccion
				//------------------------------------
				long ID=0;
				JsonElement elementoID = objeto.get("id");
				if(elementoID!=null && !elementoID.isJsonNull())
				{
					ID=elementoID.getAsLong();
					idsNodos.add(ID);
					//System.out.println("id"+ ID);
				}
				double LAT=0;
				JsonElement elementoLAT = objeto.get("lat");
				if(elementoLAT!=null && !elementoLAT.isJsonNull())
				{
					LAT=elementoLAT.getAsDouble();
					//System.out.println("lat"+LAT);
				}
				double LON=0;
				JsonElement elementoLON = objeto.get("lon");
				if(elementoLON!=null && !elementoLON.isJsonNull())
				{
					LON=elementoLON.getAsDouble();
					//System.out.println("lon"+LON);
				}	


				ArregloDinamico<Long> infracciones=new ArregloDinamico<Long>(4);
				boolean esArreglo=objeto.get("infractions").isJsonArray();
				//System.out.println(cargoArreglo);
				if(esArreglo)
				{
					JsonArray jInf=(JsonArray) objeto.get("infractions").getAsJsonArray();
					for(int l=0; l<jInf.size(); l++)
					{
						long IDInf =(long)jInf.get(l).getAsLong();
						infracciones.agregar(IDInf);
					}
				}

				//Se crea la instersecci�n con la informaci�n leida
				VOIntersections nuevaInter= new VOIntersections(ID, LAT, LON, infracciones);
				nuevaInter.setCantidad(infracciones.darTamano());
				if(i<10)
				{
					list[i]=nuevaInter;
				}			
				//Lista con nodos adyacentes
				ArregloDinamico<Long>adj = new ArregloDinamico<Long>(3);

				boolean cargoArreglo=objeto.get("adj").isJsonArray();
				//System.out.println(cargoArreglo);
				if(cargoArreglo)
				{
					JsonArray JAdj=(JsonArray) objeto.get("adj").getAsJsonArray();
					numeroArcos+=JAdj.size();
					//System.out.println("El tamanio del arreglo de nodos es "+ JAdj.size());
					//Pasar Adj a linked List
					for(int j=0; JAdj != null && j < JAdj.size(); j++)
					{						
						long IDAdj =(long)JAdj.get(j).getAsLong();
						//VOIntersections nuevaAdj= new VOIntersections(IDAdj, null, LON);
						//System.out.println(IDAdj);

						adj.agregar(IDAdj);
						//System.out.println("agrega");
					}
				}

				//Agregar vertice al grafo
				grafoJson2.addVertexWithAdj(nuevaInter.getId(), nuevaInter, adj);
				grafoPrueba.addVertexWithAdj(nuevaInter.getId(), nuevaInter, adj);
				//System.out.println(numCargados);
				numCargados++;
			}
			//pinta nodos
			//mapa.pintarMapaConMarcadores(list);
			System.out.println("El n�mero de arcos teoricos: "+ numeroArcos);
		}
		catch (Exception e)
		{
			System.out.println("Alcanz� a cargar: " + numCargados);
			e.printStackTrace();
			System.out.println(e.getStackTrace().toString());
			System.out.println(e.getMessage());
		}
	}


	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 1A: Encontrar el camino de costo m�nimo para un viaje entre dos ubicaciones geogr�ficas.
	 * @param idVertice2 
	 * @param idVertice1 
	 */
	public void caminoCostoMinimoA1(String idVertice1, String idVertice2)
	{
		double dist=0.0;
		Long goal=Long.parseLong(idVertice1);
		Vertice verGoal=grafoJson2.getVertice(goal);
		ArregloDinamico<VOIntersections> list= new ArregloDinamico<VOIntersections>(5);
		if(verGoal==null)
		{
			System.out.println("El vertice final no existe");
		}
		Vertice inicio=dijkstra(goal, Long.parseLong(idVertice2));
		Vertice actual=inicio;
		while((inicio!=null) && (verGoal!=null)&&goal!=((VOIntersections) actual.getInfo()).getId())
		{

			VOIntersections VOactual= (VOIntersections) actual.getInfo();
			list.agregar(VOactual);
			System.out.println(VOactual.toString());
			VOIntersections VOnext=(VOIntersections) actual.getNext().getInfo();
			distance(VOactual.getLat(), VOactual.getLon(), VOnext.getLat(), VOnext.getLon());
			actual=actual.getNext();

		}
		System.out.println("Distancia estimada en km: "+dist);

		mapa.pintarMapaConLineas(list);
	}



	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 2A: Determinar los n v�rtices con mayor n�mero de infracciones. Adicionalmente identificar las
	 * componentes conectadas (subgrafos) que se definan �nicamente entre estos n v�rtices
	 * @param  int n: numero de vertices con mayor numero de infracciones  
	 */
	public void mayorNumeroVerticesA2(int n) {
		// TODO Auto-generated method stub
		Object[] arreglo = grafoJson2.getVertices().arreglo();
		Heap<VOIntersections> maxHeap = new Heap<VOIntersections>(grafoJson2.V(), comparador);
		NodoTablaHash node;
		Vertice v=null;
		for(int i=0; i<arreglo.length;i++)
		{
			node=(NodoTablaHash)arreglo[i];
			if(node!=null)
			{
				v=(Vertice)(node).getValue();
			}
			if(v!=null &&(VOIntersections) (v).getInfo()!=null)
			{
				maxHeap.agregar((VOIntersections) (v).getInfo());
			}

		}
		Queue cola;
		VOIntersections[] listMarcadores=new VOIntersections[n];
		for(int j=0;j<n;j++)
		{
			VOIntersections vo=maxHeap.delMax();
			listMarcadores[j]=vo;
			System.out.println(vo.toString()+" Cantidad accidentes: "+vo.getCantidad());
			cola= new Queue<Vertice>();
			grafoJson2.dfs(grafoJson2.getVertice(vo.getId()), cola);
			System.out.println("El numero de componentes conectadas a: "+vo.getId()+ " es :"+ cola.size());
			for(int k=0; k<cola.size();k++)
			{
				System.out.println(((Vertice) cola.dequeue()).getInfo().toString());
			}
		}
		mapa.pintarMapaConMarcadores(listMarcadores);
		//Ahora se identifican las componentes conectadas
		
		//ArregloDinamico<VOCC> ccs= grafoJson2.cc();
		//System.out.println("El numero de componentes conectadas en todo el grafo es: "+ccs.darTamano());
		
		
	}

	/**
	 * Requerimiento 1B: Encontrar el camino m�s corto para un viaje entre dos ubicaciones geogr�ficas 
	 * @param idVertice2 
	 * @param idVertice1 
	 */
	@SuppressWarnings("unchecked")
	public void caminoLongitudMinimoaB1(long idVertice1, long idVertice2) {
		// Llama al método de BFS que se encarga de devolver el camino más corto entre los dos vértices 
		System.out.println(grafoJson2.V());
		Queue<Long> cola = grafoJson2.breadthFirstSearch(idVertice1, idVertice2);
		VOIntersections[] arreglito = new VOIntersections[cola.size()];
		// Recorre las posiciones del arreglo creado y le va metiendo los objetos del método anterior para mandarlo al mapa
		for(int i = 0; i < cola.size(); i++)
			arreglito[i] = (VOIntersections) grafo.getVertices().get(cola.dequeue());
		// Pinta el mapa. 
		mapa.pintarMapaConMarcadores(arreglito);
	}

	/**
	 * Requerimiento 2B:  Definir una cuadricula regular de N columnas por M filas. que incluya las longitudes y latitudes dadas
	 * @param  lonMin: Longitud minima presente dentro de la cuadricula
	 * @param  lonMax: Longitud maxima presente dentro de la cuadricula
	 * @param  latMin: Latitud minima presente dentro de la cuadricula
	 * @param  latMax: Latitud maxima presente dentro de la cuadricula
	 * @param  columnas: Numero de columnas de la cuadricula
	 * @param  filas: Numero de filas de la cuadricula
	 */
	public void definirCuadriculaB2(double lonMin, double lonMax, double latMin, double latMax, int columnas, int filas) {
		// Para encontrar los sitios donde hay que poner los filas y columnas toca dividir la diferencia entre la cantidad de filas y columnas
		double espacioLat = (latMax - latMin)/columnas;
		double espacioLon = (lonMax - lonMin)/filas;
		int contador = 0;
		VOIntersections[] arreglo = new VOIntersections[columnas * filas];
		// Hacer un arreglo que representa espacioLat x espacioLon
		for(int i = 0; i < columnas; i ++) {
			double latitud = latMin + (i * espacioLat);
			for(int j = 0; j < filas; j ++) {
				double longitud = lonMin + (j * espacioLon);
				//Se inicia el iterador que va a recorrer los vertices en el grafo y las variables auxiliares
				Iterator<Vertice> iterador = grafo.getVertices().keys();
				VOIntersections masCercano = null;
				double distanciaActual = Double.MAX_VALUE;
				// Se recorre cada uno de los vértices y se va guardando la información del más cercano hasta el momento
				while(iterador.hasNext()) {
					Vertice v = (Vertice) iterador.next();
					VOIntersections actual = (VOIntersections) v.getInfo();
					double vLat = actual.getLat();
					double vLon = actual.getLon();
					double dist = distance(vLat,vLon,latitud,longitud);
					// Si el actual es más cercano que el último más cercano, entonces lo reemplaza
					if(dist < distanciaActual) {
						masCercano = actual;
						distanciaActual = dist;
					}
				}
				// Al final se agrega al arreglo el vértice que resultò ser más cerano. . 
				arreglo[contador] = masCercano;
				System.out.println("Más cercano: " + masCercano + " a esquina " + contador + 
						" en:(" + i + "," + j + "), con latitud: " + masCercano.getLat() + "y longitud: " + masCercano.getLon());
				contador ++;
			}
		}
		mapa.pintarMapaConMarcadores(arreglo);
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 1C:  Calcular un �rbol de expansi�n m�nima (MST) con criterio distancia, utilizando el algoritmo de Kruskal.
	 */
	public void arbolMSTKruskalC1() {
		// TODO Auto-generated method stub

	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 2C: Calcular un �rbol de expansi�n m�nima (MST) con criterio distancia, utilizando el algoritmo de Prim. (REQ 2C)
	 */
	public void arbolMSTPrimC2() {
		// TODO Auto-generated method stub

	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 3C: Calcular los caminos de costo m�nimo con criterio distancia que conecten los v�rtices resultado
	 * de la aproximaci�n de las ubicaciones de la cuadricula N x M encontrados en el punto 5.
	 */
	public void caminoCostoMinimoDijkstraC3() {
		// TODO Auto-generated method stub

	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 4C:Encontrar el camino m�s corto para un viaje entre dos ubicaciones geogr�ficas escogidas aleatoriamente al interior del grafo.
	 * @param idVertice2 
	 * @param idVertice1 
	 */
	public void caminoMasCortoC4(String idVertice1, String  idVertice2) {
		// TODO Auto-generated method stub
		Long goal=Long.parseLong(idVertice1);
		Vertice inicio=dijkstra(goal, Long.parseLong(idVertice2));
		Vertice actual=inicio;
		Vertice verGoal=grafoJson2.getVertice(goal);
		ArregloDinamico<VOIntersections> list= new ArregloDinamico<VOIntersections>(5);
		if(verGoal==null)
		{
			System.out.println("El vertice final no existe");
		}
		while((inicio!=null) && (verGoal!=null)&&goal!=((VOIntersections) actual.getInfo()).getId())
		{

			VOIntersections VOactual= (VOIntersections) actual.getInfo();
			list.agregar(VOactual);
			System.out.println(VOactual.toString());
			VOIntersections VOnext=(VOIntersections) actual.getNext().getInfo();
			distance(VOactual.getLat(), VOactual.getLon(), VOnext.getLat(), VOnext.getLon());
			actual=actual.getNext();

		}
		
	}
	/**
	 * Determina cuál de los cuatrimestres convoca al método que los carga en órdne 
	 * @param pCuatrimestre
	 * @throws Exception
	 */
	public void cargarInfracciones() throws Exception {
		// Mete a un arreglo los meses para después poder leer los archivos en un ciclo.
		// Se declaran parte inicial y final de la ruta que tienen todos en común. 
		int cantidadDeInfracciones = 0; 
		String meses[] = new String[12];
		String rutai = "data/";
		String rutaf = "_wgs84.csv";
		meses[0] = "January";
		meses[1] = "February";
		meses[2] = "March";
		meses[3] = "Abril";
		meses[4] = "May";
		meses[5] = "June";
		meses[6] = "July";
		meses[7] = "August";
		meses[8] = "September";
		meses[9] = "October";
		meses[10] = "November";
		meses[11] = "December";
		// Recorrido que va procesando los archivos por mes.
		for(int i = 0; i < 12; i++) {
			// Intenta crear un archivo con la ruta creada a partir del mes
			// Llama al método que carga el archivo y los convierte a objetos de tipo VOMovingViolations
			try {
				File f = new File(rutai + meses[i] + rutaf);
				cantidadDeInfracciones += leerInfraccionesdeArchivo(f);
			}
			// Si no funciona lanza excepción. 
			catch(Exception e) {
				throw e;
			}
		}
		// Al final se escribe en la consola la cantidad de datos que se leyeron.
		System.out.println("Se leyeron en total : " +  cantidadDeInfracciones + " infracciones");
	}

	/**
	 * Lee la información de un archivo que le llega por parámetro y se encarga de meterlo al arreglo 
	 * @param pArchivo
	 * @throws Exception
	 */
	public int leerInfraccionesdeArchivo(File pArchivo) throws Exception {
		// Crea un lector que lee la información del archivo dada por parámetro. 
		// También se crea un contador que después se retorna para ver cuántas infracciones se han cargado. 
		FileReader lector = new FileReader(pArchivo);
		BufferedReader br = new BufferedReader(lector);
		int latcol = 18;
		int loncol = 19;
		int contador = 0;
		br.readLine();
		String linea = br.readLine();
		// Para el archivo del mes de enero los valores de latitud y longitud están desplazados a la izquierda.
		if(pArchivo.getName().toLowerCase().contains("january")) {
			latcol --;
			loncol --;
		}
		// Para los archivos de octubre, noviembre y diciembre los valores de latitud y longitud están desplazados a la derecha. 
		if(pArchivo.getName().toLowerCase().contains("october") || pArchivo.getName().toLowerCase().contains("november") 
				|| pArchivo.getName().toLowerCase().contains("december")) {
			latcol ++;
			loncol ++;
		}
		// Va leyendo líneas hasta que llega al final del archivo. 
		while(linea != null) {
			// Separa los valores por ";" y lee la longitud y latitud de infracción
			contador ++; 
			String arreglo[] = linea.split(";");
			int id = Integer.parseInt(arreglo[1]);
			double latitud = Double.parseDouble(arreglo[latcol].replaceAll(",", "."));
			double longitud = Double.parseDouble(arreglo[loncol].replaceAll(",", "."));
			linea = br.readLine();
			// En este punto se revisa cuál de todos los nodos es el más cercano a la ubicación de la infracción y lo mete. 
			// Cuidado: Puede ser muy demorado. 
			asignarInfraccionANodo(id, latitud, longitud);
		}
		return contador;
	}

	/**
	 * Método que recorre todos los nodos del grafo y compara a cual de los nodos está más cercano. 
	 * @param pLatitud - la latitud de la infracción
	 * @param pLongitud - la longitud de la infracción
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void asignarInfraccionANodo(long id, double pLatitud, double pLongitud) {
		//Se inicia el iterador que va a recorrer los vertices en el grafo y las variables auxiliares
		Iterator<Vertice> iterador = grafo.getVertices().keys();
		Vertice masCercano = null;
		double distanciaActual = 9999999.9999;
		// Se recorre cada uno de los vértices y se va guardando la información del más cercano hasta el momento
		while(iterador.hasNext()) {
			Vertice v = (Vertice) iterador.next();
			VOIntersections actual = (VOIntersections) v.getInfo();
			double vLat = actual.getLat();
			double vLon = actual.getLon();
			double dist = distance(vLat,vLon,pLatitud,pLongitud);
			// Si el actual es más cercano que el último más cercano, entonces lo reemplaza
			if(dist < distanciaActual) {
				masCercano = v;
				distanciaActual = dist;
			}
		}
		// Al final agrega a ese vértice específico la información de la infracicón para que este lo almacene. 
		VOIntersections aAgregar = (VOIntersections) grafo.getVertice(masCercano.getKey()).getInfo();
		aAgregar.agregarInfraccion(id);
	}


	public Vertice dijkstra(Long inicio, Long goal)
	{
		Long actual=inicio;

		Vertice verInicio=grafoJson2.getVertice(inicio);
		if(verInicio==null)
		{
			System.out.println("No existe vertice");
			return null;
		}
		Vertice verActual=verInicio;
		Double[] dist=new Double[grafoJson2.V()];

		MinPQ<VOIntersections> pq= new MinPQ<VOIntersections>(grafoJson2.V(), comparador);
		TablaHash<Long,Grafo<Long,VOIntersections,Long>.Vertice> ver=grafoJson2.getVertices();
		//Se recorren todos los nodos hasta que se llega al vertice indicado
		for(int i=0; i<grafoJson2.V() /*&& actual.compareTo(goal)!=0*/;i++)
		{
			//Se inicializan todas las distancias menos las de los nodos adyacentes en infinito
			for(int k=0;k<grafoJson2.V();k++)
			{
				/*Long adjActual=null;
				ArregloDinamico<Long> adj = verInicio.getAdjNodes();
				if(k<adj.darTamano())
				{
					adjActual=adj.darElemento(k);
				}
				if( adjActual!=null && k==ver.getIndex(adjActual))
				{
					dist[k]=(double) grafoJson2.getVertice(actual).getInfo().getCantidad();
				}
				else
				{*/
				dist[k]=Double.POSITIVE_INFINITY;
				//}
			}

			pq.agregar((VOIntersections) verInicio.getInfo());
			while(!pq.esVacia())
			{
				long idActual=pq.delMax().getId();
				verActual=grafoJson2.getVertice(idActual);
				int iActual=grafoJson2.getVertices().getIndex(idActual);
				//Se recorren los adyacentes
				for(int j=0; j<verActual.getAdjNodes().darTamano(); j++)
				{
					Long adjActual=(Long) verActual.getAdjNodes().darElemento(j);
					Vertice verAdjActual=grafoJson2.getVertice(actual);			
					int  iAdj=grafoJson2.getVertices().getIndex(adjActual);
					if(dist[iAdj]>dist[iActual]+((VOIntersections) verAdjActual.getInfo()).getCantidad())
					{
						verActual.setNext(verAdjActual);
						verAdjActual.setCameFrom(actual);
						dist[iAdj] = dist[iActual]+((VOIntersections) verAdjActual.getInfo()).getCantidad();
						((VOIntersections) verAdjActual.getInfo()).setAcomulado(dist[iAdj]);
						pq.agregar((VOIntersections) verAdjActual.getInfo());

					}

				}
			}
		}
		return verInicio;
	}
	private void toJson()
	{
		JsonWriter writer;
		System.out.println("Cantidad V: "+grafo.V()+" Cantidad E: "+grafo.E());
		try
		{

			writer = new JsonWriter(new FileWriter("./data/WashingtonGraph.json"));
			//writer.beginObject();
			//
			// VERTICES
			//
			writer.beginArray();
			//writer.name("VERTICES");
			Iterator<Vertice>  itVertices = grafo.iteratorVertices();

			while(itVertices.hasNext())
			{
				Vertice v=itVertices.next();
				VOIntersections actual= (VOIntersections)v.getInfo();

				//No se guarda si no tiene adyacentes
				if(v.getArcos().getSize()==0)
				{
					continue;
				}

				else
				{
					writer.beginObject();
					writer.name("ID").value(actual.getId());
					writer.name("LAT").value(actual.getLat());
					writer.name("LON").value(actual.getLon());

					//
					//Se escriben los adyacentes
					//
					writer.name("ADJ");
					writer.beginArray();

					LinkedList adj = v.getArcos();
					NodeList<Arco> actAdj=  adj.getFirstNode();
					VOWay actElement=(VOWay) actAdj.getelem().getInfoArco();
					while(actAdj!=null && actElement!=null)
					{
						writer.beginObject();

						writer.name("ID_ARC").value(actElement.getId());
						writer.name("NODO1").value(actElement.getNodo1());
						writer.name("NODO2").value(actElement.getNodo2());

						writer.endObject();	
						actAdj=actAdj.getNext();
					}
				}
				writer.endArray();
				writer.endObject();				
			}
			writer.endArray();
			//writer.endObject();


			writer.close();
			System.out.println("Archivo Json guardado correctamente");
			//
			// ARCOS (Opci�n 2 guardarlos por aparte)
			//
			//writer.name("Arcos");
			//writer.beginArray();
			//grafo.iteratorArcos();

			//TODO
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Fallo porque: "+e.getMessage());
		}
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

	public void loadWaysJson(String ruta) 
	{
		int numCargados=0;
		JsonParser parser = new JsonParser();
		try 
		{
			Reader reader = Files.newBufferedReader(Paths.get(ruta));
			JsonArray arreglo = (JsonArray)parser.parse(new FileReader(ruta));
			for(int i=0; arreglo != null && i < arreglo.size(); i++)
			{
				JsonObject objeto = (JsonObject)arreglo.get(i);
				//------------------------------------
				//------ Lectura de atributos del Way
				//------------------------------------
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace().toString());
			System.out.println(e.getMessage());
		}
	}


	private void toJson1()
	{
		Iterator<VOIntersections>  itVertices=grafo.iteratorVertices();

		while(itVertices.hasNext())
		{
			VOIntersections actual= itVertices.next();
			JsonObject obj = new JsonObject();
			//JsonElement joe= actual.getId();
			//obj.add("ID", actual.getId() );

		}


		/*Iterator it= grafoPrueba.iteratorVertices();
		Vertex v=(Vertex) it.next();
		int i=0;
		while(it.hasNext())
		{
			v=(Vertex) it.next();
			i++;
		}
		System.out.println("Iterador "+i);

		System.out.println("Cola: "+grafoPrueba.getVertices().keysQueue().size());*/
		/*System.out.println("Tama�o grafo: "+grafoJson2.V());
		TablaHash<Long,Grafo<Long,VOIntersections,Long>.Vertice> ver=grafoJson2.getVertices();
		Double[] distTo= new Double[grafoJson2.V()];
		Long[] edgeTo= new Long[grafoJson2.V()];
		//MinPQ<Double> menorCosto = new MinPQ<Double>(grafoJson2.V());
		int c=0;

		for(int i=0;i<ver.size();i++)
		{
			NodoTablaHash actual=ver.get(i);

			if(actual!=null)
			{
				distTo[i]=Double.POSITIVE_INFINITY;	
			}
			c++;	
		}
		long idVertIni=Long.parseLong(idVertice1);
		int index= ver.getIndex(idVertIni);
		distTo[index]=0.0;
		Vertice vertIni= grafoJson2.getVertice(idVertIni);

		//Se recorren lo adyacentes 
		ArregloDinamico<Long> adj = vertIni.getAdjNodes();
		for(int j=0; j<adj.darTamano();j++)
		{
			Long actual=adj.darElemento(j);
			int k=ver.getIndex(actual);
			distTo[k]=(double) grafoJson2.getVertice(actual).getInfo().getCantidad();
		}

		 */



	}
}
