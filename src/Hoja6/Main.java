package Hoja6;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
public class Main {
    private Map<String, String> inventory;
    
    public Main(String readArchive) {
        this.inventory = new HashMap<>();
        try {
            BufferedReader lector = new BufferedReader(new FileReader(readArchive));
            String line;
            while ((line = lector.readLine()) != null) {
                String[] data = line.split("\\|");
                String category = data[0];
                String product = data[1];
                inventory.put(product, category);
            }
            lector.close();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
    
    public void addProduct(Scanner scanner) {
        System.out.print("Ingrese la categoría del producto: ");
        String category = scanner.nextLine();
        if (!inventory.containsValue(category)) {
            System.out.println("La categoría ingresada no existe.");
            return;
        }

        System.out.print("Ingrese el nombre del producto: ");
        String productName = scanner.nextLine();
        inventory.merge(productName, category, (oldValue, newValue) -> category);
        System.out.println("Producto agregado con éxito.");
    }

    public void showCategory(Scanner scanner) {
        System.out.print("Ingrese el nombre del producto: ");
        String productName = scanner.nextLine();
        String category = inventory.get(productName);
        if (category == null) {
            System.out.println("El producto no existe.");
        } else {
            System.out.printf("La categoría del producto %s es %s.%n", productName, category);
        }
    }
    
    public void showInventory() {
        System.out.println("Inventario:");
        for (Map.Entry<String, String> entry : inventory.entrySet()) {
            String productName = entry.getKey();
            String category = entry.getValue();
            long count = inventory.entrySet().stream().filter(e -> e.getValue().equals(category)).count();
            System.out.printf("- %s (%s): %d%n", productName, category, count);
        }
    }
    
    public void showInventoryByType() {
        Map<String, List<String>> inventoryByType = new HashMap<>();
        for (Map.Entry<String, String> entry : inventory.entrySet()) {
            String productName = entry.getKey();
            String category = entry.getValue();
            inventoryByType.computeIfAbsent(category, k -> new ArrayList<>()).add(productName);
        }

        System.out.println("Inventario por tipo:");
        for (Map.Entry<String, List<String>> entry : inventoryByType.entrySet()) {
            String category = entry.getKey();
            List<String> products = entry.getValue();
            System.out.printf("- %s:%n", category);
            for (String productName : products) {
                long count = inventory.entrySet().stream().filter(e -> e.getKey().equals(productName)).count();
                System.out.printf("  * %s: %d%n", productName, count);
            }
        }
    }
    
    public void showCompleteInventory() {
        System.out.println("Inventario completo:");
        for (Map.Entry<String, String> entry : inventory.entrySet()) {
            String productName = entry.getKey();
            String category = entry.getValue();
            System.out.printf("- %s (%s)%n", productName, category);
        }
    }
    
    public void showCategoriesByType() {
        Set<String> categories = new TreeSet<>(inventory.values());
        System.out.println("Categorías existentes:");
        for (String category : categories) {
            System.out.println("- " + category);
        }
    }

    private int countProduct(String productName) {
    	int count = 0;
        for (String product : inventory.keySet()) {
            if (product.equals(productName)) {
                count++;
            }
        }
        return count;
    }
        
}
