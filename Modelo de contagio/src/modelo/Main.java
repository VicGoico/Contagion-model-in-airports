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
		HashMap<Integer, Nodo> nodos = new HashMap<Integer, Nodo>();
		red = new Red(nodos);

		new CorrespondingCountry();

		// Se lee cada fichero CSV procesando cada fila a partir de los datos que se
		// llevan hasta el momento
		ExpenditureHealthReader expHReader = new ExpenditureHealthReader(fileNameCSVSalud);
		PIBReader pibReader = new PIBReader(fileNameCSVPIB);
		AirportNodesReader nodesReader = new AirportNodesReader(fileNameCSVNodos, nodos);
		AristasReader aristasReader = new AristasReader(fileNameCSVAristas, red, nodos);
	}
	
	public static void cargarUmbral() throws IOException {
		UmbralPIBSalud umbralPIBSalud = new UmbralPIBSalud(red);
	}

	public static void comenzarInfeccion(Nodo foco) {
		UmbralesModificaciones modelo = new UmbralesModificaciones();
		//SI modelo = new SI();
		//SIR modelo = new SIR();
		//SIRConMejora modelo = new SIRConMejora();
		modelo.simular(red, red.getNodo(foco.getId()));

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
