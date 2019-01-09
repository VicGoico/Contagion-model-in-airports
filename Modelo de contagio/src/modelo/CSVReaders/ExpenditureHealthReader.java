package modelo.CSVReaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import modelo.Main;
import modelo.metricas.tools.CSVFileProcessor;
import modelo.metricas.tools.CorrespondingCountry;

public class ExpenditureHealthReader implements ReaderConsumer {
	public static double DEFAULTEXPENDITUREHEALTH = 0.0;
	private static int lineCounter = 0;
	private static ExpenditureHealthReader instance;
	private static boolean processing = true;
	private int tempCounter = 0;
	private String tempCountry = "";

	private HashMap<String, Double> countriesUmbral;

	/**
	 * Constructor
	 * @param fileName Nombre del fichero donde se encuentra el gasto en sanidad de cada país
	 * @throws IOException
	 */
	public ExpenditureHealthReader(String fileName) throws IOException {
		if(fileName == null) fileName = Main.EXPENDITUREHEALTH_FILENAME;
		this.countriesUmbral = new HashMap<>();
		this.countriesUmbral.put("", 0.0);
		new CSVFileProcessor(fileName, this).process();
		ExpenditureHealthReader.instance = this;
	}

	/**
	 * Método para obtener la instancia de la clase
	 * @return instancia
	 * @throws IOException
	 */
	public static ExpenditureHealthReader getInstance() throws IOException {
		if (ExpenditureHealthReader.instance == null)
			ExpenditureHealthReader.instance = new ExpenditureHealthReader(Main.AIRPORT_NODES_FILENAME);
		return ExpenditureHealthReader.instance;
	}

	/**
	 * Método para obtener el umbral de un país
	 * @param country País del que se quiere obtener el umbral
	 * @return Umbral del país
	 */
	public double getUmbral(String country) {
		if (!this.countriesUmbral.containsKey(country)) {
			if (CorrespondingCountry.map.containsKey(country)) {
				if (CorrespondingCountry.map.get(country) == CorrespondingCountry.DEFAULTVALUE)
					return DEFAULTEXPENDITUREHEALTH;
				return this.countriesUmbral.get(CorrespondingCountry.map.get(country));
			} else
				System.err.println("No se reconoce el pais: '" + country + "'");
		}
		return this.countriesUmbral.get(country);
	}

	/**
	 * Comprueba si existe el país
	 * @param country País
	 * @return Verdadero o falso
	 */
	public boolean countryIncluded(String country) {
		return this.countriesUmbral.containsKey(country);
	}

	/**
	 * Método para establecer el umbral de un país
	 * @param country País del que se quiere establecer el umbral
	 * @param value Nuevo umbral
	 */
	public void setUmbral(String country, double value) {
		this.countriesUmbral.put(country, value);
	}

	@Override
	public void accept(ArrayList<String> t) {
		if (lineCounter > 1) { // No uso las dos primeras lineas
			try {
				String serie = t.get(3);
				if (serie.equalsIgnoreCase("Current health expenditure (% of GDP)")) {
					String country = t.get(1);
					// Integer year = Integer.parseInt(t.get(2));
					Double value = Double.parseDouble(t.get(4));
					
					if (this.tempCountry.equalsIgnoreCase(country)) {	
						this.countriesUmbral.put(country, this.countriesUmbral.get(country) + value);
					} else {
						this.countriesUmbral.put(this.tempCountry, this.countriesUmbral.get(this.tempCountry) / (double) this.tempCounter);
						// El Siguiente pais
						this.tempCounter = 0;
						this.tempCountry = country;
						this.countriesUmbral.put(country, value);
					}
					
					tempCounter++;
				}
			} catch (Exception e) {
				System.err.println("Ha ocurrido un error al procesar el gasto en salud.");
				processing = false;
			}
		}
		lineCounter++;
	}

	@Override
	public boolean processing() {
		return processing;
	}
	
	@Override
	public void atEndProcessing() {
		this.countriesUmbral.put(this.tempCountry, this.countriesUmbral.get(this.tempCountry) / (double) this.tempCounter); // Actualizo el ultimo pais
		this.countriesUmbral.remove("");
	};
}
