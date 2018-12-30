package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class makeUsesfullCSV {
	// Atributos
	private HashMap<String, TCountries> umbral_countries;
	private ArrayList<String> name_of_countries;
	
	private double maxDestinaUnPaisASanidadPorHabitante;
	private double mediaDeLoQueDestinanLosPaisesASanidad;
	
	//Despu�s de los c�culos el m�ximo es 6892.397 y la media 616.1730162979347
	//private int opcion;
	
	
	// Constructora
	public makeUsesfullCSV(){
		this.umbral_countries = new HashMap<String, TCountries>();
		this.name_of_countries = new ArrayList<String>();
		this.maxDestinaUnPaisASanidadPorHabitante = 0.0;
		
		//this.opcion = opcion;
		this.menu();
	}
	private void menu() {
		// Primero leo los datos de los aeropuertos sin el umbral
		this.readDatas("nodos.csv");
		// Leo el fichero que contiene informacion sobre paises, en cuanto
		// al nivel de inversion en salud
		// y calculo el umbral
		this.readDatas2("salud.csv","Current health expenditure (% of GDP)");
		this.readDatas3("PIB.csv", "GDP per capita (US dollars)");
		
		// Guardo los datos de los aeropuertos en un nuevo csv llamado
		// test.csv
		System.out.println("maximo " + maxDestinaUnPaisASanidadPorHabitante);
		mediaDeLoQueDestinanLosPaisesASanidad = mediaDeLoQueDestinanLosPaisesASanidad / umbral_countries.size();
		System.out.println("media " + mediaDeLoQueDestinanLosPaisesASanidad);
		actualizaUmbralGlobal();
		actualizaUmbralLocal();
		this.guardar();
	}
	
	private void actualizaUmbralGlobal() {
		for (Map.Entry<String, TCountries> entry : umbral_countries.entrySet()) {
			
			double cantidadDestinaPais = entry.getValue().getList().get(0).getUmbral();
			double maxauxUmbral = 0.0;
			
			if(cantidadDestinaPais > mediaDeLoQueDestinanLosPaisesASanidad) {
				//Al ser mayor que la media, ser� mayor a 0.5 el humbral
				if(cantidadDestinaPais >= (maxDestinaUnPaisASanidadPorHabitante) -
						(maxDestinaUnPaisASanidadPorHabitante / 4)){
					//Si es mayor que las tres cuartas partas de lo que destina el que m�s
					maxauxUmbral = 0.85;
				}
				else if(cantidadDestinaPais >= (maxDestinaUnPaisASanidadPorHabitante) -
						(maxDestinaUnPaisASanidadPorHabitante / 3)) {
					maxauxUmbral = 0.75;
				}
				else if(cantidadDestinaPais >= (maxDestinaUnPaisASanidadPorHabitante) -
						(maxDestinaUnPaisASanidadPorHabitante / 2)) {
					maxauxUmbral = 0.7;
				}
				else {
					if(cantidadDestinaPais > mediaDeLoQueDestinanLosPaisesASanidad *2) {
						maxauxUmbral = 0.6;
					}
					else {
						maxauxUmbral = 0.5;
					}
				}
			}
			else {
				if(cantidadDestinaPais > mediaDeLoQueDestinanLosPaisesASanidad /2) {
					maxauxUmbral = 0.2;
				}
				else if(cantidadDestinaPais > mediaDeLoQueDestinanLosPaisesASanidad /2) {
					maxauxUmbral = 0.1;
				}
				else {
					maxauxUmbral = cantidadDestinaPais/ maxDestinaUnPaisASanidadPorHabitante;
				}	
			}
			for (TAirport tAirport : this.umbral_countries.get(entry.getKey()).getList()) {
				tAirport.setUmbral(maxauxUmbral);
			}
			System.out.println("Pais " + entry.getKey() + " umbral " + entry.getValue().getList().get(0).getUmbral());
		}
	}
	
	private void actualizaUmbralLocal() {
		
		for (Map.Entry<String, TCountries> entry : umbral_countries.entrySet()) {
			if(entry.getKey().equalsIgnoreCase("Germany")) {
				System.out.println("pausa");
			}
			double totalVuelosPais = entry.getValue().getMaxDegree();
			
			double maxLocal = 0.0;
			for (TAirport tAirport : this.umbral_countries.get(entry.getKey()).getList()) {
				if(tAirport.getDegree() > maxLocal) {
					maxLocal = tAirport.getDegree();
				}
			}
			for (TAirport tAirport : this.umbral_countries.get(entry.getKey()).getList()) {
				
				if((tAirport.getDegree() / maxLocal) >= 0.35) {
					//Dejamos el umbral maximo
				}
				else if((tAirport.getDegree() / maxLocal) >= 0.15) {
					//Si hace entre el 20 y el 40 % de los vuelos del pais penalizamos con un 10% del umbral
					double nuevoUmbral = tAirport.getUmbral() - (tAirport.getUmbral()*0.1);
					if(nuevoUmbral < 0.0) {
						nuevoUmbral = 0.0;
					}
					tAirport.setUmbral(nuevoUmbral);
				}
				else {
					//Si hace entre el 20 y el 40 % de los vuelos del pais penalizamos con un 20% del umbral
					double nuevoUmbral = tAirport.getUmbral() - (tAirport.getUmbral()*0.2);
					if(nuevoUmbral < 0.0) {
						nuevoUmbral = 0.0;
					}
					tAirport.setUmbral(nuevoUmbral);
				}
				
			}
			
		}
	}
	// Guardo la informacion de los aeropuertos con su umbral calculado
	private void guardar() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("test.csv"));
			StringBuilder sb = new StringBuilder();
			// Escribo la primera linea con los nombres de las columnas
			sb.append("Id");
			sb.append(',');
			sb.append("Label");
			sb.append(',');
			sb.append("timeset");
			sb.append(',');
			sb.append("name");
			sb.append(',');
			sb.append("city");
			sb.append(',');
			sb.append("country");
			sb.append(',');
			sb.append("iata");
			sb.append(',');
			sb.append("icao");
			sb.append(',');
			sb.append("latitude");
			sb.append(',');
			sb.append("longitude");
			sb.append(',');
			sb.append("altitude");
			sb.append(',');
			sb.append("indegree");
			sb.append(',');
			sb.append("outdegree");
			sb.append(',');
			sb.append("degree");
			sb.append(',');
			sb.append("umbral");
			sb.append('\n');
			
			// Escribir los datos en el csv
			for(String country :this.name_of_countries){
				for (TAirport tAirport : this.umbral_countries.get(country).getList()) {
					sb.append(tAirport.getId());
					sb.append(',');
					sb.append("");
					sb.append(',');
					sb.append("");
					sb.append(',');
					sb.append(tAirport.getName());
					sb.append(',');
					sb.append(tAirport.getCity());
					sb.append(',');
					sb.append(tAirport.getCountry());
					sb.append(',');
					sb.append(tAirport.getIata());
					sb.append(',');
					sb.append(tAirport.getIcao());
					sb.append(',');
					sb.append(tAirport.getLatitude());
					sb.append(',');
					sb.append(tAirport.getLongitude());
					sb.append(',');
					sb.append(tAirport.getAltitude());
					sb.append(',');
					sb.append(tAirport.getIndegree());
					sb.append(',');
					sb.append(tAirport.getOutdegree());
					sb.append(',');
					sb.append(tAirport.getDegree());
					sb.append(',');
					
					//tAirport.setUmbral(tAirport.getUmbral()/this.max_umbral);
					sb.append(tAirport.getUmbral());
					sb.append('\n');
				}
			}
			pw.write(sb.toString());
		//	System.out.println("Maximo umbral: "+this.max+ "Pais: " + this.c);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}	
	}
	private void readDatas3(String csvFile, String PIB) {
		BufferedReader br = null;
		String line = "";
		double umbral = 0.0;
		String country = "", pro = "GDP in current prices (millions of US dollars)";
		int numOfYears = 0;
		boolean cierto = true;
		int vueltas = 755;
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			
			while ((line = br.readLine()) != null) {
				// Este if es para no leer la primera linea, ya que es donde
				// vienen todos los nombres de los campos
				if (vueltas == 0) {
					String[] data = line.split(",");
						// Es el mismo pais, de otro a�o
						if (country.equalsIgnoreCase(data[1]) && PIB.equalsIgnoreCase(data[3])) {
							String support = "";
							boolean esNumero;
							// Compruebo si tiene decimales
							try{
								Integer.parseInt(data[5].replaceAll("\"", ""));
								esNumero = true;
							}
							catch(NumberFormatException e){
								esNumero = false;
							}
							String aux;
							if(esNumero){
								//Double numero = new Double(data[4].replaceAll("\"", "")+data[5].replaceAll("\"", "")) * 1000;
								aux = data[4]+data[5];
							}
							else{
								aux = data[4];
							}
							for(int i = 0; i < aux.length(); i++){
								if(!aux.substring(i, i + 1).equals("\"")){
									support += aux.substring(i, i + 1);
								}
							}
							//System.out.println(support+"	"+data[2]+"	"+data[0]);
							
							umbral += Double.parseDouble(support);
							numOfYears++;
						}
						// Cuando cambiamos de pais
						else if (!country.equalsIgnoreCase(data[1])) {
							cierto = true;
							country = data[1];
							umbral = 0;
							numOfYears = 0;
						}
						// Aqui es donde se realiza toda la magia de los calculos, para el umbral
						// y en el guardar le daremos el retoque final
						else if (!PIB.equalsIgnoreCase(data[3]) && cierto && !pro.equalsIgnoreCase(data[3])) {
								cierto = false;
								umbral = (umbral / numOfYears);// Esto es el PIB total
								if(country.equalsIgnoreCase("Spain")) {
									System.out.println("espa�a");
								}
								if (this.umbral_countries.containsKey(country)) {
									//System.out.println(this.umbral_countries.get(country).getList().get(0).getUmbral());
									double cuentas = umbral* this.umbral_countries.get(country).getList().get(0).getUmbral();
									System.out.println("el pais " + data[1] + " destina "+
									cuentas + " $ por habitante ");
									mediaDeLoQueDestinanLosPaisesASanidad += cuentas;
									if(cuentas > maxDestinaUnPaisASanidadPorHabitante) {
										maxDestinaUnPaisASanidadPorHabitante = cuentas;
									}
									
									for (TAirport tAirport : this.umbral_countries.get(country).getList()) {
										tAirport.setUmbral(cuentas);
									}
									/*this.max_umbral += cuentas;
									ArrayList<TAirport> listaActualizada = new ArrayList<TAirport>();
									
									for (TAirport tAirport : this.umbral_countries.get(country).getList()) {
										//double gastaElPaisDelPIBEnSalud = cuentas*(tAirport.getUmbral());// PIB medion * el tanto % que se gasta en sanidad en ese pais
										System.out.println(this.umbral_countries.get(tAirport.getCountry()).getMaxDegree());
										umbral = tAirport.getDegree()/this.umbral_countries.get(tAirport.getCountry()).getMaxDegree();
										System.out.println("Tanto % que le corresponde por la afluencia: "+umbral);
										//umbral *= cuentas;
										tAirport.setUmbral(umbral);
										listaActualizada.add(tAirport);
									}
									this.umbral_countries.get(country).setList(listaActualizada);
									ArrayList<TAirport>auxl = this.umbral_countries.get(country).getList();
									System.out.println("Pausa");
									*/
								}
								umbral = 0;
								numOfYears = 0;
						}
				
				} 
				else {
					// Para no leer las 2 primeras lineas
					vueltas--;
				}
			}
		}
		 catch (FileNotFoundException e) {
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

	
	private void readDatas2(String csvFile, String PIB) {
		BufferedReader br = null;
		String line = "";
		double umbral = 0.0;
		String country = "";
		int numOfYears = 0;
		boolean cierto = true;
		int vueltas = 2;
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			
			while ((line = br.readLine()) != null) {
				// Este if es para no leer la primera linea, ya que es donde
				// vienen todos los nombres de los campos
				if (vueltas == 0) {
					String[] data = line.split(",");
					
					// Es el mismo pais, de otro a�o
					if(country.equalsIgnoreCase(data[1]) && PIB.equalsIgnoreCase(data[3])){
						umbral += Double.parseDouble(data[4]);
						numOfYears++;
					}
					// Cuando cambiamos de pais
					else if (!country.equalsIgnoreCase(data[1])){
						cierto = true;
						country = data[1];
						umbral =0;
						umbral += Double.parseDouble(data[4]);
						numOfYears = 0;
						numOfYears++;
					}
					// Cuando estoy en el mismo pais, pero pasamos a un dato que no quiero leer
					// Y hago los calculos pertinentes(que son calcular la media de los "Gasto corriente en salud (% del PIB)")
					else if(!PIB.equalsIgnoreCase(data[3]) && cierto){
						cierto = false;
						umbral = ((umbral/numOfYears))/100;
						if(this.umbral_countries.containsKey(country)){
							for(TAirport tAirport : this.umbral_countries.get(country).getList()){
								tAirport.setUmbral(umbral);
							}
						}
						umbral = 0;
						numOfYears = 0;
					}
				} 
				else {
					// Para no leer las 2 primeras lineas
					vueltas--;
				}
				
			}
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

	
	public void readDatas(String csvFile){
		BufferedReader br = null;
		String line = "";
		// Me declaro un HashMap para ver unos determinados paises que en un archivo 
		// se llaman de una forma y en el otro de otra
		HashMap<String, String> namesOfCountrys = new HashMap<String, String>();
		namesOfCountrys.put("Congo (Kinshasa)", "Congo");
		namesOfCountrys.put("Cape Verde", "Cabo Verde");
		namesOfCountrys.put("Tanzania", "United Rep. of Tanzania");
		namesOfCountrys.put("Czech Republic", "Czechia");
		namesOfCountrys.put("Macedonia", "TFYR of Macedonia");
		namesOfCountrys.put("Moldova", "Republic of Moldova");
		namesOfCountrys.put("Iran", "Iran (Islamic Republic of)");
		namesOfCountrys.put("South Korea", "Republic of Korea");
		namesOfCountrys.put("Bolivia", "Bolivia (Plurin. State of)");
		namesOfCountrys.put("Venezuela", "Venezuela (Boliv. Rep. of)");
		namesOfCountrys.put("Russia", "Russian Federation");
		namesOfCountrys.put("Laos", "Lao People's Dem. Rep.");
		namesOfCountrys.put("Vietnam", "Viet Nam");
		namesOfCountrys.put("United States", "United States of America");
		// Habria otro pais que es Greenland que habria que cambiarlo por Denmark, ya que Groenlandia pertenece a Dinamarca
		
		
		
		try {
			TAirport tAirport;
			br = new BufferedReader(new FileReader(csvFile));
			boolean cierto = true;
			while ((line = br.readLine()) != null) {
				// Este if es para no leer la primera linea, ya que es donde
				// vienen todos los nombres de los campos
				if (!cierto) {
					// String aux = line;
					String[] data = new String[12];
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
					data[11] = linea;
					
					// Aqui es donde le cambio el nombre para que sea igual en el otro archivo
					if(namesOfCountrys.containsKey(data[3])){
						data[3] = namesOfCountrys.get(data[3]);
					}

					// Me creo el aeropuerto con sus datos
					tAirport = new TAirport(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
							data[8], data[9], data[10], data[11]);

					// Aqui lo que hago es guardar los aeropuertos en HashMap,
					// Lo ordenado por el nombre del pais
					// Calculo el max Degree
					ArrayList<TAirport> array;
					TCountries tCountrys;
					double degree = Double.parseDouble(data[11]);
					
					if(this.umbral_countries.containsKey(tAirport.getCountry())){
						tCountrys = this.umbral_countries.get(tAirport.getCountry());
						array = tCountrys.getList();
						array.add(tAirport);
						tCountrys.setList(array);
						
						tCountrys.setMaxDegree(degree+tCountrys.getMaxDegree());
						
						this.umbral_countries.put(tAirport.getCountry(), tCountrys);
					}
					else{
						this.name_of_countries.add(tAirport.getCountry());
						array = new ArrayList<TAirport>();
						tCountrys = new TCountries();
						array.add(tAirport);
						tCountrys.setList(array);
						tCountrys.setMaxDegree(degree);
						this.umbral_countries.put(tAirport.getCountry(), tCountrys);
					}
					
				}
				// Lo ponemos a false, para que empiece a leer la informacion
				else {
					cierto = false;
				}

			}
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
