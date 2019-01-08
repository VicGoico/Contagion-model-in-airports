package modelo.red;

import java.util.HashMap;

import modelo.TAirport;

public class Nodo {
	private TAirport tAirport;
	private double umbral;
	private HashMap<Integer, Integer> aeropuertosComunicados; // <IDAeropuerto, Peso>
	private int aeropuetosComunicadosInfectados = 0;
	private boolean infectado = false;

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

	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof Nodo && ((Nodo) o).tAirport.getId() == this.tAirport.getId()
				&& ((Nodo) o).tAirport.getDegree() == this.tAirport.getDegree()
				&& ((Nodo) o).tAirport.getIata() == this.tAirport.getIata()
				&& ((Nodo) o).tAirport.getName() == this.tAirport.getName();
	}

	@Override
	public String toString() {
		return this.tAirport.getName() + " " + this.tAirport.getIata() + " idNodo " + this.tAirport.getId() + " : "
				+ " grado " + this.tAirport.getDegree();
	}
}
