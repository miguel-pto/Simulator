package simulator.factories;

import org.json.JSONObject;
import simulator.model.Region;
import simulator.model.DynamicSupplyRegion;

public class DynamicSupplyRegionBuilder extends Builder<Region> {
	
	private static final String TYPE = "dynamic", DESC = "Region: dynamic";

	public DynamicSupplyRegionBuilder() {
		super(TYPE, DESC);
	}

	@Override
	protected Region create_instance(JSONObject data) {
		double factor = 2.0, food = 1000.0;
		
		if (data.has("factor")) factor = data.getDouble("factor");
		if (data.has("food")) food = data.getDouble("food");
		
		return new DynamicSupplyRegion(food, factor);
	}

}
