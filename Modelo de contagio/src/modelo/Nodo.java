package modelo;


public class Nodo {
	private int value;
	private int degree;
	private String airportName;
	private String airportCity;
	private String airportCountry;
	private String airportIata;
	private Double umbral;
	
	public Nodo(int i, int g, String airportName, String airportCity, String airportCountry,
			String airportIata, Double umbral) {
		this.value = i;
		this.degree = g;
		this.airportName = airportName;
		this.airportCity = airportCity;
		this.airportCountry = airportCountry;
		this.airportIata = airportIata;
		this.umbral = umbral;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public void incrementDegree() {
		this.degree += 1;
	}
	
	@Override
	public boolean equals(Object o) {
		return o != null &&
				o instanceof Nodo && ((Nodo) o).value == this.value && ((Nodo) o).degree == this.degree
				&& ((Nodo) o).airportIata == this.airportIata
				&& ((Nodo) o).airportName == this.airportName;
	}
	
	@Override
	public String toString() {
		return this.airportName + " " + this.airportIata + " idNodo " + 
	this.value + " : " + " grado " + this.degree;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getAirportCity() {
		return airportCity;
	}

	public void setAirportCity(String airportCity) {
		this.airportCity = airportCity;
	}

	public String getAirportCountry() {
		return airportCountry;
	}

	public void setAirportCountry(String airportCountry) {
		this.airportCountry = airportCountry;
	}

	public String getAirportIata() {
		return airportIata;
	}

	public void setAirportIata(String airportIata) {
		this.airportIata = airportIata;
	}

	public Double getUmbral() {
		return umbral;
	}

	public void setUmbral(Double umbral) {
		this.umbral = umbral;
	}
}
