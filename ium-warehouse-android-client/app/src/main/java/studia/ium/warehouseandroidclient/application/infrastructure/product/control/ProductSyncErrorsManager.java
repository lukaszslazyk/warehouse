package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import java.util.List;

import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationSyncError;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationType;
import studia.ium.warehouseandroidclient.business.product.entity.Product;

import static studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationType.ADD;
import static studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationType.DECREASE_QUANTITY;
import static studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationType.DELETE;
import static studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationType.EDIT;
import static studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationType.INCREASE_QUANTITY;

public class ProductSyncErrorsManager {
    private ProductSyncErrorMessagesRepository productSyncErrorMessagesRepository;
    private MemoryProductRepository memoryProductRepository;

    public ProductSyncErrorsManager(ProductSyncErrorMessagesRepository productSyncErrorMessagesRepository,
                                    MemoryProductRepository memoryProductRepository) {
        this.productSyncErrorMessagesRepository = productSyncErrorMessagesRepository;
        this.memoryProductRepository = memoryProductRepository;
    }

    public void process(List<ProductOperationSyncError> errors) {
        errors.forEach(this::processSingle);
    }

    public List<String> getAllSyncErrorMessages() {
        return productSyncErrorMessagesRepository.findAll();
    }

    private void processSingle(ProductOperationSyncError error) {
        if (failedOperationOfType(error, ADD))
            processAddOperationError(error);
        else if (failedOperationOfType(error, EDIT))
            processEditOperationError(error);
        else if (failedOperationOfType(error, DELETE))
            processDeleteOperationError(error);
        else if (failedOperationOfType(error, INCREASE_QUANTITY))
            processIncreaseQuantityError(error);
        else if (failedOperationOfType(error, DECREASE_QUANTITY))
            processDecreaseQuantityError(error);
    }

    private boolean failedOperationOfType(ProductOperationSyncError error, ProductOperationType type) {
        return error.getProductOperation().getType().equals(type);
    }

    private void processAddOperationError(ProductOperationSyncError error) {
        addErrorMessageWithIdReplacedToName(error);
    }

    private void processEditOperationError(ProductOperationSyncError error) {
        addErrorMessageWithIdReplacedToName(error);
    }

    private void processDeleteOperationError(ProductOperationSyncError error) {
        addErrorMessageWithIdReplacedToName(error);
    }

    private void processIncreaseQuantityError(ProductOperationSyncError error) {
        addErrorMessageWithIdReplacedToName(error);
    }

    private void processDecreaseQuantityError(ProductOperationSyncError error) {
        addErrorMessageWithIdReplacedToName(error);
    }

    private void addErrorMessageWithIdReplacedToName(ProductOperationSyncError error) {
        Product product = memoryProductRepository.findById(getProductIdFrom(error));
        String errorMsg = error.getErrorMessage().replace(
            product.getId(),
            String.format("\"%s %s\"", product.getManufacturerName(), product.getModelName()));
        productSyncErrorMessagesRepository.add(errorMsg);
    }

    private String getProductIdFrom(ProductOperationSyncError error) {
        String productId = error.getProductOperation().getProductId();
        if (productId == null)
            productId = error.getProductOperation().getProduct().getId();

        return productId;
    }
}
