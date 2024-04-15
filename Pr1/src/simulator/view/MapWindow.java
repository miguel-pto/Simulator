package simulator.view;

import java.awt.BorderLayout;
import java.awt.Frame;
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
		ctrl.addObserver(this);
	}

	private void intiGUI() {
	JPanel mainPanel = new JPanel(new BorderLayout());
	// TODO poner contentPane como mainPanel
	// TODO crear el viewer y añadirlo a mainPanel (en el centro)
	// TODO en el método windowClosing, eliminar ‘MapWindow.this’ de los observadores
	addWindowListener(new WindowListener() { … });
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
		SwingUtilities.invokeLater(() -> {
			viewer.reset(time, map, animals);
			pack();
		});
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		SwingUtilities.invokeLater(() -> {
			viewer.reset(time, map, animals);
			pack();
		});
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		SwingUtilities.invokeLater(() -> { viewer.update(animals, time); });
	}
	// TODO otros métodos van aquí….
}
