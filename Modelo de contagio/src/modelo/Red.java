package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Red {
	protected List<Arista> aristas;
	protected HashMap<Integer, Nodo> nodos;
	
	public Red() {
		this.aristas = new ArrayList<>();
		this.nodos = new HashMap<>();
	}
	
	public int numNodos() {
		return this.nodos.size();
	}
	
	public int numAristas() {
		return this.aristas.size();
	}
	
	public boolean contains(Arista a) {
		//compruebaExistenciaNodos(a);		
		return this.aristas.contains(a);
	}
	/*
	private void compruebaExistenciaNodos(Arista a) {
		int nodo1 = a.getNodo1().getValue(), nodo2 = a.getNodo2().getValue();
		if(!this.nodos.containsKey(nodo1))
			this.nodos.put(nodo1, new Nodo(nodo1, 0));
		if(!this.nodos.containsKey(nodo2))
			this.nodos.put(nodo2, new Nodo(nodo2, 0));
	}
	*/
	public void add(Arista a) {
		//compruebaExistenciaNodos(a);
		Nodo n1 = this.nodos.get(a.getNodo1().getInfo().getId());
		Nodo n2 = this.nodos.get(a.getNodo2().getInfo().getId());
		
		if(n1.getPesoAeropuertoComunicado(n2.getInfo().getId()) != null) {
			n1.addAeropuertoComunicado(n2.getInfo().getId(), n1.getPesoAeropuertoComunicado(n2.getInfo().getId()) +1);
		}
		else {
			n1.addAeropuertoComunicado(n2.getInfo().getId(), 1);
		}
		
		if(aristas.contains(a)) {
			int index = aristas.indexOf(a);
			Arista aux = aristas.get(index);
			aux.setPeso(aux.getPeso() + 1);
			
		}
		else {
			this.aristas.add(new Arista(n1, n2, 1));
			n1.getInfo().setOutdegree(n1.getInfo().getOutdegree() + 1);
			n2.getInfo().setIndegree(n2.getInfo().getIndegree() + 1);

			n1.getInfo().incrementDegree();
			n2.getInfo().incrementDegree();
		}
		
		
	}
	
	/**
	 * Devuelve el nodo de la red SI NO EXISTE lo creara
	 * @param nodo Nodo de la red
	 * @return Nodo parte de la red
	 */
	/*public Nodo getExistingNodo(int nodo) {
		if(!this.nodos.containsKey(nodo))
			this.nodos.put(nodo, new Nodo(nodo, 0));
		
		return this.nodos.get(nodo);
	}*/
	
	public Nodo getNodo(int nodo) {
		return this.nodos.get(nodo);
	}
	
	public List<Arista> getAristas() {
		return aristas;
	}

	public void setAristas(List<Arista> aristas) {
		this.aristas = aristas;
	}
	
	public Nodo getLargestHubDegree() {
		int max = 0;
		Nodo nMax = null;
		
		for(Nodo n : this.nodos.values()) {
			if(n.getInfo().getDegree() > max) {
				max = n.getInfo().getDegree();
				nMax = n;
			}
		}
		
		return nMax;
	}
	
	public void setNodos(HashMap<Integer, Nodo> nodos) {
		this.nodos = nodos;
	}
	@Override
	public String toString() {
		String sOut = "###### NODOS ######" + System.getProperty("line.separator");
		
		for (Nodo n : this.nodos.values())
			sOut += n + System.getProperty("line.separator");
		
		sOut = "###### ARISTAS ######" + System.getProperty("line.separator");
		
		for (Arista a : this.aristas)
			sOut += a + System.getProperty("line.separator");
		
		return sOut;
	}
}
