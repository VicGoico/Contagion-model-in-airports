package modelo;

import java.util.ArrayList;

public class TCountrys {

	private double maxDegree;
	private ArrayList<TAirport> list;
	
	public TCountrys(){
		this.setMaxDegree(0);
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
