package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModeloContagio {

	private ArrayList<Nodo> nodosContagiadosFin;
	
	public void simular(Red red, Nodo foco) {
		
		nodosContagiadosFin = new ArrayList<Nodo>();
		
		nodosContagiadosFin.add(foco);
		
		ArrayList<Nodo> nodosContagiados = new ArrayList<Nodo>();
		
		nodosContagiados.add(foco);
		
		foco.setInfectado(true);
		
		while(!nodosContagiados.isEmpty()) {
			
			HashMap<Integer,Integer> listaAeropuertosALosQueVuela = nodosContagiados.get(0).getAeropuertosALosQueVuela();
			
			for (Map.Entry<Integer, Integer> entry : listaAeropuertosALosQueVuela.entrySet()) {
				
				Nodo aux = red.nodos.get(entry.getKey());
				
				Arista aristaAuxiliar = new Arista(nodosContagiados.get(0),aux,0);
				
				int peso = 0;
				
				if(red.aristas.contains(aristaAuxiliar)) {
					//Aquí se comprueba porque a lo mejor el eropuerto no vuela a ese nodo
					peso = red.aristas.get(red.aristas.indexOf(aristaAuxiliar)).getPeso();
					
				}
				
				
				aux.setAeropuetosComunicadosInfectados(aux.getAeropuetosComunicadosInfectados() + peso);
				
				double porcentajeContagiado = 
						(aux.getAeropuetosComunicadosInfectados().doubleValue() / Double.valueOf(aux.getInfo().getDegree()));
				
				System.out.println("porcentaje contagiado " + porcentajeContagiado + " umbral "+
						aux.getUmbral() + " aeropuerto " + aux.getInfo().getName() + " peso " + peso +
						" aeropuertos contagiados " + aux.getAeropuetosComunicadosInfectados());
				
				if(porcentajeContagiado > aux.getUmbral() && !aux.isInfectado()) {
					//Se contagia el aeropuerto
					aux.setInfectado(true);
					nodosContagiados.add(aux);
					nodosContagiadosFin.add(aux);
					System.out.println("Se ha contagiado " + aux.getInfo().getName());
				}
			}
			
			nodosContagiados.remove(0);
			
		}
	}
	public ArrayList<Nodo> getNodosContagiados(){
		return this.nodosContagiadosFin;
	}
}
