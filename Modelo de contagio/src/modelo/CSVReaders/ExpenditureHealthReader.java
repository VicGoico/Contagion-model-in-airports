package modelo.CSVReaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import modelo.metricas.tools.CSVFileProcessor;
import modelo.metricas.tools.CorrespondingCountry;

public class ExpenditureHealthReader implements ReaderConsumer  {
	public static double DEFAULTEXPENDITUREHEALTH = 0.0;
	private static int lineCounter = 0;
	private static boolean processing = true;
	private int tempCounter = 0;

	private HashMap<String, Double> countriesUmbral;

	public ExpenditureHealthReader(String fileName) throws IOException {
		this.countriesUmbral = new HashMap<>();
		new CSVFileProcessor(fileName, this);
	}
	
	public double getUmbral(String country) {
		if(!this.countriesUmbral.containsKey(country)) {
			if(CorrespondingCountry.map.containsKey(country)) {
				if(CorrespondingCountry.map.get(country) == CorrespondingCountry.DEFAULTVALUE)
					return DEFAULTEXPENDITUREHEALTH;
				return this.countriesUmbral.get(CorrespondingCountry.map.get(country));
			} else System.err.println("No se reconoce el pais: '" + country + "'");
		}
		return this.countriesUmbral.get(country);
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
					
					if(!this.countriesUmbral.containsKey(country))
						this.countriesUmbral.put(country, value);
					else
						this.countriesUmbral.put(country, this.countriesUmbral.get(country) + value);
					
					if(tempCounter > 4) {
						 // Una media de los 5 valores de los 5 años
						this.countriesUmbral.put(country, this.countriesUmbral.get(country) / 5);
						tempCounter = 0;
					} else {
						tempCounter++;
					}
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
}
