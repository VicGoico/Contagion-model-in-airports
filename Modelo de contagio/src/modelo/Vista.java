package modelo;

import javax.swing.SwingUtilities;

public class Vista extends javax.swing.JPanel {
	private static final long serialVersionUID = 9094988387830993055L;

	/**
	 * Creates new form NewJPanel
	 */
	public Vista() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		EjecutarPrograma = new javax.swing.JButton();
		Salir = new javax.swing.JButton();
		
		EjecutarPrograma.setText("Cargar los datos de todos los vuelos");
		EjecutarPrograma.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				EjecutarPrograma.setText("Cargando...");
				EjecutarPrograma.setEnabled(false);
				Salir.setEnabled(false);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						EjecutarActionPerformed(evt);
					}
				});
			}
		});

		Salir.setText("SALIR");
		Salir.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				SalirActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap(50, Short.MAX_VALUE)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
										.addComponent(EjecutarPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 340,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(83, 83, 83))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup()
												.addComponent(Salir, javax.swing.GroupLayout.PREFERRED_SIZE, 102,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(123, 123, 123)))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap(49, Short.MAX_VALUE)
						.addComponent(EjecutarPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 129,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(44, 44, 44).addComponent(Salir, javax.swing.GroupLayout.PREFERRED_SIZE, 41,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(37, 37, 37)));
	}

	private void SalirActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void EjecutarActionPerformed(java.awt.event.ActionEvent evt) {
		Main.CargarRed();
	}

	// Variables declaration - do not modify
	private javax.swing.JButton EjecutarPrograma;
	private javax.swing.JButton Salir;
	// End of variables declaration
}
