package modelo.red;

import java.util.HashMap;

import modelo.TAirport;

public class Nodo {
	private TAirport tAirport;
	private double umbral;
	private HashMap<Integer, Integer> aeropuertosComunicados; // <IDAeropuerto, Peso>
	private int aeropuetosComunicadosInfectados = 0;
	private boolean infectado = false;
	private int indegree;
	private int outdegree;
	private int degree;

	public Nodo(TAirport info, double umbral) {
		this.tAirport = info;
		this.umbral = umbral;
		aeropuertosComunicados = new HashMap<Integer, Integer>();
	}

	public void addAeropuertoComunicado(int aeropuerto, int peso) {
		this.aeropuertosComunicados.put(aeropuerto, peso);
	}

	public Integer getPesoAeropComunicado(int aeropuerto) {
		return aeropuertosComunicados.get(aeropuerto);
	}

	public HashMap<Integer, Integer> getAeropuertosALosQueVuela() {
		return this.aeropuertosComunicados;
	}

	public Double getUmbral() {
		return umbral;
	}

	public void setUmbral(double umbral) {
		this.umbral = umbral;
	}

	public TAirport getAirportInfo() {
		return this.tAirport;
	}

	public Integer getId() {
		return this.tAirport.getId();
	}

	public void setInfoAeropuertos(TAirport info) {
		this.tAirport = info;
	}

	public Integer getAeropuetosComunicadosInfectados() {
		return aeropuetosComunicadosInfectados;
	}

	public void setAeropuetosComunicadosInfectados(Integer aeropuetosComunicadosInfectados) {
		this.aeropuetosComunicadosInfectados = aeropuetosComunicadosInfectados;
	}

	public boolean isInfectado() {
		return infectado;
	}

	public void setInfectado(boolean infectado) {
		this.infectado = infectado;
	}

	public int getIndegree() {
		return indegree;
	}

	public void setIndegree(int indegree) {
		this.indegree = indegree;
	}

	public int getOutdegree() {
		return outdegree;
	}

	public void setOutdegree(int outdegree) {
		this.outdegree = outdegree;
	}

	public int getDegree() {
		return degree;
	}

	public void setDegree(int degree) {
		this.degree = degree;
	}

	public void incrementDegree() {
		this.degree++;
	}

	public void incrementInDegree() {
		this.indegree++;
	}

	public void incrementOutDegree() {
		this.outdegree++;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof Nodo && ((Nodo) o).tAirport.getId() == this.tAirport.getId()
				&& ((Nodo) o).getDegree() == this.getDegree()
				&& ((Nodo) o).tAirport.getIata() == this.tAirport.getIata()
				&& ((Nodo) o).tAirport.getName() == this.tAirport.getName();
	}

	@Override
	public String toString() {
		return this.tAirport.getName() + " " + this.tAirport.getIata() + " idNodo " + this.tAirport.getId() + " : "
				+ " grado " + this.getDegree();
	}
}
