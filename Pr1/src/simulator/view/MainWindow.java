package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

@SuppressWarnings("serial")
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

		// SE CREA LA TABLA DE ESPECIES Y ES AÑADIDA A contentPanel
		// SE HACE USO DE setPreferredSize(new Dimension(500, 250)) PARA FIJAR SU TAMAÑO
		InfoTable species = new InfoTable("Species", new SpeciesTableModel(ctrl));
		species.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(species);

		// SE CREA LA TABLA DE REGIONES Y ES AÑADIDA A contentPanel
		// SE HACE USO DE setPreferredSize(new Dimension(500, 250)) PARA FIJAR SU TAMAÑO
		InfoTable regions = new InfoTable("Regions", new RegionsTableModel(ctrl));
		regions.setPreferredSize(new Dimension(500, 250));
		contentPanel.add(regions);

		addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				ViewUtils.quit(MainWindow.this);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}
		});
		setSize(500, 500);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
