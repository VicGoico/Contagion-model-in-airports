package modelo;

import processing.core.PApplet;


import java.util.ArrayList;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import modelo.red.Nodo;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;

/**
 * 
 * Esta clase se encarga de dibujar el mapa donde aparencen
 * los aeropuertos contagiados
 *
 */
public class HelloUnfoldingWorld extends PApplet {
    // Serial ID is optional and added by Eclipse
    private static final long serialVersionUID = 1L;
    
    UnfoldingMap mainMap;
    
    Marker[] infectados;
    
	/**
	 * Se encarga de generar un mapa con valores como el tamaño de este
	 * o el zoom inicial
	 * @return openStreetBasicMap mapa del mundo
	 */
    private UnfoldingMap getRawMap() {
    	
        UnfoldingMap openStreetBasicMap = new UnfoldingMap(this, 0, 0, 1200, 700, new OpenStreetMap.OpenStreetMapProvider());
        openStreetBasicMap.zoomToLevel(3);
        openStreetBasicMap.setBackgroundColor(240);
        return openStreetBasicMap;
    }
    /**
     * se encarga de iniciar el mapa y colocar los aeropuertos infectados
     */
    public void setup() {
    size(1200, 700, OPENGL);
        mainMap = getRawMap();
        MapUtils.createDefaultEventDispatcher(this, mainMap);
		mainMap.addMarkers(infectados);
    }
    /**
     * Se encarga de dibujar
     */
    public void draw() {
        background(240);
        resize(1200,700);
        mainMap.draw();
    }
    
    /**
     * Con una lista de aeropuertos contagiados los transdorma en ImageMarker
     * para colocar en el mapa
     * @param nodosContagiados lista de los aeropuertos que se han contagiado
     */
    public void setInfectados(ArrayList<Nodo> nodosContagiados) {
    	
    	Marker[] aux = new Marker[nodosContagiados.size()];
    	
    	for(int i = 0; i < nodosContagiados.size(); i++) {
    		Location loc = new Location(nodosContagiados.get(i).getAirportInfo().getLatitude(),
    				nodosContagiados.get(i).getAirportInfo().getLongitude());
    		ImageMarker img = new ImageMarker(loc, loadImage("marcaRoja.png"));
    		aux[i] = img;
    	}
    	this.infectados = aux;
    }
}
