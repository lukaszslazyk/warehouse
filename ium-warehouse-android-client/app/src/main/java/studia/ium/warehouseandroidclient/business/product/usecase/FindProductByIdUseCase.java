package studia.ium.warehouseandroidclient.business.product.usecase;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.entity.Product;

public class FindProductByIdUseCase {
    private ProductRepository productRepository;
    private String productId;

    public FindProductByIdUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public FindProductByIdUseCase forProductId(String productId) {
        this.productId = productId;

        return this;
    }

    public Product run() {
        return productRepository
            .findById(productId);
    }
}
