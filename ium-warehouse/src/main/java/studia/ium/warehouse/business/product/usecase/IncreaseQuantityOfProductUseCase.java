package studia.ium.warehouse.business.product.usecase;

import studia.ium.warehouse.business.product.control.IncreaseQuantityOfProductUseCaseInputValidator;
import studia.ium.warehouse.business.product.control.ProductRepository;
import studia.ium.warehouse.business.product.entity.CouldNotIncreaseQuantityOfProductException;
import studia.ium.warehouse.business.product.entity.Product;

import java.util.Optional;
import java.util.UUID;

public class IncreaseQuantityOfProductUseCase {
    private IncreaseQuantityOfProductUseCaseInputValidator inputValidator;
    private ProductRepository productRepository;
    private UUID productId;
    private Integer warehouseId;
    private Integer amount;
    private Product product;

    public IncreaseQuantityOfProductUseCase withInputValidator(IncreaseQuantityOfProductUseCaseInputValidator inputValidator) {
        this.inputValidator = inputValidator;

        return this;
    }

    public IncreaseQuantityOfProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public IncreaseQuantityOfProductUseCase forProductId(UUID productId) {
        this.productId = productId;
        inputValidator.forProductId(productId);

        return this;
    }

    public IncreaseQuantityOfProductUseCase forWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
        inputValidator.forWarehouseId(warehouseId);

        return this;
    }

    public IncreaseQuantityOfProductUseCase forAmount(Integer amount) {
        this.amount = amount;
        inputValidator.forAmount(amount);

        return this;
    }

    public void run() {
        validateInput();
        increaseQuantityOfProductInRepository();
    }

    private void validateInput() {
        inputValidator.run();
        if (!inputValidator.isValid())
            throw new CouldNotIncreaseQuantityOfProductException(inputValidator.getErrorMessages());
    }

    private void increaseQuantityOfProductInRepository() {
        fetchProductFromRepository();
        increaseFetchedProductQuantity();
        updateProductInRepository();
    }

    private void fetchProductFromRepository() {
        product = productRepository
            .findById(productId)
            .get();
    }

    private void increaseFetchedProductQuantity() {
        Optional<Integer> currentQuantity = product.getQuantityInWarehouse(warehouseId);
        if (currentQuantity.isPresent())
            product.setQuantityInWarehouse(warehouseId, currentQuantity.get() + amount);
        else
            product.setQuantityInWarehouse(warehouseId, amount);
    }

    private void updateProductInRepository() {
        productRepository
            .save(product);
    }
}
