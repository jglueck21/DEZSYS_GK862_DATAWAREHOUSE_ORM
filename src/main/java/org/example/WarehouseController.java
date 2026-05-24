package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(path = "/api")
public class WarehouseController {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PurchasesRepository purchasesRepository;

    // ─── WAREHOUSE ────────────────────────────────────────────────────────────

    /** Add one of the two predefined warehouses: use id "001" or "002" */
    @PostMapping("/warehouse/add/{warehouseId}")
    public @ResponseBody String addWarehouse(@PathVariable String warehouseId) {
        WarehouseData data = new WarehouseData();
        data.setWarehouseID(warehouseId);
        if (warehouseId.equals("001")) {
            data.setWarehouseName("Linz Bahnhof");
            data.setWarehouseAddress("Bahnhofsstrasse 27/9");
            data.setWarehouseCity("Linz");
            data.setWarehouseCountry("Austria");
            data.setWarehousePostalCode(4020);
        } else {
            data.setWarehouseName("Wien Hauptbahnhof");
            data.setWarehouseAddress("Am Hbf 1");
            data.setWarehouseCity("Wien");
            data.setWarehouseCountry("Austria");
            data.setWarehousePostalCode(1100);
        }
        warehouseRepository.save(data);
        return "Warehouse added!";
    }

    /** Get all warehouses */
    @GetMapping(value = "/warehouse/all", produces = "application/json")
    public @ResponseBody Iterable<WarehouseData> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    /** Get a single warehouse by warehouseID */
    @GetMapping(value = "/warehouse/{id}", produces = "application/json")
    public @ResponseBody WarehouseData getWarehouse(@PathVariable String id) {
        return warehouseRepository.findByWarehouseID(id).orElse(null);
    }

    /** Update name and city of a warehouse by warehouseID */
    @PutMapping("/warehouse/update/{warehouseID}")
    public @ResponseBody String updateWarehouse(@PathVariable String warehouseID,
                                                @RequestParam String name,
                                                @RequestParam String city) {
        WarehouseData data = warehouseRepository.findByWarehouseID(warehouseID).orElse(null);
        if (data == null) return "Warehouse not found!";
        data.setWarehouseName(name);
        data.setWarehouseCity(city);
        warehouseRepository.save(data);
        return "Warehouse updated!";
    }

    /** Delete a warehouse and all its products */
    @DeleteMapping("/warehouse/delete/{id}")
    public @ResponseBody String deleteWarehouse(@PathVariable String id) {
        WarehouseData data = warehouseRepository.findByWarehouseID(id).orElse(null);
        if (data == null) return "Warehouse not found!";
        warehouseRepository.delete(data);
        return "Warehouse and its products deleted!";
    }

    // ─── PRODUCTS ─────────────────────────────────────────────────────────────

    /**
     * Add a product to a warehouse.
     * Available productIds: 00-443175, 00-871895, 01-926885, 00-316253,
     *                       02-341867, 03-112233, 04-556677, 05-998877,
     *                       06-334455, 07-667788
     */
    @PostMapping("/product/add/{warehouseId}/{productId}")
    public @ResponseBody String addProduct(@PathVariable String warehouseId,
                                           @PathVariable String productId) {
        WarehouseData warehouse = warehouseRepository.findByWarehouseID(warehouseId).orElse(null);
        if (warehouse == null) return "Warehouse not found!";

        ProductData productData;
        switch (productId) {
            case "00-443175": productData = new ProductData("00-443175", "Bio Orangensaft Sonne",  "Getraenk",    100, "Packung 1L");   break;
            case "00-871895": productData = new ProductData("00-871895", "Bio Apfelsaft Gold",     "Getraenk",    120, "Packung 1L");   break;
            case "01-926885": productData = new ProductData("01-926885", "Ariel Waschmittel Color","Waschmittel",  80, "Packung 3KG");  break;
            case "00-316253": productData = new ProductData("00-316253", "Persil Discs Color",     "Waschmittel",  60, "Packung 700G"); break;
            case "02-341867": productData = new ProductData("02-341867", "Milka Tafel",            "Suesigkeit",  200, "Packung 500G"); break;
            case "03-112233": productData = new ProductData("03-112233", "Coca Cola",              "Getraenk",    150, "Flasche 1.5L"); break;
            case "04-556677": productData = new ProductData("04-556677", "Manner Schnitten",       "Suesigkeit",   90, "Packung 400G"); break;
            case "05-998877": productData = new ProductData("05-998877", "Clever Reis",            "Lebensmittel", 75, "Packung 1KG");  break;
            case "06-334455": productData = new ProductData("06-334455", "Finish Tabs",            "Haushalt",     50, "Packung 40St"); break;
            case "07-667788": productData = new ProductData("07-667788", "Domestos",               "Haushalt",     65, "Flasche 750ML");break;
            default: return "Unknown productId!";
        }
        productData.setWarehouseData(warehouse);
        productRepository.save(productData);
        return "Product added!";
    }

