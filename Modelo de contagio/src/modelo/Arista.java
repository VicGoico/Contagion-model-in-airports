package modelo;


public class Arista {

	private Nodo nodo1;
	private Nodo nodo2;
	private int peso;

	public Arista(Nodo nodo1, Nodo nodo2, int peso) {
		this.nodo1 = nodo1;
		this.nodo2 = nodo2;
		this.peso = peso;
	}

	public Nodo getNodo1() {
		return nodo1;
	}

	public void setNodo1(Nodo nodo1) {
		this.nodo1 = nodo1;
	}

	public Nodo getNodo2() {
		return nodo2;
	}

	public void setNodo2(Nodo nodo2) {
		this.nodo2 = nodo2;
	}

	@Override
	public boolean equals(Object o) {
		return o != null &&
				o instanceof Arista &&
				( ((Arista) o).nodo1.equals(this.nodo1) && ((Arista) o).nodo2.equals(this.nodo2));
	}
	
	@Override
	public String toString() {
		return "(" + this.nodo1.toString() + ", " + this.nodo2.toString() + ")";
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(int peso) {
		this.peso = peso;
	}
}
