package modelo.red;

import java.util.ArrayList;
import java.util.List;

public class Red {
	
	// Atributos
	protected ArrayList<Arista> aristas;
	
	private ArrayList<Nodo> nodos;

	// Constructora
	public Red(ArrayList<Nodo> nodos) {
		this.aristas = new ArrayList<Arista>();
		this.nodos = nodos;
	}
	
	// Numero de arisras de la red
	public int numAristas() {
		return this.aristas.size();
	}
	
	// Para saber si una arista esta en el array de aristas de la red
	public boolean contains(Arista a) {
		return this.aristas.contains(a);
	}
	
	// Numero de nodos de la red
	public int numNodos() {
		return this.getNodos().size();
	}
	

	public void add(Arista a) {
		Nodo n1 = a.getNodo1();
		Nodo n2 = a.getNodo2();

		/*
		 * Aquí guardamos cauntas veces se viaja desde el nodo a hasta el nodo b
		 * para la infección.
		 * Cada nodo tendrá un HashMap<IdNodo al que viaja, Veces que ha viajado a ese nodo>
		 * De esta forma sabemos si un aeropuerto es el unico que viaja a otro y se infecta
		 * el primero que hay que dar por infectado al segundo
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

	// Metodo que nos da el Nodo con el degree mas grande de la red
	public Nodo getLargestHubDegree() {
		int max = 0;
		Nodo nMax = null;

		for (Nodo n : this.getNodos()) {
			if (n.getDegree() > max) {
				max = n.getDegree();
				nMax = n;
			}
		}

		return nMax;
	}
	
	// Getters
	public List<Arista> getAristas() {
		return this.aristas;
	}
	
	public ArrayList<Nodo> getNodos() {
		return this.nodos;
	}
	
	// Setters
	public void setAristas(ArrayList<Arista> aristas) {
		this.aristas = aristas;
	}
	
	public void setNodos(ArrayList<Nodo> nodos) {
		this.nodos = nodos;
	}

	

	@Override
	public String toString() {
		String sOut = "###### NODOS ######" + System.getProperty("line.separator");

		for (Nodo n : this.getNodos())
			sOut += n + System.getProperty("line.separator");

		sOut = "###### ARISTAS ######" + System.getProperty("line.separator");

		for (Arista a : this.aristas)
			sOut += a + System.getProperty("line.separator");

		return sOut;
	}
}
