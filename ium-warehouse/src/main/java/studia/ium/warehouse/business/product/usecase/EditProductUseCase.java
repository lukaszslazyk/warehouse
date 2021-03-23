package studia.ium.warehouse.business.product.usecase;

import studia.ium.warehouse.business.product.control.EditProductUseCaseInputValidator;
import studia.ium.warehouse.business.product.control.ProductRepository;
import studia.ium.warehouse.business.product.entity.CouldNotEditProductException;
import studia.ium.warehouse.business.product.entity.CouldNotEntirelyEditProductException;
import studia.ium.warehouse.business.product.entity.Product;
import studia.ium.warehouse.business.product.entity.ProductPatch;

import java.util.List;
import java.util.UUID;

public class EditProductUseCase {
    private ProductRepository productRepository;
    private EditProductUseCaseInputValidator inputValidator;
    private UUID productId;
    private List<ProductPatch> productPatches;
    private Long time;
    private Product productToEdit;

    public EditProductUseCase withInputValidator(EditProductUseCaseInputValidator inputValidator) {
        this.inputValidator = inputValidator;

        return this;
    }

    public EditProductUseCase withProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;

        return this;
    }

    public EditProductUseCase forProductId(UUID productId) {
        this.productId = productId;
        inputValidator.forProductId(productId);

        return this;
    }

    public EditProductUseCase forProductPatches(List<ProductPatch> productPatches) {
        this.productPatches = productPatches;
        inputValidator.forProductPatches(productPatches);

        return this;
    }

    public EditProductUseCase forTime(Long time) {
        this.time = time;
        inputValidator.forTime(time);

        return this;
    }

    public void run() {
        validateInput();
        loadProductToEdit();
        applyPatches();
        updateProductInRepository();
        if (anyWarningsOccurred())
            throwCouldNotEntirelyEditProductException();
    }

    private void validateInput() {
        inputValidator.run();
        if (!inputValidator.isValid())
            throw new CouldNotEditProductException(inputValidator.getErrorMessages());
    }

    private void loadProductToEdit() {
        productToEdit = inputValidator.getProductToEditFetchedDuringValidation();
    }

    private void applyPatches() {
        productPatches.forEach(this::applySingle);
    }

    private void applySingle(ProductPatch productPatch) {
        if (isManufacturerNamePatch(productPatch))
            applyManufacturerNamePatch(productPatch);
        else if (isModelNamePatch(productPatch))
            applyModelNamePatch(productPatch);
        else if (isPricePatch(productPatch))
            applyPricePatch(productPatch);
        else if (isExpirationDatePatch(productPatch))
            applyExpirationDatePatch(productPatch);
    }

    private boolean isManufacturerNamePatch(ProductPatch productPatch) {
        return productPatch.getFieldName().equals("manufacturerName");
    }

    private boolean isModelNamePatch(ProductPatch productPatch) {
        return productPatch.getFieldName().equals("modelName");
    }

    private boolean isPricePatch(ProductPatch productPatch) {
        return productPatch.getFieldName().equals("price");
    }

    private boolean isExpirationDatePatch(ProductPatch productPatch) {
        return productPatch.getFieldName().equals("expirationDate");
    }

    private void applyManufacturerNamePatch(ProductPatch productPatch) {
        productToEdit.setManufacturerName((String) productPatch.getValue());
        productToEdit.setManufacturerNameModifiedTimestamp(time);
    }

    private void applyModelNamePatch(ProductPatch productPatch) {
        productToEdit.setModelName((String) productPatch.getValue());
        productToEdit.setModelNameModifiedTimestamp(time);
    }

    private void applyPricePatch(ProductPatch productPatch) {
        productToEdit.setPrice((Double) productPatch.getValue());
        productToEdit.setPriceModifiedTimestamp(time);
    }

    private void applyExpirationDatePatch(ProductPatch productPatch) {
        productToEdit.setExpirationDate((Long) productPatch.getValue());
        productToEdit.setExpirationDateModifiedTimestamp(time);
    }

    private void updateProductInRepository() {
        productRepository
            .save(productToEdit);
    }

    private boolean anyWarningsOccurred() {
        return inputValidator.anyWarningsOccurred();
    }

    private void throwCouldNotEntirelyEditProductException() {
        throw new CouldNotEntirelyEditProductException(inputValidator.getWarningMessages());
    }
}
