package simulator.factories;

import org.json.JSONObject;

import simulator.model.DefaultRegion;
import simulator.model.Region;

public class DefaultRegionBuilder extends Builder<Region> {

	private static final String TYPE = "default", DESC = "DefaultRegion builder";

	public DefaultRegionBuilder() {
		super(TYPE, DESC);
	}

	@Override
	protected Region create_instance(JSONObject data) {
		return new DefaultRegion();
	}

	protected void fill_in_data(JSONObject o) {
		// TODO Sale en oag 14 como debe ser. Al ser el json data vacio, asumo que no
		// hay que poner nada(?
	}

}