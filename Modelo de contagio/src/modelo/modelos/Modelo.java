package modelo.modelos;

import java.util.ArrayList;

import modelo.red.Nodo;
import modelo.red.Red;

public interface Modelo {
	public void simular(Nodo foco);
	public ArrayList<Nodo> getNodosContagiados();
	
}
