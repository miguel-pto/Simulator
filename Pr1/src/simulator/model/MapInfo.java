package simulator.model;

public interface MapInfo extends JSONable, Iterable<MapInfo.RegionData> {

	// ITERADOR QUE PERMITE RECORRER SOBRE LAS REGIONES SIN HACER USO DE UNA FUNCIÓN
	// TAL COMO "get_region(int row, int col)
	public record RegionData(int row, int col, RegionInfo r) {
	}

	public int get_cols();

	public int get_rows();

	public int get_width();

	public int get_height();

	public int get_region_width();

	public int get_region_height();

}
