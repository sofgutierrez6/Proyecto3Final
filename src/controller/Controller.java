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
			view.printMenu();
			int option = sc.nextInt();
			Controller controller = new Controller();
			//Recorre las posibles opciones que ingresa el usuario al ejecutar el programa.
			switch(option){
			case 0:
				grafo = contador.load(args);
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
				}
				break;

			case 1:
				System.out.println("Antes grafo E: "+grafo.E());
				//System.out.println(grafo.iteratorVertices().next().toString());
				controller.toJson();

				break;

			case 2:
				System.out.println("Carga del grafo desde un Json");
				String rutaCenter= "./data//CenterWashingtonGraph.json";
				String rutaWashington= "./data//WashingtonGraph.json";
				controller.loadGraphFromJson(rutaCenter);

				//controller.loadWaysJson(rutaWay);
				break;

			case 3:	
				fin=true;
				sc.close();
				break;
			}
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
