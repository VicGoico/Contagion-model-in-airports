package modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.BiConsumer;
import javax.swing.JFrame;

import modelo.CSVReaders.AirportNodesReader;
import modelo.CSVReaders.AristasReader;
import modelo.CSVReaders.ExpenditureHealthReader;
import modelo.CSVReaders.PIBReader;
import modelo.metricas.tools.CorrespondingCountry;
import modelo.modelos.SI;
import modelo.modelos.SIR;
import modelo.modelos.SIRConMejora;
import modelo.modelos.UmbralesModificaciones;
import modelo.red.Nodo;
import modelo.red.Red;
import umbrales.UmbralPIBSalud;
import vista.VentanaControl;
import vista.Vista;

public class Main {
	public static String OUTPUTFILENAME_PROCESSEDDATA = "test.csv";
	public static String AIRPORT_NODES_FILENAME = "nodos.csv";
	public static String AIRPORT_ARISTAS_FILENAME = "aristas.csv";
	public static String EXPENDITUREHEALTH_FILENAME = "salud.csv";
	public static String PIB_FILENAME = "PIB.csv";
	public static String CARBONDIOXIDE_FILENAME = "CarbonDioxideEmissionEstimates.csv";

	private static JFrame frame;
	private static Vista vista;
	private static VentanaControl control;
	private static HelloUnfoldingWorld papplet;
	private static Red red;

