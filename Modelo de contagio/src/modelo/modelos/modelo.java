package modelo.modelos;

import java.util.ArrayList;

import modelo.red.Nodo;
import modelo.red.Red;

public interface modelo {

	public void simular(Red red, Nodo foco);
	public ArrayList<Nodo> getNodosContagiados();
	
}
