package simulator.factories;

import org.json.JSONObject;

import simulator.model.SelectYoungest;
import simulator.model.SelectionStrategy;

public class SelectYoungestBuilder extends Builder<SelectionStrategy> {
	
	private static final String TYPE = "youngest", DESC = "SelectionStrategy: youngest";

	public SelectYoungestBuilder(String type_tag, String desc) {
		super(TYPE, DESC);
	}

	@Override
	protected SelectionStrategy create_instance(JSONObject data) {
		return new SelectYoungest();
	}

}
