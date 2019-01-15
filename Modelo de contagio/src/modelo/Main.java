package modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import modelo.CSVReaders.AirportNodesReader;
import modelo.CSVReaders.AristasReader;
import modelo.CSVReaders.ExpenditureHealthReader;
import modelo.CSVReaders.PIBReader;
import modelo.CSVWriters.AristasSimplesWriter;
import modelo.metricas.tools.CorrespondingCountry;
import modelo.metricas.tools.Performance;
import modelo.modelos.AristaContagiadaSimple;
import modelo.modelos.Modelo;
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
	public static String OUTPUTFILENAME_PROCESSEDDATA = "resultado";
	public static String AIRPORT_NODES_FILENAME = "nodos.csv";
	public static String AIRPORT_ARISTAS_FILENAME = "aristas.csv";
	public static String EXPENDITUREHEALTH_FILENAME = "salud.csv";
	public static String PIB_FILENAME = "PIB.csv";
	public static String CARBONDIOXIDE_FILENAME = "CarbonDioxideEmissionEstimates.csv";
	public static Red red;
	private static JFrame frame;
	private static Vista vista;
	private static VentanaControl control;
	private static HelloUnfoldingWorld papplet;
	private static Boolean guiMode = true;

	/**
	 * Vease showUsage para mas informacion sobre el paso de parametros
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			vista = new Vista();
			frame = new JFrame("Simulador de infeccion en aeropuertos");
			frame.setSize(500, 550);
			frame.setContentPane(vista);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else {
			guiMode = false;
			if (args.length >= 2 && args.length <= 4) {
				try {
					String modeloS = args[0];
					int focoId = Integer.parseInt(args[1]);
					Modelo modelo = parseModelo(modeloS, args.length > 2 ? args[2] : null,
							args.length > 3 ? args[3] : null);
					
					Main.cargarRed(null, null, null, null, null);
					Main.cargarUmbral();
					Main.comenzarInfeccion(modelo, focoId);
					Main.guardarNodos(OUTPUTFILENAME_PROCESSEDDATA + "-nodos-" + modelo.getClass().getSimpleName()
							+ "-" + new Date().getTime() + ".csv");
					Main.guardarAristasContagiadas(modelo, OUTPUTFILENAME_PROCESSEDDATA + "-aristasContagiadas-"
							+ modelo.getClass().getName() + "-" + new Date().getTime() + ".csv");

				} catch (Exception e) {
					e.printStackTrace();
					Main.showUsage();
				}
			} else if (args.length >= 8) {
				try {
					String modeloS = args[6], rutaCSVResultados = args[5];
					int focoId = Integer.parseInt(args[7]);
					Modelo modelo = parseModelo(modeloS, args.length > 8 ? args[8] : null,
							args.length > 9 ? args[9] : null);
					
					Main.cargarRed(args[0], args[1], args[2], args[3], args[4]);
					Main.cargarUmbral();
					Main.comenzarInfeccion(modelo, focoId);
					Main.guardarNodos(rutaCSVResultados + "-nodos-" + modelo.getClass().getSimpleName() + "-"
							+ new Date().getTime() + ".csv");
					Main.guardarAristasContagiadas(modelo, rutaCSVResultados + "-aristasContagiadas-"
							+ modelo.getClass().getName() + "-" + new Date().getTime() + ".csv");

				} catch (Exception e) {
					e.printStackTrace();
					Main.showUsage();
				}
			} else
				Main.showUsage();
		}
	}

	/**
	 * Crea el modelo que corresponde
	 * 
	 * @param modelo            Nombre del modelo a crear
	 * @param tasaContagioS
	 * @param tasaRecuperaciónS
	 * @return
	 */
	private static Modelo parseModelo(String modelo, String tasaContagioS, String tasaRecuperaciónS) {
		double tasaContagio, tasaRecuperacion;

		switch (modelo.toUpperCase()) {
		case "SI":
			if (tasaContagioS == null)
				throw new IllegalArgumentException("No se ha pasado la tasa de contagio al modelo.");
			tasaContagio = Double.parseDouble(tasaContagioS);
			return new SI(red, tasaContagio);
		case "SIR":
			if (tasaContagioS == null || tasaRecuperaciónS == null)
				throw new IllegalArgumentException("No se ha pasado la tasa de contagio o de recuperacion al modelo.");
			tasaContagio = Double.parseDouble(tasaContagioS);
			tasaRecuperacion = Double.parseDouble(tasaRecuperaciónS);
			return new SIR(red, tasaRecuperacion, tasaContagio);
		case "SIR-MEJORADO":
			if (tasaContagioS == null || tasaRecuperaciónS == null)
				throw new IllegalArgumentException("No se ha pasado la tasa de contagio o de recuperacion al modelo.");
			tasaContagio = Double.parseDouble(tasaContagioS);
			tasaRecuperacion = Double.parseDouble(tasaRecuperaciónS);
			return new SIRConMejora(red, tasaRecuperacion, tasaContagio);
		default:
			return new UmbralesModificaciones(red);
		}
	}

	/**
	 * Carga el panel de control en la ventana actual
	 */
	public static void loadVentanaControl() {
		control = new VentanaControl(red.getNodos());
		frame.setContentPane(control);
		frame.setSize(500, 630);
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
		Performance.Begin("Main.cargarRed");
		Performance.Register("Main.cargarRed");

		HashMap<Integer, Nodo> nodos = new HashMap<Integer, Nodo>();
		Main.red = new Red(nodos);
		new CorrespondingCountry();

		// Procesa cada CSV con los datos que lleva en cada momento
		new ExpenditureHealthReader(fileNameCSVSalud);
		new PIBReader(fileNameCSVPIB);
		new AirportNodesReader(fileNameCSVNodos, nodos);
		new AristasReader(fileNameCSVAristas, red, nodos);

		Performance.Register("Main.cargarRed");
		Performance.getSummary("Main.cargarRed");
	}

	/**
	 * Carga el umbral de salud con PIB
	 * 
	 * @throws IOException
	 */
	public static void cargarUmbral() throws IOException {
		Performance.Begin("Main.cargarUmbral");
		Performance.Register("Main.cargarUmbral");

		new UmbralPIBSalud(red);

		Performance.Register("Main.cargarUmbral");
		Performance.getSummary("Main.cargarUmbral");
	}

	/**
	 * Comienza la infeccion en un modelo desde un foco de infeccion
	 * 
	 * @param modelo Modelo
	 * @param foco   Nodo infectado
	 */
	public static void comenzarInfeccion(Modelo modelo, int foco) {
		
		Performance.Begin("Main.comenzarInfeccion");
		Performance.Register("Main.comenzarInfeccion");

		Nodo nodoInfeccion = red.getNodo(foco);
		
		
		if(nodoInfeccion != null)
			modelo.simular(nodoInfeccion);
		else
			throw new IllegalArgumentException("Foco de infeccion erroneo, la red no contiene ese nodo.");

		guardarAristasContagiadas(modelo,"aristasInfectadas.csv");
		if (Main.guiMode) {
			Performance.Register("Main.comenzarInfeccion");
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
		}
		Performance.Register("Main.comenzarInfeccion");
		Performance.getSummary("Main.comenzarInfeccion");
		restablecer(red);
	}

	/**
	 * Guarda un CSV con las aristas contagiadas del modelo
	 * 
	 * @param modelo
	 * @param fileName Nombre del fichero
	 */
	public static void guardarAristasContagiadas(Modelo modelo, String fileName) {
		// A partir de aquí genera el CSV con las aristas infectadas
		ArrayList<AristaContagiadaSimple> aristasFinalmenteContagiadas = new ArrayList<>(),
				aristasContagiadas = modelo.getAristasContagiadas();
		ArrayList<Nodo> nodosContagiados = modelo.getNodosContagiados();

		for (int i = 0; i < aristasContagiadas.size(); i++) {
			Nodo aux1 = red.getNodo(aristasContagiadas.get(i).getNodo1());
			Nodo aux2 = red.getNodo(aristasContagiadas.get(i).getNodo2());
			if (nodosContagiados.contains(aux1) && nodosContagiados.contains(aux2)) {
				aristasFinalmenteContagiadas.add(aristasContagiadas.get(i));
			}
		}

		try {
			new AristasSimplesWriter().write(aristasFinalmenteContagiadas, fileName);
		} catch (IOException e) {
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame,
					"Ha ocurrido un error al guardar los resultados de la simulacion!\n" + e.getMessage(), "Error!",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Guarda los nodos de una red en un fichero
	 * 
	 * @param outputFileName Nombre del fichero
	 * @throws IOException
	 */
	public static void guardarNodos(String outputFileName) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));

		out.write(
				"id,umbral,contagiado,airportId,name,city,country,iata,icao,latitude,longitude,altitude,calculatedIndegree,calculatedOutdegree,calculatedDegree\n");

		for (Iterator<Nodo> i = red.getNodos().values().iterator(); i.hasNext();) {
			Nodo n = (Nodo) i.next();
			
			out.write(n.getId() + "," + n.getUmbral() + "," + n.isInfectado() + "," + n.getAirportInfo().toString() + "\n");
		}

		out.close();
	}

	/**
	 * Muestra la informacion sobre el uso del programa sin interfaz
	 */
	public static void showUsage() {
		System.out.println("POR FAVOR INGRESE LOS PARAMETROS DE LA SIGUIENTE FORMA:");
		System.out.println(
				"\tprograma.jar <ruta-CSV-Nodos> <ruta-CSV-Aristas> <ruta-CSV-Salud> <ruta-CSV-PIB> <ruta-CSV-CO2> <ruta-CSV-resultados> <modelo> <foco-AeropuertoID> <otros-parametros-modelo>");
		System.out.println(
				"Donde ruta-CSV-xxx es la ruta al archivo de datos xxx en formato CSV; si no se ingresa ninguno, se usara las rutas por defecto.");
		System.out.println(
				"En modelo puede usarse alguna de las siguientes opciones: 'BASADO-EN-UMBRAL' (por defecto), 'SI', 'SIR', 'SIR-MEJORADO' y a continuacion los parametros del modelo si son necesarios.");
		System.out.println("El foto-AeropuertoID es el id del nodo aeropuerto desde el cual comienza la infeccion.");
	}
	private static void restablecer(Red red) {
		for (Map.Entry<Integer, Nodo> entry : red.getNodos().entrySet()) {
		    entry.getValue().setInfectado(false);
		    entry.getValue().setAeropuetosComunicadosInfectados(0);
		}
	}
}
