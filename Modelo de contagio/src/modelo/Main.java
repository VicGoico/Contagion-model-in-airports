package modelo;

import java.util.ArrayList;

import javax.swing.JFrame;

public class Main {
	private static JFrame frame;
	private static Vista vista;
	private static VentanaControl control;
	private static CargarRed cr;
	private static HelloUnfoldingWorld papplet;
	protected static String OUTPUTFILENAME_PROCESSEDDATA = "test.csv";
	protected static makeUsesfullCSV processCSVDatas;

	public static void main(String[] args) {
		vista = new Vista();
		
		frame = new JFrame("Bienvenidos al lector de datos");
		frame.setSize(450, 420);
		frame.setContentPane(vista);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		processCSVDatas = new makeUsesfullCSV();
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
