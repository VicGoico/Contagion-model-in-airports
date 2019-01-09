package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelo.red.Arista;
import modelo.red.Nodo;
import modelo.red.Red;

public class ModeloContagio {

	private ArrayList<Nodo> nodosContagiadosFin;
	
	/**
	 * Método que se encarga de simular una infección
	 * @param red Red sobre la que se pretende realizar la infección
	 * @param foco Aeropuerto en el cual se quiere iniciar la infección
	 */
	public void simular(Red red, Nodo foco) {
		
		nodosContagiadosFin = new ArrayList<Nodo>();
		
		nodosContagiadosFin.add(foco);
		
		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();
		
		nodosContagiados.add(foco);
		
		foco.setInfectado(true);
		
		while(!nodosContagiados.isEmpty()) {
			
			HashMap<Integer,Integer> listaAeropuertosALosQueVuela = nodosContagiados.get(0).getAeropuertosALosQueVuela();
			
			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				
				Nodo aux = red.getNodos().get(entry.getKey());
				
				Arista aristaAuxiliar = new Arista(nodosContagiados.get(0),aux,0);
				
				int peso = 0;
				
				if(red.getAristas().contains(aristaAuxiliar)) {
					//Aquí se comprueba porque a lo mejor el eropuerto no vuela a ese nodo
					peso = red.getAristas().get(red.getAristas().indexOf(aristaAuxiliar)).getPeso();
					
				}
				
				
				aux.setAeropuetosComunicadosInfectados(aux.getAeropuetosComunicadosInfectados() + peso);
				
				double porcentajeContagiado = 
						(aux.getAeropuetosComunicadosInfectados().doubleValue() / Double.valueOf(aux.getDegree()));
				
				System.out.println("porcentaje contagiado " + porcentajeContagiado + " umbral "+
						aux.getUmbral() + " aeropuerto " + aux.getAirportInfo().getName() + " peso " + peso +
						" aeropuertos contagiados " + aux.getAeropuetosComunicadosInfectados());
				
				if(porcentajeContagiado > aux.getUmbral() && !aux.isInfectado()) {
					//Se contagia el aeropuerto
					aux.setInfectado(true);
					nodosContagiados.add(aux);
					nodosContagiadosFin.add(aux);
					System.out.println("Se ha contagiado " + aux.getAirportInfo().getName());
				}
			}
			
			nodosContagiados.remove(0);
			
		}
	}
	/**
	 * Método para obtener los aeropuertos que se han contagiado tras
	 * la simulación
	 * @return ArrayList con los aeropuertos que se han infectado
	 */
	public ArrayList<Nodo> getNodosContagiados(){
		return this.nodosContagiadosFin;
	}
}
