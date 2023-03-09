package Hoja6;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Maps {
	public static Map<String, Integer> getMap(int choice) {
        Map<String, Integer> map;
        switch (choice) {
            case 1:
                map = new HashMap<>();
                break;
            case 2:
                map = new TreeMap<>();
                break;
            case 3:
                map = new LinkedHashMap<>();
                break;
            default:
                throw new IllegalArgumentException("Invalid choice");
        }
        return map;
    }
}
