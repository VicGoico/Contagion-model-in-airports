package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import modelo.red.Nodo;
import modelo.red.Red;

public class SI implements Modelo {
	private ArrayList<Nodo> nodosContagiadosFin;
	private ArrayList<AristaContagiadaSimple> aristasHanProvocadoInfeccion;
	private Red red;
	private double tasaContagio;

	public SI(Red red, double tasaContagio) {
		this.red = red;
		this.tasaContagio = tasaContagio;
	}

	/**
	 * Modelo de contagio simple tasa de contagio = 0.6
	 */
	@Override
	public void simular(Nodo foco) {
		nodosContagiadosFin = new ArrayList<>();
		aristasHanProvocadoInfeccion = new ArrayList<>();

		nodosContagiadosFin.add(foco);

		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();

		nodosContagiados.add(foco);

		foco.setInfectado(true);

		while (!nodosContagiados.isEmpty()) {
			Nodo nodoContagiado = nodosContagiados.get(0);
			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodoContagiado.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				Random r = new Random();
				Nodo aux = red.getNodos().get(entry.getKey());
				if (r.nextDouble() < this.tasaContagio && !aux.isInfectado()) {
					this.aristasHanProvocadoInfeccion.add(new AristaContagiadaSimple(nodoContagiado.getId(), aux.getId(), 1));
					aux.setInfectado(true);
					nodosContagiados.add(aux);
					nodosContagiadosFin.add(aux);
					System.out.println("Se ha contagiado " + aux.getAirportInfo().getName());
				}
			}

			nodosContagiados.remove(0);
		}
	}

	@Override
	public ArrayList<Nodo> getNodosContagiados() {
		return this.nodosContagiadosFin;
	}

	@Override
	public ArrayList<AristaContagiadaSimple> getAristasContagiadas() {
		return this.aristasHanProvocadoInfeccion;
	}

}
