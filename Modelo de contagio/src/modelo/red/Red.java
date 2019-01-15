package modelo.red;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Red {
	protected ArrayList<Arista> aristas;
	private ArrayList<Nodo> nodos;
	//private HashMap<Integer, Nodo> nodos;

	/**
	 * Constructor
	 * @param nodos Nodos de la red
	 */
	public Red(ArrayList<Nodo> nodos) {
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
		
		n1.addAeropuertoComunicado(n2.getId(), 1);

		int index = this.aristas.indexOf(a);
		if (index >= 0) {
			Arista aux = this.aristas.get(index);
			aux.setPeso(aux.getPeso() + 1);
		} else {
			a.setPeso(1);
			n1.incrementOutDegree();
			n2.incrementInDegree();
			n1.incrementDegree();
			n2.incrementDegree();
			this.aristas.add(a);
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

	public ArrayList<Arista> getAristas() {
		return aristas;
	}

	public void setAristas(ArrayList<Arista> aristas) {
		this.aristas = aristas;
	}

	public void setNodos(ArrayList<Nodo> nodos) {
		this.nodos = nodos;
	}

	public ArrayList<Nodo> getNodos() {
		return nodos;
	}
	
	public void toCSVFormat(BufferedWriter s) throws IOException {
		for(Arista a : this.aristas) {
			s.write(a.getNodo1().getId() + "," + a.getNodo2().getId() + System.getProperty("line.separator"));
		}
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
	
	public static Red clonarRed(Red red) {
		ArrayList<Nodo> nodos = new ArrayList<Nodo>(red.getNodos());
		Red redAux = new Red(nodos);
		ArrayList<Arista> aristas = new ArrayList<Arista>(red.getAristas());
		redAux.setAristas(aristas);
		return redAux;
	}

	public static void restablecer(Red red) {
		for(int i = 0; i < red.getNodos().size(); i++) {
			red.getNodo(i).setInfectado(false);
			red.getNodo(i).setAeropuetosComunicadosInfectados(0);
			
		}
	}
}
