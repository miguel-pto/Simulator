package simulator.factories;

import org.json.JSONObject;
import org.json.JSONArray;
import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;

public class SheepBuilder extends Builder<Animal> {

	private static final String TYPE = "sheep", DESC = "Sheep builder";
	private Factory<SelectionStrategy> strategy_factory;

	public SheepBuilder(Factory<SelectionStrategy> strategy_factory) {
		super(TYPE, DESC);
		this.strategy_factory = strategy_factory;
	}

	protected void fill_in_data(JSONObject o) {
		// TODO Y LO MISMO EN WOLF
		// Permiteme preguntarte sobre esto Miguel
		// Esto es para mostrar al usuario. Todavía no tengo muy claro que habrá que
		// hacer pero por ahora me parece opcional. Ya lo pondremos bonito luego
	}

	@Override
	protected Animal create_instance(JSONObject data) {
		SelectionStrategy mate_strategy = new SelectFirst();
		SelectionStrategy danger_strategy = new SelectFirst();
		Vector2D pos = null;

		if (data.has("mate_strategy")) {
			JSONObject info = data.getJSONObject("mate_strategy");
			mate_strategy = strategy_factory.create_instance(info);
		}
		if (data.has("danger_strategy")) {
			JSONObject info = data.getJSONObject("danger_strategy");
			danger_strategy = strategy_factory.create_instance(info);
		}
		if (data.has("pos")) {
			JSONObject o = data.getJSONObject("pos");
			JSONArray x = o.getJSONArray("x_range");
			JSONArray y = o.getJSONArray("y_range");
			pos = new Vector2D(Utils.rand.nextDouble(x.getDouble(0), x.getDouble(1)),
					Utils.rand.nextDouble(y.getDouble(0), y.getDouble(1)));
		}
		return new Sheep(mate_strategy, danger_strategy, pos);
	}
}
