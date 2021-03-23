package studia.ium.warehouseandroidclient.application.infrastructure.product.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.UUID;

import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

@Entity
public class ProductOperation {
    @PrimaryKey
    @NonNull
    private String guid = UUID.randomUUID().toString();
    private Long timestamp;
    private ProductOperationType type;
    private String productId;
    private Product product;
    private List<ProductPatch> productPatches;
    private Integer amount;
    private Integer warehouseId;

    public ProductOperation() {
    }

    @Ignore
    public ProductOperation(ProductOperationType type) {
        this.timestamp = getCurrentTime();
        this.type = type;
    }

    public static ProductOperation createAddProductOperation(Product product) {
        ProductOperation operation = new ProductOperation(ProductOperationType.ADD);
        operation.setProduct(product.getCopy());

        return operation;
    }

    public static ProductOperation createEditProductOperation(String productId, List<ProductPatch> productPatches) {
        ProductOperation operation = new ProductOperation(ProductOperationType.EDIT);
        operation.setProductId(productId);
        operation.setProductPatches(productPatches);

        return operation;
    }

    public static ProductOperation createDeleteProductOperation(String productId) {
        ProductOperation operation = new ProductOperation(ProductOperationType.DELETE);
        operation.setProductId(productId);

        return operation;
    }

    public static ProductOperation createIncreaseQuantityProductOperation(String productId, Integer amount, Integer warehouseId) {
        ProductOperation operation = new ProductOperation(ProductOperationType.INCREASE_QUANTITY);
        operation.setProductId(productId);
        operation.setAmount(amount);
        operation.setWarehouseId(warehouseId);

        return operation;
    }

    public static ProductOperation createDecreaseQuantityProductOperation(String productId, Integer amount, Integer warehouseId) {
        ProductOperation operation = new ProductOperation(ProductOperationType.DECREASE_QUANTITY);
        operation.setProductId(productId);
        operation.setAmount(amount);
        operation.setWarehouseId(warehouseId);

        return operation;
    }

    @NonNull
    public String getGuid() {
        return guid;
    }

    public void setGuid(@NonNull String guid) {
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
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

    private Long getCurrentTime() {
        return System.currentTimeMillis();
    }
}

