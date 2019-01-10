package umbrales;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.TAirport;
import modelo.CSVReaders.ExpenditureHealthReader;
import modelo.red.Nodo;

/**
 * 
 * En esta clase
 *
 */
public class UmbralPIBSalud {
	private HashMap<Integer, Nodo> loadedNodes;
	private ExpenditureHealthReader expHealthReader;
	private HashMap<String, ArrayList<TAirport>> airportsByCountry;
	
	public UmbralPIBSalud(HashMap<Integer, Nodo> nodes, HashMap<String, ArrayList<TAirport>> airportsByCountry) {
		try {
			this.expHealthReader = ExpenditureHealthReader.getInstance();
			this.loadedNodes = nodes;
			this.airportsByCountry = airportsByCountry;
			calculaUmbralPorPaises();
			calculaUmbralPorAeropuerto();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * M�todo que da a cada aeropuerto del mismo pa�s el mismo umbral.
	 * Este umbral com�n a todos los aeropuertos del mismo pa�s se basa
	 * en cuanto gasta ese pa�s por habitante al a�o en sanidad.
	 * En funci�n de si gasta m�s de la media o menos se establece un umbral
	 */
	private void calculaUmbralPorPaises() {
		double mediaPaises = calculaMediaGastoTodosPaises();
		
		for (Map.Entry<Integer, Nodo> aeropuerto : this.loadedNodes.entrySet()) {
			
			double gastoPais = calcExpenditureHealthUmbral(aeropuerto.getValue().getAirportInfo());

			if(gastoPais > mediaPaises) {
				if(gastoPais < (mediaPaises * 1.5)) {
					//Si el pa�s gasta en sanidad mas que la media de pa�ses pero menos que la media
					aeropuerto.getValue().setUmbral(0.6);
				}
				else if(gastoPais < (mediaPaises * 3)) {
					aeropuerto.getValue().setUmbral(0.75);
				}
				else {
					aeropuerto.getValue().setUmbral(0.8);
				}
			}
			else {
				if(gastoPais > (mediaPaises * 0.75)) {
					aeropuerto.getValue().setUmbral(0.4);
				}
				else if(gastoPais > (mediaPaises * 0.5)) {
					aeropuerto.getValue().setUmbral(0.25);
				}
				else {
					aeropuerto.getValue().setUmbral(((gastoPais * 50) / mediaPaises)/100);
				}
			}
			
		}
	}
	/**
	 * M�todo para calcular la media de gasto de todos los pa�ses en sanidad
	 * @return Media de gasto
	 */
	private double calculaMediaGastoTodosPaises() {
		double mediaGasto = 0.0;

		for (Map.Entry<String, ArrayList<TAirport>> entry : this.airportsByCountry.entrySet()) {

			mediaGasto += this.expHealthReader.getUmbral(entry.getKey());
			
		}
		return mediaGasto / this.airportsByCountry.size();
	}

	/**
	 * M�todo que se encarga de dar un umbral espec�fico a cada aeropuerto de un pa�s.
	 * A los aeropuertos que apenas tienen tr�fico les baja el umbral.
	 */
	private void calculaUmbralPorAeropuerto() {
		for (Map.Entry<String, ArrayList<TAirport>> entry : this.airportsByCountry.entrySet()) {

			int totalVuelosPais = 0;
			
			for(TAirport aeropuerto: entry.getValue()) {
				totalVuelosPais += aeropuerto.getCalculatedDegree();
			}
			
			for(TAirport aeropuerto: entry.getValue()) {
				//Si el aeropuerto efectua menos del 30% de los vuelos del pa�s sufre una peque�a penalizaci�n del 10%
				if(((aeropuerto.getCalculatedDegree()*100) / totalVuelosPais) < 15) {
					loadedNodes.get(aeropuerto.getId()).setUmbral(loadedNodes.get(aeropuerto.getId()).getUmbral()*0.9);
				}
				if(((aeropuerto.getCalculatedDegree()*100) / totalVuelosPais) < 5) {
					loadedNodes.get(aeropuerto.getId()).setUmbral(loadedNodes.get(aeropuerto.getId()).getUmbral()*0.9);
				}
				System.out.println("Aeropuerto " + aeropuerto.getName() + " umbral " + loadedNodes.get(aeropuerto.getId()).getUmbral());
			}
		}
	}
	/**
	 * Obtiene el umbral de salud de un aeropuerto
	 * @param airport Aeropuerto
	 * @return umbral de salud
	 */
	private double calcExpenditureHealthUmbral(TAirport airport) {
		if(airport.getCountry().equalsIgnoreCase("Spain")) {
			System.out.println("Estas aqu� " + this.expHealthReader.getUmbral(airport.getCountry()));

		}
		return this.expHealthReader.getUmbral(airport.getCountry());
	}
}
