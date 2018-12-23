package modelo;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main {

	private static JFrame frame;
	
	
	private static Vista vista;
	private static VentanaControl control;
	
	private static CargarRed cr;
	
	public static void main(String[] args) {
		frame = new JFrame("Bienvenidos al lector de datos");
		frame.setSize(450, 420);
		frame.setLayout(new BorderLayout());
		vista = new Vista();
		frame.add(vista, BorderLayout.WEST);
		frame.setVisible(true);*/
		//CargarRed cr = new CargarRed();
		makeUsesfullCSV hola = new makeUsesfullCSV();

		frame.setVisible(true);
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

}
