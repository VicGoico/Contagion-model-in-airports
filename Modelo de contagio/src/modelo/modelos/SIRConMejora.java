package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import modelo.red.Nodo;
import modelo.red.Red;

public class SIRConMejora implements modelo {

	private ArrayList<Nodo> nodosContagiadosFin;
	private ArrayList<Nodo> nodosImmunes;

	@Override
	public void simular(Red red, Nodo foco) {
		nodosContagiadosFin = new ArrayList<>();
		nodosImmunes = new ArrayList<>();

		nodosContagiadosFin.add(foco);

		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();

		nodosContagiados.add(foco);

		foco.setInfectado(true);

		double tasaRecuperación = 0.2;
		
		double tasaContagio = 0.6;
		
		while (!nodosContagiados.isEmpty()) {

			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodosContagiados.get(0)
					.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				
				Random r = new Random();
				
				if(tasaRecuperación < r.nextDouble()) {
					//Se recupera alguien
					
					ArrayList<Nodo> todosNodos = new ArrayList<Nodo>(nodosContagiados);
					todosNodos.addAll(nodosContagiadosFin);
					Nodo nodoRecuperado = todosNodos.get(0);
					for(int i = 1; i < todosNodos.size(); i++) {
						if(todosNodos.get(i).getUmbral() > nodoRecuperado.getUmbral()) {
							nodoRecuperado = todosNodos.get(i);
						}
					}
					if(nodosContagiados.contains(nodoRecuperado)) {
						nodosContagiados.remove(nodosContagiados.indexOf(nodoRecuperado));
					}
					else {
						nodosContagiadosFin.remove(nodosContagiadosFin.indexOf(nodoRecuperado));
					}
					nodoRecuperado.setInfectado(false);
					this.nodosImmunes.add(nodoRecuperado);
				}
				Nodo aux = red.getNodos().get(entry.getKey());
				if(tasaContagio < r.nextDouble() && !this.nodosImmunes.contains(aux)) {					
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
