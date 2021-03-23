package studia.ium.warehouseandroidclient.business.product.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Entity
public class Product implements Serializable {
    @PrimaryKey
    @NonNull
    private String id = UUID.randomUUID().toString();
    private String manufacturerName;
    private String modelName;
    private Double price;
    private Long expirationDate;
    private Map<Integer, Integer> quantitiesInWarehouses = new HashMap<>();
    private boolean deleted;

    public Product getCopy() {
        Product copy = new Product();
        copy.setId(id);
        copy.setManufacturerName(manufacturerName);
        copy.setModelName(modelName);
        copy.setPrice(price);
        copy.setExpirationDate(expirationDate);
        copy.setQuantitiesInWarehouses(quantitiesInWarehouses);

        return copy;
    }

    public Optional<Integer> getQuantityInWarehouse(Integer warehouseId) {
        return Optional.ofNullable(quantitiesInWarehouses.get(warehouseId));
    }

    public void setQuantityInWarehouse(Integer warehouseId, Integer amount) {
        quantitiesInWarehouses.put(warehouseId, amount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Map<Integer, Integer> getQuantitiesInWarehouses() {
        return quantitiesInWarehouses;
    }

    public void setQuantitiesInWarehouses(Map<Integer, Integer> quantitiesInWarehouses) {
        this.quantitiesInWarehouses = quantitiesInWarehouses;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
