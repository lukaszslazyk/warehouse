package studia.ium.warehouseandroidclient.business.product.usecase;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;

public class IncreaseQuantityOfProductUseCase {
    private ProductRepository productRepository;
    private String productId;
    private Integer warehouseId;
    private Integer amount;

    public IncreaseQuantityOfProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public IncreaseQuantityOfProductUseCase forProductId(String productId) {
        this.productId = productId;

        return this;
    }

    public IncreaseQuantityOfProductUseCase forWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;

        return this;
    }

    public IncreaseQuantityOfProductUseCase forAmount(Integer amount) {
        this.amount = amount;

        return this;
    }

    public void run() {
        productRepository
            .increaseQuantity(productId, amount, warehouseId);
    }
}