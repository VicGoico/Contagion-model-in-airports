package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import modelo.red.Nodo;
import modelo.red.Red;

public class SI implements Modelo {

	private ArrayList<Nodo> nodosContagiadosFin;
	private Red red;
	private double tasaContagio;

	public SI(Red red, double tasaContagio) {
		this.red = red;
		this.tasaContagio = tasaContagio;
	}
	/**
	 * Modelo de contagio simple
	 * tasa de contagio = 0.6
	 */
	@Override
	public void simular(Nodo foco) {

		nodosContagiadosFin = new ArrayList<Nodo>();

		nodosContagiadosFin.add(foco);

		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();

		nodosContagiados.add(foco);

		foco.setInfectado(true);
		
		while (!nodosContagiados.isEmpty()) {

			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodosContagiados.get(0)
					.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				
				Random r = new Random();
				Nodo aux = red.getNodos().get(entry.getKey());
				if(r.nextDouble() < this.tasaContagio  && !aux.isInfectado()) {
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

}
