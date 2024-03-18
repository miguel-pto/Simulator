package simulator.model;

public interface MapInfo extends JSONable,

	Iterable<MapInfo.RegionData> {
		public record RegionData(int row, int col, RegionInfo r) {
			/*EL REGISTRO RegionData SIMPLEMENTE INCLUYE LA POSICIÓN DE LA
			 * REGIÓN Y LA REGIÓN PERO COMO RegionInfo EN LUGAR DE Region
			 * PARA ASEGURARNOS QUE NO SE ALTERA SU ESTADO DESDE FUERA.*/
	}
	
	
	public int get_cols();

	public int get_rows();

	public int get_width();

	public int get_height();

	public int get_region_width();

	public int get_region_height();

}
