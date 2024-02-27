package simulator.control;

import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.AnimalInfo;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

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
		SimpleObjectViewer view = null;
		if (sv) {
			MapInfo m = sim.get_map_info();
			view = new SimpleObjectViewer("[ECOSYSTEM]", m.get_width(), m.get_height(), m.get_cols(), m.get_rows());
			view.update(to_animals_info(sim.get_animals()), sim.get_time(), dt);
		}

		JSONObject init_state = sim.as_JSON();
		while (t <= sim.get_time()) {
			sim.advance(dt);
			if (sv) view.update(to_animals_info(sim.get_animals()), sim.get_time(), dt);
		}
		
		JSONObject final_state = sim.as_JSON();
		JSONObject output = new JSONObject();
		output.put("in", init_state);
		output.put("out", final_state);
		
		if (sv) view.close();
	}
	
	private int size_age(AnimalInfo a) {
		return (int)Math.round(a.get_age()) + 2;
	}

	private List<ObjInfo> to_animals_info(List<? extends AnimalInfo> animals) {
		List<ObjInfo> ol = new ArrayList<>(animals.size());
		for (AnimalInfo a : animals)
			ol.add(new ObjInfo(a.get_genetic_code(), (int) a.get_position().getX(), (int) a.get_position().getY(), size_age(a)));
		return ol;
	}
}
