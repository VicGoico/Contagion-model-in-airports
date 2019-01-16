package modelo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import modelo.modelos.UmbralesModificaciones;
import modelo.red.Nodo;
import modelo.red.Red;
import umbrales.UmbralPIBSalud;
import vista.VentanaControl;
import vista.Vista;

public class Main {
	public static String OUTPUTFILENAME_PROCESSEDDATA = "resultado";
	public static String AIRPORT_NODES_FILENAME = "nodosCambiados2.csv";
	public static String AIRPORT_ARISTAS_FILENAME = "aristasCambiadas2.csv";
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
					Main.guardarNodos(OUTPUTFILENAME_PROCESSEDDATA + "-nodos-" + modelo.getClass().getSimpleName() + "-"
							+ new Date().getTime() + ".csv");

					Main.guardarResultadosInfeccion(modelo);
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
					Main.guardarResultadosInfeccion(modelo);
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
			/*
			 * if (tasaContagioS == null || tasaRecuperaciónS == null) throw new
			 * IllegalArgumentException("No se ha pasado la tasa de contagio o de recuperacion al modelo."
			 * ); tasaContagio = Double.parseDouble(tasaContagioS); tasaRecuperacion =
			 * Double.parseDouble(tasaRecuperaciónS); return new SIRConMejora(red,
			 * tasaRecuperacion, tasaContagio);
			 */
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

		ArrayList<Nodo> nodos = new ArrayList<Nodo>();
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

		/*Performance.Begin("Main.comenzarInfeccion");
		Performance.Register("Main.comenzarInfeccion");*/
		long empieza = System.currentTimeMillis();
		int MegaBytes = 1000000;
		
		long freeMemory = Runtime.getRuntime().freeMemory();
		long totalMemory = Runtime.getRuntime().totalMemory();
		long maxMemory = Runtime.getRuntime().maxMemory();
		long memoriaUsadaAntes = maxMemory-freeMemory;
		
		Nodo nodoInfeccion = red.getNodo(foco);

		if (nodoInfeccion != null)
			modelo.simular(nodoInfeccion);
		else
			throw new IllegalArgumentException("Foco de infeccion erroneo, la red no contiene ese nodo.");

		/*
		 * guardarAristasContagiadas(modelo,"aristasInfectadas.csv");
		 * 
		 * try { guardarInfeccionEnFuncionDeT(modelo,"infeccionEnT.csv"); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */

		if (Main.guiMode) {
			//Performance.Register("Main.comenzarInfeccion");
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
		long acaba = System.currentTimeMillis();
		
		System.out.println("Tiempo que ha tardao: "+ (acaba-empieza));
		// Memoria usada por nuestra aplicacion
		freeMemory = Runtime.getRuntime().freeMemory();
		totalMemory = Runtime.getRuntime().totalMemory();
		maxMemory = Runtime.getRuntime().maxMemory();

		System.out.println("Pruebas de memoria");

		System.out.println("Memoria usada por nuestra aplicacion:	"
				+ (int) (((totalMemory - freeMemory)) / MegaBytes) + "	MegaBytes");
		/*Performance.Register("Main.comenzarInfeccion");
		Performance.getSummary("Main.comenzarInfeccion");*/
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

		for (Nodo n : red.getNodos()) {
			out.write(n.getId() + "," + n.getUmbral() + "," + n.isInfectado() + "," + n.getAirportInfo().toString()
					+ "\n");

		}
		out.close();

	}

	/**
	 * Guarda un estadistico del numero de infectados a lo largo del tiempo
	 * 
	 * @param modelo
	 * @param outputFileName
	 * @throws IOException
	 */
	public static void guardarInfeccionEnFuncionDeT(Modelo modelo, String outputFileName) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));

		out.write("tiempo,infectados\n");
		int i = 0;
		//int total = 0;
		for (ArrayList<Integer> instante : modelo.getInfeccionTiempo()) {
			//total += instante.size();
			out.write(i + "," + instante.size() + System.getProperty("line.separator"));
			i += 1;
		}
		out.close();
	}

	/**
	 * Guarda los aeropuertos contagiados en un instante de tiempo
	 * 
	 * @param modelo
	 * @param outputFileName
	 * @param instante
	 * @throws IOException
	 */
	public static void guardarInfeccionEnFuncionDeTDetallada(Modelo modelo, String outputFileName, int instante)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));

		out.write(
				"id,umbral,contagiado,airportId,name,city,country,iata,icao,latitude,longitude,altitude,calculatedIndegree,calculatedOutdegree,calculatedDegree\n");

		for (int i = 0; i < instante; i++) {
			for (Integer k : modelo.getInfeccionTiempo().get(i)) {
				Nodo n = red.getNodo(k);
				out.write(n.getId() + "," + n.getUmbral() + "," + n.isInfectado() + "," + n.getAirportInfo().toString()
						+ "\n");
			}
		}

		out.close();
	}
	public static void guardarRecuperados(Modelo modelo, String outputFileName) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));

		out.write("tiempo,recuperados\n");
		int i = 0;
		for (ArrayList<Integer> instante : modelo.getInfeccionRecuperados()) {
			out.write(i + "," + instante.size() + System.getProperty("line.separator"));
			i += 1;
		}
		out.close();
	}

	/**
	 * Permite guardar los resultados de la infeccion
	 * 
	 * @param modelo
	 * @throws IOException
	 */
	public static void guardarResultadosInfeccion(Modelo modelo) throws IOException {
		Main.guardarAristasContagiadas(modelo, OUTPUTFILENAME_PROCESSEDDATA + "-aristasContagiadas-"
				+ modelo.getClass().getName() + "-" + new Date().getTime() + ".csv");
		Main.guardarInfeccionEnFuncionDeT(modelo, OUTPUTFILENAME_PROCESSEDDATA + "-infeccionEnT-"
				+ modelo.getClass().getName() + "-" + new Date().getTime() + ".csv");
		
		if(modelo.getInfeccionRecuperados() != null) {
			Main.guardarRecuperados(modelo, OUTPUTFILENAME_PROCESSEDDATA + "-aeropuertosRecuperados-"
				+ modelo.getClass().getName() + "-" + new Date().getTime() + ".csv");
		}
		
		System.out.println("PAUSA");
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
		for (Nodo entry : red.getNodos()) {
			entry.setInfectado(false);
			entry.setAeropuetosComunicadosInfectados(0);
		}
	}
}
