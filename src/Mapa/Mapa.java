package Mapa;

import com.teamdev.jxmaps.swing.MapView;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.teamdev.jxmaps.*;

public class Mapa extends MapView{

	// Atributos ---------------------------------------------------------------
	
	private Map map;
	
	// Constructor ---------------------------------------------------------------
	
	/**
	 * Crea un mapa con un nombre dado por parámetro
	 * @param pNombre - El nombre del mapa
	 */
	public Mapa(String pNombre) {
		JFrame frame = new JFrame(pNombre);
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
		frame.add(this,BorderLayout.CENTER);
		frame.setSize(700, 500);
		frame.setVisible(true);
	}
	
	
	public void ponerZoom(double pZoom) {
		map.setZoom(pZoom);
	}
}
