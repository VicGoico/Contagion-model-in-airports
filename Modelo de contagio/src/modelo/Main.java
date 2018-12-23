package modelo;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import processing.core.PApplet;

public class Main {

	private static JFrame frame;
	
	
	private static Vista vista;
	private static VentanaControl control;
	
	private static CargarRed cr;
	
	private static HelloUnfoldingWorld papplet;
	
	public static void main(String[] args) {
		frame = new JFrame("Bienvenidos al lector de datos");
		frame.setSize(450, 420);
		frame.setLayout(new BorderLayout());
		vista = new Vista();
		frame.add(vista, BorderLayout.WEST);
		frame.setVisible(true);
		//CargarRed cr = new CargarRed();
		//makeUsesfullCSV hola = new makeUsesfullCSV();

		//frame.setVisible(true);
		//CargarRed cr = new CargarRed();
	}
	
	public static void CargarRed() {
		cr = new CargarRed();
		
		frame.remove(vista);
		frame.setSize(622, 307);
		control = new VentanaControl(cr.getNodos());
		frame.add(control, BorderLayout.WEST);
		frame.setVisible(true);
	}
	
	public static void comenzarInfeccion(Nodo foco) {
		frame.remove(control);
		frame.setSize(1200, 700);
		papplet = new HelloUnfoldingWorld();
		
		papplet.frame = frame;
		frame.setResizable(true);
		frame.add(papplet, BorderLayout.CENTER);
        papplet.init();
		//PApplet.main(new String[] { HelloUnfoldingWorld.class.getName() });
		//HelloUnfoldingWorld world = new HelloUnfoldingWorld();
		//world.frame.setVisible(true);
		//world.setup();
		//world.draw();
		
	}
	

}
