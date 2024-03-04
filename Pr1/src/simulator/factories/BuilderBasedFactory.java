package simulator.factories;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;

public class BuilderBasedFactory<T> implements Factory<T> {

	private Map<String, Builder<T>> builders;
	private List<JSONObject> builders_info;

	private BuilderBasedFactory() {
		// INICIALIZAR LAS LISTAS VACIAS
		builders = new HashMap<String, Builder<T>>();
		builders_info = new LinkedList<JSONObject>();
	}

	public BuilderBasedFactory(List<Builder<T>> builders) {
		this();
		// AÑADE LOS TIPOS DE BUILDERS
		for (Builder<T> builder : builders) {
			add_builder(builder);
		}
	}

	// MÉTODO PRIVADO PARA EL CONSTRUCTOR
	private void add_builder(Builder<T> b) {
		builders.put(b.get_type_tag(), b);
		builders_info.add(b.get_info());
	}

	@Override
	public T create_instance(JSONObject info) {
		if (info == null)
			throw new IllegalArgumentException("’info’ cannot be null");
		// OBTENER EL TIPO DE BUILDER
		Builder<T> builder = builders.get(info.getString("type"));
		if (builder != null) {
			// CREA EL ELEMENTO
			T result = builder.create_instance(info.has("data") ? info.getJSONObject("data") : new JSONObject());
			if (result != null)
				return result;
		}
		throw new IllegalArgumentException("Unrecognized ‘info’:" + info.toString());
	}

	// DEVUELVE INFO PARA EL USUARIO
	@Override
	public List<JSONObject> get_info() {
		return Collections.unmodifiableList(builders_info);
	}
}
