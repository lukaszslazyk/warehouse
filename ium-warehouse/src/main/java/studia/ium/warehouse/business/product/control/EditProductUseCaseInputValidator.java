package studia.ium.warehouse.business.product.control;

import studia.ium.warehouse.business.product.entity.Product;
import studia.ium.warehouse.business.product.entity.ProductPatch;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class EditProductUseCaseInputValidator {
    private ProductValidationHelper validationHelper;
    private ProductRepository productRepository;
    private UUID productId;
    private List<ProductPatch> productPatches;
    private Long time;
    private Product productToEdit;
    private List<ProductPatch> skippedProductPatches = new ArrayList<>();

    public EditProductUseCaseInputValidator(ProductValidationHelper validationHelper,
                                            ProductRepository productRepository) {
        this.validationHelper = validationHelper;
        this.productRepository = productRepository;
    }

    public EditProductUseCaseInputValidator forProductId(UUID productId) {
        this.productId = productId;

        return this;
    }

    public EditProductUseCaseInputValidator forProductPatches(List<ProductPatch> productPatches) {
        this.productPatches = productPatches;

        return this;
    }

    public EditProductUseCaseInputValidator forTime(Long time) {
        this.time = time;

        return this;
    }

    public void run() {
        prepareValidation();
        validateProductId();
        if (isValid()) {
            fetchProductFromRepository();
            validateProductPatches();
        }
    }

    public boolean isValid() {
        return validationHelper.isValid();
    }

    public boolean anyWarningsOccurred() {
        return validationHelper.anyWarningsOccurred();
    }

    public List<String> getErrorMessages() {
        return validationHelper.getErrorMessages();
    }

    public List<String> getWarningMessages() {
        return validationHelper.getWarningMessages();
    }

    public Product getProductToEditFetchedDuringValidation() {
        return productToEdit;
    }

    private void prepareValidation() {
        validationHelper.prepareValidation();
    }

    private void validateProductId() {
        if (validateProductIdIsNotNull())
            validateProductExistsInRepository();
    }

    private boolean validateProductIdIsNotNull() {
        return validate(nonNull(productId), "Product id must not be null");
    }

    private void validateProductExistsInRepository() {
        validate(productExistsInRepository(),
            "Product does not exist or have been deleted");
    }

    private boolean productExistsInRepository() {
        return productRepository.existsById(productId);
    }

    private void fetchProductFromRepository() {
        productToEdit = productRepository
            .findById(productId)
            .get();
    }

    private void validateProductPatches() {
        productPatches.forEach(this::validateSingle);
        removeSkippedPatchesFromInput();
        validateProductWithGivenManufacturerNameAndModelNameDoesNotAlreadyExists();
    }

    private void validateSingle(ProductPatch productPatch) {
        if (isManufacturerNamePatch(productPatch))
            validateManufacturerNamePatch(productPatch);
        else if (isModelNamePatch(productPatch))
            validateModelNamePatch(productPatch);
        else if (isPricePatch(productPatch))
            validatePricePatch(productPatch);
        else if (isExpirationDatePatch(productPatch))
            validateExpirationDatePatch(productPatch);
        else
            handleNonExistingFieldPatch(productPatch);
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

    private void validateManufacturerNamePatch(ProductPatch productPatch) {
        if (validateManufacturerNameIsNotEmpty(productPatch) && validateManufacturerNamePatchIsNotTheSameAsCurrent(productPatch))
            validateManufacturerNameEditTimeConflict(productPatch);
    }

    private void validateModelNamePatch(ProductPatch productPatch) {
        if (validateModelNameIsNotEmpty(productPatch) && validateModelNamePatchIsNotTheSameAsCurrent(productPatch))
            validateModelNameEditTimeConflict(productPatch);
    }

    private void validatePricePatch(ProductPatch productPatch) {
        if (patchValueIsAssigned(productPatch))
            validatePriceIsNotNegative(productPatch);
        validatePriceEditTimeConflict(productPatch);
    }

    private void validateExpirationDatePatch(ProductPatch productPatch) {
        validateExpirationDateEditTimeConflict(productPatch);
    }

    private boolean validateManufacturerNameIsNotEmpty(ProductPatch productPatch) {
        return validate(!patchValueIsEmptyString(productPatch), "Manufacturer name must not be empty");
    }

    private boolean validateModelNameIsNotEmpty(ProductPatch productPatch) {
        return validate(!patchValueIsEmptyString(productPatch), "Model name must not be empty");
    }

    private void validatePriceIsNotNegative(ProductPatch productPatch) {
        validate((Double) productPatch.getValue() >= 0.0,
            "Product price must not be negative");
    }

    private boolean validateManufacturerNamePatchIsNotTheSameAsCurrent(ProductPatch productPatch) {
        return validateProductPatchWithSkipOnFailure(
            !productPatch.getValue().equals(productToEdit.getManufacturerName()), productPatch);
    }

    private boolean validateModelNamePatchIsNotTheSameAsCurrent(ProductPatch productPatch) {
        return validateProductPatchWithSkipOnFailure(
            !productPatch.getValue().equals(productToEdit.getModelName()), productPatch);
    }

    private void validateManufacturerNameEditTimeConflict(ProductPatch productPatch) {
        if (!validateFieldTimeConflict(productPatch, productToEdit.getManufacturerNameModifiedTimestamp()))
            addWarningMessage(getWarningMessageForFieldTimeConflict(
                "Manufacturer name", productPatch.getValue().toString(), productToEdit.getManufacturerName()));
    }

    private void validateModelNameEditTimeConflict(ProductPatch productPatch) {
        if (!validateFieldTimeConflict(productPatch, productToEdit.getModelNameModifiedTimestamp()))
            addWarningMessage(getWarningMessageForFieldTimeConflict(
                "Model name", productPatch.getValue().toString(), productToEdit.getModelName()));
    }

    private void validatePriceEditTimeConflict(ProductPatch productPatch) {
        if (!validateFieldTimeConflict(productPatch, productToEdit.getPriceModifiedTimestamp()))
            addWarningMessage(getWarningMessageForFieldTimeConflict(
                "Price",
                mapNullableToString(productPatch.getValue()),
                mapNullableToString(productToEdit.getPrice()))
            );
    }

    private void validateExpirationDateEditTimeConflict(ProductPatch productPatch) {
        if (!validateFieldTimeConflict(productPatch, productToEdit.getExpirationDateModifiedTimestamp()))
            addWarningMessage(getWarningMessageForFieldTimeConflict(
                "Expiration date",
                mapDateLongToString((Long) productPatch.getValue()),
                mapDateLongToString(productToEdit.getExpirationDate())));
    }

    private void handleNonExistingFieldPatch(ProductPatch productPatch) {
        skippedProductPatches.add(productPatch);
        validationHelper.getWarningMessages()
            .add(String.format("Field \"%s\" does not exists", productPatch.getFieldName()));
    }

    private String getWarningMessageForFieldTimeConflict(String fieldName, String patchValueText, String currentValueText) {
        return String.format(
            "Failed to change field \"%s\" value to %s because someone changed it to %s after you",
            fieldName, formatFieldValueText(patchValueText), formatFieldValueText(currentValueText)
        );
    }

    private String formatFieldValueText(String fieldValueText) {
        return fieldValueText.isEmpty() ?
            "empty" :
            '\"' + fieldValueText + '\"';
    }

    private void validateProductWithGivenManufacturerNameAndModelNameDoesNotAlreadyExists() {
        Optional<ProductPatch> manufacturerNamePatch = findPatchOfManufacturerName();
        Optional<ProductPatch> modelNamePatch = findPatchOfModelName();
        String manufacturerName = manufacturerNamePatch.isPresent() ?
            (String) manufacturerNamePatch.get().getValue() : productToEdit.getManufacturerName();
        String modelName = modelNamePatch.isPresent() ?
            (String) modelNamePatch.get().getValue() : productToEdit.getModelName();
        if (manufacturerNamePatch.isPresent() || modelNamePatch.isPresent())
            validate(!productWithManufacturerNameAndModelNameAlreadyExists(manufacturerName, modelName),
                String.format("Product \"%s %s\" already exists", manufacturerName, modelName));
    }

    private boolean productWithManufacturerNameAndModelNameAlreadyExists(String manufacturerName, String modelName) {
        return productRepository
            .findByManufacturerNameAndModelName(manufacturerName, modelName)
            .isPresent();
    }

    private boolean validate(boolean condition, String validationErrorMessage) {
        return validationHelper.validate(condition, validationErrorMessage);
    }

    private boolean validateProductPatchWithSkipOnFailure(boolean condition, ProductPatch productPatch) {
        if (!condition)
            skippedProductPatches.add(productPatch);

        return condition;
    }

    private boolean validateFieldTimeConflict(ProductPatch productPatch, Long currentTimestamp) {
        return validateProductPatchWithSkipOnFailure(currentTimestamp < time, productPatch);
    }

    private boolean patchValueIsAssigned(ProductPatch productPatch) {
        return nonNull(productPatch.getValue());
    }

    private boolean patchValueIsEmptyString(ProductPatch productPatch) {
        return isNull(productPatch.getValue()) || ((String) productPatch.getValue()).isEmpty();
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

    private void addWarningMessage(String warningMessage) {
        validationHelper.getWarningMessages().add(warningMessage);
    }

    private void removeSkippedPatchesFromInput() {
        productPatches.removeAll(skippedProductPatches);
    }

    private String mapNullableToString(Object value) {
        return nonNull(value) ?
            value.toString() :
            "";
    }

    private String mapDateLongToString(Long expirationDate) {
        return nonNull(expirationDate) ?
            new SimpleDateFormat("dd/MM/yyyy").format(new Date(expirationDate)) :
            "";
    }
}
