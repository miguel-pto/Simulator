package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.launcher.Main;

public class ControlPanel extends JPanel {
	
	//EL PANEL DE CONTROL ES EL RESPONSABLE DE LA INTERACCIÓN ENTRE EL USUARIO Y EL SIMULADOR.
	// INCLUYE BOTONES PARA INTERACTUAR CON EL SIMULADOR, UN JSpinner PARA SELECCIONAR LOS
	// PASOS DE LA SIMULACION Y UN JTextField PARA ACTUALIZAR EL DELTA-TIME.

	private Controller ctrl;
	private ChangeRegionsDialog changeRegionsDialog;
	private JToolBar toolsBar;
	private JFileChooser fc;
	private boolean stopped = true; // UTILIZADO EN LOS BOTONES DE RUN/STOP
	private JButton quitButton;


	private JSpinner stepsSpinner; // POR EJEMPLO
	private JTextField dtField;
	private JButton fcButton, mapButton, regionButton, runButton, stopButton;

	//INICIALIZA MEDIANTE EL CONTROLER, INICIALIZANDO changeRegionsDialog CON ESTE E INICIALIZANDO LA GUI
	ControlPanel(Controller ctrl) {
		this.ctrl = ctrl;
		changeRegionsDialog = new ChangeRegionsDialog(ctrl);
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout());
		toolsBar = new JToolBar();
		add(toolsBar, BorderLayout.PAGE_START);

		// SE CREAN LOS BOTONES Y ATRIBUTOS PARA AÑADIRLOS A LA toolsBar
		// TODOS POSEEN SU CORRESPONDIENTE tooltip

		// SE DEFINE EL BOTON DE FILE CHOOSER Y SE AÑADE
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		fcButton = new JButton();
		fcButton.setToolTipText("Input File Chooser");
		fcButton.setIcon(loadImage("resources/icons/open.png"));
		fcButton.addActionListener((e) -> choose_file());
		toolsBar.add(fcButton);
		//AL PULSARLO SE ABRE EL SELECTOR DE FICHEROS PARA ESTABLECER SU ARCHIVO DE ENTRADA
		
		//SE AÑADE UNA DISTANCIA EN EL EJE VERTICAL ENTRE BOTONES
		toolsBar.addSeparator();

		//SE DEFINE EL BOTON DE MAPA Y SE AÑADE
		mapButton = new JButton();
		mapButton.setToolTipText("Map");
		mapButton.setIcon(loadImage("resources/icons/viewer.png"));
		mapButton.addActionListener((e) -> new MapWindow( ViewUtils.getWindow(this), ctrl));
		toolsBar.add(mapButton);
		//AL PULSARLO PERMITE VER LA REPRESENTACIÓN DE LA SIMULACIÓN, PUDIENDO TENER VARIOS ABIERTOS A LA VEZ
		
		//SE DEFINE EL BOTON DE CAMBIO DE REGIONES Y SE AÑADE
		regionButton = new JButton();
		regionButton.setToolTipText("Change Regions");
		regionButton.setIcon(loadImage("resources/icons/regions.png"));
		regionButton.addActionListener((e) -> changeRegionsDialog.open(ViewUtils.getWindow(this)));
		toolsBar.add(regionButton);
		//AL PULSARLO ABRE EL DIALOGO DE REGIONES
		
		//SE AÑADE UNA DISTANCIA EN EL EJE VERTICAL ENTRE BOTONES
		toolsBar.addSeparator();

		//SE DEFINE EL BOTON DE RUN SIMULATION Y SE AÑADE
		runButton = new JButton();
		runButton.setToolTipText("Run Simulation");
		runButton.setIcon(loadImage("resources/icons/run.png"));
		runButton.addActionListener((e) -> { //ACCIONES QUE REALIZA EL BOTON
			try {
			update_buttons(false);
			stopped = false;
			//run_sim((Integer)stepsSpinner.getValue(), 0.03);
			run_sim((Integer)stepsSpinner.getValue(),Double.parseDouble(dtField.getText()));
			}
			catch () {
				//TODO encontrar el error de parse Double para ponerlo
			}
		});
		toolsBar.add(runButton);
		//AL PULSARLO SE DESHABILITAN LOS BOTONES SALVO EL DE STOP Y, EN NUESTRO CASO, EL DE MAP EN CASO DE QUERER ABRIR OTRA IMAGEN
		//INICIAL LA SIMULACIONZ Y CAMBIA EL ESTADO DE stopped A FALSE
		
