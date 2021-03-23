package studia.ium.warehouse.business.product.usecase;

import studia.ium.warehouse.business.product.control.DecreaseQuantityOfProductUseCaseInputValidator;
import studia.ium.warehouse.business.product.control.ProductRepository;
import studia.ium.warehouse.business.product.entity.CouldNotDecreaseQuantityOfProductException;
import studia.ium.warehouse.business.product.entity.Product;

import java.util.Optional;
import java.util.UUID;

public class DecreaseQuantityOfProductUseCase {
    private DecreaseQuantityOfProductUseCaseInputValidator inputValidator;
    private ProductRepository productRepository;
    private UUID productId;
    private Integer warehouseId;
    private Integer amount;
    private Product product;

    public DecreaseQuantityOfProductUseCase withInputValidator(DecreaseQuantityOfProductUseCaseInputValidator inputValidator) {
        this.inputValidator = inputValidator;

        return this;
    }

    public DecreaseQuantityOfProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public DecreaseQuantityOfProductUseCase forProductId(UUID productId) {
        this.productId = productId;
        inputValidator.forProductId(productId);

        return this;
    }

    public DecreaseQuantityOfProductUseCase forWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
        inputValidator.forWarehouseId(warehouseId);

        return this;
    }

    public DecreaseQuantityOfProductUseCase forAmount(Integer amount) {
        this.amount = amount;
        inputValidator.forAmount(amount);

        return this;
    }

    public void run() {
        validateInput();
        decreaseQuantityOfProductInRepository();
    }

    private void validateInput() {
        inputValidator.run();
        if (!inputValidator.isValid())
            throw new CouldNotDecreaseQuantityOfProductException(inputValidator.getErrorMessages());
    }

    private void decreaseQuantityOfProductInRepository() {
        loadProduct();
        decreaseProductQuantity();
        updateProductInRepository();
    }

    private void loadProduct() {
        product = inputValidator.getProductFetchedDuringValidation();
    }

    private void decreaseProductQuantity() {
        Optional<Integer> currentQuantity = product.getQuantityInWarehouse(warehouseId);
        if (currentQuantity.isPresent())
            product.setQuantityInWarehouse(warehouseId, currentQuantity.get() - amount);
        else
            product.setQuantityInWarehouse(warehouseId, 0);
    }

    private void updateProductInRepository() {
        productRepository
            .save(product);
    }
}
