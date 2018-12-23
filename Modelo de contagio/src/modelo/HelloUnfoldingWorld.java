package modelo;

import processing.core.PApplet;

import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;

public class HelloUnfoldingWorld extends PApplet {
    // Serial ID is optional and added by Eclipse
    private static final long serialVersionUID = 1L;
    
    UnfoldingMap mainMap;
    
    List<ImageMarker> infectados;
    
    Location berlinLocation = new Location(52.5f, 13.4f);
    Location madridLocation = new Location(40.471926, -3.56264);
	Location veniceLocation = new Location(45.44f, 12.34f);
	Location lisbonLocation = new Location(38.71f, -9.14f);
	
    private UnfoldingMap getRawMap() {
    	
        UnfoldingMap openStreetBasicMap = new UnfoldingMap(this, 0, 0, 1200, 700, new OpenStreetMap.OpenStreetMapProvider());
        openStreetBasicMap.zoomToLevel(3);
        openStreetBasicMap.setBackgroundColor(240);
        return openStreetBasicMap;
    }
    /*public void addInfectado(double lat, double longit, String nombre) {
    	Location loc = new Location(lat, longit);
    	mainMap.addMarker( new ImageMarker(loc, loadImage("marcaRoja.png")));
    }*/
     
    public void setup() {
    size(1200, 700, OPENGL);
        mainMap = getRawMap();
        MapUtils.createDefaultEventDispatcher(this, mainMap);
        //ImageMarker imgMarker1 = new ImageMarker(lisbonLocation, loadImage("marcaRoja.png"));
		//ImageMarker imgMarker2 = new ImageMarker(veniceLocation, loadImage("marcaRoja.png"));
		//ImageMarker imgMarker3 = new ImageMarker(berlinLocation, loadImage("marcaRoja.png"));
		ImageMarker imgMarker4 = new ImageMarker(madridLocation, loadImage("marcaRoja.png"));
		mainMap.addMarker(imgMarker4);
        //mainMap.addMarkers(infectados);
    }
    public void draw() {
        background(240);
        resize(1200,700);
        mainMap.draw();
    }
}
