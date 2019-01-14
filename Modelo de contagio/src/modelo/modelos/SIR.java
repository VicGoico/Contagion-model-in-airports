package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import modelo.red.Arista;
import modelo.red.Nodo;
import modelo.red.Red;

public class SIR implements Modelo {

	private ArrayList<Nodo> nodosContagiadosFin;
	private ArrayList<Nodo> nodosInmunes;
	private Red red;
	private double tasaRecuperacion;
	private double tasaContagio;
	
	public SIR(Red red, double tasaRecuperación, double tasaContagio) {
		this.red = red;
		this.tasaRecuperacion = tasaRecuperación;
		this.tasaContagio = tasaContagio;
		
	}

	@Override
	public void simular(Nodo foco) {
		nodosContagiadosFin = new ArrayList<>();
		nodosInmunes = new ArrayList<>();

		nodosContagiadosFin.add(foco);

		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();

		nodosContagiados.add(foco);

		foco.setInfectado(true);
		
		while (!nodosContagiados.isEmpty()) {

			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodosContagiados.get(0)
					.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				
				Random r = new Random();
				
				if(r.nextDouble() < this.tasaRecuperacion) {
					//Se recupera alguien
					ArrayList<Nodo> todosNodos = new ArrayList<Nodo>(nodosContagiados);
					todosNodos.addAll(nodosContagiadosFin);
					if(todosNodos.size() > 0) {
						Nodo nodoRecuperado = todosNodos.get(r.nextInt(todosNodos.size()));
						//System.out.println("Se ha recuperado " + nodoRecuperado.getAirportInfo().getName());
						if(nodosContagiados.contains(nodoRecuperado)) {
							nodosContagiados.remove(nodosContagiados.indexOf(nodoRecuperado));
						}
						else {
							nodosContagiadosFin.remove(nodosContagiadosFin.indexOf(nodoRecuperado));
						}
						nodoRecuperado.setInfectado(false);
						this.nodosInmunes.add(nodoRecuperado);
					}					
				}
				Nodo aux = red.getNodos().get(entry.getKey());
				if(r.nextDouble() < this.tasaContagio && !aux.isInfectado() && !this.nodosInmunes.contains(aux)) {	
					System.out.println("Se ha infectado " + aux.getId() + " tiene un grado de " + aux.getDegree());
					aux.setInfectado(true);
					nodosContagiados.add(aux);
					nodosContagiadosFin.add(aux);
					//System.out.println("Se ha contagiado " + aux.getAirportInfo().getName());
					
				}
		
			}
			if(nodosContagiados.size() > 0)
			nodosContagiados.remove(0);

		}

	}

	@Override
	public ArrayList<Nodo> getNodosContagiados() {
		return this.nodosContagiadosFin;
	}

}
