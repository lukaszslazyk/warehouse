package studia.ium.warehouse.business.product.usecase;

import studia.ium.warehouse.business.product.control.AddProductUseCaseInputValidator;
import studia.ium.warehouse.business.product.control.ProductRepository;
import studia.ium.warehouse.business.product.entity.CouldNotAddProductException;
import studia.ium.warehouse.business.product.entity.Product;

public class AddProductUseCase {
    private AddProductUseCaseInputValidator inputValidator;
    private ProductRepository productRepository;
    private Product product;

    public AddProductUseCase withInputValidator(AddProductUseCaseInputValidator inputValidator) {
        this.inputValidator = inputValidator;

        return this;
    }

    public AddProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public AddProductUseCase forProduct(Product product) {
        this.product = product;
        inputValidator.forProduct(product);

        return this;
    }

    public void run() {
        validateInput();
        setProductQuantityToZeroInDefaultWarehouse();
        addNewProductToRepository();
    }

    private void validateInput() {
        inputValidator.run();
        if (!inputValidator.isValid())
            throw new CouldNotAddProductException(inputValidator.getErrorMessages());
    }

    private void setProductQuantityToZeroInDefaultWarehouse() {
        product.setQuantityInWarehouse(0, 0);
    }

    private void addNewProductToRepository() {
        productRepository
            .save(product);
    }
}
