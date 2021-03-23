package studia.ium.warehouseandroidclient.business.product.boundary;

import java.util.List;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.entity.Product;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;
import studia.ium.warehouseandroidclient.business.product.usecase.AddProductUseCase;
import studia.ium.warehouseandroidclient.business.product.usecase.DecreaseQuantityOfProductUseCase;
import studia.ium.warehouseandroidclient.business.product.usecase.DeleteProductUseCase;
import studia.ium.warehouseandroidclient.business.product.usecase.EditProductUseCase;
import studia.ium.warehouseandroidclient.business.product.usecase.FindAllProductsUseCase;
import studia.ium.warehouseandroidclient.business.product.usecase.FindProductByIdUseCase;
import studia.ium.warehouseandroidclient.business.product.usecase.IncreaseQuantityOfProductUseCase;

public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return new FindAllProductsUseCase()
            .withProductRepository(productRepository)
            .run();
    }

    public Product findById(String productId) {
        return new FindProductByIdUseCase()
            .withProductRepository(productRepository)
            .forProductId(productId)
            .run();
    }

    public void add(Product product) {
        new AddProductUseCase()
            .withProductRepository(productRepository)
            .forProduct(product)
            .run();
    }

    public void edit(String productId, List<ProductPatch> productPatches) {
        new EditProductUseCase()
            .withProductRepository(productRepository)
            .forProductId(productId)
            .forProductPatches(productPatches)
            .run();
    }

    public void delete(String productId) {
        new DeleteProductUseCase()
            .withProductRepository(productRepository)
            .forProductId(productId)
            .run();
    }

    public void increaseQuantityOfProduct(String productId, Integer amount, Integer warehouseId) {
        new IncreaseQuantityOfProductUseCase()
            .withProductRepository(productRepository)
            .forProductId(productId)
            .forWarehouseId(warehouseId)
            .forAmount(amount)
            .run();
    }

    public void decreaseQuantityOfProduct(String productId, Integer amount, Integer warehouseId) {
        new DecreaseQuantityOfProductUseCase()
            .withProductRepository(productRepository)
            .forProductId(productId)
            .forWarehouseId(warehouseId)
            .forAmount(amount)
            .run();
    }
}
