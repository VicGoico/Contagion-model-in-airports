package modelo.CSVReaders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

	public AirportNodesReader(String fileName, HashMap<Integer, Nodo> nodes) throws IOException {
		this.loadedNodes = nodes;
		this.airportsById = new HashMap<>();
		this.airportsByCountry = new HashMap<>();
		this.expHealthReader = ExpenditureHealthReader.getInstance();

		new CSVFileProcessor(fileName, this).process();
	}

	public TAirport getAirportById(int id) {
		return this.airportsById.get(id);
	}

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
	
	private double calcExpenditureHealthUmbral(TAirport airport) {
		return this.expHealthReader.getUmbral(airport.getCountry());
	}

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
				this.loadedNodes.put(airportId, new Nodo(airport, this.calcExpenditureHealthUmbral(airport)));
				
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
}
