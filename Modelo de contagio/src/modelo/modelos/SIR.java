package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import modelo.red.Arista;
import modelo.red.Nodo;
import modelo.red.Red;



public class SIR implements modelo {

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

		double tasaRecuperaci�n = 0.2;
		
		double tasaContagio = 0.6;
		
		while (!nodosContagiados.isEmpty()) {

			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodosContagiados.get(0)
					.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				
				Random r = new Random();
				
				if(tasaRecuperaci�n < r.nextDouble()) {
					//Se recupera alguien
					int posNodoRecuperado = r.nextInt(nodosContagiados.size() + nodosContagiadosFin.size());
					Nodo nodoRecuperado = null;
					if(posNodoRecuperado < nodosContagiados.size()) {
						nodoRecuperado = nodosContagiados.get(posNodoRecuperado);						
						nodosContagiados.remove(posNodoRecuperado);
					}
					else {
						nodoRecuperado = nodosContagiadosFin.get(posNodoRecuperado - nodosContagiadosFin.size());
						nodosContagiadosFin.remove(posNodoRecuperado - nodosContagiadosFin.size());
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
