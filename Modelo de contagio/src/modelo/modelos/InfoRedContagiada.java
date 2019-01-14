package modelo.modelos;

public class InfoRedContagiada {

	private int nodo1;
	private int nodo2;
	private int peso;
	
	public InfoRedContagiada(int nodo1, int nodo2, int peso) {
		this.setNodo1(nodo1);
		this.setNodo2(nodo2);
		this.setPeso(peso);
	}

	public int getNodo1() {
		return nodo1;
	}

	public void setNodo1(int nodo1) {
		this.nodo1 = nodo1;
	}

	public int getNodo2() {
		return nodo2;
	}

	public void setNodo2(int nodo2) {
		this.nodo2 = nodo2;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
	
}
