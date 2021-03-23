package studia.ium.warehouse.business.product.usecase;

import studia.ium.warehouse.business.product.control.ProductRepository;
import studia.ium.warehouse.business.product.entity.Product;

import java.util.List;

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
