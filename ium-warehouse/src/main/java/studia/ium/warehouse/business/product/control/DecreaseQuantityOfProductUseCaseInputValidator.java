package studia.ium.warehouse.business.product.control;

import studia.ium.warehouse.business.product.entity.Product;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

public class DecreaseQuantityOfProductUseCaseInputValidator {
    private ProductValidationHelper validationHelper;
    private ProductRepository productRepository;
    private UUID productId;
    private Integer warehouseId;
    private Integer amount;
    private Product product;
    private boolean productIdIsValid;

    public DecreaseQuantityOfProductUseCaseInputValidator(ProductValidationHelper validationHelper,
                                                          ProductRepository productRepository) {
        this.validationHelper = validationHelper;
        this.productRepository = productRepository;
    }

    public DecreaseQuantityOfProductUseCaseInputValidator forProductId(UUID productId) {
        this.productId = productId;

        return this;
    }

    public DecreaseQuantityOfProductUseCaseInputValidator forWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;

        return this;
    }

    public DecreaseQuantityOfProductUseCaseInputValidator forAmount(Integer amount) {
        this.amount = amount;

        return this;
    }

    public void run() {
        prepareValidation();
        validateProductId();
        validateAmount();
    }

    public boolean isValid() {
        return validationHelper.isValid();
    }

    public List<String> getErrorMessages() {
        return validationHelper.getErrorMessages();
    }

    public Product getProductFetchedDuringValidation() {
        return product;
    }

    private void prepareValidation() {
        validationHelper.prepareValidation();
    }

    private void validateProductId() {
        if (validateProductIdIsNotNull() && validateProductWithGivenIdExistsInRepository())
            productIdIsValid = true;
        else
            productIdIsValid = false;
    }

    private void validateAmount() {
        if (validateAmountIsNotNull() && validateAmountIsNotNegative() && productIdIsValid) {
            fetchProductFromRepository();
            validateProductQuantityIsNotLowerThanAmountToDecrease();
        }
    }

    private boolean validateProductIdIsNotNull() {
        return validate(nonNull(productId), "Product id must not be null");
    }

    private boolean validateProductWithGivenIdExistsInRepository() {
        return validate(productWithGivenIdExistsInRepository(), "Product does not exist or have been deleted");
    }

    private boolean validateAmountIsNotNull() {
        return validate(nonNull(amount), "Amount must not be null");
    }

    private boolean validateAmountIsNotNegative() {
        return validate(!amountIsNegative(), "Amount must not be negative");
    }

    private void validateProductQuantityIsNotLowerThanAmountToDecrease() {
        validate(!productQuantityIsLowerThanAmountToDecrease(),
            String.format("Product quantity in warehouse %d is lower than amount to decrease", warehouseId));
    }

    private boolean productWithGivenIdExistsInRepository() {
        return productRepository
            .existsById(productId);
    }

    private boolean amountIsNegative() {
        return amount < 0;
    }

    private boolean productQuantityIsLowerThanAmountToDecrease() {
        return product
            .getQuantityInWarehouse(warehouseId)
            .map(quantity -> quantity < amount)
            .orElseGet(() -> amount != 0);
    }

    private void fetchProductFromRepository() {
        product = productRepository
            .findById(productId)
            .get();
    }

    private boolean validate(boolean condition, String errorMessage) {
        return validationHelper.validate(condition, errorMessage);
    }
}
