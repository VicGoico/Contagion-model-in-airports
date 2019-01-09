package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import modelo.Main;

public class Vista extends JPanel {
	private static final long serialVersionUID = 9094988387830993055L;
	private boolean redCargada = false;
	private JButton ejecutarProgramaButton;
	private JButton salirButton;
	private JButton continuarButton;
	private JTextField fileNameProcesadosTextField;
	private JTextField fileNameNodesTextField;
	private JTextField fileNameAristasTextField;
	private JTextField fileNameExpenditureHelathTextField;
	private JTextField fileNamePIBTextField;
	private JButton guardarDatosProcesadosButton;
	private JPanel centerPanel;
	private JPanel leftPanel;
	private JPanel rightPanel;
	private JPanel topPanel;
	private JPanel bottPanel;

	/**
	 * Creates new form NewJPanel
	 */
	public Vista() {
		initGUI();
		initComponents();
	}

	private void initComponents() {
		ejecutarProgramaButton = new JButton("Cargar los datos de todos los vuelos");
		salirButton = new JButton("Salir");
		guardarDatosProcesadosButton = new JButton("Guardar datos procesados");
		fileNameProcesadosTextField = new JTextField(Main.OUTPUTFILENAME_PROCESSEDDATA);
		fileNameExpenditureHelathTextField = new JTextField(Main.EXPENDITUREHEALTH_FILENAME);
		fileNameAristasTextField = new JTextField(Main.AIRPORT_ARISTAS_FILENAME);
		fileNameNodesTextField = new JTextField(Main.AIRPORT_NODES_FILENAME);
		fileNamePIBTextField = new JTextField(Main.PIB_FILENAME);

		continuarButton = new JButton("Continuar");
		continuarButton.setEnabled(false);

		ejecutarProgramaButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ejecutarProgramaButton.setText("Cargando...");
				ejecutarProgramaButton.setEnabled(false);
				salirButton.setEnabled(false);
				guardarDatosProcesadosButton.setEnabled(false);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							Main.CargarRed();
							continuarButton.setEnabled(true);
							salirButton.setEnabled(true);
							guardarDatosProcesadosButton.setEnabled(true);
							redCargada = true;
						} catch (IOException e) {
							JFrame frame = new JFrame();
							JOptionPane.showMessageDialog(frame, "Ha ocurrido un error al leer los datos!", "Error!",
									JOptionPane.ERROR_MESSAGE);

							e.printStackTrace();
						}
					}
				});
			}
		});

		continuarButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.loadVentanaControl();
			}
		});

		salirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.exit(0);
			}
		});

		guardarDatosProcesadosButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				guardarDatosProcesadosButton.setText("Cargando...");
				ejecutarProgramaButton.setEnabled(false);
				salirButton.setEnabled(false);
				guardarDatosProcesadosButton.setEnabled(false);

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (redCargada) {
							try {
								Main.guardar(fileNameProcesadosTextField.getText());

								JFrame frame = new JFrame();
								JOptionPane.showMessageDialog(frame, "Archivo guardado correctamente!", "Listo!",
										JOptionPane.INFORMATION_MESSAGE);
							} catch (IOException e) {
								JFrame frame = new JFrame();
								JOptionPane.showMessageDialog(frame, e.getMessage(), "Error!",
										JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JFrame frame = new JFrame();
							JOptionPane.showMessageDialog(frame, "Primero debes cargar la red!", "Error!",
									JOptionPane.ERROR_MESSAGE);
						}

						// Restablezco los botones
						guardarDatosProcesadosButton.setText("Guardar datos procesados");
						ejecutarProgramaButton.setEnabled(true);
						salirButton.setEnabled(true);
						guardarDatosProcesadosButton.setEnabled(true);
					}
				});
			}
		});

		centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
		centerPanel.add(ejecutarProgramaButton);

		centerPanel.add(new JLabel("NOMBRE DE FICHEROS A CARGAR"));
		JPanel csvsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		csvsPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		csvsPanel.add(new JLabel("CSV Nodos:"));
		csvsPanel.add(fileNameNodesTextField);
		csvsPanel.add(new JLabel("CSV Aristas:"));
		csvsPanel.add(fileNameAristasTextField);
		csvsPanel.add(new JLabel("CSV Gasto en Salud:"));
		csvsPanel.add(fileNameExpenditureHelathTextField);
		csvsPanel.add(new JLabel("CSV PIB:"));
		csvsPanel.add(fileNamePIBTextField);
		csvsPanel.add(guardarDatosProcesadosButton);
		centerPanel.add(csvsPanel);

		JPanel exportPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		exportPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		exportPanel.add(new JLabel("Nombre del fichero a exportar:"));
		exportPanel.add(fileNameProcesadosTextField);
		exportPanel.add(guardarDatosProcesadosButton);
		centerPanel.add(exportPanel);

		centerPanel.add(continuarButton);
		centerPanel.add(salirButton);
	}

	private void initGUI() {
		this.setLayout(new BorderLayout(5, 5));
		centerPanel = createPanel(null, 50, 50);
		this.add(centerPanel, BorderLayout.CENTER);
		leftPanel = createPanel(null, 50, 50);
		this.add(leftPanel, BorderLayout.LINE_START);
		rightPanel = createPanel(null, 50, 50);
		this.add(rightPanel, BorderLayout.LINE_END);
		topPanel = createPanel(null, 20, 20);
		this.add(topPanel, BorderLayout.PAGE_START);
		bottPanel = createPanel(null, 20, 20);
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
}
