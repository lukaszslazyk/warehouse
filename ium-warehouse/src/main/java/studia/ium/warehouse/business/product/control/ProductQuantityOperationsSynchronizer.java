package studia.ium.warehouse.business.product.control;

import studia.ium.warehouse.business.product.boundary.ProductService;
import studia.ium.warehouse.business.product.entity.CouldNotDecreaseQuantityOfProductException;
import studia.ium.warehouse.business.product.entity.CouldNotIncreaseQuantityOfProductException;
import studia.ium.warehouse.business.product.entity.ProductOperation;
import studia.ium.warehouse.business.product.entity.ProductOperationSyncError;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.nonNull;
import static studia.ium.warehouse.business.product.entity.ProductOperationType.DECREASE_QUANTITY;
import static studia.ium.warehouse.business.product.entity.ProductOperationType.INCREASE_QUANTITY;

public class ProductQuantityOperationsSynchronizer {
    private ProductService productService;
    private List<ProductOperation> increaseOperations = new LinkedList<>();
    private List<ProductOperation> decreaseOperations = new LinkedList<>();
    private List<ProductOperationSyncError> errors = new LinkedList<>();

    public ProductQuantityOperationsSynchronizer(ProductService productService) {
        this.productService = productService;
    }

    public void addOperation(ProductOperation productOperation) {
        preprocessOperation(productOperation);
        putOperationToCorrectQueue(productOperation);
    }

    public List<ProductOperationSyncError> synchronizeOperations() {
        clearErrors();
        sortDecreaseOperations();
        executeIncreaseOperations();
        executeDecreaseOperations();
        clearOperations();

        return errors;
    }

    private void preprocessOperation(ProductOperation productOperation) {
        if (!warehouseIdIsSet(productOperation))
            setWarehouseIdToDefaultValue(productOperation);
    }

    private boolean warehouseIdIsSet(ProductOperation productOperation) {
        return nonNull(productOperation.getWarehouseId());
    }

    private void setWarehouseIdToDefaultValue(ProductOperation productOperation) {
        productOperation.setWarehouseId(0);
    }

    private void putOperationToCorrectQueue(ProductOperation productOperation) {
        if (productOperation.getType().equals(INCREASE_QUANTITY))
            increaseOperations.add(productOperation);
        else if (productOperation.getType().equals(DECREASE_QUANTITY))
            decreaseOperations.add(productOperation);
    }

    private void clearErrors() {
        errors.clear();
    }

    private void sortDecreaseOperations() {
        decreaseOperations.sort(Comparator.comparingInt(ProductOperation::getAmount));
    }

    private void executeIncreaseOperations() {
        increaseOperations.forEach(this::executeSingleIncreaseOperation);
    }

    private void executeSingleIncreaseOperation(ProductOperation increaseOperation) {
        try {
            productService.increaseQuantityOfProduct(
                increaseOperation.getProductId(), increaseOperation.getAmount(), increaseOperation.getWarehouseId());
        } catch (CouldNotIncreaseQuantityOfProductException e) {
            errors.add(getIncreaseErrorMessage(e, increaseOperation));
        }
    }

    private void executeDecreaseOperations() {
        decreaseOperations.forEach(this::executeSingleDecreaseOperation);
    }

    private void executeSingleDecreaseOperation(ProductOperation decreaseOperation) {
        try {
            productService.decreaseQuantityOfProduct(
                decreaseOperation.getProductId(), decreaseOperation.getAmount(), decreaseOperation.getWarehouseId());
        } catch (CouldNotDecreaseQuantityOfProductException e) {
            errors.add(getDecreaseErrorMessage(e, decreaseOperation));
        }
    }

    private ProductOperationSyncError getIncreaseErrorMessage(CouldNotIncreaseQuantityOfProductException e,
                                                              ProductOperation increaseOperation) {
        String errorMessage = String.format("Could not synchronize increase operation of product %s by amount %d"
                + " due to the following errors:\n%s",
            increaseOperation.getProductId().toString(), increaseOperation.getAmount(), e.getErrorMessagesInSingleString());

        return new ProductOperationSyncError(increaseOperation, errorMessage);
    }

    private ProductOperationSyncError getDecreaseErrorMessage(CouldNotDecreaseQuantityOfProductException e,
                                                              ProductOperation decreaseOperation) {
        String errorMessage = String.format("Could not synchronize decrease operation of product %s by amount %d"
                + " due to the following errors:\n%s",
            decreaseOperation.getProductId().toString(), decreaseOperation.getAmount(), e.getErrorMessagesInSingleString());

        return new ProductOperationSyncError(decreaseOperation, errorMessage);
    }

    private void clearOperations() {
        increaseOperations.clear();
        decreaseOperations.clear();
    }
}
