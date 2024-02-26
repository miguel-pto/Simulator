package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;
import simulator.model.Region;

public class DefaultRegionBuilder extends Builder<Region> {

	private static final String TYPE = "default", DESC = "Region: default";

	public DefaultRegionBuilder() {
		super(TYPE, DESC);
	}

	@Override
	protected Region create_instance(JSONObject data) {
		return new DefaultRegion();
	}

}
