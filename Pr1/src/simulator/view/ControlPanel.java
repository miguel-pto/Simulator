package simulator.view;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;

public class ControlPanel extends JPanel {
	
	private Controller ctrl;
	private ChangeRegionsDialog changeRegionsDialog; // TODO DESCUBIR QUE PASA CON ESTO
	private JToolBar toolsBar;
	private JFileChooser fc;
	private boolean stopped = true; // UTILIZADO EN LOS BOTONES DE RUN/STOP
	private JButton quitButton;

	// TODO AÑADE MÁS ATRIBUTOS
	private JSpinner selector; // POR EJEMPLO
	private JTextField dt;
	private JButton fcButton, mapButton, regionButton, runButton, stopButton;

	ControlPanel(Controller ctrl) {
		this.ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		toolsBar = new JToolBar();
		add(toolsBar, BorderLayout.PAGE_START);

		// TODO crear los diferentes botones/atributos y añadirlos a toolsBar.
		// Todos ellos han de tener su correspondiente tooltip.
		// Puedes utilizar _toolsBar.addSeparator() para añadir la línea de separación
		// vertical entre las componentes que lo necesiten.

		// QUIT BUTTON
		toolsBar.add(Box.createGlue());
		toolsBar.addSeparator();
		quitButton = new JButton();
		quitButton.setToolTipText("Quit");
		quitButton.setIcon(loadImage("resources/icons/exit.png"));
		quitButton.addActionListener((e) -> ViewUtils.quit(this));
		toolsBar.add(quitButton);

		// FILE CHOOSER TODO: REPASAR
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		fcButton = new JButton();
		fcButton.setToolTipText("Input File Chooser");
		fcButton.setIcon(loadImage("resources/icons/open.png"));
		fcButton.addActionListener((e) -> choose_file());
		toolsBar.add(fcButton);
		
		mapButton = new JButton();
		mapButton.setToolTipText("Map");
		mapButton.setIcon(loadImage("resources/icons/viewer.png"));
		mapButton.addActionListener((e) -> create_map());
		toolsBar.add(mapButton);
		
		// TODO Inicializar _changeRegionsDialog con instancias del diálogo de cambio de
		// regiones
		regionButton = new JButton();
		regionButton.setToolTipText("Change Regions");
		regionButton.setIcon(loadImage("resources/icons/regions.png"));
		regionButton.addActionListener((e) -> changeRegionsDialog.open(ViewUtils.getWindow(this)));
		toolsBar.add(regionButton);
		
		runButton = new JButton();
		runButton.setToolTipText("Run Simulation");
		runButton.setIcon(loadImage("resources/icons/run.png"));
		runButton.addActionListener((e) -> run());
		toolsBar.add(runButton);

		stopButton = new JButton();
		stopButton.setToolTipText("Stop Simulation");
		stopButton.setIcon(loadImage("resources/icons/stop.png"));
		stopButton.addActionListener((e) -> stop());
		toolsBar.add(stopButton);
}

	// METODO PARA FIJAR LOS ICONOS
	private ImageIcon loadImage(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
	}
	
	private void choose_file() {
		int option = fc.showOpenDialog(ViewUtils.getWindow(this));
		if (option == JFileChooser.APPROVE_OPTION) {
			try {
			InputStream is = new FileInputStream(fc.getSelectedFile());
			JSONObject in = new JSONObject(new JSONTokener(is));
			is.close();
			
			int rows = in.getInt("rows");
			int cols = in.getInt("cols");
			int width = in.getInt("width");
			int height = in.getInt("height");
			ctrl.reset(cols, rows, width, height);
			ctrl.load_data(in.getJSONObject("data"));
			} catch(Exception e) {}
		}
	}
	
	private void create_map() {
		
	}
	
	private void run() {
		
	}
	
	private void stop() {
		
	}

	// PARA FIJAR EL TAMAÑO DEL JSPINNER SE PUEDE USAR:
	/*
	 * stepsSpinner.setToolTipText("Simulation steps to run: 1-10000");
	 * stepsSpinner.setMaximumSize(new Dimension(80, 40));
	 * stepsSpinner.setMinimumSize(new Dimension(80, 40));
	 * stepsSpinner.setPreferredSize(new Dimension(80, 40))
	 */

	// TODO el resto de métodos van aquí…

}
