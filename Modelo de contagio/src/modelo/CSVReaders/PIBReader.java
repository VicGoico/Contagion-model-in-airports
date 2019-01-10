package modelo.CSVReaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import modelo.Main;
import modelo.metricas.tools.CSVFileProcessor;
import modelo.metricas.tools.CorrespondingCountry;

public class PIBReader implements ReaderConsumer {
	public static double DEFAULPIB = 0.0;
	private static int lineCounter = 0;
	private static boolean processing = true;
	private static PIBReader instance;
	private int tempCounter = 0;
	private int calcExpHealthCountryCounter = 0;
	private double sumExpHealthCountry = 0.0;
	private String tempCountry = "";
	private ExpenditureHealthReader expHealthReader;
	private HashMap<String, Double> countriesUmbral;
	private HashMap<String, Double> countriesExpHealthUmbral;

	/**
	 * Constructor
	 * 
	 * @param fileName Nombre del fichero donde se encuentran los datos del PIB de
	 *                 cada país
	 * @throws IOException
	 */
	public PIBReader(String fileName) throws IOException {
		if (fileName == null)
			fileName = Main.PIB_FILENAME;
		this.countriesUmbral = new HashMap<>();
		this.countriesExpHealthUmbral = new HashMap<>();
		this.countriesUmbral.put("", 0.0);
		this.expHealthReader = ExpenditureHealthReader.getInstance();
		new CSVFileProcessor(fileName, this).process();
		PIBReader.instance = this;
	}

	/**
	 * Método para obtener la instancia de la clase
	 * 
	 * @return instancia
	 * @throws IOException
	 */
	public static PIBReader getInstance() throws IOException {
		if (PIBReader.instance == null)
			PIBReader.instance = new PIBReader(Main.PIB_FILENAME);
		return PIBReader.instance;
	}

	/**
	 * Método para obtener el umbral de PIB de un país
	 * 
	 * @param country País
	 * @return Umbral PIB del País
	 */
	public double getPIBUmbral(String country) {
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

	/**
	 * Método para obtener el umbral de gasto en salud respecto del PIB de un país
	 * 
	 * @param country País
	 * @return Gasto en salud respecto del PIB del País
	 */
	public double getExpHealthUmbral(String country) {
		if (!this.countriesExpHealthUmbral.containsKey(country)) {
			if (CorrespondingCountry.map.containsKey(country)) {
				if (CorrespondingCountry.map.get(country) == CorrespondingCountry.DEFAULTVALUE)
					return ExpenditureHealthReader.DEFAULTEXPENDITUREHEALTH * DEFAULPIB / 100;
				return this.countriesExpHealthUmbral.get(CorrespondingCountry.map.get(country));
			} else {
				System.out.println("No se reconoce el pais: '" + country + "', se le asigna el gasto en salud por defecto en USD");
				return ExpenditureHealthReader.DEFAULTEXPENDITUREHEALTH * DEFAULPIB / 100;
			}
		}
		return this.countriesExpHealthUmbral.get(country);
	}

	@Override
	public void accept(ArrayList<String> t) {
		if (lineCounter > 29) { // No uso las 29 primeras lineas
			try {
				String serie = t.get(3);
				String country = t.get(1).replaceAll("\"", "");

				if (serie.equalsIgnoreCase("GDP per capita (US dollars)")) {
					// Integer year = Integer.parseInt(t.get(2));
					Double value = Double.parseDouble(t.get(4).replace("\"", "").replace(",", ""));

					if (this.tempCountry.equalsIgnoreCase(country)) {
						this.countriesUmbral.put(country, this.countriesUmbral.get(country) + value);
					} else {
						this.countriesUmbral.put(this.tempCountry,
								this.countriesUmbral.get(this.tempCountry) / (double) this.tempCounter);
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

	/**
	 * Método que se encarga de actualizar el umbral
	 * 
	 * @param country país del que se quiere actualizar el umbral
	 */
	private void updateExpenditureHealthUmbral(String country) {
		/*
		 * Cuanto gasta al año en salud: Obtenemos cuanto gasta al año el país por
		 * habitante en salud multiplicando el PIB por habitante y el porcentaje del PIB
		 * que destinan a salud
		 */
		double umbral = this.expHealthReader.getUmbral(country) == 0 ? 0.0 : this.expHealthReader.getUmbral(country) * (this.countriesUmbral.get(country) / 100);
		this.countriesExpHealthUmbral.put(country, umbral);
		this.calcExpHealthCountryCounter++;
		this.sumExpHealthCountry += umbral;
	}

	/**
	 * Devuelve el gasto medio en salud respecto al PIB en Dolares NOTA: Si no se ha
	 * terminado de procesar el lector, el retorno no sera valido.
	 * 
	 * @return Gasto medio en USD
	 */
	public double getAvgHealthExpenditure() {
		return sumExpHealthCountry / calcExpHealthCountryCounter;
	}

	@Override
	public boolean processing() {
		return processing;
	}

	@Override
	public void atEndProcessing() {
		this.countriesUmbral.put(this.tempCountry,
				this.countriesUmbral.get(this.tempCountry) / (double) this.tempCounter); // Actualizo el ultimo pais
		this.updateExpenditureHealthUmbral(tempCountry);
		this.countriesUmbral.remove("");
		System.out.println(
				"Suma de gastos en salud: " + sumExpHealthCountry + " de cuantos: " + this.calcExpHealthCountryCounter);
	};
}
