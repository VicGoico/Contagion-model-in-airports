package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.function.BiConsumer;
import javax.swing.JFrame;

import modelo.CSVReaders.AirportNodesReader;
import modelo.CSVReaders.AristasReader;
import modelo.CSVReaders.ExpenditureHealthReader;
import modelo.CSVReaders.PIBReader;
import modelo.metricas.tools.CorrespondingCountry;
import modelo.red.Arista;
import modelo.red.Nodo;
import modelo.red.Red;
import vista.VentanaControl;
import vista.Vista;

public class Main {
	public static String OUTPUTFILENAME_PROCESSEDDATA = "test.csv";
	public static String AIRPORT_NODES_FILENAME = "nodos.csv";
	public static String AIRPORT_ARISTAS_FILENAME = "aristas.csv";
	public static String EXPENDITUREHEALTH_FILENAME = "salud.csv";
	public static String PIB_FILENAME = "PIB.csv";
	
	
	private static JFrame frame;
	private static Vista vista;
	private static VentanaControl control;
	private static HelloUnfoldingWorld papplet;
	private static Red red;
	

	public static void main(String[] args) throws IOException {
		
		/*
		vista = new Vista();
		
		frame = new JFrame("Bienvenidos al lector de datos");
		frame.setSize(450, 420);
		frame.setContentPane(vista);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 */
		
		// processCSVDatas = new makeUsesfullCSV();
		
		// new CSVFileProcessor("CarbonDioxideEmissionEstimates.csv", );
		
		
		HashMap<Integer, Nodo> nodos = new HashMap<>();
		red = new Red(nodos);
		new CorrespondingCountry();
		
		ExpenditureHealthReader expHReader = new ExpenditureHealthReader(EXPENDITUREHEALTH_FILENAME);
		PIBReader pibReader = new PIBReader(PIB_FILENAME);
		AirportNodesReader nodesReader = new AirportNodesReader(AIRPORT_NODES_FILENAME, nodos);
		AristasReader aristasReader = new AristasReader(AIRPORT_ARISTAS_FILENAME, red, nodos);
		
		// IGNORAR DE AQUI EN ADELANTE
		BufferedWriter out = new BufferedWriter(new FileWriter("temp" + new Date().getTime() + ".csv"));
		
		HashMap<String, Boolean> lola = new HashMap<>();
		
		nodos.forEach(new BiConsumer<Integer, Nodo>() {
			@Override
			public void accept(Integer t, Nodo n) {
				TAirport u = n.getAirportInfo();
				
				System.out.println(t + "\t" + u.getCountry() + "\t\t" + n.getUmbral() + "\t\t\t" + n.getAirportInfo().getDegree());
				try {
					if(!lola.containsKey(u.getCountry())) {
						out.write(t + "," + u.getCountry() + "," + expHReader.getUmbral(u.getCountry()) + "\n");
						lola.put(u.getCountry(), true);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		out.close();
		
		/* cr = new CargarRed(OUTPUTFILENAME_PROCESSEDDATA, airports, expHReader);
		control = new VentanaControl(cr.getNodos());
		
		frame = new JFrame("Bienvenidos al lector de datos");
		frame.setSize(450, 420);
		frame.setContentPane(control);
		frame.setSize(622, 307);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		*/
	}

	public static void CargarRed() {
		/*cr = new CargarRed(OUTPUTFILENAME_PROCESSEDDATA, airports);
		control = new VentanaControl(cr.getNodos());
		
		frame.setContentPane(control);
		frame.setSize(622, 307);*/
	}

	public static void comenzarInfeccion(Nodo foco) {
		ModeloContagio modelo = new ModeloContagio();
		modelo.simular(red, red.getNodo(foco.getId()));

		ArrayList<Nodo> nodosContagiados = modelo.getNodosContagiados();

		frame.setSize(1200, 700);
		
		papplet = new HelloUnfoldingWorld();
		papplet.setInfectados(nodosContagiados);
		papplet.frame = frame;
		
		frame.setResizable(true);
		frame.setContentPane(papplet);
		// frame.add(papplet, BorderLayout.CENTER);
		papplet.init();

		// PApplet.main(new String[] { HelloUnfoldingWorld.class.getName() });
		// HelloUnfoldingWorld world = new HelloUnfoldingWorld();
		// world.frame.setVisible(true);
		// world.setup();
		// world.draw();

	}

}
