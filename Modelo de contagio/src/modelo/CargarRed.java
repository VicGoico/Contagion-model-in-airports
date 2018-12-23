package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class CargarRed {
	private Red red;
	private HashMap<Integer, Nodo> nodos;	
	
	private HashMap<Integer,TAirport> info_airport;
	
	private HashMap<String,ArrayList<HashMap<Integer,TAirport>>> countries;
	
	public CargarRed() {
		this.red = new Red();
		this.nodos = new HashMap<Integer, Nodo>();
		//leerNodos();
		this.info_airport = new HashMap<Integer, TAirport>();
		this.countries = new HashMap<String,ArrayList<HashMap<Integer,TAirport>>>();
		
		this.readDatas("test.csv");
		this.red.setNodos(nodos);
		this.generaRed();
		//System.out.println(red);
	}
	
	public void readDatas(String csvFile){
		BufferedReader br = null;
		String line = "";

		try {
			TAirport tAirport;
			br = new BufferedReader(new FileReader(csvFile));
			boolean cierto = true;
			while ((line = br.readLine()) != null) {
				// Este if es para no leer la primera linea, ya que es donde
				// vienen todos los nombres de los campos
				if (!cierto) {
					String aux = line;
					String[] data = new String[13];
					int conta = 0;
					String linea = "";
					boolean libre = false;
					// Recorro toda la linea que he leido del csv
					for (int i = 0; i < line.length(); i++) {
						// Compruebo que puedo guardarme el dato sin guardar
						// basura
						if (!libre && (line.substring(i, i + 1).equals(",")) && !linea.equalsIgnoreCase("")) {
							data[conta] = linea;
							linea = "";
							conta++;
						}
						// Cuando nos encontramos las comillas dobles "", por
						// primera vez
						else if (line.substring(i, i + 1).equals("\"") && !libre) {
							libre = true;
							linea += "\"";
						}
						// Cuando nos encontramos el final de las dobles
						// comillas ""
						else if (line.substring(i, i + 1).equals("\"") && line.substring(i + 1, i + 2).equals(",")) {
							linea += "\"";
							data[conta] = linea;
							linea = "";
							conta++;
							libre = false;
						}
						// Voy sumando las letras de las palabras
						else {
							linea += line.substring(i, i + 1);
							// Por si guardo una coma, la borro
							if (linea.equals(",")) {
								linea = "";
							}
						}
					}
					// Le pongo al final del todo el ultimo dato leido
					data[12] = linea;

					// Me creo el aeropuerto con sus datos
					tAirport = new TAirport(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
							data[8], data[9], data[10], data[11], data[12]);
					
					
					// Guardo el aeropuerto en mi mapa
					this.info_airport.put(Integer.parseInt(data[0]), tAirport);
					
					Nodo n = new Nodo(Integer.parseInt(data[0]), 0, 0.0, tAirport, 0,0);
	                
	                nodos.put(Integer.parseInt(data[0]), n);

					// Miro que no este en el HashMap de paises

					/*
					 * // No esta, pues lo meto if
					 * (!this.countrys.containsKey(data[3])) {
					 * this.countrys.put(data[3], 1); } // Esta, le sumo un mas
					 * para saber cuantos aeropuertos de ese pais tengo else {
					 * Integer cont = this.countrys.get(data[3]); cont++;
					 * this.countrys.put(data[3], cont); }
					 */
				} else {
					cierto = false;
				}

			}
			// System.out.println("Numero de paises: " + this.world.size());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
                
                //System.out.println(linea[3]);
                System.out.println(linea[3]);
                System.out.println(linea[5]);
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
	
}
