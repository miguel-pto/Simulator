package simulator.factories;

import org.json.JSONObject;
import org.json.JSONArray;
import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Wolf;

public class WolfBuilder extends Builder<Animal> {

	private static final String TYPE = "wolf", DESC = "Wolf builder";
	private Factory<SelectionStrategy> strategy_factory;

	public WolfBuilder(Factory<SelectionStrategy> strategy_factory) {
		super(TYPE, DESC);
		this.strategy_factory = strategy_factory;
	}
	
	protected void fill_in_data(JSONObject o) {
		o.put("pos", "Se trata de un factor Vector2D con una variable x & una variable y, de manera que si sale más allá del marco establecido se ajustan en su contrario.");
		o.put("mate strategy", "Se trata de una Selection Strategy que dictamina el método que utiliza para seleccionar a su mate una vez tenga suficiente desire.");
		o.put("hunting strategy", "Se trata de una Selection Strategy que dictamina el método por el que selecciona a la presa que cazar.");
	}

	@Override
	protected Animal create_instance(JSONObject data) {
		// LA ESTRATEGIA POR DEFECTO ES FIRST
		SelectionStrategy mate_strategy = new SelectFirst();
		SelectionStrategy hunt_strategy = new SelectFirst();
		Vector2D pos = null;
		// MATE STRATEGY
		if (data.has("mate_strategy")) {
			JSONObject info = data.getJSONObject("mate_strategy");
			mate_strategy = strategy_factory.create_instance(info);
		}
		// DANGER STRATEGY
		if (data.has("danger_strategy")) {
			JSONObject info = data.getJSONObject("hunt_strategy");
			hunt_strategy = strategy_factory.create_instance(info);
		}
		// POSICIÓN
		if (data.has("pos")) {
			JSONObject o = data.getJSONObject("pos");
			JSONArray x = o.getJSONArray("x_range");
			JSONArray y = o.getJSONArray("y_range");
			pos = new Vector2D(Utils.rand.nextDouble(x.getDouble(0), x.getDouble(1)),
					Utils.rand.nextDouble(y.getDouble(0), y.getDouble(1)));
		}
		
		return new Wolf(mate_strategy, hunt_strategy, pos);
	}

}
