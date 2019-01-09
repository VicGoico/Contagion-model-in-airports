package modelo.red;

public class Arista {

	private Nodo nodo1;
	private Nodo nodo2;
	private int peso;

	/**
	 * Constructor
	 * @param nodo1 Nodo origen de la arista
	 * @param nodo2 Nodo destino de la arista
	 * @param peso Peso que tiene la arista
	 */
	public Arista(Nodo nodo1, Nodo nodo2, int peso) {
		this.nodo1 = nodo1;
		this.nodo2 = nodo2;
		this.peso = peso;
	}

	/**
	 * Método que permite obtener el nodo origen
	 * @return Nodo origen
	 */
	public Nodo getNodo1() {
		return nodo1;
	}

	/**
	 * Método que permite establecer el nodo origen
	 * @param nodo1 Nodo origen
	 */
	public void setNodo1(Nodo nodo1) {
		this.nodo1 = nodo1;
	}

	/**
	 * Método que permite obtener el nodo destino
	 * @return Nodo destino
	 */
	public Nodo getNodo2() {
		return nodo2;
	}

	/**
	 * Método que permite establecer el nodo destino
	 * @param nodo2 nodo destino
	 */
	public void setNodo2(Nodo nodo2) {
		this.nodo2 = nodo2;
	}

	/**
	 * Método que permite obetener el peso de la arista
	 * @return Peso de la arista
	 */
	public int getPeso() {
		return peso;
	}

	/**
	 * Método que permite establecer el peso de la arista
	 * @param peso Peso de la arista
	 */
	public void setPeso(int peso) {
		this.peso = peso;
	}

	/**
	 * {@inheritDoc}
	 * Método para comprobar si dos aristas son iguales
	 * Serán iguales si sus nodos origen y destino son iguales
	 */
	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof Arista
				&& (((Arista) o).nodo1.equals(this.nodo1) && ((Arista) o).nodo2.equals(this.nodo2));
	}

	@Override
	public String toString() {
		return "(" + this.nodo1.toString() + ", " + this.nodo2.toString() + ")";
	}
}
