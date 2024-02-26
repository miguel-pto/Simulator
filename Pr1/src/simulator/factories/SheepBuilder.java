package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;

public class SheepBuilder extends Builder<Animal> {
	
	private static final String TYPE = "sheep", DESC = "Animal: sheep";
	private Factory<SelectionStrategy> strategy_factory;

	public SheepBuilder(Factory<SelectionStrategy> strategy_factory) {
		super(TYPE, DESC);
		this.strategy_factory = strategy_factory;
	}
	
	protected void fill_in_data(JSONObject o) {
		// TODO Y LO MISMO EN WOLF
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
			JSONObject x = o.getJSONObject("x_range");
			JSONObject y = o.getJSONObject("y_range");
			pos = new Vector2D(Utils._rand.nextDouble(x.getDouble("0"), x.getDouble("1")),
					Utils._rand.nextDouble(y.getDouble("0"), y.getDouble("1")));
		}
		return new Sheep(mate_strategy, danger_strategy, pos);
	}
}
