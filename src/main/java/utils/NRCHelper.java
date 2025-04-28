package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NRCHelper {

	private Map<String, List<String>> stateMap;
	// Kachin
	List<String> state1 = List.of("AhGaYa", "BaMaNa", "KhaPhaNa", "DaPhaYa", "HaPaNa", "HpaKaNa", "KaMaTa", "KaPaTa", "KhaLaPha", "LaGaNa", "MaKhaBa", "MaSaNa", "MaKaTa", "MaNyaNa", "MaMaNa", "MaKaNa", "MaLaNa", "NaMaNa", "PaWaNa", "PaNaDa", "PaTaAh", "SaDaNa", "YaBaYa", "YaKaNa", "SaBaNa", "SaPaYa", "TaNaNa", "TaSaLa", "WaMaLa");
	// Kayar
	List<String> state2 = List.of("BaLaKha", "DaMaSa", "HpaSaNa", "HpaYaSa", "LaKaNa", "MaSaNa", "YaTaNa", "YaThaNa");
	// Kayin
	List<String> state3 = List.of("BaGaLa", "LaBaNa", "BaAhNa", "HpaPaNa", "BaThaSa", "KaMaMa", "MaMaNa", "KaKaYa", "KaDaNa", "KaSaKa", "KaDaTa", "LaThaNa", "MaWaTa", "PaKaNa", "YaYaTha", "SaKaLa", "ThaTaNa", "ThaTaKa", "WaLaMa");
	// Chin
	List<String> state4 = List.of("KaKhaNa", "HpaLaNa", "HaKhaNa", "KaPaLa", "MaTaPa", "MaTaNa", "PaLaWa", "YaZaNa", "YaKhaDa", "SaMaNa", "TaTaNa", "HtaTaLa", "TaZaNa");
	// Sagaing
	List<String> state5 = List.of("AhYaTa", "BaMaNa", "BaTaLa", "KhaOuTa", "KhaTaNa", "HaMaLa", "AhTaNa", "KaLaHta", "KaLaWa", "KaBaLa", "KaNaNa", "KaThaNa", "KaLaTA", "KhaOuNa", "KaLaNa", "LaHaNa", "LaYaSa", "MaLaNa", "MaKaNa", "MaYaNa", "MaMaNa", "MaMaTa", "NaYaNa", "NgaZaNa", "PaLaNa", "PhaPaNa", "PaLaBa", "SaKaNa", "SaLaKa", "YaBaNa", "DaPaYa", "TaMaNa", "TaSaNa", "HtaKaNa", "WaLaNa", "WaThaNa", "YaOuNa", "YaMaPa", "KaMaNa", "KhaPaNa");
	// Ta Nin Thar Yi
	List<String> state6 = List.of("BaPaNa", "HtaWaNa", "KaLaAh", "KaThaNa", "KaSaNa", "LaLaNa", "MaMaNa", "PaLaNa", "TaThaYa", "ThaYaKha", "YaHpaNa", "KhaMaNa", "MaTaNa", "PaLaTa", "KaYaYa");
	// Bago
	List<String> state7 = List.of("DaOuNa", "KaPaKa", "KaWaNa", "KaKaNa", "KhaTaNa", "LaPaTa", "MaLaNa", "MaNyaNa", "NaTaLa", "NyaLaPa", "AhHpaNa", "AhTaNa", "PaTaNa", "PaKhaTa", "PaKhaNa", "PaTaTa", "PaNaKa", "HpaMaNa", "PaMaNa", "YaTaNa", "YaKaNa", "HtaTaPa", "TaNgaNa", "ThaNaPa", "ThaWaTa", "ThaKaNa", "ThaSaNa", "WaMaNa", "YaTaYa", "ZaKaNa", "PaTaSa");

	public NRCHelper() {
		stateMap = new HashMap<>();
		stateMap.put("1", state1);
		stateMap.put("2", state2);
		stateMap.put("3", state3);
		stateMap.put("4", state4);
		stateMap.put("5", state5);
		stateMap.put("6", state6);
		stateMap.put("7", state7);
	}
	
	// stateCode (1 to 14)
	public List<String> getRegions(String stateCode) {
        return stateMap.get(stateCode);
    }
}
