package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.red.Arista;
import modelo.red.Nodo;
import modelo.red.Red;

public class UmbralesModificaciones implements Modelo {
	private Red redContagiada;
	private Red red;
	private ArrayList<Nodo> nodosContagiadosFin;

	public UmbralesModificaciones(Red red) {
		this.red = red;
		this.redContagiada = new Red((HashMap<Integer, Nodo>) red.getNodos().clone());
	}

	/**
	 * Método que se encarga de simular una infección Modificaciones: Umbral
	 * especifico para cada aeropuerto No se mira el % de los vecinos si no el
	 * numero de vuelos infectados
	 * 
	 * @param red  Red sobre la que se pretende realizar la infeccion
	 * @param foco Aeropuerto en el cual se quiere iniciar la infeccion
	 */
	public void simular(Nodo foco) {

		nodosContagiadosFin = new ArrayList<Nodo>();

		nodosContagiadosFin.add(foco);

		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();

		nodosContagiados.add(foco);

		foco.setInfectado(true);

		while (!nodosContagiados.isEmpty()) {
			Nodo nodoContagiado = nodosContagiados.get(0);
			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodoContagiado.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				Nodo aux = this.red.getNodos().get(entry.getKey());
				Arista aristaAuxiliar = new Arista(nodoContagiado, aux, 0);
				int peso = 0;

				if (this.red.getAristas().contains(aristaAuxiliar)) {
					// Aquí se comprueba porque a lo mejor el eropuerto no vuela a ese nodo
					peso = this.red.getAristas().get(this.red.getAristas().indexOf(aristaAuxiliar)).getPeso();
				}

				if (!aux.isInfectado()) {
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
						nodosContagiadosFin.add(aux);
						System.out.println("Se ha contagiado " + aux.getAirportInfo().getName());
						
						// Formo una red contagiada que contiene solo aristas entre nodos contagiados
						Nodo nC = this.redContagiada.getNodo(aux.getId()), nC2 = this.redContagiada.getNodo(nodoContagiado.getId());
						nC.setInfectado(true);
						nC2.setInfectado(true);
						this.redContagiada.add(new Arista(nC2, nC, 0));
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
	public ArrayList<Nodo> getNodosContagiados() {
		return this.nodosContagiadosFin;
	}
	public Red getRedContagiada() {
		return this.redContagiada;
	}
}
