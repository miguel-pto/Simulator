package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.*;

import simulator.control.Controller;
import simulator.model.AnimalInfo;
import simulator.model.EcoSysObserver;
import simulator.model.MapInfo;
import simulator.model.RegionInfo;

public class StatusBar extends JPanel implements EcoSysObserver {
	
	private Controller ctrl;
	private double time;
	private int animals, width, height, rows, cols;

	StatusBar(Controller ctrl) {
		initGUI();
		this.ctrl = ctrl;
		// TODO registrar this como observador
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		JLabel timeLabel = new JLabel("Time: " + time);
		this.add(timeLabel);
		
		JSeparator s1 = new JSeparator(JSeparator.VERTICAL);
		s1.setPreferredSize(new Dimension(10, 20));
		this.add(s1);
		
		JLabel animalsLabel = new JLabel("Total Animals: " + animals);
		this.add(animalsLabel);

		JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
		s2.setPreferredSize(new Dimension(10, 20));
		this.add(s2);
		
		JLabel dimensionLabel = new JLabel("Dimension: " + width + 'x' + height + ' ' + rows + 'x' + cols);
		this.add(dimensionLabel);
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {
		this.time = time;
		this.animals = animals.size();
		width = map.get_width();
		height = map.get_height();
		rows = map.get_rows();
		cols = map.get_cols();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		this.time = time;
		this.animals = animals.size();
		width = map.get_width();
		height = map.get_height();
		rows = map.get_rows();
		cols = map.get_cols();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		this.animals = animals.size();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		width = map.get_width();
		height = map.get_height();
		rows = map.get_rows();
		cols = map.get_cols();
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		this.time = time;
	}
}
