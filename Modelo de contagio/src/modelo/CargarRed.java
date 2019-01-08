package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import modelo.CSVReaders.ExpenditureHealthReader;

public class CargarRed {
	private Red red;
	private HashMap<Integer, Nodo> nodos;	
	
	private HashMap<Integer,TAirport> info_airport;
	
	// private HashMap<String,ArrayList<HashMap<Integer,TAirport>>> countries;
	
	public CargarRed(String fileName, HashMap<Integer, TAirport> infoAirport, ExpenditureHealthReader expHReader) {
		this.red = new Red();
		this.nodos = new HashMap<Integer, Nodo>();
		//leerNodos();
		this.info_airport = infoAirport;
		// this.countries = new HashMap<String,ArrayList<HashMap<Integer,TAirport>>>();
		
		/*if(Main.processCSVDatas == null)
			this.readDatas(fileName);
		else {
			//this.info_airport = Main.processCSVDatas.toHashMap();
			for (TAirport tAirp : this.info_airport.values()) {
				Nodo n = new Nodo(tAirp, expHReader.getUmbral(tAirp.getCountry()));
				this.nodos.put(tAirp.getId(), n);
			}
		}*/
		
		for (TAirport tAirp : this.info_airport.values()) {
			Nodo n = new Nodo(tAirp, expHReader.getUmbral(tAirp.getCountry()));
			this.nodos.put(tAirp.getId(), n);
		}
		
		this.red.setNodos(nodos);
		this.generaRed();
		//System.out.println(red);
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
                
                //System.out.println(linea[3]);
                //System.out.println(linea[3]);
                //System.out.println(linea[5]);
                if(!linea[3].equalsIgnoreCase("N") && !linea[5].equalsIgnoreCase("N")) {
                	
                	Nodo n1 = nodos.get(Integer.parseInt(linea[3]));
                    
                    Nodo n2 = nodos.get(Integer.parseInt(linea[5]));
                    
                    Arista a = new Arista(n1, n2, 1);
                    
                    red.add(a);	
                }                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	public HashMap<Integer, Nodo> getNodos() {
		return this.nodos;
	}
	public Red getRed() {
		return this.red;
	}
}
