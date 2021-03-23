package studia.ium.warehouse.business.product.usecase;

import studia.ium.warehouse.business.product.control.DeleteProductUseCaseInputValidator;
import studia.ium.warehouse.business.product.control.ProductRepository;
import studia.ium.warehouse.business.product.entity.CouldNotDeleteProductException;

import java.util.UUID;

public class DeleteProductUseCase {
    private DeleteProductUseCaseInputValidator inputValidator;
    private ProductRepository productRepository;
    private UUID productId;

    public DeleteProductUseCase withInputValidator(DeleteProductUseCaseInputValidator inputValidator) {
        this.inputValidator = inputValidator;

        return this;
    }

    public DeleteProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public DeleteProductUseCase forProductId(UUID productId) {
        this.productId = productId;
        inputValidator.forProductId(productId);

        return this;
    }

    public void run() {
        validateInput();
        deleteProductFromRepository();
    }

    private void validateInput() {
        inputValidator.run();
        if (!inputValidator.isValid())
            throw new CouldNotDeleteProductException(inputValidator.getErrorMessages());
    }

    private void deleteProductFromRepository() {
        productRepository
            .deleteById(productId);
    }
}
