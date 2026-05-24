package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class WarehouseData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String warehouseID;
    private String warehouseName;
    private String warehouseAddress;
    private int warehousePostalCode;
    private String warehouseCity;
    private String warehouseCountry;
    private String timestamp;

    @OneToMany(mappedBy = "warehouseData", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("warehouseData")
    private List<ProductData> productData;

    public WarehouseData() {
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    public Long getId() { return id; }
    public String getWarehouseID() { return warehouseID; }
    public void setWarehouseID(String warehouseID) { this.warehouseID = warehouseID; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseAddress() { return warehouseAddress; }
    public void setWarehouseAddress(String warehouseAddress) { this.warehouseAddress = warehouseAddress; }
    public int getWarehousePostalCode() { return warehousePostalCode; }
    public void setWarehousePostalCode(int warehousePostalCode) { this.warehousePostalCode = warehousePostalCode; }
    public String getWarehouseCity() { return warehouseCity; }
    public void setWarehouseCity(String warehouseCity) { this.warehouseCity = warehouseCity; }
    public String getWarehouseCountry() { return warehouseCountry; }
    public void setWarehouseCountry(String warehouseCountry) { this.warehouseCountry = warehouseCountry; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public List<ProductData> getProductData() { return productData; }
    public void setProductData(List<ProductData> productData) { this.productData = productData; }

    @Override
    public String toString() {
        return String.format("Warehouse Info: ID = %s, timestamp = %s", warehouseID, timestamp);
    }
}