		//SE DEFINE EL BOTON DE STOP SIMULATION Y SE AÑADE
		stopButton = new JButton();
		stopButton.setToolTipText("Stop Simulation");
		stopButton.setIcon(loadImage("resources/icons/stop.png"));
		stopButton.addActionListener((e) -> stopped = true); //ACCION QUE REALIZA EL BOTON
		toolsBar.add(stopButton);
		//AL PULSARLO CAMBIA EL ESTADO A stopped Y SE RECUPERAN LOS BOTONES DE NUEVO
		
		//SE AÑADE EL JSpinner QUE PERMITE MODIFICAR LOS steps DE LA SIMULACION
		stepsSpinner = new JSpinner();
		stepsSpinner.setToolTipText("Simulation steps to run: 1-10000");
		stepsSpinner.setMaximumSize(new Dimension(80, 40));
		stepsSpinner.setMinimumSize(new Dimension(80, 40));
		stepsSpinner.setPreferredSize(new Dimension(80, 40));
		stepsSpinner.setValue(10000); //SE INICIA CON EL VALOR DADO
		JLabel stepsLabel = new JLabel("Steps: "); //SE AÑADE EL TEXTO EXPLICATIVO
		toolsBar.add(stepsLabel);
		toolsBar.add(stepsSpinner);
		
		//SE AÑADE EL DeltaTime QUE PERMITE MODIFICAR EL VALOR CORRESPONDIENTE
		dtField = new JTextField();
		dtField.setToolTipText("Delta-Time Value");
		dtField.setText(Main.default_delta_time.toString());
		JLabel dtLabel = new JLabel("Delta-Time: "); //SE AÑADE EL TEXTO EXPLICATIVO
		toolsBar.add(dtLabel);
		toolsBar.add(dtField);
		
		//SE AÑADE UNA DISTANCIA EN EL EJE VERTICAL ENTRE BOTONES
		toolsBar.addSeparator();
		
		//SE DEFINE EL BOTON DE QUIT Y SE AÑADE
		toolsBar.add(Box.createGlue()); //Alinea el boton a la derecha
		toolsBar.addSeparator(); //Añade distancia entre botones
		quitButton = new JButton();
		quitButton.setToolTipText("Quit");
		quitButton.setIcon(loadImage("resources/icons/exit.png"));
		quitButton.addActionListener((e) -> ViewUtils.quit(this)); //OPERACION AL PULSAR EL BOTON
		toolsBar.add(quitButton);
		//AL PULSARLO SE CIERRA ESTA VENTANA, TERMINANDO LA SIMULACIÓN
	}

	// FUNCION DISEÑADA PARA LA FIJACION DE LOS ICONOS
	private ImageIcon loadImage(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
	}
	
	// FUNCION AUXILIAR PARA ESCOGER EL ARCHIVO
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
				ctrl.load_data(in);
			} catch (Exception e) {
			}
		}
	}

	private void run_sim(int n, double dt) {
		if (n > 0 && !stopped) { //REALIZA EL PASO dt Y LLAMA A LA SIGUIENTE ITERACIÓN DEL SIMULADOR
			try {
				ctrl.advance(dt);
				SwingUtilities.invokeLater(() -> run_sim(n - 1, dt));
			} catch (Exception e) {
				ViewUtils.showErrorMsg("error"); // TODO
				stopped = true;
				update_buttons(true); //AL ERRAR EN EL BUCLE SE VUELVE A ACTIVAR LOS BOTONES QUE SE HAN DESACTIVADO
			}
		} else {
			stopped = true;
			update_buttons(true); //AL TERMINAR EL BUCLE SE VUELVE A ACTIVAR LOS BOTONES QUE SE HAN DESACTIVADO
		}
	}
	
	//FUNCION AUXILIAR QUE ESTABLECE LOS BOTONES AL ESTADO ASIGNADO
	private void update_buttons(boolean enable) {
		fcButton.setEnabled(enable);
		regionButton.setEnabled(enable);
		runButton.setEnabled(enable);
		quitButton.setEnabled(enable);
	}
}
