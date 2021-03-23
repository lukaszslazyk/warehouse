package studia.ium.warehouseandroidclient.business.product.usecase;

import java.util.List;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.entity.Product;

public class FindAllProductsUseCase {
    private ProductRepository productRepository;

    public FindAllProductsUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public List<Product> run() {
        return productRepository
            .findAll();
    }
}
