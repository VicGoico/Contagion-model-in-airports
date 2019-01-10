package modelo.CSVReaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.Main;
import modelo.TAirport;
import modelo.metricas.tools.CSVFileProcessor;
import modelo.metricas.tools.CorrespondingCountry;
import modelo.red.Nodo;

public class AirportNodesReader implements ReaderConsumer {
	private static int lineCounter = 0;
	private static boolean processing = true;
	private HashMap<Integer, TAirport> airportsById;
	private HashMap<String, ArrayList<TAirport>> airportsByCountry;
	private HashMap<Integer, Nodo> loadedNodes;
	private ExpenditureHealthReader expHealthReader;
	private double maxGasto = 0.0;
	private double totalGasto = 0.0;
	
	/**
	 * Constructora
	 * @param fileName Nombre del fichero donde se encuentran los nodos
	 * @param nodes Nodos
	 * @throws IOException
	 */
	public AirportNodesReader(String fileName, HashMap<Integer, Nodo> nodes) throws IOException {
		if(fileName == null) fileName = Main.AIRPORT_NODES_FILENAME;
		this.loadedNodes = nodes;
		this.airportsById = new HashMap<>();
		this.airportsByCountry = new HashMap<>();
		this.expHealthReader = ExpenditureHealthReader.getInstance();

		new CSVFileProcessor(fileName, this).process();
		
		calculaUmbralPorPaises();
		
		calculaUmbralPorAeropuerto();
		
		System.out.println("pepe");
	}

	

	/**
	 * Mediante un id obtiene un aeropuerto
	 * @param id Identificador el aeropuerto que se quiere obtener
	 * @return aeropuerto
	 */
	public TAirport getAirportById(int id) {
		return this.airportsById.get(id);
	}

	/**
	 * Obtiene todos los aeropuertos de un país
	 * @param country País del que se quieren obtener los aeropuertos
	 * @return Lista de los aeropuertos del país
	 */
	public ArrayList<TAirport> getAirportsByCountry(String country) {
		if (!this.airportsByCountry.containsKey(country)) {
			if(CorrespondingCountry.map.containsKey(country)) {
				if(CorrespondingCountry.map.get(country) == CorrespondingCountry.DEFAULTVALUE)
					return null;
				return this.airportsByCountry.get(CorrespondingCountry.map.get(country));
			}
			else System.out.println("No se han podido obtener aeropuertos del pais: '" + country + "'");
		}
		return this.airportsByCountry.get(country);
	}
	
	/**
	 * Obtiene el umbral de salud de un aeropuerto
	 * @param airport Aeropuerto
	 * @return umbral de salud
	 */
	private double calcExpenditureHealthUmbral(TAirport airport) {
		if(airport.getCountry().equalsIgnoreCase("Spain")) {
			System.out.println("Estas aquí " + this.expHealthReader.getUmbral(airport.getCountry()));

		}
		return this.expHealthReader.getUmbral(airport.getCountry());
	}

	/**
	 * {@inheritDoc}
	 * Se encarga de obtener de la información de los aeropuertos
	 */
	@Override
	public void accept(ArrayList<String> t) {
		if (!processing)
			return;

		if (lineCounter != 0) { // Me salto la cabecera
			String airportName = "";
			Integer airportId = 0;
			String countryName = "";
			TAirport airport = null;

			try {
				airportName = t.get(3);
				airportId = Integer.parseInt(t.get(0));
				countryName = t.get(5);
				
				airport = new TAirport(airportId, airportName, t.get(4), countryName, t.get(6), t.get(7),
						Double.parseDouble(t.get(8)), Double.parseDouble(t.get(9)), Integer.parseInt(t.get(10)),
						Integer.parseInt(t.get(11)), Integer.parseInt(t.get(12)), Integer.parseInt(t.get(13)));

				this.airportsById.put(airportId, airport);
				this.loadedNodes.put(airportId, new Nodo(airport, 0.0));
				if(this.calcExpenditureHealthUmbral(airport) > this.maxGasto){
					this.maxGasto = this.calcExpenditureHealthUmbral(airport);
				}
				if (!airportsByCountry.containsKey(countryName))
					airportsByCountry.put(countryName, new ArrayList<>());
				airportsByCountry.get(countryName).add(airport);
			} catch (Exception e) {
				System.err.println("No se ha podido leer el aeropuerto ( o no se ha podido almacenar ), compruebe el CSV que no este corrupto. (Cadenas no pueden contener \" y si contienen , se encierran entre comillas dobles");
				e.printStackTrace();
				System.out.println(t);
				processing = false;
			}

		}
		lineCounter++;
	}
	
	@Override
	public void atEndProcessing() {
		if (processing) {
			// Aquellos aeropuertos que tienen un nombre distinto en los ficheros
			this.airportsByCountry.put("Congo", new ArrayList<>());
			this.airportsByCountry.get("Congo").addAll(this.airportsByCountry.get("Congo (Brazzaville)"));
			this.airportsByCountry.get("Congo").addAll(this.airportsByCountry.get("Congo (Kinshasa)"));
		}
	}

	@Override
	public boolean processing() {
		return processing;
	}
	
	/**
	 * Método que da a cada aeropuerto del mismo país el mismo umbral.
	 * Este umbral común a todos los aeropuertos del mismo país se basa
	 * en cuanto gasta ese país por habitante al año en sanidad.
	 * En función de si gasta más de la media o menos se establece un umbral
	 */
	private void calculaUmbralPorPaises() {
		double mediaPaises = calculaMediaGastoTodosPaises();
		
		for (Map.Entry<Integer, Nodo> aeropuerto : this.loadedNodes.entrySet()) {
			
			double gastoPais = calcExpenditureHealthUmbral(aeropuerto.getValue().getAirportInfo());

			if(gastoPais > mediaPaises) {
				if(gastoPais < (mediaPaises * 1.5)) {
					//Si el país gasta en sanidad mas que la media de países pero menos que la media
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
	 * Método para calcular la media de gasto de todos los países en sanidad
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
	 * Método que se encarga de dar un umbral específico a cada aeropuerto de un país.
	 * A los aeropuertos que apenas tienen tráfico les baja el umbral.
	 */
	private void calculaUmbralPorAeropuerto() {
		for (Map.Entry<String, ArrayList<TAirport>> entry : this.airportsByCountry.entrySet()) {

			int totalVuelosPais = 0;
			
			for(TAirport aeropuerto: entry.getValue()) {
				totalVuelosPais += aeropuerto.getCalculatedDegree();
			}
			
			for(TAirport aeropuerto: entry.getValue()) {
				//Si el aeropuerto efectua menos del 30% de los vuelos del país sufre una pequeña penalización del 10%
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
}
