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
	
	//LA BARRA DE ESTADO ES LA RESPONSABLE DE MOSTRAR LA INFORMACIÓN GENERAL SOBRE EL SIMULADOR
		//INCLUYE INFORMACIÓN SOBRE EL TIEMPO DE SIMULACIÓN, LA CANTIDAD DE ANIMALES, LA DIMENSIÓN DE LA SIMULACIÓN...
	
	private Controller ctrl;
	private double time;
	private int animals, width, height, rows, cols;
	JLabel timeLabel, animalsLabel, dimensionsLabel;
	
	private static final String TIME = "Time: ", ANIMALS = "Total Animals: ", DIMENSION = "Dimension: ";

	StatusBar(Controller ctrl) {
		this.ctrl = ctrl;
		initGUI();
		this.ctrl.addObserver(this); //ES REGISTRADO COMO OBSERVADOR
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		timeLabel = new JLabel(time_text()); 
		this.add(timeLabel); //EL TIEMPO ES AÑADIDO
		
		JSeparator s1 = new JSeparator(JSeparator.VERTICAL);
		s1.setPreferredSize(new Dimension(10, 20));
		this.add(s1); //SE AÑADE UNA SEPARACION
		
		animalsLabel = new JLabel(animals_text());
		this.add(animalsLabel); //EL NUMERO DE ANIMALES ES AÑADIDO

		JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
		s2.setPreferredSize(new Dimension(10, 20));
		this.add(s2); //SE AÑADE UNA SEPARACION
		
		dimensionsLabel = new JLabel(dimensions_text());
		this.add(dimensionsLabel); //LA DIMENSION ES AÑADIDA
	}
	
	//FUNCION AUXILIAR PARA Total Animals: X
	private String animals_text() {
		return ANIMALS + animals;
	}
	
	//FUNCION AUXILIAR PARA Time: T
	private String time_text() {
		return TIME + (float)time;
	}
	
	//FUNCION AUXILIAR PARA Dimension: Dims
	private String dimensions_text() {
		return DIMENSION + width + 'x' + height + ' ' + rows + 'x' + cols;
	}
	
	//FUNCION PARA ASIGNAR ASIGNAR EL TEXTO A LOS ATRIBUTOS
	private void update_labels() {
		timeLabel.setText(time_text());
		animalsLabel.setText(animals_text());
		dimensionsLabel.setText(dimensions_text());
	}

	@Override
	public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) { 
		//SE REGISTRAN LOS ATRIBUTOS DESDE LOS ARGUMENTOS DADOS
		this.time = time;
		this.animals = animals.size();
		width = map.get_width();
		height = map.get_height();
		rows = map.get_rows();
		cols = map.get_cols();
		update_labels();
	}

	@Override
	public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {
		//SE REINICIAN DE NUEVO LOS ATRIBUTOS DESDE LOS ARGUMENTOS DADOS
		this.time = time;
		this.animals = animals.size();
		width = map.get_width();
		height = map.get_height();
		rows = map.get_rows();
		cols = map.get_cols();
		update_labels();
	}

	@Override
	public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals) {
		//SE ESTABLECE LOS ANIMALES DESDE EL ARGUMENTO Y SE ACTUALIZAN LOS TEXTOS DE LOS ATRIBUTOS
		this.animals = animals.size();
		update_labels();
	}

	@Override
	public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {
		//SE ESTABLECEN LAS DIMENSIONES DESDE EL ARGUMENTO Y SE ACTUALIZAN LOS TEXTOS DE LOS ATRIBUTOS
		width = map.get_width();
		height = map.get_height();
		rows = map.get_rows();
		cols = map.get_cols();
		update_labels();
	}

	@Override
	public void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt) {
		//SE ESTABLECE EL TIEMPO DESDE EL ARGUMENTO Y SE ACTUALIZAN LOS TEXTOS DE LOS ATRIBUTOS
		this.time = time;
		update_labels();
	}
}
