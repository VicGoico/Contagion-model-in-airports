package modelo.red;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Red {
	protected ArrayList<Arista> aristas;
	private HashMap<Integer, Nodo> nodos;

	/**
	 * Constructor
	 * @param nodos Nodos de la red
	 */
	public Red(HashMap<Integer, Nodo> nodos) {
		this.aristas = new ArrayList<>();
		this.nodos = nodos;
	}

	public int numNodos() {
		return this.getNodos().size();
	}

	public int numAristas() {
		return this.aristas.size();
	}

	public boolean contains(Arista a) {
		return this.aristas.contains(a);
	}
	
	/**
	 * Método que añade las aristas.
	 * @param a Arista que se quiere añadir
	 */
	public void add(Arista a) {
		Nodo n1 = a.getNodo1();
		Nodo n2 = a.getNodo2();
		/*
		 * Se actualiza la lista de aeropuertos a los que se vuela desde un aeropuerto
		 * De esta forma cada aeropuerto tendrá una lista con todos los aeropuertos a los
		 * que vuela y cuantas veces lo hace
		 */
		if (n1.getPesoAeropComunicado(n2.getId()) != null) { // Existe nodo
			n1.addAeropuertoComunicado(n2.getId(), n1.getPesoAeropComunicado(n2.getId()) + 1);
		} else {
			n1.addAeropuertoComunicado(n2.getId(), 1);
		}

		int index = aristas.indexOf(a);
		if (index >= 0) {
			Arista aux = aristas.get(index);
			aux.setPeso(aux.getPeso() + 1);
		} else {
			this.aristas.add(new Arista(n1, n2, 1));
			n1.incrementOutDegree();
			n2.incrementInDegree();
			
			n1.incrementDegree();
			n2.incrementDegree();
		}

	}

	/**
	 * Devuelve el nodo de la red SI NO EXISTE lo creara
	 * 
	 * @param nodo Nodo de la red
	 * @return Nodo parte de la red
	 */
	/*
	 * public Nodo getExistingNodo(int nodo) { if(!this.nodos.containsKey(nodo))
	 * this.nodos.put(nodo, new Nodo(nodo, 0));
	 * 
	 * return this.nodos.get(nodo); }
	 */

	public Nodo getNodo(int nodo) {
		return this.getNodos().get(nodo);
	}

	public List<Arista> getAristas() {
		return aristas;
	}

	public void setAristas(ArrayList<Arista> aristas) {
		this.aristas = aristas;
	}

	/**
	 * Método para obtener el nodo con el grado más alto
	 * @return Nodo con el grado más alto
	 */
	public Nodo getLargestHubDegree() {
		int max = 0;
		Nodo nMax = null;

		for (Nodo n : this.getNodos().values()) {
			if (n.getDegree() > max) {
				max = n.getDegree();
				nMax = n;
			}
		}

		return nMax;
	}

	public void setNodos(HashMap<Integer, Nodo> nodos) {
		this.nodos = nodos;
	}

	public HashMap<Integer, Nodo> getNodos() {
		return nodos;
	}

	@Override
	public String toString() {
		String sOut = "###### NODOS ######" + System.getProperty("line.separator");

		for (Nodo n : this.getNodos().values())
			sOut += n + System.getProperty("line.separator");

		sOut = "###### ARISTAS ######" + System.getProperty("line.separator");

		for (Arista a : this.aristas)
			sOut += a + System.getProperty("line.separator");

		return sOut;
	}
}
