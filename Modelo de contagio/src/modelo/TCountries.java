package modelo;

import java.util.ArrayList;

public class TCountries {

	private double maxDegree;
	private ArrayList<TAirport> list;
	
	/**
	 * Constructor
	 */
	public TCountries(){
		this.setMaxDegree(0.0);
		this.setList(new ArrayList<TAirport>());
	}

	/**
	 * Obtiene el grado máximo de los aeropuertos que tiene un país
	 * @return grado máximo
	 */
	public double getMaxDegree() {
		return this.maxDegree;
	}
	
	/**
	 * Estable el grado máximo de los aeropuertos que tiene un país
	 * @param maxDegree nuevo grado máximo
	 */
	public void setMaxDegree(double maxDegree) {
		this.maxDegree = maxDegree;
	}

	/**
	 * Obtiene la lista de los aeropuertos de un país
	 * @return lista de los aeropuertos de un país
	 */
	public ArrayList<TAirport> getList() {
		return this.list;
	}

	/**
	 * Establece la lista de los aeropuertos de un país
	 * @param list Lista de los aeropuertos
	 */
	public void setList(ArrayList<TAirport> list) {
		this.list = list;
	}
	
}
