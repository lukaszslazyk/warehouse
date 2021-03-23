package studia.ium.warehouse.business.product.control;

import studia.ium.warehouse.business.product.entity.Product;

import java.util.List;

import static java.util.Objects.nonNull;

public class AddProductUseCaseInputValidator {
    private ProductValidationHelper validationHelper;
    private ProductRepository productRepository;
    private Product product;

    public AddProductUseCaseInputValidator(ProductValidationHelper validationHelper,
                                           ProductRepository productRepository) {
        this.validationHelper = validationHelper;
        this.productRepository = productRepository;
    }

    public AddProductUseCaseInputValidator forProduct(Product product) {
        this.product = product;

        return this;
    }

    public void run() {
        prepareValidation();
        validateProduct();
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

    private void validateProduct() {
        if (validateProductIsNotNull()) {
            validateProductWithGivenIdDoesNotAlreadyExist();
            boolean manufacturerNameIsAssigned = validateProductManufacturerNameIsAssigned();
            boolean modelNameIsAssigned = validateProductModelNameIsAssigned();
            if (manufacturerNameIsAssigned && modelNameIsAssigned)
                validateProductWithTheSameManufacturerNameAndModelNameDoesNotAlreadyExist();
            if (productPriceIsAssigned())
                validateProductPriceIsNotNegative();
        }
    }

    private boolean validateProductIsNotNull() {
        return validate(nonNull(product), "Product must not be null");
    }

    private boolean validateProductManufacturerNameIsAssigned() {
        return validate(productManufacturerNameIsAssigned(), "Product manufacturer name must be assigned");
    }

    private boolean validateProductModelNameIsAssigned() {
        return validate(productModelNameIsAssigned(), "Product model name must be assigned");
    }

    private void validateProductWithGivenIdDoesNotAlreadyExist() {
        validate(!productWithGivenIdExists(), "Product with given id already exists");
    }

    private void validateProductWithTheSameManufacturerNameAndModelNameDoesNotAlreadyExist() {
        validate(!productWithTheSameManufacturerNameAndModelNameExist(),
            "Product with the same manufacturer name and model name already exists");
    }

    private void validateProductPriceIsNotNegative() {
        validate(!productPriceIsNegative(), "Product price must not be negative");
    }

    private boolean productManufacturerNameIsAssigned() {
        return nonNull(product.getManufacturerName()) && !product.getManufacturerName().isEmpty();
    }

    private boolean productModelNameIsAssigned() {
        return nonNull(product.getModelName()) && !product.getModelName().isEmpty();
    }

    private boolean productWithGivenIdExists() {
        return nonNull(product.getId()) && productRepository.existsById(product.getId());
    }

    private boolean productWithTheSameManufacturerNameAndModelNameExist() {
        return productRepository
            .findByManufacturerNameAndModelName(product.getManufacturerName(), product.getModelName())
            .isPresent();
    }

    private boolean productPriceIsAssigned() {
        return nonNull(product.getPrice());
    }

    private boolean productPriceIsNegative() {
        return product.getPrice() < 0.0;
    }

    private boolean validate(boolean condition, String errorMessage) {
        return validationHelper.validate(condition, errorMessage);
    }
}
