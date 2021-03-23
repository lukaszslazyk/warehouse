package studia.ium.warehouse.business.product.entity;

public class ProductOperationSyncError {
    private ProductOperation productOperation;
    private String errorMessage;

    public ProductOperationSyncError(ProductOperation productOperation, String errorMessage) {
        this.productOperation = productOperation;
        this.errorMessage = errorMessage;
    }

    public ProductOperation getProductOperation() {
        return productOperation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
