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
	// Magway
	List<String> state8 = List.of("AhLaNa", "KhaMaNa", "GaGaNa", "KaMaNa", "MaKaNa", "MaBaNa", "MaTaNa", "MaLaNa", "MaMaNa", "MaHtaNa", "MaThaNa", "NaMaNa", "NgaHpaNa", "PaKhaKa", "PaMaNa", "PaHpaNa", "SaLaNa", "SaMaNa", "SaHpaNa", "SaTaYa", "SaPaWa", "TaTaKa", "ThaYaNa", "HtaLaNa", "YaNaKha", "YaSaKa", "KaHtaNa");
	// Mandalay
	List<String> state9 = List.of("AhMaNa", "AhMaZa", "KhaAhZa", "KhaMaSa", "KaPaTa", "KaSaNa", "MaTaYa", "MaHaMa", "MaLaNa", "MaHtaLa", "MaKaNa", "MaKhaNa", "MaThaNa", "NaHtaKa", "NgaThaYa", "NgaZaNa", "NyaOuNa", "PaThaKa", "PaBaNa", "PaKaKha", "PaOuLa", "SaKaNa", "SaKaTa", "ThaPaKa", "TaTaOu", "TaThaNa", "ThaSaNa", "WaTaNa", "YaMaTha", "TaKaTa", "MaMaNa", "DaKhaTha", "LaWaNa", "OuTaTha", "PaBaTha", "PaMaNa", "TaKaNa", "ZaBaTha", "ZaYaTha");
	// Mon
	List<String> state10 = List.of("BaLaNa", "KhaSaNa", "KhaZaNa", "KaMaYa", "KaHtaNa", "LaMaNa", "MaLaMa", "MaDaNa", "PaMaNa", "ThaHpaYa", "ThaHtaNa", "YaMaNa");
	// Rakhine
	List<String> state11 = List.of("AhMaNa", "BaThaTa", "GaMaNa", "KaHpaNa", "KaTaNa", "MaAhTa", "MaTaNa", "MaPaNa", "MaAhNa", "MaOuNa", "MaPaTa", "PaTaNa", "PaNaTa", "YaBaNa", "YaThaTa", "SaTaNa", "ThaTaNa", "TaKaNa", "KaTaLa", "TaPaWa");
	// Yangon
	List<String> state12 = List.of("AhLaNa", "BaHaNa", "BaTaHta", "KaKaKa", "DaGaYa", "DaGaMa", "DaGaSa", "DaGaTa", "DaGaNa", "DaLaNa", "DaPaNa", "LaThaYa", "LaMaNa", "LaKaNa", "MaBaNa", "HtaTaPa", "AhSaNa", "KaMaYa", "KaMaNa", "KhaYaNa", "KaKhaKa", "KaTaTa", "KaTaNa", "KaMaTa", "LaMaTa", "LaThaNa", "MaYaKa", "MaGaDa", "MaGaTa", "OuKaMa", "PaBaTa", "PaZaTa", "SaKhaNa", "SaKaKha", "SaKaNa", "YaPaTha", "OuKaTa", "TaTaHta", "TaKaNa", "TaMaNa", "ThaKaTa", "ThaLaNa", "ThaGaKa", "ThaKhaNa", "TaTaNa", "YaKaNa", "OuKaNa");
	// Shan
	List<String> state13 = List.of("AhKhaNa", "KhaYaHa", "KhaMaNa", "HaTaNa", "HaPaNa", "HaPaTa", "SaHpaNa", "ThaNaNa", "SaSaNa", "ThaPaNa", "KaLaHpa", "KaLaNa", "KaLaDa", "KaMaSa", "KaTaNa", "KaYaNa", "KaTaTa", "KaHaNa", "KaLaTa", "KaKhaNa", "KaMaNa", "KaTaLa", "KaThaNa", "LaKhaNa", "LaKhaTa", "LaYaNa", "LaKaNa", "LaHaNa", "LaLaNa", "MaBaNa", "MaMaSa", "MaTaNa", "MaTaTa", "MaMaNa", "MaMaNa", "MaHpaNa", "MaKaNa", "MaPaNa", "MaHpaNa", "MaSaNa", "MaYaNa", "MaKaNa", "MaKhaNa", "MaLaNa", "MaMaTa", "MaMaTa", "MaNaNa", "MaPana", "MaTaNa", "MaYaTa", "MaYaNa", "MaYaNa", "MaSaTa", "NaKhaWa", "NaTaNa", "NaKhaNa", "NaMaTa", "NaHpaNa", "NaSaNa", "NaKaNa", "NaWaNa", "NaPhaNa", "NaKhaNa", "NaKhaTa", "NyaYaNa", "PaKhaNa", "PaYaNa", "PaSaNa", "PaWaNa", "HpaKhaNa", "PaTaYa", "PaLaNa", "TaKhaLa", "TaYaNa", "TaKana", "YaLaNa", "YaSaNa", "YaHpaNa");
	// Ayarwaddy
	List<String> state14 = List.of("AhMaTa", "BaKaLa", "DaNaHpa", "DaDaYa", "AhMaNa", "HaKaKa", "HaThaTa", "AhGaPa", "KaKaHta", "KaLaNa", "KaKhaNa", "KaKaNa", "KaPaNa", "LaPaTa", "LaMaNa", "MaAhPa", "MaMaKa", "MaAhNa", "MaMaNa", "NgaPaTa");

	public NRCHelper() {
		stateMap = new HashMap<>();
		stateMap.put("1/", state1);
		stateMap.put("2/", state2);
		stateMap.put("3/", state3);
		stateMap.put("4/", state4);
		stateMap.put("5/", state5);
		stateMap.put("6/", state6);
		stateMap.put("7/", state7);
		stateMap.put("8/", state8);
		stateMap.put("9/", state9);
		stateMap.put("10/", state10);
		stateMap.put("11/", state11);
		stateMap.put("12/", state12);
		stateMap.put("13/", state13);
		stateMap.put("14/", state14);
	}
	
	// stateCode (1 to 14)
	public List<String> getRegions(String stateCode) {
        return stateMap.get(stateCode);
    }
}
