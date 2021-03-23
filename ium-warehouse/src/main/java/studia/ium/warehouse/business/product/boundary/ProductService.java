package studia.ium.warehouse.business.product.boundary;

import studia.ium.warehouse.business.product.control.*;
import studia.ium.warehouse.business.product.entity.Product;
import studia.ium.warehouse.business.product.entity.ProductPatch;
import studia.ium.warehouse.business.product.usecase.*;

import java.util.List;
import java.util.UUID;

public class ProductService {
    private ProductRepository productRepository;
    private FindByIdUseCaseInputValidator findByIdUseCaseInputValidator;
    private AddProductUseCaseInputValidator addProductUseCaseInputValidator;
    private EditProductUseCaseInputValidator editProductUseCaseInputValidator;
    private DeleteProductUseCaseInputValidator deleteProductUseCaseInputValidator;
    private IncreaseQuantityOfProductUseCaseInputValidator increaseQuantityOfProductUseCaseInputValidator;
    private DecreaseQuantityOfProductUseCaseInputValidator decreaseQuantityOfProductUseCaseInputValidator;

    public ProductService(ProductRepository productRepository,
                          FindByIdUseCaseInputValidator findByIdUseCaseInputValidator,
                          AddProductUseCaseInputValidator addProductUseCaseInputValidator,
                          EditProductUseCaseInputValidator editProductUseCaseInputValidator,
                          DeleteProductUseCaseInputValidator deleteProductUseCaseInputValidator,
                          IncreaseQuantityOfProductUseCaseInputValidator increaseQuantityOfProductUseCaseInputValidator,
                          DecreaseQuantityOfProductUseCaseInputValidator decreaseQuantityOfProductUseCaseInputValidator) {
        this.productRepository = productRepository;
        this.findByIdUseCaseInputValidator = findByIdUseCaseInputValidator;
        this.addProductUseCaseInputValidator = addProductUseCaseInputValidator;
        this.editProductUseCaseInputValidator = editProductUseCaseInputValidator;
        this.deleteProductUseCaseInputValidator = deleteProductUseCaseInputValidator;
        this.increaseQuantityOfProductUseCaseInputValidator = increaseQuantityOfProductUseCaseInputValidator;
        this.decreaseQuantityOfProductUseCaseInputValidator = decreaseQuantityOfProductUseCaseInputValidator;
    }

    public List<Product> findAll() {
        return new FindAllProductsUseCase()
            .withProductRepository(productRepository)
            .run();
    }

    public Product findById(UUID productId) {
        return new FindProductByIdUseCase()
            .withInputValidator(findByIdUseCaseInputValidator)
            .withProductRepository(productRepository)
            .forProductId(productId)
            .run();
    }

    public void add(Product product) {
        new AddProductUseCase()
            .withInputValidator(addProductUseCaseInputValidator)
            .withProductRepository(productRepository)
            .forProduct(product)
            .run();
    }

    public void edit(UUID productId, List<ProductPatch> productPatches, Long time) {
        new EditProductUseCase()
            .withInputValidator(editProductUseCaseInputValidator)
            .withProductRepository(productRepository)
            .forProductId(productId)
            .forProductPatches(productPatches)
            .forTime(time)
            .run();
    }

    public void delete(UUID productId) {
        new DeleteProductUseCase()
            .withInputValidator(deleteProductUseCaseInputValidator)
            .withProductRepository(productRepository)
            .forProductId(productId)
            .run();
    }

    public void increaseQuantityOfProduct(UUID productId, Integer amount, Integer warehouseId) {
        new IncreaseQuantityOfProductUseCase()
            .withInputValidator(increaseQuantityOfProductUseCaseInputValidator)
            .withProductRepository(productRepository)
            .forProductId(productId)
            .forWarehouseId(warehouseId)
            .forAmount(amount)
            .run();
    }

    public void decreaseQuantityOfProduct(UUID productId, Integer amount, Integer warehouseId) {
        new DecreaseQuantityOfProductUseCase()
            .withInputValidator(decreaseQuantityOfProductUseCaseInputValidator)
            .withProductRepository(productRepository)
            .forProductId(productId)
            .forWarehouseId(warehouseId)
            .forAmount(amount)
            .run();
    }
}
