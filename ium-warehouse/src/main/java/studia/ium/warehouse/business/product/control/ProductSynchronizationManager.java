package studia.ium.warehouse.business.product.control;

import studia.ium.warehouse.business.product.boundary.ProductService;
import studia.ium.warehouse.business.product.entity.*;

import java.util.*;

import static studia.ium.warehouse.business.product.entity.ProductOperationType.*;

public class ProductSynchronizationManager {
    private ProductService productService;
    private ProductQuantityOperationsSynchronizer productQuantityOperationsSynchronizer;
    private Set<UUID> operationGuids = new HashSet<>();
    private List<ProductOperationSyncError> errors = new LinkedList<>();

    public ProductSynchronizationManager(ProductService productService,
                                         ProductQuantityOperationsSynchronizer productQuantityOperationsSynchronizer) {
        this.productService = productService;
        this.productQuantityOperationsSynchronizer = productQuantityOperationsSynchronizer;
    }

    public List<ProductOperationSyncError> synchronizeOperations(List<ProductOperation> productOperations) {
        clearErrors();
        removeAlreadyExisting(productOperations);
        saveGuids(productOperations);
        processOperations(productOperations);
        synchronizeProductQuantityOperations();

        return errors;
    }

    private void clearErrors() {
        errors.clear();
    }

    private void removeAlreadyExisting(List<ProductOperation> productOperations) {
        productOperations.removeIf(operation -> operationGuids.contains(operation.getGuid()));
    }

    private void saveGuids(List<ProductOperation> productOperations) {
        productOperations.stream()
            .map(ProductOperation::getGuid)
            .forEach(operationGuids::add);
    }

    private void processOperations(List<ProductOperation> productOperations) {
        productOperations.forEach(this::processSingleOperation);
    }

    private void processSingleOperation(ProductOperation productOperation) {
        if (isOfType(productOperation, ADD))
            processAddOperation(productOperation);
        else if (isOfType(productOperation, EDIT))
            processEditOperation(productOperation);
        else if (isOfType(productOperation, DELETE))
            processDeleteOperation(productOperation);
        else if (isOfType(productOperation, INCREASE_QUANTITY))
            processIncreaseQuantityOperation(productOperation);
        else if (isOfType(productOperation, DECREASE_QUANTITY))
            processDecreaseQuantityOperation(productOperation);
    }

    private boolean isOfType(ProductOperation productOperation, ProductOperationType type) {
        return productOperation.getType().equals(type);
    }

    private void processAddOperation(ProductOperation productOperation) {
        try {
            productService.add(productOperation.getProduct());
        } catch (CouldNotAddProductException e) {
            errors.add(getAddErrorMessage(e, productOperation));
        }
    }

    private void processEditOperation(ProductOperation productOperation) {
        try {
            productService.edit(productOperation.getProductId(),
                productOperation.getProductPatches(), productOperation.getTimestamp());
        } catch (CouldNotEditProductException e) {
            errors.add(getEditErrorMessage(e, productOperation));
        } catch (CouldNotEntirelyEditProductException e) {
            errors.add(getPartialEditErrorMessage(e, productOperation));
        }
    }

    private void processDeleteOperation(ProductOperation productOperation) {
        try {
            productService.delete(productOperation.getProductId());
        } catch (CouldNotDeleteProductException e) {
            errors.add(getDeleteErrorMessage(e, productOperation));
        }
    }

    private void processIncreaseQuantityOperation(ProductOperation productOperation) {
        productQuantityOperationsSynchronizer.addOperation(productOperation);
    }

    private void processDecreaseQuantityOperation(ProductOperation productOperation) {
        productQuantityOperationsSynchronizer.addOperation(productOperation);
    }

    private void synchronizeProductQuantityOperations() {
        List<ProductOperationSyncError> productQuantityOperationsSyncErrors =
            productQuantityOperationsSynchronizer.synchronizeOperations();
        errors.addAll(productQuantityOperationsSyncErrors);
    }

    private ProductOperationSyncError getAddErrorMessage(CouldNotAddProductException e,
                                                         ProductOperation addOperation) {
        String errorMessage = String.format("Could not synchronize add operation of product %s"
                + " due to the following errors:\n%s",
            addOperation.getProduct().getId().toString(), e.getErrorMessagesInSingleString());

        return new ProductOperationSyncError(addOperation, errorMessage);
    }

    private ProductOperationSyncError getEditErrorMessage(CouldNotEditProductException e,
                                                          ProductOperation editOperation) {
        String errorMessage = String.format("Could not synchronize edit operation of product %s"
                + " due to the following errors:\n%s",
            editOperation.getProductId(), e.getErrorMessagesInSingleString());

        return new ProductOperationSyncError(editOperation, errorMessage);
    }

    private ProductOperationSyncError getPartialEditErrorMessage(CouldNotEntirelyEditProductException e,
                                                                 ProductOperation editOperation) {
        String errorMessage = String.format("Could not synchronize all fields during edit operation of product %s"
                + " due to the following errors:\n%s",
            editOperation.getProductId(), e.getErrorMessagesInSingleString());

        return new ProductOperationSyncError(editOperation, errorMessage);
    }

    private ProductOperationSyncError getDeleteErrorMessage(CouldNotDeleteProductException e,
                                                            ProductOperation deleteOperation) {
        String errorMessage = String.format("Could not synchronize delete operation of product %s"
                + " due to the following errors:\n%s",
            deleteOperation.getProductId(), e.getErrorMessagesInSingleString());

        return new ProductOperationSyncError(deleteOperation, errorMessage);
    }
}
