package studia.ium.warehouse.business.product.boundary;

import studia.ium.warehouse.business.product.control.ProductSynchronizationManager;
import studia.ium.warehouse.business.product.entity.ProductOperation;
import studia.ium.warehouse.business.product.entity.ProductOperationSyncError;
import studia.ium.warehouse.business.product.usecase.SynchronizeProductOperationsUseCase;

import java.util.List;

public class ProductSynchronizationService {
    private ProductSynchronizationManager productSynchronizationManager;

    public ProductSynchronizationService(ProductSynchronizationManager productSynchronizationManager) {
        this.productSynchronizationManager = productSynchronizationManager;
    }

    public List<ProductOperationSyncError> synchronizeProductOperations(List<ProductOperation> productOperations) {
        return new SynchronizeProductOperationsUseCase()
            .withProductSynchronizationManager(productSynchronizationManager)
            .forProductOperations(productOperations)
            .run();
    }
}
