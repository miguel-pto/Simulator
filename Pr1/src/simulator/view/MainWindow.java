package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

//TODO COMPROBAR QUE ESTA CLASE ESTÁ BIEN CREADA AQUÍ, ES LO QUE DICE EN EL MODELO, PERO PARA/POR CONFIRMALO
public class MainWindow extends JFrame {
	private Controller ctrl;

	public MainWindow(Controller ctrl) {
		super("[ECOSYSTEM SIMULATOR]");
		this.ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);

		// TODO CREAR ControlPanel Y AÑADIRLO EN PAGE_START DE mainPanel
		ControlPanel ctrlPanel = new ControlPanel(ctrl);
		mainPanel.add(ctrlPanel, BorderLayout.PAGE_START);

		// TODO crear StatusBar y añadirlo en PAGE_END de mainPanel
		StatusBar bar = new StatusBar(ctrl);
		mainPanel.add(bar, BorderLayout.PAGE_END);

		// Definición del panel de tablas
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		// TODO crear la tabla de especies y añadirla a contentPanel.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
		InfoTable species = new InfoTable("Species", new SpeciesTableModel(ctrl));
		species.setPreferredSize(new Dimension(500, 250));
		mainPanel.add(species);

		// TODO crear la tabla de regiones.
		// Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño

		// TODO llama a ViewUtils.quit(MainWindow.this) en el método windowClosing

		// TODO COMPLETAR ESTO
		addWindowListener(null);
		setSize(500, 500);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
