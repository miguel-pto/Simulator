package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectFirst;
import simulator.model.SelectionStrategy;

public class SelectFirstBuilder extends Builder<SelectionStrategy> {
	
	private static final String TYPE = "first", DESC = "SelectionStrategy: first";

	public SelectFirstBuilder() {
		super(TYPE, DESC);
	}

	@Override
	protected SelectionStrategy create_instance(JSONObject data) {
		return new SelectFirst();
	}

}
