package modelo;


public class Nodo {
	private int value;
	private int degree;
	private InfoAeropuertos infoAeropuerto;
	private Double umbral;
	
	public Nodo(int i, int g, Double umbral, InfoAeropuertos info) {
		this.value = i;
		this.degree = g;
		this.infoAeropuerto = info;
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
				&& ((Nodo) o).infoAeropuerto.getIata() == this.infoAeropuerto.getIata()
				&& ((Nodo) o).infoAeropuerto.getName() == this.infoAeropuerto.getName();
	}
	
	@Override
	public String toString() {
		return this.infoAeropuerto.getName() + " " + this.infoAeropuerto.getIata() + " idNodo " + 
	this.value + " : " + " grado " + this.degree;
	}

	public Double getUmbral() {
		return umbral;
	}

	public void setUmbral(Double umbral) {
		this.umbral = umbral;
	}
	public InfoAeropuertos getInfo() {
		return this.infoAeropuerto;
	}
	public void setInfoAeropuertos(InfoAeropuertos info) {
		this.infoAeropuerto = info;
	}
}
