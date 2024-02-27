package simulator.factories;

import org.json.JSONObject;

public abstract class Builder<T> {
	private String type_tag; // COINCIDE CON EL CAMPO TYPE DE LA ESTRUCTURA JSON
	private String desc; // DICE LOS TIPOS DE OBJETOS QUE PUEDE CREAR EL BUILDER

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
	} // SE SOBREESCRIBE EN LAS SUBCLASES PARA RELLENAR LOS DATOS NECESARIOS

	public String toString() {
		return desc;
	}

	protected abstract T create_instance(JSONObject data);

}
