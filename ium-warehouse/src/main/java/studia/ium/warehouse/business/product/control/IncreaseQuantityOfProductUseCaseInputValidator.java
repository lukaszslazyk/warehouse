package studia.ium.warehouse.business.product.control;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

public class IncreaseQuantityOfProductUseCaseInputValidator {
    private ProductValidationHelper validationHelper;
    private ProductRepository productRepository;
    private UUID productId;
    private Integer warehouseId;
    private Integer amount;

    public IncreaseQuantityOfProductUseCaseInputValidator(ProductValidationHelper validationHelper,
                                                          ProductRepository productRepository) {
        this.validationHelper = validationHelper;
        this.productRepository = productRepository;
    }

    public IncreaseQuantityOfProductUseCaseInputValidator forProductId(UUID productId) {
        this.productId = productId;

        return this;
    }

    public IncreaseQuantityOfProductUseCaseInputValidator forWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;

        return this;
    }

    public IncreaseQuantityOfProductUseCaseInputValidator forAmount(Integer amount) {
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

    private void prepareValidation() {
        validationHelper.prepareValidation();
    }

    private void validateProductId() {
        if (validateProductIdIsNotNull())
            validateProductWithGivenIdExistsInRepository();
    }

    private void validateAmount() {
        if (validateAmountIsNotNull())
            validateAmountIsNotNegative();
    }

    private boolean validateProductIdIsNotNull() {
        return validate(nonNull(productId), "Product id must not be null");
    }

    private void validateProductWithGivenIdExistsInRepository() {
        validate(productWithGivenIdExistsInRepository(), "Product does not exist or have been deleted");
    }

    private boolean validateAmountIsNotNull() {
        return validate(nonNull(amount), "Amount must not be null");
    }

    private void validateAmountIsNotNegative() {
        validate(!amountIsNegative(), "Amount must not be negative");
    }

    private boolean productWithGivenIdExistsInRepository() {
        return productRepository
            .existsById(productId);
    }

    private boolean amountIsNegative() {
        return amount < 0;
    }

    private boolean validate(boolean condition, String errorMessage) {
        return validationHelper.validate(condition, errorMessage);
    }
}
