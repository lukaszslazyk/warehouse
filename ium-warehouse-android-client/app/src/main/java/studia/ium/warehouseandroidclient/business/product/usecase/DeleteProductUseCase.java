package studia.ium.warehouseandroidclient.business.product.usecase;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;

public class DeleteProductUseCase {
    private ProductRepository productRepository;
    private String productId;

    public DeleteProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public DeleteProductUseCase forProductId(String productId) {
        this.productId = productId;

        return this;
    }

    public void run() {
        productRepository
            .deleteById(productId);
    }
}
