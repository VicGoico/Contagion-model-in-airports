package modelo.modelos;

import java.util.ArrayList;
import modelo.red.Nodo;

public interface Modelo {
	public void simular(Nodo foco);
	public ArrayList<Nodo> getNodosContagiados();
	public ArrayList<AristaContagiadaSimple> getAristasContagiadas();
	public ArrayList<ArrayList<Integer>> getInfeccionTiempo();
}
