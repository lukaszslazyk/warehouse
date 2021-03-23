package studia.ium.warehouseandroidclient.business.product.control;

import java.util.List;

import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

public interface ProductRepository {
    List<Product> findAll();

    Product findById(String productId);

    void addProduct(Product product);

    void editProduct(String productId, List<ProductPatch> productPatches);

    void deleteById(String productId);

    void increaseQuantity(String productId, Integer amount, Integer warehouseId);

    void decreaseQuantity(String productId, Integer amount, Integer warehouseId);

    boolean existsByManufacturerNameAndModelName(String manufacturerName, String modelName);
}