    /** Get all products */
    @GetMapping(value = "/product/all", produces = "application/json")
    public @ResponseBody Iterable<ProductData> getAllProducts() {
        return productRepository.findAll();
    }

    /** Get a single product by warehouseID and productID */
    @GetMapping(value = "/product/{warehouseID}/{productID}", produces = "application/json")
    public @ResponseBody ProductData getProduct(@PathVariable String warehouseID,
                                                @PathVariable String productID) {
        return productRepository.findByWarehouseData_WarehouseIDAndProductID(warehouseID, productID).orElse(null);
    }

    /** Delete a single product */
    @DeleteMapping("/product/delete/{warehouseID}/{productID}")
    public @ResponseBody String deleteProduct(@PathVariable String warehouseID,
                                              @PathVariable String productID) {
        ProductData product = productRepository
                .findByWarehouseData_WarehouseIDAndProductID(warehouseID, productID).orElse(null);
        if (product == null) return "Product not found!";
        productRepository.delete(product);
        return "Product deleted!";
    }

    // ─── PURCHASES ────────────────────────────────────────────────────────────

    /** Insert 30 purchase records */
    @PostMapping("/purchase/add30")
    public @ResponseBody String createPurchases30() {
        return createPurchaseRecords(30);
    }

    /** Insert 300 purchase records */
    @PostMapping("/purchase/add300")
    public @ResponseBody String createPurchases300() {
        return createPurchaseRecords(300);
    }

    private String createPurchaseRecords(int count) {
        WarehouseData w1 = warehouseRepository.findByWarehouseID("001").orElse(null);
        WarehouseData w2 = warehouseRepository.findByWarehouseID("002").orElse(null);
        ProductData p1 = productRepository.findByWarehouseData_WarehouseIDAndProductID("001", "00-443175").orElse(null);
        ProductData p2 = productRepository.findByWarehouseData_WarehouseIDAndProductID("001", "00-871895").orElse(null);
        if (w1 == null || w2 == null || p1 == null || p2 == null)
            return "Make sure warehouses 001, 002 and products 00-443175, 00-871895 exist first!";

        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        for (int i = 0; i < count; i++) {
            PurchasesData purchase = new PurchasesData(
                    dateStr,
                    (int)(Math.random() * 10) + 1,
                    (i % 2 == 0 ? p1 : p2),
                    (i % 2 == 0 ? w1 : w2)
            );
            purchasesRepository.save(purchase);
        }
        return count + " purchases created!";
    }

    /** Get all purchases */
    @GetMapping(value = "/purchase/all", produces = "application/json")
    public @ResponseBody Iterable<PurchasesData> getAllPurchases() {
        Iterable<PurchasesData> list = purchasesRepository.findAll();
        for (PurchasesData p : list) {
            if (p.getWarehouse() != null) p.getWarehouse().setProductData(null);
        }
        return list;
    }

    /** Delete all purchases */
    @DeleteMapping("/purchase/deleteAll")
    public @ResponseBody String deleteAllPurchases() {
        purchasesRepository.deleteAll();
        return "All purchases deleted!";
    }

    // ─── LLM PROGNOSIS ────────────────────────────────────────────────────────

    /** Ask local Ollama (llama3) to forecast next month's sales */
    @GetMapping("/prognosis")
    public @ResponseBody String prognosis() {
        try {
            Iterable<PurchasesData> purchases = purchasesRepository.findAll();
            StringBuilder data = new StringBuilder();
            int count = 0;
            for (PurchasesData p : purchases) {
                if (count > 50) break;
                if (p.getWarehouse() != null && p.getProduct() != null) {
                    data.append("Warehouse ").append(p.getWarehouse().getWarehouseID())
                            .append(" Product ").append(p.getProduct().getProductID())
                            .append(" Amount ").append(p.getAmount()).append("\n");
                }
                count++;
            }

            String prompt = "Here is sales data:\n" + data +
                    "\nPredict sales for next month per warehouse and product. Give numbers.";
            String safePrompt = prompt.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", " ");
            String json = "{\"model\":\"llama3\",\"prompt\":\"" + safePrompt + "\",\"stream\":false}";

            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.getOutputStream().write(json.getBytes());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) result.append(line);

            return result.toString().split("\"response\":\"")[1].split("\",\"done\"")[0];
        } catch (Exception e) {
            e.printStackTrace();
            return "Ollama error: " + e.getMessage();
        }
    }
}