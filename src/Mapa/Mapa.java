package Mapa;

import com.teamdev.jxmaps.swing.MapView;

import model.data_structures.ArregloDinamico;
import model.vo.VOIntersections;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.teamdev.jxmaps.*;

public class Mapa extends MapView{

	// Atributos ---------------------------------------------------------------
	
	private Map map;
	
	private String nombre;
	
	// Constructor ---------------------------------------------------------------
	
	/**
	 * Crea un mapa con un nombre dado por parámetro
	 * @param pNombre - El nombre del mapa
	 */
	public Mapa(String pNombre) {
		nombre = pNombre;
		setOnMapReadyHandler(new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status) {
				if(status == MapStatus.MAP_STATUS_OK){
					//Genera el mapa en donde se va a guardar toda la información
					map = getMap();
					MapOptions mapOptions = new MapOptions();
					MapTypeControlOptions controlOptions = new MapTypeControlOptions();
					mapOptions.setMapTypeControlOptions(controlOptions);
					map.setOptions(mapOptions);
					map.setCenter(new LatLng(38.89894777000,-76.94837018300));
					map.setZoom(11.0);
				}
			}
		});
	}
	
	public void ponerZoom(double pZoom) {
		map.setZoom(pZoom);
	}
	
	public void pintarMapaConMarcadores(VOIntersections[] listMarcadores)
	{
		JFrame frame = new JFrame(nombre);
		frame.add(this,BorderLayout.CENTER);
		frame.setSize(700, 500);
		frame.setVisible(true);
		ponerMarcadores(listMarcadores);
		
	}
	
	public void ponerMarcador(double lat, double lon)
	{
		Marker mark= new Marker(map);
		mark.setPosition(new LatLng(lat,lon));
		System.out.println("Intenta pintar");
	}
	public void ponerMarcadores(VOIntersections[] list)
	{
		double lat, lon;
		for(int i=0; i<list.length || i < 300; i++)
		{
			lat=list[i].getLat();
			lon=list[i].getLon();
			Marker mark= new Marker(map);
			mark.setPosition(new LatLng(lat,lon));
			//System.out.println("Intenta pintar");
		}
	}
	public void hacerLinea(LatLng start,LatLng end)
	{
		LatLng[] path = {start,end};
		Polyline polyline = new Polyline(map);
		polyline.setPath(path);
		
	}
	
	public void ponerLineas(ArregloDinamico<VOIntersections> list)
	{
		double latStart, lonStart, latFin,lonFin;
		for(int i=0; i<list.darTamano()-1; i++)
		{
			latStart=list.darElemento(i).getLat();
			lonStart=list.darElemento(i).getLon();
			
			latFin=list.darElemento(i+1).getLat();
			lonFin=list.darElemento(i+1).getLon();
			
			hacerLinea(new LatLng(latStart, lonStart),new LatLng(latFin,lonFin));
			//System.out.println("Intenta pintar");
		}
	}
	public void pintarMapaConLineas(ArregloDinamico<VOIntersections> list )
	{
		JFrame frame = new JFrame(nombre);
		frame.add(this,BorderLayout.CENTER);
		frame.setSize(700, 500);
		frame.setVisible(true);
		ponerLineas(list);
	}
	
	public void pintarMapaConLineasIniciofin(VOIntersections in, VOIntersections fin, ArregloDinamico<VOIntersections> list )
	{
		JFrame frame = new JFrame(nombre);
		frame.add(this,BorderLayout.CENTER);
		frame.setSize(700, 500);
		frame.setVisible(true);
		ponerLineas(list);
		ponerMarcador(in.getLat(), in.getLon());
		ponerMarcador(fin.getLat(), fin.getLon());
	}
}
