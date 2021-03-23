package studia.ium.warehouseandroidclient.business.product.usecase;

import java.util.List;
import java.util.Optional;

import studia.ium.warehouseandroidclient.business.product.control.ProductRepository;
import studia.ium.warehouseandroidclient.business.product.entity.CouldNotEditProductException;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

public class EditProductUseCase {
    private ProductRepository productRepository;
    private String productId;
    private List<ProductPatch> productPatches;

    public EditProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public EditProductUseCase forProductId(String productId) {
        this.productId = productId;

        return this;
    }

    public EditProductUseCase forProductPatches(List<ProductPatch> productPatches) {
        this.productPatches = productPatches;

        return this;
    }

    public void run() {
        validateInput();
        editProduct();
    }

    private void validateInput() {
        checkProductWithGivenManufacturerNameAndModelNameAlreadyExists();
    }

    private void checkProductWithGivenManufacturerNameAndModelNameAlreadyExists() {
        Optional<ProductPatch> manufacturerNamePatch = findPatchOfManufacturerName();
        Optional<ProductPatch> modelNamePatch = findPatchOfModelName();
        if (manufacturerNamePatch.isPresent() && modelNamePatch.isPresent()) {
            String manufacturerName = (String) manufacturerNamePatch.get().getValue();
            String modelName = (String) modelNamePatch.get().getValue();
            if (productWithManufacturerNameAndModelNameAlreadyExists(manufacturerName, modelName))
                throw new CouldNotEditProductException(
                    String.format("Product \"%s %s\" already exists", manufacturerName, modelName));
        }
    }

    private Optional<ProductPatch> findPatchOfManufacturerName() {
        return findPatchOfField("manufacturerName");
    }

    private Optional<ProductPatch> findPatchOfModelName() {
        return findPatchOfField("modelName");
    }

    private Optional<ProductPatch> findPatchOfField(String fieldName) {
        return productPatches.stream()
            .filter(productPatch -> productPatch.getFieldName().equals(fieldName))
            .findFirst();
    }

    private boolean productWithManufacturerNameAndModelNameAlreadyExists(String manufacturerName, String modelName) {
        return productRepository.existsByManufacturerNameAndModelName(manufacturerName, modelName);
    }

    private void editProduct() {
        productRepository
            .editProduct(productId, productPatches);
    }
}
