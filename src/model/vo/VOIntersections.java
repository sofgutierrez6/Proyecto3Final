package model.vo;

import model.data_structures.ArregloDinamico;

public class VOIntersections implements Comparable<VOIntersections> {
	
	// Atributos ------------------------------------------------------------------------------------
	
	/** Identificador del nodo de intersección */
	private long id;
	
	/** Latitud del nodo */
	private double lat;
	
	/** Longitud del nodo */
	private double lon;
	
	/** Cantidad de infracciones que se cometieron en esta intersección.*/
	private int cantidad;
	
	private Double acomulado;
	/**Arreglo dinamico con id de infracciones */
	private ArregloDinamico<Long> infracciones;
	
	// Constructor ------------------------------------------------------------------------------------
	
	/**
	 * Construye un objeto de tipo VOIntersection que representa un nodo del grafo. 
	 * @param pId
	 * @param pLat
	 * @param pLon
	 */
	public VOIntersections(long pId,double pLat, double pLon, ArregloDinamico<Long> pInfracciones) 
	{
		id = pId;
		lat = pLat;
		lon = pLon;
		cantidad = pInfracciones==null?0:pInfracciones.darTamano();
		acomulado=0.0;
		infracciones=pInfracciones;
	}
	
	// Métodos ------------------------------------------------------------------------------------
	
	/**
	 * @return Retorna el identificador del nodo
	 */
	public long getId() {
		return id;
	}
	
	public void setCantidad(int cant)
	{
		cantidad=cant;
	}
	/**
	 * @return la cantidad de infracciones que se cometieron en esta intersección. 
	 */
	public int getCantidad() {
		return cantidad;
	}
	public int getCant()
	{
		return infracciones.darTamano();
	}
	
	public Double getAcomulado()
	{
		return acomulado;
	}
	public void aumentarAcomulado(int mas)
	{
		acomulado+=mas;
	}
	public void setAcomulado(Double a)
	{
		acomulado=a;
	}
	/**
	 * Agrega una instancia a la cantidad de infracciones que se cometieron. 
	 */
	public void agregarInfraccion(Long id) {
		infracciones.agregar(id);
		cantidad ++;
	}
	/**
	 * @return latitud de la ubicación del nodo
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * @return longitud de la ubicación del nodo
	 */
	public double getLon() {
		return lon;
	}

	@Override
	public int compareTo(VOIntersections o) {
		
		return (int) id - (int) o.getId();
	}
	
	public String toString()
	{
		return "id: "+id+" lat: "+lat+" lon: "+lon;
	}

}
