package modelo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JFrame;

import modelo.CSVReaders.AirportNodesReader;
import modelo.CSVReaders.ExpenditureHealthReader;
import modelo.metricas.tools.CSVFileProcessor;
import modelo.metricas.tools.CorrespondingCountry;

public class Main {
	private static JFrame frame;
	private static Vista vista;
	private static VentanaControl control;
	private static CargarRed cr;
	private static HelloUnfoldingWorld papplet;
	protected static String OUTPUTFILENAME_PROCESSEDDATA = "test.csv";
	protected static makeUsesfullCSV processCSVDatas = null;

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
		
		HashMap<Integer, TAirport> airports = new HashMap<>();
		AirportNodesReader apReader = new AirportNodesReader("nodos.csv", airports);
		ExpenditureHealthReader expHReader = new ExpenditureHealthReader("salud.csv");
		
		
		new CorrespondingCountry();
		
		airports.forEach(new BiConsumer<Integer, TAirport>() {
			@Override
			public void accept(Integer t, TAirport u) {
				System.out.println(t + "\t" + u.getCountry() + "\t\t" + expHReader.getUmbral(u.getCountry()));
			}
		});
		
	}

	public static void CargarRed() {
		cr = new CargarRed(OUTPUTFILENAME_PROCESSEDDATA);
		control = new VentanaControl(cr.getNodos());
		
		frame.setContentPane(control);
		frame.setSize(622, 307);
	}

	public static void comenzarInfeccion(Nodo foco) {
		ModeloContagio modelo = new ModeloContagio();
		modelo.simular(cr.getRed(), cr.getRed().nodos.get(foco.getValue()));

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
