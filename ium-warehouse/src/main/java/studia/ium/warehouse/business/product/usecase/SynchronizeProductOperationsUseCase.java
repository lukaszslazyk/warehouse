package studia.ium.warehouse.business.product.usecase;

import studia.ium.warehouse.business.product.control.ProductSynchronizationManager;
import studia.ium.warehouse.business.product.entity.ProductOperation;
import studia.ium.warehouse.business.product.entity.ProductOperationSyncError;

import java.util.List;

public class SynchronizeProductOperationsUseCase {
    private ProductSynchronizationManager productSynchronizationManager;
    private List<ProductOperation> productOperations;

    public SynchronizeProductOperationsUseCase withProductSynchronizationManager(ProductSynchronizationManager productSynchronizationManager) {
        this.productSynchronizationManager = productSynchronizationManager;

        return this;
    }

    public SynchronizeProductOperationsUseCase forProductOperations(List<ProductOperation> productOperations) {
        this.productOperations = productOperations;

        return this;
    }

    public List<ProductOperationSyncError> run() {
        return productSynchronizationManager
            .synchronizeOperations(productOperations);
    }
}
