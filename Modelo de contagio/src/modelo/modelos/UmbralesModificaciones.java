package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import modelo.red.Arista;
import modelo.red.Nodo;
import modelo.red.Red;

public class UmbralesModificaciones implements Modelo {
	// private Red redContagiada;
	private ArrayList<AristaContagiadaSimple> aristasHanProvocadoInfeccion;
	private Red red;
	private ArrayList<Nodo> nodosContagiadosFin;

	private ArrayList<ArrayList<Integer>> nodosSusceptibles;
	// ej pos0 -> array con todos los aeropuertos susceptibles en el instante 0
	private ArrayList<ArrayList<Integer>> nodosInfectados;
	// ej pos0 -> array con todos los aeropuertos infectados en el instante 0

	public UmbralesModificaciones(Red red) {
		this.red = red;
		nodosSusceptibles = new ArrayList<>();
		nodosInfectados = new ArrayList<>();
	}

	/**
	 * Método que se encarga de simular una infección Modificaciones: Umbral
	 * especifico para cada aeropuerto No se mira el % de los vecinos si no el
	 * numero de vuelos infectados
	 * 
	 * @param red
	 *            Red sobre la que se pretende realizar la infeccion
	 * @param foco
	 *            Aeropuerto en el cual se quiere iniciar la infeccion
	 */
	public void simular(Nodo foco) {
		nodosContagiadosFin = new ArrayList<>();
		aristasHanProvocadoInfeccion = new ArrayList<>();

		nodosContagiadosFin.add(foco);

		foco.setInfectado(true);

		int instante = 0;

		ArrayList<Integer> instanteCero = new ArrayList<Integer>();
		instanteCero.add(foco.getId());
		nodosInfectados.add(instante, instanteCero);

		while (nodosInfectados.get(instante) != null && nodosInfectados.get(instante).size() > 0) {

			ArrayList<Integer> nodosInfectadosInstante = new ArrayList<>();

			for (Integer i : nodosInfectados.get(instante)) {
				HashMap<Integer, Integer> listaAeropuertosALosQueVuela = red.getNodo(i).getAeropuertosALosQueVuela();
				for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
					Random r = new Random();
					Nodo aux = red.getNodos().get(entry.getKey());
					Arista aristaAuxiliar = new Arista(red.getNodo(i), aux, 0);
					int peso = 0;

					if (this.red.getAristas().contains(aristaAuxiliar)) {
						// Aquí se comprueba porque a lo mejor el eropuerto no vuela a ese nodo
						peso = this.red.getAristas().get(this.red.getAristas().indexOf(aristaAuxiliar)).getPeso();
					}

					aristasHanProvocadoInfeccion.add(new AristaContagiadaSimple(i, aux.getId(), peso));

					if (!aux.isInfectado()) {
						aux.setAeropuetosComunicadosInfectados(aux.getAeropuetosComunicadosInfectados() + peso);

						double porcentajeContagiado = (aux.getAeropuetosComunicadosInfectados().doubleValue()
								/ Double.valueOf(aux.getIndegree()));

						System.out.println("porcentaje contagiado " + porcentajeContagiado + " umbral "
								+ aux.getUmbral() + " aeropuerto " + aux.getAirportInfo().getName() + " peso " + peso
								+ " aeropuertos contagiados " + aux.getAeropuetosComunicadosInfectados());

						if (porcentajeContagiado > aux.getUmbral() && !aux.isInfectado()) {
							// Se contagia el aeropuerto
							aux.setInfectado(true);
							this.nodosContagiadosFin.add(aux);
							nodosInfectadosInstante.add(aux.getId());
							System.out.println("Se ha contagiado " + aux.getAirportInfo().getName());
							/*
							 * // Formo una red contagiada que contiene solo aristas entre nodos contagiados
							 * Nodo nC = this.redContagiada.getNodo(aux.getId()), nC2 =
							 * this.redContagiada.getNodo(nodoContagiado.getId()); nC.setInfectado(true);
							 * nC2.setInfectado(true); this.redContagiada.add(new Arista(nC2, nC, 0));
							 */
						}
					}
				}

			}
			instante += 1;
			nodosInfectados.add(instante, nodosInfectadosInstante);

		}

		System.out.println("PAUSA");
	}

	/**
	 * Método para obtener los aeropuertos que se han contagiado tras la simulación
	 * 
	 * @return ArrayList con los aeropuertos que se han infectado
	 */
	@Override
	public ArrayList<Nodo> getNodosContagiados() {
		return this.nodosContagiadosFin;
	}

	@Override
	public ArrayList<AristaContagiadaSimple> getAristasContagiadas() {
		return this.aristasHanProvocadoInfeccion;
	}
}
