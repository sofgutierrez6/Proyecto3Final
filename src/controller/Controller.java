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
import java.util.Iterator;
import java.util.Scanner;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import model.data_structures.ArregloDinamico;
import model.data_structures.BST;
import model.data_structures.Grafo;
import model.data_structures.Grafo.Arco;
import model.data_structures.Grafo.Vertice;
import model.data_structures.IQueue;
import model.data_structures.IStack;
import model.data_structures.LinkedList;
import model.data_structures.MaxColaPrioridad;
import model.data_structures.NodeList;
import model.data_structures.RedBlackBST;
import model.data_structures.TablaHash;
import model.vo.Counter;
import model.vo.LocationVO;
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

	/** Grafo donde se almacena toda la red vial de Washington con arcos de tipo highway */
	private static Grafo grafo;

	private Grafo grafoJson;
	// Constructor -------------------------------------------------------------------

	/**
	 * Construye el controlador 
	 */
	public Controller() {
		view = new MovingViolationsManagerView();
		//grafo= new Grafo<Long,VOIntersections,VOWay>();
		grafoJson = new Grafo<Long,VOIntersections,VOWay>();
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
		Counter contador = new Counter();
		// Mantiene el programa corriendo hasta que el usuario decida temrinarlo. 

		while(!fin) {

			int idVertice1 = 0;
			int idVertice2 = 0;
			Controller controller = new Controller();

			view.printMenu();

			int option = sc.nextInt();

			//Recorre las posibles opciones que ingresa el usuario al ejecutar el programa.
			switch(option){
			case 0:
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
				String RutaArchivo = "";
				view.printMessage("Escoger el grafo a cargar: (1) Downtown  o (2)Ciudad Completa.");

				int ruta = sc.nextInt();

				if(ruta == 1)
				{
					RutaArchivo = "./data//WashingtonGraph.json"; //la ruta del archivo de Downtown
					startTime = System.currentTimeMillis();
					controller.loadGraphFromJson(RutaArchivo);
					endTime = System.currentTimeMillis();
				}

				else if(ruta == 2)
				{
					RutaArchivo = "./data//finalGraph.json"; //ruta del archivo de la ciudad completa

					startTime = System.currentTimeMillis();
					controller.loadJSON(RutaArchivo);
					endTime = System.currentTimeMillis();
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
				view.printMessage("Ingrese El id del primer vertice (Ej. 901839): ");
				idVertice1 = sc.nextInt();
				view.printMessage("Ingrese El id del segundo vertice (Ej. 901839): ");
				idVertice2 = sc.nextInt();


				startTime = System.currentTimeMillis();
				caminoCostoMinimoA1(idVertice1, idVertice2);
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
				idVertice1 = sc.nextInt();
				view.printMessage("Ingrese El id del segundo vertice (Ej. 901839): ");
				idVertice2 = sc.nextInt();

				startTime = System.currentTimeMillis();
				caminoMasCortoC4(idVertice1, idVertice2);
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
			}
		}

	}


	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia


	/**
	 * Cargar el Grafo No Dirigido de la malla vial: Downtown o Ciudad Completa
	 * @param rutaArchivo 
	 */

	public void loadJSON(String rutaArchivo) 
	{
		// TODO Auto-generated method stub
	}


	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 1A: Encontrar el camino de costo m�nimo para un viaje entre dos ubicaciones geogr�ficas.
	 * @param idVertice2 
	 * @param idVertice1 
	 */
	public void caminoCostoMinimoA1(int idVertice1, int idVertice2) {
		// TODO Auto-generated method stub
	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 2A: Determinar los n v�rtices con mayor n�mero de infracciones. Adicionalmente identificar las
	 * componentes conectadas (subgrafos) que se definan �nicamente entre estos n v�rtices
	 * @param  int n: numero de vertices con mayor numero de infracciones  
	 */
	public void mayorNumeroVerticesA2(int n) {
		// TODO Auto-generated method stub

	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 1B: Encontrar el camino m�s corto para un viaje entre dos ubicaciones geogr�ficas 
	 * @param idVertice2 
	 * @param idVertice1 
	 */
	public void caminoLongitudMinimoaB1(int idVertice1, int idVertice2) {
		// TODO Auto-generated method stub

	}

	// TODO El tipo de retorno de los m�todos puede ajustarse seg�n la conveniencia
	/**
	 * Requerimiento 2B:  Definir una cuadricula regular de N columnas por M filas. que incluya las longitudes y latitudes dadas
	 * @param  lonMin: Longitud minima presente dentro de la cuadricula
	 * @param  lonMax: Longitud maxima presente dentro de la cuadricula
	 * @param  latMin: Latitud minima presente dentro de la cuadricula
	 * @param  latMax: Latitud maxima presente dentro de la cuadricula
	 * @param  columnas: Numero de columnas de la cuadricula
	 * @param  filas: Numero de filas de la cuadricula
	 */
	public void definirCuadriculaB2(double lonMin, double lonMax, double latMin, double latMax, int columnas,
			int filas) {
		// TODO Auto-generated method stub
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
	public void caminoMasCortoC4(int idVertice1, int idVertice2) {
		// TODO Auto-generated method stub

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
			double latitud = Double.parseDouble(arreglo[latcol].replaceAll(",", "."));
			double longitud = Double.parseDouble(arreglo[loncol].replaceAll(",", "."));
			linea = br.readLine();
		}
		return contador;
	}


	public void loadGraphFromJson(String ruta) 
	{
		int numCargados=0;
		JsonParser parser = new JsonParser();
		try 
		{
			Reader reader = Files.newBufferedReader(Paths.get(ruta));
			JsonArray arreglo = (JsonArray)parser.parse(new FileReader(ruta));

			for(int i=0; arreglo != null && i < arreglo.size(); i++)
			{
				//System.out.println("Entra for");
				JsonObject objeto = (JsonObject)arreglo.get(i);
				//------------------------------------
				//------ Lectura de atributos de la interseccion
				//------------------------------------
				int ID=0;
				JsonElement elementoID = objeto.get("ID");
				if(elementoID!=null && !elementoID.isJsonNull())
				{
					ID=elementoID.getAsInt();
					//System.out.print("a");
				}
				double LAT=0;
				JsonElement elementoLAT = objeto.get("LAT");
				if(elementoLAT!=null && !elementoLAT.isJsonNull())
				{
					LAT=elementoLAT.getAsDouble();
					//System.out.print("b");
				}
				double LON=0;
				JsonElement elementoLON = objeto.get("LON");
				if(elementoLON!=null && !elementoLON.isJsonNull())
				{
					LON=elementoLON.getAsDouble();
					//System.out.print("c");
				}
				VOIntersections nuevaInter= new VOIntersections(ID, LAT, LON);
				LinkedList<VOWay>adj=new LinkedList<VOWay>();
				boolean cargoArreglo=objeto.get("ADJ").isJsonArray();
				//System.out.println(cargoArreglo);
				if(cargoArreglo)
				{
					JsonArray JAdj=(JsonArray) objeto.get("ADJ").getAsJsonArray();

					//Pasar Adj a linked List
					for(int j=0; JAdj != null && i < JAdj.size(); j++)
					{
						JsonObject objetoAdj = (JsonObject)arreglo.get(j);
						int IDAdj=0;
						JsonElement elementoIDAdj = objetoAdj.get("ID_ARC");
						if(elementoIDAdj!=null && !elementoIDAdj.isJsonNull())
						{
							IDAdj=elementoIDAdj.getAsInt();
							//System.out.print("a");
						}
						Long NODO1=(long) 0.0;
						JsonElement elementoNODO1 = objeto.get("NODO1");
						if(elementoNODO1!=null && !elementoNODO1.isJsonNull())
						{
							NODO1=elementoNODO1.getAsLong();
							//System.out.print("b");
						}
						Long NODO2=(long) 0.0;
						JsonElement elementoNODO2 = objeto.get("NODO2");
						if(elementoNODO2!=null && !elementoNODO2.isJsonNull())
						{
							NODO2=elementoNODO2.getAsLong();
							//System.out.print("c");
						}
						// se crea un nuevo VOWay
						VOWay nuevoVOWay = new VOWay(IDAdj,NODO1,NODO2);
						adj.add(nuevoVOWay);
					}
				}

				//Agregar vertice al grafo
				grafoJson.addVertexSecondForm(nuevaInter.getId(), nuevaInter, adj);
				numCargados++;
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace().toString());
			System.out.println(e.getMessage());
		}
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


	}
}
