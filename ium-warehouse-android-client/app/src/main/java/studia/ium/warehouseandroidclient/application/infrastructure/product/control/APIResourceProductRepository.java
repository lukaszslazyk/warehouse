package studia.ium.warehouseandroidclient.application.infrastructure.product.control;

import java.util.List;

import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperation;
import studia.ium.warehouseandroidclient.application.infrastructure.product.entity.ProductOperationSyncError;
import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

public class APIResourceProductRepository implements ProductRepository {
    private ProductAPIClient productAPIClient;

    public APIResourceProductRepository(ProductAPIClient productAPIClient) {
        this.productAPIClient = productAPIClient;
    }

    @Override
    public List<Product> findAll() {
        return productAPIClient.findAll();
    }

    @Override
    public Product findById(String productId) {
        return productAPIClient.findById(productId);
    }

    @Override
    public void addProduct(Product product) {
        productAPIClient.addProduct(product);
    }

    @Override
    public void editProduct(String productId, List<ProductPatch> productPatches) {
        productAPIClient.editProduct(productId, productPatches);
    }

    @Override
    public void deleteById(String productId) {
        productAPIClient.deleteProduct(productId);
    }

    @Override
    public void increaseQuantity(String productId, Integer amount, Integer warehouseId) {
        productAPIClient.increaseQuantityOfProduct(productId, amount, warehouseId);
    }

    @Override
    public void decreaseQuantity(String productId, Integer amount, Integer warehouseId) {
        productAPIClient.decreaseQuantityOfProduct(productId, amount, warehouseId);
    }

    @Override
    public boolean existsByManufacturerNameAndModelName(String manufacturerName, String modelName) {
        throw new UnsupportedOperationException();
    }

    public List<ProductOperationSyncError> synchronize(List<ProductOperation> productOperations) {
        return productAPIClient.synchronize(productOperations);
    }
}
