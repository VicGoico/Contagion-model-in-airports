package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import modelo.red.Nodo;
import modelo.red.Red;

public class SI implements modelo {

	private ArrayList<Nodo> nodosContagiadosFin;

	
	/**
	 * Modelo de contagio simple
	 * tasa de contagio = 0.6
	 */
	@Override
	public void simular(Red red, Nodo foco) {

		nodosContagiadosFin = new ArrayList<Nodo>();

		nodosContagiadosFin.add(foco);

		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();

		nodosContagiados.add(foco);

		foco.setInfectado(true);

		double tasaContagio = 0.6;
		
		while (!nodosContagiados.isEmpty()) {

			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodosContagiados.get(0)
					.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				
				Random r = new Random();
				if(tasaContagio < r.nextDouble()) {
					Nodo aux = red.getNodos().get(entry.getKey());
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
