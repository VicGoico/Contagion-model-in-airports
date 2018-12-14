package modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class downloadDatas {
	private HashMap<Integer,TAirport> info_airport;
	private HashMap<String,ArrayList<HashMap<Integer,TAirport>>> countrys;
	
	public downloadDatas(){
		this.info_airport = new HashMap<Integer, TAirport>();
		this.countrys = new HashMap<String,ArrayList<HashMap<Integer,TAirport>>>();
		
		this.readDatas();
	}
	
	
	public void readDatas(){
		String csvFile = "src/tabla exportada de nodos.csv";// Poner la ruta del csv
		BufferedReader br = null;
		String line = "";

		try {
			/*HashMap<String,TAirport>mundo = new HashMap<String, TAirport>();
			HashMap<String,Integer> countrys = new HashMap<String, Integer>();*/
			TAirport tAirport;
			br = new BufferedReader(new FileReader(csvFile));
			boolean cierto = true;
			while ((line = br.readLine()) != null) {
				// Este if es para no leer la primera linea, ya que es donde vienen todos los nombres de los campos
				if(!cierto){
					String aux = line;
					// use comma as separator
					//String[] data = line.split(coma);					
					//String prueba = "234,,,\"Hola,amigos\",pepe";
					String[] data = new String[12];
					int conta = 0;
					String linea = "";
					boolean libre = false;
					// Recorro toda la linea que he leido del csv
					for(int i= 0; i < line.length(); i++){
						// Compruebo que puedo guardarme el dato sin guardar basura
						if(!libre && (line.substring(i, i+1).equals(","))&& !linea.equalsIgnoreCase("")){
							data[conta] = linea; 
							//System.out.println(linea);
							linea = "";
							conta++;
						}
						// Cuando nos encontramos las comillas dobles "", por primera vez
						else if(line.substring(i, i+1).equals("\"") &&!libre){
							libre = true;
							linea += "\"";
						}
						// Cuando nos encontramos el final de las dobles comillas ""
						else if(line.substring(i, i+1).equals("\"") && line.substring(i+1, i+2).equals(",")){
								linea += "\"";
								//System.out.println(linea);
								data[conta] = linea;
								linea="";
								conta++;
								libre = false;
						}
						// Voy sumando las letras de las palabras
						else{
							linea += line.substring(i, i+1);
							// Por si guardo una coma, la borro
							if(linea.equals(",")){
								linea = "";
							}
						}	
					}
					// Le pongo al final del todo el ultimo dato leido
					data[11] = linea;
					//System.out.println("Datos [id= " + data[0] + " , name ="+data[3]+" , city ="+data[4]+ " , country =" + data[5] + "]");
					
					// Me creo el aeropuerto con sus datos
					tAirport = new TAirport(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
							data[8], data[9], data[10], data[11]);
					
					// Guardo el aeropuerto en mi mapa
					this.info_airport.put(Integer.parseInt(data[0]), tAirport);
					
					// Miro que no este en el HashMap de paises
					
					/*// No esta, pues lo meto
					if (!this.countrys.containsKey(data[3])) {
						this.countrys.put(data[3], 1);
					} 
					// Esta, le sumo un mas para saber cuantos aeropuertos de ese pais tengo
					else {
						Integer cont = this.countrys.get(data[3]);
						cont++;
						this.countrys.put(data[3], cont);
					}*/
				}
				else{
					cierto = false;
				}

			}
			//System.out.println("Numero de paises: " + this.world.size());

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
	

}
