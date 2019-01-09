package modelo.red;

import java.util.HashMap;

import modelo.TAirport;

// Clase que contiene la informacion necesaria para un nodo
public class Nodo {
	// Atributos
	private TAirport tAirport;
	private double umbral;
	/*
	 * Se guarda un HashMap<Id aeropuerto al que vuela, cuantas veces vuela>
	 * No hemos hecho un vector porque hay 12.000 aeropuertos y creemos que 
	 * es mejor tener un hashMap de 3 elementos que un array de 12.000 con la
	 * mayoría de posiciones a null de cada aeropuerto
	 */
	private HashMap<Integer, Integer> aeropuertosComunicados; 
	// <IDAeropuerto, Veces que vuela a ese aeropuerto concreto> 
	private int aeropuetosComunicadosInfectados = 0;
	private boolean infectado = false;
	private int indegree;
	private int outdegree;
	private int degree;

	// Constructora
	public Nodo(TAirport info, double umbral) {
		this.tAirport = info;
		this.umbral = umbral;
		aeropuertosComunicados = new HashMap<Integer, Integer>();
	}

	// Getters
	public Integer getPesoAeropComunicado(int aeropuerto) {
		return aeropuertosComunicados.get(aeropuerto);
	}

	public HashMap<Integer, Integer> getAeropuertosALosQueVuela() {
		return this.aeropuertosComunicados;
	}

	public Double getUmbral() {
		return this.umbral;
	}

	public TAirport getAirportInfo() {
		return this.tAirport;
	}

	public Integer getId() {
		return this.tAirport.getId();
	}

	public Integer getAeropuetosComunicadosInfectados() {
		return aeropuetosComunicadosInfectados;
	}
	
	public boolean isInfectado() {
		return infectado;
	}
	
	public int getIndegree() {
		return indegree;
	}
	
	public int getOutdegree() {
		return outdegree;
	}
	
	public int getDegree() {
		return degree;
	}
	
	// Setters
	public void setInfoAeropuertos(TAirport info) {
		this.tAirport = info;
	}

	public void setAeropuetosComunicadosInfectados(Integer aeropuetosComunicadosInfectados) {
		this.aeropuetosComunicadosInfectados = aeropuetosComunicadosInfectados;
	}

	public void setInfectado(boolean infectado) {
		this.infectado = infectado;
	}

	public void setIndegree(int indegree) {
		this.indegree = indegree;
	}

	public void setOutdegree(int outdegree) {
		this.outdegree = outdegree;
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

	public void setUmbral(double umbral) {
		this.umbral = umbral;
	}
	
	// Añade al atributo HashMap, de aeropuertos comunicados, un nuevo aeropuerto con su id y su peso
	public void addAeropuertoComunicado(int aeropuerto, int peso) {
		this.aeropuertosComunicados.put(aeropuerto, peso);
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
