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
	private String tempCountry = "";
	private ExpenditureHealthReader expHealthReader;
	private HashMap<String, Double> countriesUmbral;

	public PIBReader(String fileName) throws IOException {
		this.countriesUmbral = new HashMap<>();
		this.countriesUmbral.put("", 0.0);
		this.expHealthReader = ExpenditureHealthReader.getInstance();
		new CSVFileProcessor(fileName, this).process();
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
		if (lineCounter > 2) { // No uso las dos primeras lineas
			try {
				String serie = t.get(3);
				String country = t.get(1).replaceAll("\"", "");

				if (serie.equalsIgnoreCase("GDP per capita (US dollars)")) {
					// Integer year = Integer.parseInt(t.get(2));
					Double value = Double.parseDouble(t.get(4).replace("\"", "").replace(",", ""));
					
					if (this.tempCountry.equalsIgnoreCase(country)) {
						this.countriesUmbral.put(country, this.countriesUmbral.get(country) + value);
					} else {
						this.countriesUmbral.put(this.tempCountry, this.countriesUmbral.get(this.tempCountry) / (double) this.tempCounter);
						this.updateExpenditureHealthUmbral(tempCountry);
						
						// El Siguiente pais
						this.tempCounter = 0;
						this.tempCountry = country;
						this.countriesUmbral.put(country, value);						
					}
					
					tempCounter++;
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
	
	@Override
	public void atEndProcessing() {
		this.countriesUmbral.put(this.tempCountry, this.countriesUmbral.get(this.tempCountry) / (double) this.tempCounter); // Actualizo el ultimo pais
		this.updateExpenditureHealthUmbral(tempCountry);
		this.countriesUmbral.remove("");
	};
}
