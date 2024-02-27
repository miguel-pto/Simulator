package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Animal;
import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;
import simulator.model.Sheep;

public class WolfBuilder extends Builder<Animal> {

	private static final String TYPE = "wolf", DESC = "Wolf builder";
	private Factory<SelectionStrategy> strategy_factory;

	public WolfBuilder(Factory<SelectionStrategy> strategy_factory) {
		super(TYPE, DESC);
		this.strategy_factory = strategy_factory;
	}

	@Override
	protected Animal create_instance(JSONObject data) {
		SelectionStrategy mate_strategy = new SelectFirst();
		SelectionStrategy hunt_strategy = new SelectFirst();
		Vector2D pos = null;

		if (data.has("mate_strategy")) {
			JSONObject info = data.getJSONObject("mate_strategy");
			mate_strategy = strategy_factory.create_instance(info);
		}
		if (data.has("danger_strategy")) {
			JSONObject info = data.getJSONObject("hunt_strategy");
			hunt_strategy = strategy_factory.create_instance(info);
		}
		if (data.has("pos")) {
			JSONObject o = data.getJSONObject("pos");
			JSONObject x = o.getJSONObject("x_range");
			JSONObject y = o.getJSONObject("y_range");
			pos = new Vector2D(Utils.rand.nextDouble(x.getDouble("0"), x.getDouble("1")),
					Utils.rand.nextDouble(y.getDouble("0"), y.getDouble("1")));
		}
		return new Sheep(mate_strategy, hunt_strategy, pos);
	}

}
