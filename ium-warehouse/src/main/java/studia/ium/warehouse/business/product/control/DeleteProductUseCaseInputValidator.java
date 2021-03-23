package studia.ium.warehouse.business.product.control;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

public class DeleteProductUseCaseInputValidator {
    private ProductValidationHelper validationHelper;
    private ProductRepository productRepository;
    private UUID productId;

    public DeleteProductUseCaseInputValidator(ProductValidationHelper validationHelper,
                                              ProductRepository productRepository) {
        this.validationHelper = validationHelper;
        this.productRepository = productRepository;
    }

    public DeleteProductUseCaseInputValidator forProductId(UUID productId) {
        this.productId = productId;

        return this;
    }

    public void run() {
        prepareValidation();
        validateProductId();
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

    private boolean validateProductIdIsNotNull() {
        return validate(nonNull(productId), "Product id must not be null");
    }

    private void validateProductWithGivenIdExistsInRepository() {
        validate(productWithGivenIdExistsInRepository(), "Product does not exist or have already been deleted");
    }

    private boolean productWithGivenIdExistsInRepository() {
        return productRepository.existsById(productId);
    }

    private boolean validate(boolean condition, String errorMessage) {
        return validationHelper.validate(condition, errorMessage);
    }
}
