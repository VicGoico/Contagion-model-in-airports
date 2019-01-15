package modelo.CSVReaders;

import java.io.IOException;
import java.util.ArrayList;
import modelo.Main;
import modelo.metricas.tools.CSVFileProcessor;
import modelo.red.Arista;
import modelo.red.Nodo;
import modelo.red.Red;

public class AristasReader implements ReaderConsumer {
	private static int lineCounter = 0;
	private static boolean processing = true;
	private ArrayList<Nodo> nodos;
	private Red red;

	/**
	 * Contructor
	 * @param fileName Nombre del fichero donde se encuentran las aristas
	 * @param red Red de aeropuertos
	 * @param nodos Aeropuertos
	 * @throws IOException
	 */
	public AristasReader(String fileName, Red red, ArrayList<Nodo> nodos) throws IOException {
		if(fileName == null) fileName = Main.AIRPORT_ARISTAS_FILENAME;
		this.red = red;
		this.nodos = nodos;
		
		CSVFileProcessor fileProcessor = new CSVFileProcessor(fileName, this);
		fileProcessor.setColumnDelimiter(";");
		fileProcessor.process();
	}

	/**
	 * {@inheritDoc}
	 * Se encarga de obtener de la información de las conexiones entre aeropuertos
	 */
	@Override
	public void accept(ArrayList<String> t) {
		if (!processing)
			return;

		if (lineCounter != 0) { // Me salto la cabecera
			try {
				if(!t.get(3).equalsIgnoreCase("N") && !t.get(5).equalsIgnoreCase("N")) {
                	Nodo n1 = nodos.get(Integer.parseInt(t.get(3)));
                    Nodo n2 = nodos.get(Integer.parseInt(t.get(5)));
                    
                    Arista a = new Arista(n1, n2, 1);
                    this.red.add(a);
                }   
			} catch (Exception e) {
				System.err.println("No se ha podido leer la arista entre aeropuertos.");
				e.printStackTrace();
				System.out.println(t);
				processing = false;
			}
		}
		lineCounter++;
	}

	@Override
	public boolean processing() {
		return processing;
	}
}
