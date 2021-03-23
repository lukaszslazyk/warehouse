package studia.ium.warehouse.business.product.entity;

import java.util.List;
import java.util.UUID;

public class ProductOperation {
    private UUID guid = UUID.randomUUID();
    private Long timestamp;
    private ProductOperationType type;
    private UUID productId;
    private Product product;
    private List<ProductPatch> productPatches;
    private Integer amount;
    private Integer warehouseId;

    public ProductOperation() {
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public ProductOperationType getType() {
        return type;
    }

    public void setType(ProductOperationType type) {
        this.type = type;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<ProductPatch> getProductPatches() {
        return productPatches;
    }

    public void setProductPatches(List<ProductPatch> productPatches) {
        this.productPatches = productPatches;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}
