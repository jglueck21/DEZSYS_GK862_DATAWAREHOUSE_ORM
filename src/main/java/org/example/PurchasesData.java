package org.example;

import jakarta.persistence.*;

@Entity
public class PurchasesData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String date;
    private int amount;

    @ManyToOne
    private ProductData product;

    @ManyToOne
    private WarehouseData warehouse;

    public PurchasesData() {}

    public PurchasesData(String date, int amount, ProductData product, WarehouseData warehouse) {
        this.date = date;
        this.amount = amount;
        this.product = product;
        this.warehouse = warehouse;
    }

    public Long getId() { return id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public ProductData getProduct() { return product; }
    public void setProduct(ProductData product) { this.product = product; }

    public WarehouseData getWarehouse() { return warehouse; }
    public void setWarehouse(WarehouseData warehouse) { this.warehouse = warehouse; }
}
