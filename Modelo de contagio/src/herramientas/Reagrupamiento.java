package herramientas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


// Clase que cambia los IDs ramdom que nos da 
// el csv, por IDs, nuestros para poner ser mas 
// eficientes a la hora de usar un array
public class Reagrupamiento {

	public static final String SEPARATORNODO = ",";
	public static final String SEPARATORARISTA = ";";

	public static final String QUOTE = "\"";
	private static HashMap<Integer,Integer> equivalente;

	public static void main(String[] args) {
		equivalente = new HashMap<>();
		nodos();
		aristas();
	}

	// Escribe en el "nodosCambioados2.csv" rellenando el atributo equivalente
	private static void nodos() {
		BufferedReader br = null;
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("nodosCambiados2.csv"));
			// Leo el "nodos.csv"
			br = new BufferedReader(new FileReader("nodos.csv"));
			String line = br.readLine();
			String[] fieldsTitulo = line.split(SEPARATORNODO);
			String titulo = "";
			// Recorro el array con los campos, separados por comas
			for(int j = 0; j < fieldsTitulo.length; j++) {
				titulo = titulo + fieldsTitulo[j] + ",";
			}
			// Guardo la linea en el csv
			out.write(titulo + System.getProperty("line.separator"));
			// Hasta aqui es para leer la primera linea
			// Empezamos a leer los datos
			int i = 0;
			line = br.readLine();
			// Guardamos los ids en un HashMap
			while (null != line) {
				String[] fields = line.split(SEPARATORNODO);
				equivalente.put(Integer.valueOf(fields[0]), i);
				String aux = i + ",";
				// Juntamos todo de nuevo y lo metemos en el nuevo csv
				for(int j = 1; j < fields.length; j++) {
					aux = aux + fields[j] + ",";
				}
				out.write(aux + System.getProperty("line.separator"));
				line = br.readLine();			
				i += 1;
			}
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (null != br) {
				try {
					br.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	// Leo el csv de las aristas y lo guardo en el "aristasCambiadas2.csv", donde ya estan los IDs modificados
	private static void aristas() {
		BufferedReader br = null;
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("aristasCambiadas2.csv"));
			br = new BufferedReader(new FileReader("aristas.csv"));
			String line = br.readLine();
			String[] fieldsTitulo = line.split(SEPARATORARISTA);
			String titulo = "";
			for(int j = 0; j < fieldsTitulo.length; j++) {
				titulo = titulo + fieldsTitulo[j] + SEPARATORARISTA;
			}
			out.write(titulo + System.getProperty("line.separator"));
			// Hemos leido la primera linea del csv
			// Pasamos a leer los datos
			line = br.readLine();
			while (null != line) {
				String[] fields = line.split(SEPARATORARISTA);
				if(!fields[3].equalsIgnoreCase("N") && !fields[5].equalsIgnoreCase("N")) {
					String aux = "";
					fields[3] = String.valueOf(equivalente.get(Integer.valueOf(fields[3])));
					fields[5] = String.valueOf(equivalente.get(Integer.valueOf(fields[5])));
					for(int j = 0; j < fields.length; j++) {
						aux = aux + fields[j] + SEPARATORARISTA;
					}
					
					out.write(aux + System.getProperty("line.separator"));
				}			
				line = br.readLine();	
			}//3 y 5
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (null != br) {
				try {
					br.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
