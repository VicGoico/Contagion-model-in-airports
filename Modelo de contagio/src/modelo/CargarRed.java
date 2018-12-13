package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CargarRed {
	private Red red;
	private HashMap<Integer, Nodo> nodos;
	
	public CargarRed() {
		red = new Red();
		nodos = new HashMap<Integer, Nodo>();
		leerNodos();
		red.setNodos(nodos);
		generaRed();
		System.out.println(red);
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
                
                
                Nodo n = new Nodo(Integer.parseInt(linea[0]), 0, 0.0, info);
                
                nodos.put(Integer.parseInt(linea[0]), n);
                
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void generaRed() {
		String csvFile = "aristas.csv";
		String line = "";
		String cvsSplitBy = ";";
		
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			 br.readLine();

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] linea = line.split(cvsSplitBy);
                
                /*InfoAeropuertos info = new InfoAeropuertos(linea[0], linea[3],
                		linea[4], linea[5], linea[6], linea[7], linea[8],
                		linea[9], linea[10], linea[11], linea[12], linea[13]);*/
                
                //nodos.put(Integer.parseInt(linea[0]), info);
                
                System.out.println(linea[3]);
                if(!linea[3].equalsIgnoreCase("N") && !linea[5].equalsIgnoreCase("N")) {
                	
                	Nodo n1 = nodos.get(Integer.parseInt(linea[3]));
                    
                    Nodo n2 = nodos.get(Integer.parseInt(linea[5]));
                    
                    Arista a = new Arista(n1, n2);
                    
                    red.add(a);
                	
                }
                
                
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
}
