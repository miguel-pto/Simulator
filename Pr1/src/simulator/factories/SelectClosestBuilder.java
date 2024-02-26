package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectClosest;
import simulator.model.SelectionStrategy;

public class SelectClosestBuilder extends Builder<SelectionStrategy> {
	
	private static final String TYPE = "closest", DESC = "SelectionStrategy: closest";

	public SelectClosestBuilder(String type_tag, String desc) {
		super(TYPE, DESC);
	}

	@Override
	protected SelectionStrategy create_instance(JSONObject data) {
		return new SelectClosest();
	}

}
