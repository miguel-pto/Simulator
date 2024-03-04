package simulator.factories;

import org.json.JSONObject;
import simulator.model.Region;
import simulator.model.DynamicSupplyRegion;

public class DynamicSupplyRegionBuilder extends Builder<Region> {

	private static final String TYPE = "dynamic", DESC = "DynamicSupplyRegion builder";

	public DynamicSupplyRegionBuilder() {
		super(TYPE, DESC);
	}

	@Override
	protected Region create_instance(JSONObject data) {
		double factor = DynamicSupplyRegion.INIT_FACTOR, food = DynamicSupplyRegion.INIT_FOOD;
		// INICIALIZA EL FACTOR, SI ESTÁ ESPECIFICADO
		if (data.has("factor"))
			factor = data.getDouble("factor");
		// INICIALIZA LA COMIDA, SI ESTÁ ESPECIFICADA
		if (data.has("food"))
			food = data.getDouble("food");

		return new DynamicSupplyRegion(food, factor);
	}

}
