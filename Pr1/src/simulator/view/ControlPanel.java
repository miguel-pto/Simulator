package simulator.view;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import simulator.control.Controller;

public class ControlPanel extends JPanel{
	private Controller ctrl;
	private ChangeRegionsDialog changeRegionsDialog; //TODO DESCUBIR QUE PASA CON ESTO
	private JToolBar toolaBar;
	private JFileChooser fc;
	private boolean stopped = true; //UTILIZADO EN LOS BOTONES DE RUN/STOP
	private JButton quitButton;
	
	//TODO AÑADE MÁS ATRIBUTOS
	
	ControlPanel(Controller ctrl){
		this.ctrl = ctrl;
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		toolaBar = new JToolBar();
		add(toolaBar, BorderLayout.PAGE_START);
		
		// TODO crear los diferentes botones/atributos y añadirlos a toolaBar.
		 // Todos ellos han de tener su correspondiente tooltip. 
		//Puedes utilizar _toolaBar.addSeparator() para añadir la línea de separación vertical entre las componentes que lo necesiten.
		 
		
		// Quit Button
		toolaBar.add(Box.createGlue()); // this aligns the button tothe right
		toolaBar.addSeparator();
		quitButton = new JButton();
		quitButton.setToolTipText("Quit");
		quitButton.setIcon(loadImage("resources/icons/exit.png"));
		quitButton.addActionListener((e) -> Utils.quit(this)); //TODO AVERIGUAR COMO SOLUCIONAR ESTO
		toolaBar.add(quitButton);
		
		// TODO Inicializar _fc con una instancia de JFileChooser. 
		//Para que siempre abra en la carpeta de ejemplos puedes usar: fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
		
		// TODO Inicializar _changeRegionsDialog con instancias del diálogo de cambio de regiones

	}
	
	//METODO PARA FIJAR LOS ICONOS 
	private ImageIcon loadImage(String path) {
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path));
	}
	
	//PARA FIJAR EL TAMAÑO DEL JSPINNER SE PUEDE USAR: 
	/*
	  	stepsSpinner.setToolTipText("Simulation steps to run: 1-10000");
		stepsSpinner.setMaximumSize(new Dimension(80, 40));
		stepsSpinner.setMinimumSize(new Dimension(80, 40));
		stepsSpinner.setPreferredSize(new Dimension(80, 40))
	 */
	
	// TODO el resto de métodos van aquí…
	
	
}
