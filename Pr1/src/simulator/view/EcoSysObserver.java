package simulator.view;


import simulator.model.MapInfo;
import simulator.model.RegionInfo;
import java.util.List;
import simulator.model.AnimalInfo;

public interface EcoSysObserver {
	void onRegister(double time, MapInfo map, List<AnimalInfo> animals);
	void onReset(double time, MapInfo map, List<AnimalInfo> animals);
	void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals);
	void onRegionSet(int row, int col, MapInfo map, RegionInfo r);
	void onAdvanced(double time, MapInfo map, List<AnimalInfo> animals, double dt);
}
