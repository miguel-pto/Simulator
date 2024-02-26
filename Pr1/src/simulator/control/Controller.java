package simulator.control;

import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Simulator;

public class Controller {

	private Simulator sim;

	public Controller(Simulator sim) {
		this.sim = sim;
	}

	public void load_data(JSONObject data) {
		if (data.has("regions")) {
			JSONArray regions = data.getJSONArray("regions");
			for (int i = 0; i < regions.length(); i++) {
				JSONObject region = regions.getJSONObject(i);
				JSONArray row = region.getJSONArray("row");
				JSONArray col = region.getJSONArray("col");
				int rf = row.getInt(0);
				int rt = row.getInt(1);
				int cf = col.getInt(0);
				int ct = col.getInt(1);
				JSONObject o = region.getJSONObject("spec");
				for (int r = rf; r <= rt; r++)
					for (int c = cf; c <= ct; c++)
						sim.set_region(r, c, o);
			}
		}

		JSONArray animals = data.getJSONArray("animals");
		for (int i = 0; i < animals.length(); i++) {
			JSONObject animal = animals.getJSONObject(i);
			int n = animal.getInt("amount");
			JSONObject o = animal.getJSONObject("spec");
			for (int j = 0; j < n; j++)
				sim.add_animal(o);
		}
	}

	public void run(double t, double dt, boolean sv, OutputStream out) {
		JSONObject init_state = sim.as_JSON();
		while (t <= sim.get_time()) {
			sim.advance(dt);
		}
		JSONObject final_state = sim.as_JSON();
		JSONObject output = new JSONObject();
		output.put("in", init_state);
		output.put("out", final_state);
		if (sv) {
			// TODO VISOR
		}
	}

}
