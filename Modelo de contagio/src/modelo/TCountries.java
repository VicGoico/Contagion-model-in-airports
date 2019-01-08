package modelo;

import java.util.ArrayList;

// Transfer que almacena el degree maximo de todos los aeropuertos de 
// un mismo pais y en un array se va guardando la infomacion de cada aeropuerto

public class TCountries {

	// Atributos
	private double maxDegree;
	private ArrayList<TAirport> list;
	
	// Constructora
	public TCountries(){
		this.setMaxDegree(0.0);
		this.setList(new ArrayList<TAirport>());
	}

	// Getters
	public double getMaxDegree() {
		return this.maxDegree;
	}

	public ArrayList<TAirport> getList() {
		return this.list;
	}
	
	// Setters
	public void setMaxDegree(double maxDegree) {
		this.maxDegree = maxDegree;
	}

	

	public void setList(ArrayList<TAirport> list) {
		this.list = list;
	}
	
}
