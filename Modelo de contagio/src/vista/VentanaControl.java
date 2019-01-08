package vista;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

import modelo.Main;
import modelo.red.Nodo;

import java.util.TreeMap;

public class VentanaControl extends javax.swing.JPanel {
	private static final long serialVersionUID = 5388318295809628575L;

	/**
	 * Creates new form Control
	 */
	public VentanaControl(HashMap<Integer, Nodo> nodos) {
		initComponents(nodos);
	}

	private void initComponents(HashMap<Integer, Nodo> nodos) {

		jLabel1 = new javax.swing.JLabel();
		jComboBox1 = new javax.swing.JComboBox<>();
		jButton1 = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		jComboBoxPaises = new javax.swing.JComboBox<>();

		aeropuertosOrdenados = new TreeMap<String, Nodo>();
		informacionCompleta = new TreeMap<String, TreeMap<String, Nodo>>();

		jLabel1.setText("Indica el aeropuerto en el que deseas comenzar la infección");

		int i = 0;
		aeropuertosOrdenados = new TreeMap<String, Nodo>();
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

		jButton1.setText("COMENZAR INFECCIÓN");

		jLabel2.setText("Inidica el pais del aeropuerto donde quieres infectar");
		/*
		 * jComboBox1.setModel( new javax.swing.DefaultComboBoxModel<>(aeropuertos));
		 */
		jComboBoxPaises.setModel(new javax.swing.DefaultComboBoxModel<>(paises));

		jComboBox1.setVisible(false);
		jButton1.setVisible(false);
		jLabel1.setVisible(false);
		jComboBoxPaises.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				PaisesActionPerformed(evt);
			}
		});

		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				
				jButton1.setText("Cargando...");
				jButton1.setEnabled(false);
				jComboBoxPaises.setEnabled(false);
				jComboBox1.setEnabled(false);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						InfeccionActionPerformed(evt);
					}
				});
				
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(170, 170, 170).addComponent(jButton1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup().addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 295,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel2))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
										.addComponent(jComboBoxPaises, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(jComboBox1, 0, 258, Short.MAX_VALUE))))
				.addContainerGap(49, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(49, 49, 49)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2)
						.addComponent(jComboBoxPaises, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
						.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 84,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addGap(18, 18, 18).addComponent(jButton1).addContainerGap(95, Short.MAX_VALUE)));
	}

	protected void PaisesActionPerformed(java.awt.event.ActionEvent evt) {
		if (!this.jComboBoxPaises.getSelectedItem().equals("Selección")) {
			System.out.println(this.jComboBoxPaises.getSelectedItem());

			TreeMap<String, Nodo> aeropuertosPaisSeleccionado = informacionCompleta
					.get(this.jComboBoxPaises.getSelectedItem());
			int i = 0;
			String[] aeropuertos = new String[aeropuertosPaisSeleccionado.size()];
			for (Entry<String, Nodo> entry : aeropuertosPaisSeleccionado.entrySet()) {
				aeropuertos[i] = entry.getKey();
				i += 1;
			}
			jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(aeropuertos));
			jComboBox1.setVisible(true);
			jButton1.setVisible(true);
			jLabel1.setVisible(true);
			paisSeleccionado = (String) this.jComboBoxPaises.getSelectedItem();
		}
	}

	private void InfeccionActionPerformed(java.awt.event.ActionEvent evt) {
		Nodo foco = informacionCompleta.get(paisSeleccionado).get(this.jComboBox1.getSelectedItem());
		System.out.println("Va a comenzar la infeccion desde " + foco.getAirportInfo().getName());
		Main.comenzarInfeccion(foco);
	}

	private String paisSeleccionado;
	private TreeMap<String, Nodo> aeropuertosOrdenados;
	private TreeMap<String, TreeMap<String, Nodo>> informacionCompleta; // <Pais, <NombreAeropuerto,Nodo>>
	private javax.swing.JButton jButton1;
	private javax.swing.JComboBox<String> jComboBox1;
	private javax.swing.JComboBox<String> jComboBoxPaises;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
}
