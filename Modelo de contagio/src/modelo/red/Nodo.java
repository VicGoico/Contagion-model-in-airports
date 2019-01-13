package modelo.red;

import java.util.HashMap;

import modelo.TAirport;

public class Nodo {
	private TAirport tAirport;
	private double umbral;
	private HashMap<Integer, Integer> aeropuertosComunicados; // <IDAeropuerto, Peso>
	private int numAeropuetosComunicadosInfectados = 0;
	private boolean infectado = false;
	private int indegree;
	private int outdegree;
	private int degree;

	/**
	 * Constructor
	 * 
	 * @param info   contiene la información del aeropuerto
	 * @param umbral Umbral que va a tener el aeropuerto
	 */
	public Nodo(TAirport info, double umbral) {
		this.tAirport = info;
		this.umbral = umbral;
		this.aeropuertosComunicados = new HashMap<Integer, Integer>();
	}

	/**
	 * Añade a los aeropuertos a los que viaja ese aeropuerto un nuevo aeropuerto
	 * 
	 * @param aeropuerto Identificador del aeropuerto al que viaja
	 * @param peso       Cuantas veces viaja al aeropuerto
	 */
	public void addAeropuertoComunicado(int aeropuerto, int peso) {
		this.aeropuertosComunicados.put(aeropuerto,
				(this.aeropuertosComunicados.containsKey(aeropuerto) ? this.aeropuertosComunicados.get(aeropuerto) : 0)
						+ peso);
	}

	/**
	 * Método para obtener cuantas veces viaja a un aeropuerto
	 * 
	 * @param aeropuerto Aeropuerto al que se quiere saber cuantas veces se viaja
	 *                   desde este
	 * @return Cuántas veces se viaja
	 */
	public Integer getPesoAeropComunicado(int aeropuerto) {
		return this.aeropuertosComunicados.get(aeropuerto);
	}

	/**
	 * Método para obtener la información de todos los aeropuertos a los que vuela
	 * este Nodo
	 * 
	 * @return Todos los aeropuertos a los que vuela y cuantas veces lo hace
	 */
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
		return numAeropuetosComunicadosInfectados;
	}

	public void setAeropuetosComunicadosInfectados(Integer aeropuetosComunicadosInfectados) {
		this.numAeropuetosComunicadosInfectados = aeropuetosComunicadosInfectados;
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

	/**
	 * {@inheritDoc} Método para comprobar si dos nodos son iguales Serán iguales si
	 * tienen el mismo identificador y el mismo código iata, grado y nombre
	 */
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
