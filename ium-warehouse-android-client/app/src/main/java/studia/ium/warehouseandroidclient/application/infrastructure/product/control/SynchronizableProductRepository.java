package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import java.util.List;
import java.util.function.Supplier;

import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperation;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationSyncError;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ServerUnreachableException;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.SynchronizationErrorsOccurredException;
import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.control.ProductSynchronizationManager;
import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;
import studia.ium.warehouseandroidclient.business.product.entity.ProductRepositoryCallFailedException;

public class SynchronizableProductRepository implements ProductRepository, ProductSynchronizationManager {
    private MemoryProductRepository memoryProductRepository;
    private APIResourceProductRepository apiResourceProductRepository;
    private ProductOperationsRepository productOperationsRepository;
    private ProductSyncErrorsManager productSyncErrorsManager;
    private boolean online = true;

    public SynchronizableProductRepository(MemoryProductRepository memoryProductRepository,
                                           APIResourceProductRepository apiResourceProductRepository,
                                           ProductOperationsRepository productOperationsRepository,
                                           ProductSyncErrorsManager productSyncErrorsManager) {
        this.memoryProductRepository = memoryProductRepository;
        this.apiResourceProductRepository = apiResourceProductRepository;
        this.productOperationsRepository = productOperationsRepository;
        this.productSyncErrorsManager = productSyncErrorsManager;
    }

    @Override
    public List<Product> findAll() {
        return handleExceptionsFrom(this::handleFindAllCall);
    }

    @Override
    public Product findById(String productId) {
        return handleExceptionsFrom(() -> handleFindByIdCall(productId));
    }

    @Override
    public void addProduct(Product product) {
        handleExceptionsFrom(() -> handleAddProductCall(product));
    }

    @Override
    public void editProduct(String productId, List<ProductPatch> productPatches) {
        handleExceptionsFrom(() -> handleEditProductCall(productId, productPatches));
    }

    @Override
    public void deleteById(String productId) {
        handleExceptionsFrom(() -> handleDeleteByIdCall(productId));
    }

    @Override
    public void increaseQuantity(String productId, Integer amount, Integer warehouseId) {
        handleExceptionsFrom(() -> handleIncreaseQuantityCall(productId, amount, warehouseId));
    }

    @Override
    public void decreaseQuantity(String productId, Integer amount, Integer warehouseId) {
        handleExceptionsFrom(() -> handleDecreaseQuantityCall(productId, amount, warehouseId));
    }

    @Override
    public boolean existsByManufacturerNameAndModelName(String manufacturerName, String modelName) {
        return memoryProductRepository.existsByManufacturerNameAndModelName(manufacturerName, modelName);
    }

    @Override
    public void toggleOfflineMode() {
        if (online)
            online = false;
        else {
            online = true;
            performOperationsSynchronizationOnGoingBackOnline();
        }
    }

    @Override
    public List<String> getAllSyncErrorMessages() {
        return productSyncErrorsManager.getAllSyncErrorMessages();
    }

    @Override
    public void clearLocalData() {
        memoryProductRepository.deleteAll();
        productOperationsRepository.deleteAll();
    }

    private List<Product> handleFindAllCall() {
        if (online) {
            synchronizeOperations();
            List<Product> products = apiResourceProductRepository.findAll();
            memoryProductRepository.resetDataTo(products);
            return products;
        } else
            return memoryProductRepository.findAll();
    }

    private Product handleFindByIdCall(String productId) {
        if (online) {
            Product product = apiResourceProductRepository.findById(productId);
            memoryProductRepository.replaceProduct(product);
            return product;
        } else
            return memoryProductRepository.findById(productId);
    }

    private void handleAddProductCall(Product product) {
        if (online) {
            apiResourceProductRepository.addProduct(product);
            memoryProductRepository.addProduct(product);
        } else {
            memoryProductRepository.addProduct(product);
            productOperationsRepository.add(ProductOperation.createAddProductOperation(product));
        }
    }

    private void handleEditProductCall(String productId, List<ProductPatch> productPatches) {
        if (online) {
            apiResourceProductRepository.editProduct(productId, productPatches);
            memoryProductRepository.editProduct(productId, productPatches);
        } else {
            memoryProductRepository.editProduct(productId, productPatches);
            productOperationsRepository.add(
                ProductOperation.createEditProductOperation(productId, productPatches));
        }
    }

    private void handleDeleteByIdCall(String productId) {
        if (online) {
            apiResourceProductRepository.deleteById(productId);
            memoryProductRepository.deleteById(productId);
        } else {
            productOperationsRepository.add(ProductOperation.createDeleteProductOperation(productId));
            memoryProductRepository.deleteById(productId);
        }
    }

    private void handleIncreaseQuantityCall(String productId, Integer amount, Integer warehouseId) {
        if (online) {
            apiResourceProductRepository.increaseQuantity(productId, amount, warehouseId);
            memoryProductRepository.increaseQuantity(productId, amount, warehouseId);
        } else {
            memoryProductRepository.increaseQuantity(productId, amount, warehouseId);
            productOperationsRepository.add(
                ProductOperation.createIncreaseQuantityProductOperation(productId, amount, warehouseId));
        }
    }

    private void handleDecreaseQuantityCall(String productId, Integer amount, Integer warehouseId) {
        if (online) {
            apiResourceProductRepository.decreaseQuantity(productId, amount, warehouseId);
            memoryProductRepository.decreaseQuantity(productId, amount, warehouseId);
        } else {
            memoryProductRepository.decreaseQuantity(productId, amount, warehouseId);
            productOperationsRepository.add(
                ProductOperation.createDecreaseQuantityProductOperation(productId, amount, warehouseId));
        }
    }

    private void performOperationsSynchronizationOnGoingBackOnline() {
        synchronizeOperations();
        synchronizeLocalData();
    }

    private void synchronizeOperations() {
        if (anyPendingOperations())
            performSynchronization(getOperations());
    }

    private boolean anyPendingOperations() {
        return !productOperationsRepository.isEmpty();
    }

    private List<ProductOperation> getOperations() {
        return productOperationsRepository.findAll();
    }

    private void performSynchronization(List<ProductOperation> productOperations) {
        List<ProductOperationSyncError> syncErrors = apiResourceProductRepository.synchronize(productOperations);
        productSyncErrorsManager.process(syncErrors);
        productOperationsRepository.deleteAll();
        if (!syncErrors.isEmpty())
            throw new SynchronizationErrorsOccurredException("Errors occurred during synchronization with server");
    }

    private void synchronizeLocalData() {
        memoryProductRepository.resetDataTo(apiResourceProductRepository.findAll());
    }

    private void handleExceptionsFrom(Runnable runnable) {
        handleExceptionsFrom(() -> {
            runnable.run();
            return null;
        });
    }

    private <T> T handleExceptionsFrom(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (ServerUnreachableException e) {
            throw new ProductRepositoryCallFailedException("Server unreachable");
        } catch (SynchronizationErrorsOccurredException e) {
            throw new ProductRepositoryCallFailedException(e.getMessage());
        } catch (Exception e) {
            throw new ProductRepositoryCallFailedException("Unexpected error occurred");
        }
    }
}
