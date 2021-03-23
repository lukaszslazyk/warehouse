package studia.ium.warehouseandroidclient.business.product.usecase;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;

public class DecreaseQuantityOfProductUseCase {
    private ProductRepository productRepository;
    private String productId;
    private Integer warehouseId;
    private Integer amount;

    public DecreaseQuantityOfProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public DecreaseQuantityOfProductUseCase forProductId(String productId) {
        this.productId = productId;

        return this;
    }

    public DecreaseQuantityOfProductUseCase forWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;

        return this;
    }

    public DecreaseQuantityOfProductUseCase forAmount(Integer amount) {
        this.amount = amount;

        return this;
    }

    public void run() {
        productRepository
            .decreaseQuantity(productId, amount, warehouseId);
    }
}
