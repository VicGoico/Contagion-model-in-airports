package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import modelo.Main;
import modelo.red.Nodo;

import java.util.TreeMap;

public class VentanaControl extends javax.swing.JPanel {
	private static final long serialVersionUID = 5388318295809628575L;
	private JButton salirButton;
	private JButton comenzarInfeccionButton;
	private JButton volverButton;
	private JPanel centerPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel topPanel;
	private JPanel bottPanel;
	private JComboBox<String> jComboBoxAeropuertos;
	private JComboBox<String> jComboBoxPaises;	
	
	private String paisSeleccionado;
	private TreeMap<String, Nodo> aeropuertosOrdenados;
	private TreeMap<String, TreeMap<String, Nodo>> informacionCompleta; // <Pais, <NombreAeropuerto,Nodo>>

	/**
	 * Creates new form Control
	 */
	public VentanaControl(HashMap<Integer, Nodo> nodos) {
		initGUI();
		initComponents(nodos);
	}

	private void initComponents(HashMap<Integer, Nodo> nodos) {
		salirButton = new JButton("Salir");
		volverButton = new JButton("Volver");
		volverButton.setVisible(false);
		comenzarInfeccionButton = new JButton("COMENZAR INFECCIÓN");
		comenzarInfeccionButton.setEnabled(false);		
		jComboBoxPaises = new JComboBox<>();
		jComboBoxAeropuertos = new JComboBox<>();
		jComboBoxAeropuertos.setEnabled(false);
		informacionCompleta = new TreeMap<String, TreeMap<String, Nodo>>();

		aeropuertosOrdenados = new TreeMap<String, Nodo>();
		int i = 0;
		for (Map.Entry<Integer, Nodo> entry : nodos.entrySet()) {
			aeropuertosOrdenados.put(entry.getValue().getAirportInfo().getName(), entry.getValue());
			if (informacionCompleta.containsKey(entry.getValue().getAirportInfo().getCountry())) {
				informacionCompleta.get(entry.getValue().getAirportInfo().getCountry())
						.put(entry.getValue().getAirportInfo().getName(), entry.getValue());
			} else {
				informacionCompleta.put(entry.getValue().getAirportInfo().getCountry(), new TreeMap<String, Nodo>());
				informacionCompleta.get(entry.getValue().getAirportInfo().getCountry())
						.put(entry.getValue().getAirportInfo().getName(), entry.getValue());
			}
		}
		String[] paises = new String[informacionCompleta.size() + 1];
		paises[i] = "Selección";
		i++;

		for (Entry<String, TreeMap<String, Nodo>> entry : informacionCompleta.entrySet()) {
			paises[i] = entry.getKey();
			i++;
		}
		/*
		 * String[] aeropuertos = new String[aeropuertosOrdenados.size()];
		 * System.out.println(aeropuertosOrdenados.size()); for (Entry<String, Nodo>
		 * entry : aeropuertosOrdenados.entrySet()) { aeropuertos[i] = entry.getKey(); i
		 * += 1; } jComboBox1.setModel(new
		 * javax.swing.DefaultComboBoxModel<>(aeropuertos));
		 */
		
		jComboBoxPaises.setModel(new javax.swing.DefaultComboBoxModel<>(paises));
		jComboBoxPaises.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!jComboBoxPaises.getSelectedItem().equals("Selección")) {
					System.out.println("Pais seleccionado: " + jComboBoxPaises.getSelectedItem());

					TreeMap<String, Nodo> aeropuertosPaisSeleccionado = informacionCompleta.get(jComboBoxPaises.getSelectedItem());
					int i = 0;
					String[] aeropuertos = new String[aeropuertosPaisSeleccionado.size()];
					for (Entry<String, Nodo> entry : aeropuertosPaisSeleccionado.entrySet()) {
						aeropuertos[i] = entry.getKey();
						i += 1;
					}
					jComboBoxAeropuertos.setModel(new javax.swing.DefaultComboBoxModel<>(aeropuertos));
					jComboBoxAeropuertos.setEnabled(true);
					
					comenzarInfeccionButton.setEnabled(true);
					paisSeleccionado = (String) jComboBoxPaises.getSelectedItem();
				}
			}
		});

		comenzarInfeccionButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				comenzarInfeccionButton.setText("Cargando...");
				comenzarInfeccionButton.setEnabled(false);
				jComboBoxPaises.setEnabled(false);
				jComboBoxAeropuertos.setEnabled(false);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							Nodo foco = informacionCompleta.get(paisSeleccionado).get(jComboBoxAeropuertos.getSelectedItem());
							System.out.println("Se va a cargar el umbral.");
							Main.cargarUmbral();
							System.out.println("Va a comenzar la infeccion desde " + foco.getAirportInfo().getName());
							Main.comenzarInfeccion(foco);
							comenzarInfeccionButton.setVisible(false);
						} catch (IOException e) {
							JFrame frame = new JFrame();
							JOptionPane.showMessageDialog(frame,
									"Ha ocurrido un error al cargar el umbral y realizar la infeccion!\n" + e.getMessage(), "Error!",
									JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						} finally {
							comenzarInfeccionButton.setText("COMENZAR INFECCIÓN");
							salirButton.setEnabled(true);
						}
					}
				});
			}
		});
		
		salirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});
		
		volverButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restoreGUI();
			}
		});

		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
		centerPanel.add(new JLabel("Indica el aeropuerto en el que deseas comenzar la infección:"));
		centerPanel.add(jComboBoxPaises);
		centerPanel.add(new JLabel("Inidica el pais del aeropuerto donde quieres infectar:"));
		centerPanel.add(jComboBoxAeropuertos);
		
		bottPanel.add(comenzarInfeccionButton);
		bottPanel.add(volverButton);
		bottPanel.add(salirButton);
	}

	private void initGUI() {
		this.setLayout(new BorderLayout(5, 5));
		centerPanel = createPanel(null, 50, 50);
		this.add(centerPanel, BorderLayout.CENTER);
		leftPanel = createPanel(null, 10, 50);
		this.add(leftPanel, BorderLayout.LINE_START);
		rightPanel = createPanel(null, 10, 50);
		this.add(rightPanel, BorderLayout.LINE_END);
		topPanel = createPanel(null, 20, 20);
		this.add(topPanel, BorderLayout.PAGE_START);
		bottPanel = createPanel(null, 20, 50);
		this.add(bottPanel, BorderLayout.PAGE_END);
	}

	private JPanel createPanel(Color color, int x, int y) {
		JPanel panel;
		panel = new JPanel();
		if (color != null)
			panel.setBackground(color);
		panel.setPreferredSize(new Dimension(x, y));
		return panel;
	}
	
	public void setFullCenterPanel(Container c) {
		this.remove(this.leftPanel);
		this.remove(this.rightPanel);
		this.remove(this.centerPanel);
		this.remove(this.topPanel);
		this.add(c, BorderLayout.CENTER);
		this.volverButton.setVisible(true);
	}
	
	private void restoreGUI() {
		this.volverButton.setVisible(false);
		this.comenzarInfeccionButton.setVisible(true);
		this.jComboBoxPaises.setEnabled(true);
		this.jComboBoxAeropuertos.setEnabled(true);
		
		this.removeAll();
		
		this.setLayout(new BorderLayout(5, 5));
		this.add(centerPanel, BorderLayout.CENTER);
		this.add(leftPanel, BorderLayout.LINE_START);
		this.add(rightPanel, BorderLayout.LINE_END);
		this.add(topPanel, BorderLayout.PAGE_START);
		this.add(bottPanel, BorderLayout.PAGE_END);
	}
}
