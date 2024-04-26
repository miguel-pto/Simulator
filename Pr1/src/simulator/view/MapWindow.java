package simulator.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

public class MapWindow extends JFrame implements EcoSysObserver {

	private Controller ctrl;
	private AbstractMapViewer viewer;
	private Frame parent;

	MapWindow(Frame parent, Controller ctrl) {
		super("[MAP VIEWER]");
		this.ctrl = ctrl;
		this.parent = parent;
		intiGUI();
		ctrl.addObserver(this); // SE REGISTRA COMO OBSERVADOR
	}

	private void intiGUI() {
	JPanel mainPanel = new JPanel(new BorderLayout());
	// contentPane ES PUESTO COMO mainPanel
	this.setContentPane(mainPanel);
	// EL VIEWER ES CREADO Y AÑADIDO A mainPanel (en el centro)
	viewer = new MapViewer();
	mainPanel.add(viewer);
	// SE ELIMINA MapWindow.this DE LOS OBSERVADORES EN EL MÉTODO windowClosing
	addWindowListener(new WindowListener() {

		@Override
		public void windowOpened(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
			mainPanel.remove(viewer);
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
		}});
	pack();
	if (parent != null)
	setLocation(parent.getLocation().x + parent.getWidth()/2 -
	getWidth()/2,parent.getLocation().y + parent.getHeight()/2 -
	getHeight()/2);
	setResizable(false);
	setVisible(true);
	}

	
	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		// LLAMAN AL RESET DEL viewer Y CAMBIAN EL TAMAÑO DE LA PESTAÑA USANDO pack()
		SwingUtilities.invokeLater(() -> {
			viewer.reset(time, map, animals);
			pack();
		});
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		// LLAMAN AL RESET DEL viewer Y CAMBIAN EL TAMAÑO DE LA PESTAÑA USANDO pack()
		SwingUtilities.invokeLater(() -> {
			viewer.reset(time, map, animals);
			pack();
		});
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		// LLAMA A update DE viewer
		SwingUtilities.invokeLater(() -> {
			viewer.update(animals, time);
		});
	}
}
