package modelo;


public class Nodo {
	private int id;
	private int degree;
	private TAirport tAirport;
	private Double umbral;
	
	public Nodo(int i, int g, Double umbral, TAirport info) {
		this.id = i;
		this.degree = g;
		this.tAirport = info;
		this.umbral = umbral;
	}
	
	public int getValue() {
		return id;
	}
	public void setValue(int value) {
		this.id = value;
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
				o instanceof Nodo && ((Nodo) o).id == this.id && ((Nodo) o).degree == this.degree
				&& ((Nodo) o).tAirport.getIata() == this.tAirport.getIata()
				&& ((Nodo) o).tAirport.getName() == this.tAirport.getName();
	}
	
	@Override
	public String toString() {
		return this.tAirport.getName() + " " + this.tAirport.getIata() + " idNodo " + 
	this.id + " : " + " grado " + this.degree;
	}

	public Double getUmbral() {
		return umbral;
	}

	public void setUmbral(Double umbral) {
		this.umbral = umbral;
	}
	public TAirport getInfo() {
		return this.tAirport;
	}
	public void setInfoAeropuertos(TAirport info) {
		this.tAirport = info;
	}
}
