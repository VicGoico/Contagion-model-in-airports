package modelo.modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import modelo.red.Nodo;
import modelo.red.Red;

public class SIRConMejora implements Modelo {
	private ArrayList<Nodo> nodosContagiadosFin;
	private ArrayList<Nodo> nodosInmunes;
	private ArrayList<AristaContagiadaSimple> aristasHanProvocadoInfeccion;
	private Red red;
	private double tasaRecuperacion;
	private double tasaContagio;
	
	public SIRConMejora(Red red, double tasaRecuperacion, double tasaContagio) {
		this.red = red;
		this.tasaRecuperacion = tasaRecuperacion;
		this.tasaContagio = tasaContagio;
		
	}

	@Override
	public void simular(Nodo foco) {
		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();
		
		nodosContagiadosFin = new ArrayList<>();
		nodosInmunes = new ArrayList<>();
		aristasHanProvocadoInfeccion = new ArrayList<>();
		
		nodosContagiadosFin.add(foco);
		nodosContagiados.add(foco);

		foco.setInfectado(true);

		while (!nodosContagiados.isEmpty()) {
			Nodo nodoContagiado = nodosContagiados.get(0);
			HashMap<Integer, Integer> listaAeropuertosALosQueVuela = nodoContagiado.getAeropuertosALosQueVuela();

			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {

				Random r = new Random();

				if (r.nextDouble() < this.tasaRecuperacion) {
					// Se recupera alguien

					ArrayList<Nodo> todosNodos = new ArrayList<Nodo>(nodosContagiados);
					todosNodos.addAll(nodosContagiadosFin);
					if (todosNodos.size() > 0) {
						Nodo nodoRecuperado = todosNodos.get(0);
						for (int i = 1; i < todosNodos.size(); i++) {
							if (todosNodos.get(i).getUmbral() > nodoRecuperado.getUmbral()) {
								nodoRecuperado = todosNodos.get(i);
							}
						}
						if (nodosContagiados.contains(nodoRecuperado)) {
							nodosContagiados.remove(nodosContagiados.indexOf(nodoRecuperado));
						} else {
							nodosContagiadosFin.remove(nodosContagiadosFin.indexOf(nodoRecuperado));
						}
						System.out.println("Se ha recuperado " + nodoRecuperado.getAirportInfo().getName());
						nodoRecuperado.setInfectado(false);
						this.nodosInmunes.add(nodoRecuperado);
					}
				}
				
				Nodo aux = red.getNodos().get(entry.getKey());
				this.aristasHanProvocadoInfeccion.add(new AristaContagiadaSimple(nodoContagiado.getId(), aux.getId(), 0));
				if (!aux.isInfectado() && r.nextDouble() < this.tasaContagio  && !this.nodosInmunes.contains(aux)) {
					aux.setInfectado(true);
					nodosContagiados.add(aux);
					nodosContagiadosFin.add(aux);
					System.out.println("Se ha contagiado " + aux.getAirportInfo().getName());

				}

			}
			if(nodosContagiados.size() > 0) //Puede que hayamos eliminado justo este nodo y sea el ultimo
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
