package simulator.control;

import java.io.OutputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Simulator;


public class Controller {

	public static final String REGIONES = "regions";
	public static final String ROW = "row";
	public static final String COL = "col";
	public static final String SPEC = "spec";
	public static final String ANIMALS = "animals";
	public static final String AMOUNT = "amount";

	private Simulator sim;

	public Controller(Simulator sim) {
		this.sim = sim;
	}

	public void load_data(JSONObject data) {
		JSONArray regiones = data.getJSONArray(REGIONES);
		if (!regiones.isEmpty()) {
			for (int i=0; i<regiones.length(); i++) {
				JSONObject region = (JSONObject) regiones.get(i);
				int row[] = (int[]) region.get(ROW);
				int col[] = (int[]) region.get(COL); 
				JSONObject spec = region.getJSONObject(SPEC); 
				for (int j=row[0]; j<=row[1]; j++) {
					for (int k = col[0]; k<=col[1]; k++) {
						sim.set_region(k, k, spec);
					}
				}
				
			}
		}
		
		JSONArray animales = data.getJSONArray(ANIMALS);
		for (int i=0; i<animales.length(); i++) {
			JSONObject animal = (JSONObject) animales.get(i);
			int cant = animal.getInt(AMOUNT);
			JSONObject spec = animal.getJSONObject(SPEC);
			for (int j=0; j<cant; j++)
				sim.add_animal(spec);
		}
		// TODO REVISAR Y QUE ESTE BIEN
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {
		while (t <= sim.get_time()) {
			sim.advance(dt);
		}
		// TODO EL OUTPUT Y EL VISOR DE OBJETOS
	}
	
	
}
