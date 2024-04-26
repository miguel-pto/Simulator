package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.view.ViewUtils;

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
		

		// SE CREA ControlPanel Y SE AÑADE EN PAGE_START DE mainPanel
		ControlPanel ctrlPanel = new ControlPanel(ctrl);
		mainPanel.add(ctrlPanel, BorderLayout.PAGE_START);

		// SE CREA StatusBar Y SE AÑADE EN PAGE_END de mainPanel
		StatusBar bar = new StatusBar(ctrl);
		mainPanel.add(bar, BorderLayout.PAGE_END);

		// DEFINICIÓN DEL PANEL DE TABLAS Y ES AÑADIDO A mainPanel
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		//SE CREA LA TABLA DE ESPECIES Y ES AÑADIDA A contentPanel
		//SE HACE USO DE setPreferredSize(new Dimension(500, 250)) PARA FIJAR SU TAMAÑO
		InfoTable species = new InfoTable("Species", new SpeciesTableModel(ctrl));
		species.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(species);

		// SE CREA LA TABLA DE REGIONES Y ES AÑADIDA A contentPanel
		// SE HACE USO DE setPreferredSize(new Dimension(500, 250)) PARA FIJAR SU TAMAÑO
		InfoTable regions = new InfoTable("Regions", new RegionsTableModel(ctrl));
		regions.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(regions);

		// TODO llama a ViewUtils.quit(MainWindow.this) en el método windowClosing
		//ViewUtils.quit(MainWindow.this);
			//Supuestamente hay que ponerlo y así al querrer cerrar pide confirmarlo, pero
			// el problema es que tmb pide confirmación al empezar
		// TODO COMPLETAR ESTO
		addWindowListener(null);
		setSize(500, 500);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
}
