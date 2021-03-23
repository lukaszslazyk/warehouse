package studia.ium.warehouse.business.product.usecase;

import studia.ium.warehouse.business.product.control.FindByIdUseCaseInputValidator;
import studia.ium.warehouse.business.product.control.ProductRepository;
import studia.ium.warehouse.business.product.entity.CouldNotFindProductByIdException;
import studia.ium.warehouse.business.product.entity.Product;

import java.util.UUID;

public class FindProductByIdUseCase {
    private FindByIdUseCaseInputValidator inputValidator;
    private ProductRepository productRepository;
    private UUID productId;

    public FindProductByIdUseCase withInputValidator(FindByIdUseCaseInputValidator inputValidator) {
        this.inputValidator = inputValidator;

        return this;
    }

    public FindProductByIdUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public FindProductByIdUseCase forProductId(UUID productId) {
        this.productId = productId;
        inputValidator.forProductId(productId);

        return this;
    }

    public Product run() {
        validateInput();

        return findProduct();
    }

    private void validateInput() {
        inputValidator.run();
        if (!inputValidator.isValid())
            throw new CouldNotFindProductByIdException(inputValidator.getErrorMessages());
    }

    private Product findProduct() {
        return productRepository
            .findById(productId)
            .get();
    }
}
