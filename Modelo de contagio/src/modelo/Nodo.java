package modelo;

import java.util.HashMap;

public class Nodo {
	private TAirport tAirport;
	//PARA LA INFECCIÓN
	private double umbral;
	private HashMap<Integer, Integer> aeropuertos_comunicados; //<IDAeropuerto, Peso>
	private int aeropuetosComunicadosInfectados = 0;
	private boolean infectado = false;
	
	
	public Nodo(TAirport info, double umbral) {
		this.tAirport = info;
		this.umbral = umbral;
		aeropuertos_comunicados = new HashMap<Integer, Integer>();
	}
	
	public void addAeropuertoComunicado(Integer aeropuerto, Integer peso) {
		this.aeropuertos_comunicados.put(aeropuerto, peso);
	}
	
	public Integer getPesoAeropuertoComunicado(Integer aeropuerto) {
		return aeropuertos_comunicados.get(aeropuerto);
	}
	
	public HashMap<Integer, Integer> getAeropuertosALosQueVuela(){
		return this.aeropuertos_comunicados;
	}
	
	
	@Override
	public boolean equals(Object o) {
		return o != null &&
				o instanceof Nodo && ((Nodo) o).tAirport.getId() == this.tAirport.getId() 
				&& ((Nodo) o).tAirport.getDegree() == this.tAirport.getDegree()
				&& ((Nodo) o).tAirport.getIata() == this.tAirport.getIata()
				&& ((Nodo) o).tAirport.getName() == this.tAirport.getName();
	}
	
	@Override
	public String toString() {
		return this.tAirport.getName() + " " + this.tAirport.getIata() + " idNodo " + 
	this.tAirport.getId() + " : " + " grado " + this.tAirport.getDegree();
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

	public int getOutdegree() {
		return outdegree;
	}

	public void setOutdegree(int outdegree) {
		this.outdegree = outdegree;
	}

	public int getIndegree() {
		return indegree;
	}

	public void setIndegree(int indegree) {
		this.indegree = indegree;
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
}
