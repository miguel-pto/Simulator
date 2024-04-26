package simulator.view;

import simulator.model.AnimalInfo;
import simulator.model.MapInfo;
import simulator.model.State;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class MapViewer extends AbstractMapViewer {

	// Anchura/altura/ de la simulación -- se supone que siempre van a ser iguales
	// al tamaño del componente
	private int width;
	private int height;

	// Número de filas/columnas de la simulación
	private int rows;
	private int cols;

	// Anchura/altura de una región
	int rwidth;
	int rheight;

	// Mostramos sólo animales con este estado. Los posibles valores de _currState
	// son null y los valores de Animal.State.values(). Si es null mostramos todo.
	State currState;

	// En estos atributos guardamos la lista de animales y el tiempo que hemos
	// recibido la última vez para dibujarlos.
	volatile private Collection<AnimalInfo> objs;
	volatile private Double time;

	// Una clase auxiliar para almacenar información sobre una especie
	private static class SpeciesInfo {
		private Integer count;
		private Color color;

		SpeciesInfo(Color color) {
			count = 0;
			this.color = color;
		}
	}

	// Un mapa para la información sobre las especies
	Map<String, SpeciesInfo> kindsInfo = new HashMap<>();
	
	

	// El font que usamos para dibujar texto
	private Font font = new Font("Arial", Font.BOLD, 12);

	// Indica si mostramos el texto la ayuda o no
	private boolean showHelp;

	public MapViewer() {
		initGUI();
	}

	private void initGUI() {
		// TODO REVISAR

		List<State> states = new ArrayList<State>();

		states.add(null);
		for (State s : State.values())
			states.add(s);

		addKeyListener(new KeyAdapter() {

			private void next_state() {
				int i = 0;

				while (currState != states.get(i))
					i++;
				i++;
				if (i == states.size())
					currState = null;
				else
					currState = states.get(i);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case 'h':
					showHelp = !showHelp;
					repaint();
					break;
				case 's':
					next_state();
					repaint();
					break;
				default:
					break;
				}
			}

		});

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus(); // Esto es necesario para capturar las teclas cuando el ratón está sobre este
								// componente.
			}
		});

		// Por defecto mostramos todos los animales
		currState = null;

		// Por defecto mostramos el texto de ayuda
		showHelp = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Cambiar el font para dibujar texto
		g.setFont(font);

		// Dibujar fondo blanco
		gr.setBackground(Color.WHITE);
		gr.clearRect(0, 0, width, height);

		// Dibujar los animales, el tiempo, etc.
		if (objs != null)
			drawObjects(gr, objs, time);

		// TODO Mostrar el texto de ayuda si _showHelp es true. El texto a mostrar es el
		// siguiente (en 2 líneas):
		if (showHelp) {
			g.setColor(Color.RED);
			g.drawString("h: toggle help", 30, 20);
			g.drawString("s: show animals of a specific state", 30, 35);
		}

	}

	private boolean visible(AnimalInfo a) {
		return currState == null || a.get_state() == currState;
	}

	private void drawObjects(Graphics2D g, Collection<AnimalInfo> animals, Double time) {

		// TODO Dibujar el grid de regiones
		int aux = rwidth;
		for (int i = 0; i < cols - 1; i++) {
			g.drawLine(aux, 0, aux, height);
			aux += rwidth;
		}
		aux = rheight;
		for (int i = 0; i < rows - 1; i++) {
			g.drawLine(0, aux, width, aux);
			aux += rheight;
		}
		
		kindsInfo.clear(); //Se limpia para que se resetee el count y no diverja a infinito
		
		// Dibujar los animales
		for (AnimalInfo a : animals) {

			// Si no es visible saltamos la iteración
			if (!visible(a))
				continue;

			// La información sobre la especie de 'a'
			SpeciesInfo esp_info = kindsInfo.get(a.get_genetic_code());

			// Si esp_info es null, añade una entrada correspondiente al mapa. Para el
			// color usa ViewUtils.get_color(a.get_genetic_code())
			if (esp_info == null) {
				esp_info = new SpeciesInfo(ViewUtils.get_color(a.get_genetic_code()));
				kindsInfo.put(a.get_genetic_code(), esp_info);
			}
			
			// Incrementar el contador de la especie (es decir el contador dentro de
			// tag_info)
			esp_info.count++;

			// Dibujar el animal en la posicion correspondiente, usando el color
			// tag_info._color. Su tamaño tiene que ser relativo a su edad, por ejemplo
			// edad/2+2. Se puede dibujar usando fillRoundRect, fillRect o fillOval.
			g.setColor(esp_info.color);
			g.fillOval((int) a.get_position().getX(), (int) a.get_position().getY(), (int) a.get_age() / 2 + 4,
					(int) a.get_age() / 2 + 4);
		}

		// TODO Dibujar la etiqueta del estado visible, sin no es null.
		if (currState != null)
			drawStringWithRect(g, 30, height - 120, "State: " + currState.toString());

		// TODO Dibujar la etiqueta del tiempo. Para escribir solo 3 decimales puede
		// usar String.format("%.3f", time)
		drawStringWithRect(g, 30, height - 30, "Time: " + String.format("%.3f", time));

		// TODO Dibujar la información de todas la especies. Al final de cada iteración
		// poner el contador de la especie correspondiente a 0 (para resetear el cuento)
		aux = 60;
		for (Entry<String, SpeciesInfo> e : kindsInfo.entrySet()) {
			g.setColor(e.getValue().color);
			//drawStringWithRect(g, 30, height - aux, e.getKey()); Lo que teniamos puesto, pero hay que cambiarlo
			//TODO tiene que poner el numero de bichos de cada especie, 
			//en principio es esto, pero hay que resetearlo pq no para de ascender
			drawStringWithRect(g, 30, height - aux, e.getKey() + ": " + e.getValue().count);
			aux += 30;
		}
	}

	// Un método que dibujar un texto con un rectángulo
	void drawStringWithRect(Graphics2D g, int x, int y, String s) {
		Rectangle2D rect = g.getFontMetrics().getStringBounds(s, g);
		g.drawString(s, x, y);
		g.drawRect(x - 1, y - (int) rect.getHeight(), (int) rect.getWidth() + 1, (int) rect.getHeight() + 5);
	}

	@Override
	public void update(List<AnimalInfo> objs, Double time) {
		// Almacenar objs y time en los atributos correspondientes y llamar a
		// repaint() para redibujar el componente.
		this.objs = objs;
		this.time = time;
		repaint();
	}

	@Override
	public void reset(double time, MapInfo map, List<AnimalInfo> animals) {
		// Actualizar los atributos _width, _height, _cols, _rows, etc.
		width = map.get_width();
		height = map.get_height();
		cols = map.get_cols();
		rows = map.get_rows();
		rwidth = map.get_region_width();
		rheight = map.get_region_height();

		// Esto cambia el tamaño del componente, y así cambia el tamaño de la ventana
		// porque en MapWindow llamamos a pack() después de llamar a reset
		setPreferredSize(new Dimension(map.get_width(), map.get_height()));

		// Dibuja el estado
		update(animals, time);
	}

}