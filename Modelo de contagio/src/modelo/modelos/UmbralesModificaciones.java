package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.red.Arista;
import modelo.red.Nodo;
import modelo.red.Red;

public class UmbralesModificaciones implements Modelo {
	// private Red redContagiada;
	private ArrayList<AristaContagiadaSimple> aristasHanProvocadoInfeccion;
	private Red red;
	private ArrayList<Nodo> nodosContagiadosFin;

	public UmbralesModificaciones(Red red) {
		this.red = red;
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
		this.aristasHanProvocadoInfeccion = new ArrayList<>();
		this.nodosContagiadosFin = new ArrayList<Nodo>();
		this.nodosContagiadosFin.add(foco);

		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();

		nodosContagiados.add(foco);

		foco.setInfectado(true);

		System.out.println("Comienza la infección de " + foco.getId());

		while (!nodosContagiados.isEmpty()) {

			Nodo nodoContagiado = nodosContagiados.get(0);
			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodoContagiado.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				Nodo aux = this.red.getNodos().get(entry.getKey());

				System.out.println("Vuela a  " + entry.getKey());
				Arista aristaAuxiliar = new Arista(nodoContagiado, aux, 0);
				int peso = 0;

				if (this.red.getAristas().contains(aristaAuxiliar)) {
					// Aquí se comprueba porque a lo mejor el eropuerto no vuela a ese nodo
					peso = this.red.getAristas().get(this.red.getAristas().indexOf(aristaAuxiliar)).getPeso();
				}

				aristasHanProvocadoInfeccion.add(new AristaContagiadaSimple(nodoContagiado.getId(), aux.getId(), peso));
				
				System.out.println("INFECTADO: " + aux.isInfectado());

				if (!aux.isInfectado()) {
					System.out
							.println("Aeropuertos comunicados infectados " + aux.getAeropuetosComunicadosInfectados());
					aux.setAeropuetosComunicadosInfectados(aux.getAeropuetosComunicadosInfectados() + peso);

					double porcentajeContagiado = (aux.getAeropuetosComunicadosInfectados().doubleValue()
							/ Double.valueOf(aux.getIndegree()));

					System.out.println("porcentaje contagiado " + porcentajeContagiado + " umbral " + aux.getUmbral()
							+ " aeropuerto " + aux.getAirportInfo().getName() + " peso " + peso
							+ " aeropuertos contagiados " + aux.getAeropuetosComunicadosInfectados());

					if (porcentajeContagiado > aux.getUmbral() && !aux.isInfectado()) {
						// Se contagia el aeropuerto
						aux.setInfectado(true);
						nodosContagiados.add(aux);
						this.nodosContagiadosFin.add(aux);
						
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

			nodosContagiados.remove(0);

		}
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
