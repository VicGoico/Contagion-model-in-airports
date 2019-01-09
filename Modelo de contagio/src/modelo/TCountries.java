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
	 * Obtiene el grado m�ximo de los aeropuertos que tiene un pa�s
	 * @return grado m�ximo
	 */
	public double getMaxDegree() {
		return this.maxDegree;
	}
	
	/**
	 * Estable el grado m�ximo de los aeropuertos que tiene un pa�s
	 * @param maxDegree nuevo grado m�ximo
	 */
	public void setMaxDegree(double maxDegree) {
		this.maxDegree = maxDegree;
	}

	/**
	 * Obtiene la lista de los aeropuertos de un pa�s
	 * @return lista de los aeropuertos de un pa�s
	 */
	public ArrayList<TAirport> getList() {
		return this.list;
	}

	/**
	 * Establece la lista de los aeropuertos de un pa�s
	 * @param list Lista de los aeropuertos
	 */
	public void setList(ArrayList<TAirport> list) {
		this.list = list;
	}
	
}
