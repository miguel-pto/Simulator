package simulator.factories;

import org.json.JSONObject;

public abstract class Builder<T> {
	private String type_tag; // Coincide con el campo type de la estructura JSON
	private String desc; // Dice los tipos de objetos que puede crear este builder

	public Builder(String type_tag, String desc) {
		if (type_tag == null || desc == null || type_tag.isBlank() || desc.isBlank())
			throw new IllegalArgumentException("Invalid type/desc");

		this.type_tag = type_tag;
		this.desc = desc;
	}

	public String get_type_tag() {
		return type_tag;
	}

	public JSONObject get_info() {
		JSONObject info = new JSONObject();
		info.put("type", type_tag);
		info.put("desc", desc);
		JSONObject data = new JSONObject();
		fill_in_data(data);
		info.put("data", data);

		return info;
	}

	protected void fill_in_data(JSONObject o) {
	} // Se sobreescribe en las subclases para rellenar los datos necesarios

	public String toString() {
		return desc;
	}

	protected abstract T create_instance(JSONObject data);

}
