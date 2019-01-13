package umbrales;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import modelo.TAirport;
import modelo.CSVReaders.AirportNodesReader;
import modelo.CSVReaders.PIBReader;
import modelo.red.Nodo;
import modelo.red.Red;

/**
 * 
 * En esta clase
 *
 */
public class UmbralPIBSalud {
	private HashMap<Integer, Nodo> loadedNodes;
	private PIBReader PIBReader;
	private HashMap<String, ArrayList<TAirport>> airportsByCountry;

	public UmbralPIBSalud(Red red) throws IOException {
		this.PIBReader = modelo.CSVReaders.PIBReader.getInstance();
		this.loadedNodes = red.getNodos();
		this.airportsByCountry = AirportNodesReader.getInstance().getAirportsByCountry();

		calculaUmbralPorPaises();
		calculaUmbralPorAeropuerto();
	}

	/**
	 * M�todo que da a cada aeropuerto del mismo pa�s el mismo umbral. Este umbral
	 * com�n a todos los aeropuertos del mismo pa�s se basa en cuanto gasta ese pa�s
	 * por habitante al a�o en sanidad. En funci�n de si gasta m�s de la media o
	 * menos se establece un umbral
	 */
	private void calculaUmbralPorPaises() {
		double mediaPaises = this.PIBReader.getAvgHealthExpenditure();

		for (Map.Entry<Integer, Nodo> aeropuerto : this.loadedNodes.entrySet()) {
			double gastoPais = this.PIBReader.getExpHealthUmbral(aeropuerto.getValue().getAirportInfo().getCountry());

			if (gastoPais > mediaPaises) {
				if (gastoPais < (mediaPaises * 1.25)) {
					// Si el pa�s gasta en sanidad mas que la media de pa�ses pero menos que la
					// media
					aeropuerto.getValue().setUmbral(0.6);
				} else if (gastoPais < (mediaPaises * 2)) {
					aeropuerto.getValue().setUmbral(0.75);
				} else if (gastoPais < (mediaPaises * 3)){
					aeropuerto.getValue().setUmbral(0.8);
				}
				else {
					aeropuerto.getValue().setUmbral(0.9);
				}
			} else {
				if (gastoPais > (mediaPaises * 0.75)) {
					aeropuerto.getValue().setUmbral(0.4);
				} else if (gastoPais > (mediaPaises * 0.5)) {
					aeropuerto.getValue().setUmbral(0.25);
				} else if (gastoPais > (mediaPaises * 0.25)) {
					aeropuerto.getValue().setUmbral(0.15);
				}
				else {
					aeropuerto.getValue().setUmbral(((gastoPais * 50) / mediaPaises) / 100);
				}
			}

		}
	}

	/**
	 * M�todo que se encarga de dar un umbral espec�fico a cada aeropuerto de un
	 * pa�s. A los aeropuertos que apenas tienen tr�fico les baja el umbral.
	 */
	private void calculaUmbralPorAeropuerto() {
		for (Map.Entry<String, ArrayList<TAirport>> entry : this.airportsByCountry.entrySet()) {

			int totalVuelosPais = 0;

			// Obtengo el total de vuelos de los aeropuertos del pais actual
			for (TAirport aeropuerto : entry.getValue()) {
				totalVuelosPais += aeropuerto.getCalculatedDegree();
			}

			for (TAirport aeropuerto : entry.getValue()) {
				// Si el aeropuerto efectua menos del 10% de los vuelos del pa�s sufre una
				// peque�a penalizaci�n del 10%, si tiene menos del 5% de los vuelos, 
				//la penaizaci�n es del 15%
				if (((aeropuerto.getCalculatedDegree() * 100) / totalVuelosPais) < 5) {
					loadedNodes.get(aeropuerto.getId())
							.setUmbral(loadedNodes.get(aeropuerto.getId()).getUmbral() * 0.85);
				}
				else if (((aeropuerto.getCalculatedDegree() * 100) / totalVuelosPais) < 10) {
					loadedNodes.get(aeropuerto.getId())
							.setUmbral(loadedNodes.get(aeropuerto.getId()).getUmbral() * 0.9);
				}
			}
		}
	}
}
