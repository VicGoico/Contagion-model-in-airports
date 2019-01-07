package modelo.CSVReaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import modelo.metricas.tools.CSVFileProcessor;
import modelo.metricas.tools.CorrespondingCountry;

public class PIBReader implements ReaderConsumer {
	public static double DEFAULPIB = 0.0;
	private static int lineCounter = 0;
	private static boolean processing = true;
	private int tempCounter = 0;
	private String tempCounty = "";
	private ExpenditureHealthReader expHealthReader;
	private HashMap<String, Double> countriesUmbral;

	public PIBReader(String fileName) throws IOException {
		this.countriesUmbral = new HashMap<>();
		this.expHealthReader = ExpenditureHealthReader.getInstance();
		new CSVFileProcessor(fileName, this);
	}

	public double getUmbral(String country) {
		if (!this.countriesUmbral.containsKey(country)) {
			if (CorrespondingCountry.map.containsKey(country)) {
				if (CorrespondingCountry.map.get(country) == CorrespondingCountry.DEFAULTVALUE)
					return DEFAULPIB;
				return this.countriesUmbral.get(CorrespondingCountry.map.get(country));
			} else
				System.err.println("No se reconoce el pais: '" + country + "'");
		}
		return this.countriesUmbral.get(country);
	}

	@Override
	public void accept(ArrayList<String> t) {

		if (lineCounter > 1) { // No uso las dos primeras lineas
			try {
				String serie = t.get(3);
				String country = t.get(1).replaceAll("\"", "");

				if (!country.equalsIgnoreCase("Total, all countries or areas")
						&& serie.equalsIgnoreCase("GDP per capita (US dollars)")) {
					if(country.equalsIgnoreCase("centinela")) {
						System.out.println("pepe");
					}
					if (!this.tempCounty.equalsIgnoreCase(country)) {
						double umbralAux = 0.0;
						if(this.countriesUmbral.containsKey(tempCounty)) {
							umbralAux = this.countriesUmbral.get(tempCounty);
						}
						this.countriesUmbral.put(tempCounty, umbralAux / (double)tempCounter);
						this.updateExpenditureHealthUmbral(tempCounty);
						this.tempCounter = 0;
						this.tempCounty = country;
						
					}
					// Integer year = Integer.parseInt(t.get(2));
					Double value = Double.parseDouble(t.get(4).replace("\"", "").replace(",", ""));
					//value = value + 2.0;
					if (!this.countriesUmbral.containsKey(country))
						this.countriesUmbral.put(country, value);
					else
						this.countriesUmbral.put(country, this.countriesUmbral.get(country) + value);
					tempCounter += 1;
					/*
					 * if(tempCounter >= 6) { // Una media de los 7 valores de los 7 años
					 * this.countriesUmbral.put(country, this.countriesUmbral.get(country) / 7);
					 * this.updateExpenditureHealthUmbral(country); tempCounter = 0; } else {
					 * tempCounter++; }
					 */
				}
			} catch (Exception e) {
				System.err.println("Ha ocurrido un error al procesar el PIB.");
				System.err.println(t);
				e.printStackTrace();
				processing = false;
			}
		}
		lineCounter++;
	}

	private void updateExpenditureHealthUmbral(String country) {
		if (this.expHealthReader.countryIncluded(country)) {
			// Cuanto gasta al año en salud
			double umbral = this.expHealthReader.getUmbral(country) * (this.countriesUmbral.get(country)/100);

			this.expHealthReader.setUmbral(country, umbral);
		}
	}

	@Override
	public boolean processing() {
		return processing;
	}
}