	public static void main(String[] args) {
		vista = new Vista();
		frame = new JFrame("Simulador de infeccion en aeropuertos");
		frame.setSize(500, 550);
		frame.setContentPane(vista);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void loadVentanaControl() {
		control = new VentanaControl(red.getNodos());
		frame.setContentPane(control);
		frame.setSize(500, 500);
	}

	/**
	 * Carga la "red" formada por los distintos datos cargados de los CSVs
	 * 
	 * @param fileNameCSVNodos   Nombre del fichero CSV que contiene los nodos
	 *                           'aeropuertos'
	 * @param fileNameCSVAristas Nombre del fichero CSV que contiene las aristas
	 * @param fileNameCSVSalud   Nombre del fichero CSV que contiene el gasto en
	 *                           salud respecto al PIB de cada pais
	 * @param fileNameCSVPIB     Nombre del fichero CSV que contiene el PIB de cada
	 *                           pais
	 * @param fileNameCO2        Nombre del fichero CSV que contiene los datos de
	 *                           emision de Dioxido de carbono en cada pais
	 * @throws IOException Excepcion lanzada en caso de un error durante la lectura
	 *                     de algun fichero.
	 */
	public static void cargarRed(String fileNameCSVNodos, String fileNameCSVAristas, String fileNameCSVSalud,
			String fileNameCSVPIB, String fileNameCO2) throws IOException {
		
		// Empezamos a contar el tiempo que tarda a cargar y generar la red de aeropuertos
		long tiempoInicio = System.currentTimeMillis();
		
		int MegaBytes = 1000000;
		
		long freeMemory = Runtime.getRuntime().freeMemory();
		//long totalMemory = Runtime.getRuntime().totalMemory();
		long maxMemory = Runtime.getRuntime().maxMemory();
		long memoriaUsadaAntes = maxMemory-freeMemory;
		System.out.println("Memoria usada antes de ejecutar nuestra aplicación nuestra aplicacion:	" + (int)(memoriaUsadaAntes/MegaBytes) + "	MegaBytes");
		
		HashMap<Integer, Nodo> nodos = new HashMap<Integer, Nodo>();
		red = new Red(nodos);

		new CorrespondingCountry();

		// Se lee cada fichero CSV procesando cada fila a partir de los datos que se
		// llevan hasta el momento
		ExpenditureHealthReader expHReader = new ExpenditureHealthReader(fileNameCSVSalud);
		PIBReader pibReader = new PIBReader(fileNameCSVPIB);
		AirportNodesReader nodesReader = new AirportNodesReader(fileNameCSVNodos, nodos);
		AristasReader aristasReader = new AristasReader(fileNameCSVAristas, red, nodos);
		// Acabamos de contar
		long tiempoFinal = System.currentTimeMillis();
		// Restamos el tiempoFinal menos el tiempoInicio para saber la diferencia (en milisegundos)
		long diferenciaTiempo =  tiempoFinal - tiempoInicio;
		System.out.println("Aqui mirar");
		System.out.println("Tiempo que tarda en cargar los ficheros y generar la red: " + diferenciaTiempo + " milisegundos");
		long segundos = diferenciaTiempo/1000;// 1 segundos son 1000 milisegundos
		if(segundos > 0)
			System.out.println("En segundos: " + segundos);
		long minutos = diferenciaTiempo/60000;// 1 minuto son 60000 milisegundos
		if(minutos > 0){
			System.out.println("En minutos: " + minutos);
		}
		
		
		
		// Memoria usada por nuestra aplicación
		freeMemory = Runtime.getRuntime().freeMemory();
		//totalMemory = Runtime.getRuntime().totalMemory();
		maxMemory = Runtime.getRuntime().maxMemory();
		
		System.out.println("Pruebas de memoria");
		
		System.out.println("Memoria usada por nuestra aplicacion:	" + (int)(((maxMemory-freeMemory)-memoriaUsadaAntes)/MegaBytes) + "	MegaBytes");
		/*System.out.println("Memoria libre en JVM:	" + freeMemory + "	MegaBytes");
		System.out.println("Memoria total en JVM:	" + totalMemory + "	MegaBytes");
		System.out.println("Memoria maxima en JVM:	" + maxMemory + "	MegaBytes");*/
		
		
	}
	
	public static void cargarUmbral() throws IOException {
		UmbralPIBSalud umbralPIBSalud = new UmbralPIBSalud(red);
	}

	public static void comenzarInfeccion(Nodo foco) {
		// Empezamos a contar el tiempo que tarda a cargar y generar la red de aeropuertos
		long tiempoInicio = System.currentTimeMillis();
		UmbralesModificaciones modelo = new UmbralesModificaciones(red);
		//SI modelo = new SI(0.6);
		//SIR modelo = new SIR(0.1,0.6);
		//SIRConMejora modelo = new SIRConMejora(0.1,0.6);
		modelo.simular(red.getNodo(foco.getId()));

		ArrayList<Nodo> nodosContagiados = modelo.getNodosContagiados();

		frame.setSize(1200, 700);

		papplet = new HelloUnfoldingWorld();
		papplet.setInfectados(nodosContagiados);
		papplet.frame = frame;

		frame.setResizable(true);
		
		((VentanaControl) frame.getContentPane()).setFullCenterPanel(papplet);
		
		// frame.setContentPane(papplet);
		// frame.add(papplet, BorderLayout.CENTER);
		papplet.init();
		// Acabamos de contar
		long tiempoFinal = System.currentTimeMillis();
		// Restamos el tiempoFinal menos el tiempoInicio para saber la
		// diferencia (en milisegundos)
		long diferenciaTiempo = tiempoFinal - tiempoInicio;
		System.out.println("Aqui mirar");
		System.out.println("Tiempo que tarda en infectar la red con el aeropuerto "+ foco.getAirportInfo().getName()+" del pais "+ foco.getAirportInfo().getCountry() + " : " + diferenciaTiempo + " milisegundos");
		long segundos = diferenciaTiempo / 1000;// 1 segundos son 1000
												// milisegundos
		if (segundos > 0)
			System.out.println("En segundos: " + segundos);
		long minutos = diferenciaTiempo / 60000;// 1 minuto son 60000
												// milisegundos
		if (minutos > 0) {
			System.out.println("En minutos: " + minutos);
		}
		// PApplet.main(new String[] { HelloUnfoldingWorld.class.getName() });
		// HelloUnfoldingWorld world = new HelloUnfoldingWorld();
		// world.frame.setVisible(true);
		// world.setup();
		// world.draw();

	}

	public static void guardar(String outputFileName) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));

		out.write(
				"id,umbral,airportId,name,city,country,iata,icao,latitude,longitude,altitude,calculatedIndegree,calculatedOutdegree,calculatedDegree\n");

		for (Iterator<Nodo> i = red.getNodos().values().iterator(); i.hasNext();) {
			Nodo n = (Nodo) i.next();
			out.write(n.getId() + "," + n.getUmbral() + "," + n.getAirportInfo().toString() + "\n");
		}

		out.close();
	}

}
