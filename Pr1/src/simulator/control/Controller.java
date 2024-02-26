package simulator.control;

import java.io.OutputStream;

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
		if (data.has("regions")) {
			JSONArray regions = data.getJSONArray("regions");
			for (int i = 0; i < regions.length(); i++) {
				JSONObject region = regions.getJSONObject(i);
				
			}
		}
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {
		while (t <= sim.get_time()) {
			sim.advance(dt);
		}
		// TODO EL OUTPUT Y EL VISOR DE OBJETOS
	}
	
	
}
