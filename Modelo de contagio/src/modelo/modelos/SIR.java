package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import modelo.red.Nodo;
import modelo.red.Red;

public class SIR implements Modelo {
	private ArrayList<Nodo> nodosContagiadosFin;
	private ArrayList<Nodo> nodosInmunes;
	private ArrayList<AristaContagiadaSimple> aristasHanProvocadoInfeccion;
	private Red red;
	private double tasaRecuperacion;
	private double tasaContagio;

	private ArrayList<ArrayList<Integer>> nodosSusceptibles;
	// ej pos0 -> array con todos los aeropuertos susceptibles en el instante 0
	private ArrayList<ArrayList<Integer>> nodosInfectados;
	// ej pos0 -> array con todos los aeropuertos infectados en el instante 0
	private ArrayList<ArrayList<Integer>> nodosRecuperados;
	// ej pos0 -> array con todos los aeropuertos infectados en el instante 0

	public SIR(Red red, double tasaRecuperacion, double tasaContagio) {
		this.red = red;
		this.tasaRecuperacion = tasaRecuperacion;
		this.tasaContagio = tasaContagio;
		nodosSusceptibles = new ArrayList<>();
		nodosInfectados = new ArrayList<>();
		nodosRecuperados = new ArrayList<>();
	}

	@Override
	public void simular(Nodo foco) {
		nodosContagiadosFin = new ArrayList<>();
		aristasHanProvocadoInfeccion = new ArrayList<>();
		nodosInmunes = new ArrayList<>();
		nodosContagiadosFin.add(foco);

		foco.setInfectado(true);

		int instante = 0;

		ArrayList<Integer> instanteCero = new ArrayList<Integer>();
		instanteCero.add(foco.getId());
		nodosInfectados.add(instante, instanteCero);
		nodosRecuperados.add(instante, new ArrayList<>());

		while (nodosInfectados.get(instante) != null && nodosInfectados.get(instante).size() > 0 && !estancamiento()) {

			ArrayList<Integer> nodosInfectadosInstante = new ArrayList<>();
			ArrayList<Integer> nodosRecuperadosInstante = new ArrayList<>();

			Random r = new Random();

			for (int j = 0; j < nodosInfectados.get(instante).size(); j++) {
				Nodo aux = red.getNodos().get(nodosInfectados.get(instante).get(j));
				if (r.nextDouble() < this.tasaRecuperacion) {
					// Se recupera el nodo
					aux.setInfectado(false);
					this.nodosInmunes.add(aux);
					nodosRecuperadosInstante.add(aux.getId());
					nodosInfectados.get(instante).remove(nodosInfectados.get(instante).indexOf(aux.getId()));
				}
			}
			for (Integer i : nodosInfectados.get(instante)) {

				HashMap<Integer, Integer> listaAeropuertosALosQueVuela = red.getNodo(i).getAeropuertosALosQueVuela();
				for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {

					Nodo aux = red.getNodos().get(entry.getKey());
					if (r.nextDouble() < this.tasaContagio && !aux.isInfectado() && !this.nodosInmunes.contains(aux)) {

						this.aristasHanProvocadoInfeccion
								.add(new AristaContagiadaSimple(red.getNodo(i).getId(), aux.getId(), 1));
						nodosContagiadosFin.add(aux);
						aux.setInfectado(true);
						nodosInfectadosInstante.add(aux.getId());

					}
				}

			}
			instante += 1;
			nodosInfectadosInstante.addAll(nodosInfectados.get(instante - 1));
			nodosInfectados.add(instante, nodosInfectadosInstante);
			nodosRecuperadosInstante.addAll(nodosRecuperados.get(instante - 1));
			nodosRecuperados.add(instante, nodosRecuperadosInstante);
		}

		System.out.println("PAUSA");
	}

	@Override
	public ArrayList<Nodo> getNodosContagiados() {
		return this.nodosContagiadosFin;
	}

	@Override
	public ArrayList<AristaContagiadaSimple> getAristasContagiadas() {
		return this.aristasHanProvocadoInfeccion;
	}

	private void actualizarNodosInfectados(int instante) {
		ArrayList<Integer> nodosInfectadosEnInstante = new ArrayList<>();
		for (Nodo n : this.nodosContagiadosFin) {
			nodosInfectadosEnInstante.add(n.getId());
		}
		this.nodosInfectados.add(instante, nodosInfectadosEnInstante);

	}

	private void actualizarNodosSusceptibles(int instante) {
		ArrayList<Integer> nodosSusceptiblesEnInstante = new ArrayList<>();
		for (Nodo nodo : this.red.getNodos()) {
			if (!nodosContagiadosFin.contains(nodo)) {
				nodosSusceptiblesEnInstante.add(nodo.getId());
			}
		}

	}

	private boolean estancamiento() {
		if(nodosInfectados.size() < 5) {
			//Si ha habido menos de 5 iteraciones
			return false;
		}
		else {
			boolean estancamiento = true;
			//inicializamos los nodos infectados a el total de nodos infectados de la ultima iteracion
			//si en las ultimas 5 iteraciones no ha aumentado el numero de infecciones se ha estancado
			int infectados = nodosInfectados.get(nodosInfectados.size() -1).size();
			for(int i = nodosInfectados.size() -1; i > nodosInfectados.size() - 5; i--) {
				if(nodosInfectados.get(i).size() != infectados) {
					estancamiento = false;
				}
			}
			return estancamiento;
		}

	}

	@Override
	public ArrayList<ArrayList<Integer>> getInfeccionTiempo() {
		return this.nodosInfectados;
	}

	@Override
	public int numInfectados() {
		int max = 0;
		
		for (int i = 0; i < this.nodosInfectados.size(); i++) {
			if(this.nodosInfectados.get(i). size() > max) {
				max = this.nodosInfectados.get(i). size();
			}
		}
		return max;
	}

	@Override
	public ArrayList<ArrayList<Integer>> getInfeccionRecuperados() {
		return this.nodosRecuperados;
	}
}
