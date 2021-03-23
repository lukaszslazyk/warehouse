package studia.ium.warehouseandroidclient.business.product.usecase;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.entity.CouldNotAddProductException;
import studia.ium.warehouseandroidclient.business.product.entity.Product;

public class AddProductUseCase {
    private ProductRepository productRepository;
    private Product product;

    public AddProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public AddProductUseCase forProduct(Product product) {
        this.product = product;

        return this;
    }

    public void run() {
        validateInput();
        addProduct();
    }

    private void validateInput() {
        checkProductDoesNotExist();
    }

    private void checkProductDoesNotExist() {
        if (productWithTheSameManufacturerNameAndModelNameAlreadyExists())
            throw new CouldNotAddProductException(
                String.format("Product \"%s %s\" already exists", product.getManufacturerName(), product.getModelName()));
    }

    private boolean productWithTheSameManufacturerNameAndModelNameAlreadyExists() {
        return productRepository
            .existsByManufacturerNameAndModelName(product.getManufacturerName(), product.getModelName());
    }

    private void addProduct() {
        productRepository
            .addProduct(product);
    }
}
