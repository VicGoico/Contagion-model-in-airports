package modelo;

import processing.core.PApplet;

import java.util.ArrayList;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;

public class HelloUnfoldingWorld extends PApplet {
    // Serial ID is optional and added by Eclipse
    private static final long serialVersionUID = 1L;
    
    UnfoldingMap mainMap;
    
    Marker[] infectados;
    
	
    private UnfoldingMap getRawMap() {
    	
        UnfoldingMap openStreetBasicMap = new UnfoldingMap(this, 0, 0, 1200, 700, new OpenStreetMap.OpenStreetMapProvider());
        openStreetBasicMap.zoomToLevel(3);
        openStreetBasicMap.setBackgroundColor(240);
        return openStreetBasicMap;
    }
     
    public void setup() {
    size(1200, 700, OPENGL);
        mainMap = getRawMap();
        MapUtils.createDefaultEventDispatcher(this, mainMap);
		mainMap.addMarkers(infectados);
    }
    public void draw() {
        background(240);
        resize(1200,700);
        mainMap.draw();
    }
    public void setInfectados(ArrayList<Nodo> nodosContagiados) {
    	
    	Marker[] aux = new Marker[nodosContagiados.size()];
    	
    	for(int i = 0; i < nodosContagiados.size(); i++) {
    		Location loc = new Location(nodosContagiados.get(i).getInfo().getLatitude(),
    				nodosContagiados.get(i).getInfo().getLongitude());
    		ImageMarker img = new ImageMarker(loc, loadImage("marcaRoja.png"));
    		aux[i] = img;
    	}
    	this.infectados = aux;
    }
}
