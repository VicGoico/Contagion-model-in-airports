package modelo;

import java.util.ArrayList;

public class TCountries {

	private double maxDegree;
	private ArrayList<TAirport> list;
	
	public TCountries(){
		this.setMaxDegree(0.0);
		this.setList(new ArrayList<TAirport>());
	}

	public double getMaxDegree() {
		return this.maxDegree;
	}

	public void setMaxDegree(double maxDegree) {
		this.maxDegree = maxDegree;
	}

	public ArrayList<TAirport> getList() {
		return this.list;
	}

	public void setList(ArrayList<TAirport> list) {
		this.list = list;
	}
	
}
