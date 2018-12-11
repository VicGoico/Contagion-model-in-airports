package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CargarRed {
	private Red red;
	private Map<Integer, InfoAeropuertos> nodos;
	
	public CargarRed() {
		red = new Red();
		nodos = new HashMap<Integer, InfoAeropuertos>();
		leerNodos();
	}
	
	private void leerNodos() {
		String csvFile = "nodos.csv";
		String line = "";
		String cvsSplitBy = ",";
		
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			 br.readLine();

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] linea = line.split(cvsSplitBy);
                
                InfoAeropuertos info = new InfoAeropuertos(linea[0], linea[3],
                		linea[4], linea[5], linea[6], linea[7], linea[8],
                		linea[9], linea[10], linea[11], linea[12], linea[13]);
                
                nodos.put(Integer.parseInt(linea[0]), info);
                
                
            }
            
            System.out.println("");


        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
