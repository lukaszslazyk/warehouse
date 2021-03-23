package studia.ium.warehouse.business.product.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Entity
public class Product {
    @Id
    private UUID id = UUID.randomUUID();
    private String manufacturerName;
    private String modelName;
    private Double price;
    private Long expirationDate;
    @ElementCollection
    @JoinTable(name = "QUANTITIES_IN_WAREHOUSES", joinColumns = @JoinColumn(name = "ID"))
    @MapKeyColumn(name = "WAREHOUSE_ID")
    @Column(name = "QUANTITY")
    private Map<Integer, Integer> quantitiesInWarehouses = new HashMap<>();
    private Long manufacturerNameModifiedTimestamp = 0L;
    private Long modelNameModifiedTimestamp = 0L;
    private Long priceModifiedTimestamp = 0L;
    private Long expirationDateModifiedTimestamp = 0L;

    public Product() {
    }

    public Product(String manufacturerName, String modelName, Double price) {
        this.manufacturerName = manufacturerName;
        this.modelName = modelName;
        this.price = price;
    }

    public Integer getQuantityInDefaultWarehouse() {
        return getQuantityInWarehouse(0).get();
    }

    public Optional<Integer> getQuantityInWarehouse(Integer warehouseId) {
        return Optional.ofNullable(quantitiesInWarehouses.get(warehouseId));
    }

    public void setQuantityInWarehouse(Integer warehouseId, Integer amount) {
        quantitiesInWarehouses.put(warehouseId, amount);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getManufacturerNameModifiedTimestamp() {
        return manufacturerNameModifiedTimestamp;
    }

    public void setManufacturerNameModifiedTimestamp(Long manufacturerNameModifiedTimestamp) {
        this.manufacturerNameModifiedTimestamp = manufacturerNameModifiedTimestamp;
    }

    public Long getModelNameModifiedTimestamp() {
        return modelNameModifiedTimestamp;
    }

    public void setModelNameModifiedTimestamp(Long modelNameModifiedTimestamp) {
        this.modelNameModifiedTimestamp = modelNameModifiedTimestamp;
    }

    public Long getPriceModifiedTimestamp() {
        return priceModifiedTimestamp;
    }

    public void setPriceModifiedTimestamp(Long priceModifiedTimestamp) {
        this.priceModifiedTimestamp = priceModifiedTimestamp;
    }

    public Long getExpirationDateModifiedTimestamp() {
        return expirationDateModifiedTimestamp;
    }

    public void setExpirationDateModifiedTimestamp(Long expirationDateModifiedTimestamp) {
        this.expirationDateModifiedTimestamp = expirationDateModifiedTimestamp;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Map<Integer, Integer> getQuantitiesInWarehouses() {
        return quantitiesInWarehouses;
    }

    public void setQuantitiesInWarehouses(Map<Integer, Integer> quantitiesInWarehouses) {
        this.quantitiesInWarehouses = quantitiesInWarehouses;
    }
}
