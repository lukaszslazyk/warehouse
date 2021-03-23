package studia.ium.warehouseandroidclient.application.ui.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import studia.ium.warehouseandroidclient.application.ui.product.model.PresentableProduct;
import studia.ium.warehouseandroidclient.business.product.boundary.ProductService;
import studia.ium.warehouseandroidclient.business.product.entity.ProductPatch;

import static java.util.Objects.nonNull;

public class EditProductViewModel extends ViewModel {
    private ProductService productService;
    private PresentableProduct product;
    private MutableLiveData<String> editableManufacturerName = new MutableLiveData<>();
    private MutableLiveData<String> editableModelName = new MutableLiveData<>();
    private MutableLiveData<String> editablePriceText = new MutableLiveData<>();
    private MutableLiveData<String> editableExpirationDateText = new MutableLiveData<>();
    private MutableLiveData<String> manufacturerNameValidationErrorMessage = new MutableLiveData<>();
    private MutableLiveData<String> modelNameValidationErrorMessage = new MutableLiveData<>();
    private MutableLiveData<String> expirationDateValidationErrorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> saveProductSuccess = new MutableLiveData<>();
    private MutableLiveData<String> saveProductErrorMessage = new MutableLiveData<>();
    private Executor backgroundTaskExecutor = Executors.newSingleThreadExecutor();
    private boolean productValid = false;

    public EditProductViewModel(ProductService productService) {
        this.productService = productService;
    }

    public EditProductViewModel withProduct(PresentableProduct product) {
        this.product = product;

        return this;
    }

    public void init() {
        updateInputFieldTexts();
    }

    public void validateProduct() {
        prepareForValidation();
        validateManufacturerName();
        validateModelName();
        validateExpirationDate();
    }

    public boolean productIsValid() {
        return productValid;
    }

    public void saveProduct() {
        executeInBackground(() -> performEditProductCall(product.getId(), getProductPatches()));
    }

    public MutableLiveData<String> getEditableManufacturerName() {
        return editableManufacturerName;
    }

    public MutableLiveData<String> getEditableModelName() {
        return editableModelName;
    }

    public MutableLiveData<String> getEditablePriceText() {
        return editablePriceText;
    }

    public MutableLiveData<String> getEditableExpirationDateText() {
        return editableExpirationDateText;
    }

    public LiveData<String> getManufacturerNameValidationErrorMessage() {
        return manufacturerNameValidationErrorMessage;
    }

    public LiveData<String> getModelNameValidationErrorMessage() {
        return modelNameValidationErrorMessage;
    }

    public LiveData<String> getExpirationDateValidationErrorMessage() {
        return expirationDateValidationErrorMessage;
    }

    public LiveData<Boolean> getSaveProductSuccess() {
        return saveProductSuccess;
    }

    public LiveData<String> getSaveProductErrorMessage() {
        return saveProductErrorMessage;
    }

    private void prepareForValidation() {
        productValid = true;
        manufacturerNameValidationErrorMessage.setValue("");
        modelNameValidationErrorMessage.setValue("");
        expirationDateValidationErrorMessage.setValue("");
    }

    private void validateManufacturerName() {
        validate(manufacturerNameIsEmpty(), manufacturerNameValidationErrorMessage,
            "Manufacturer name cannot be empty");
    }

    private void validateModelName() {
        validate(modelNameIsEmpty(), modelNameValidationErrorMessage,
            "Model name cannot be empty");
    }

    private void validateExpirationDate() {
        validate(expirationDateIsInvalid(), expirationDateValidationErrorMessage,
            "Expiration date is invalid");
    }

    private void validate(boolean condition, MutableLiveData<String> errorMessageHolder, String errorMessage) {
        if (condition) {
            errorMessageHolder.setValue(errorMessage);
            productValid = false;
        }
    }

    private boolean manufacturerNameIsEmpty() {
        return emptyOrOnlyWhitespace(editableManufacturerName.getValue());
    }

    private boolean modelNameIsEmpty() {
        return emptyOrOnlyWhitespace(editableModelName.getValue());
    }

    private boolean emptyOrOnlyWhitespace(String text) {
        return text.trim().isEmpty();
    }

    private boolean expirationDateIsInvalid() {
        try {
            if (editableExpirationDateText.getValue() != null && !editableExpirationDateText.getValue().isEmpty()) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                df.setLenient(false);
                df.parse(editableExpirationDateText.getValue());
            }
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private List<ProductPatch> getProductPatches() {
        List<ProductPatch> productPatches = new ArrayList<>();
        if (manufacturerNameChanged())
            addProductPatch(productPatches, "manufacturerName", editableManufacturerName.getValue());
        if (modelNameChanged())
            addProductPatch(productPatches, "modelName", editableModelName.getValue());
        if (priceChanged())
            addProductPatch(productPatches, "price", mapToDouble(editablePriceText.getValue()));
        if (expirationDateChanged())
            addProductPatch(productPatches, "expirationDate", mapToMillis(editableExpirationDateText.getValue()));

        return productPatches;
    }

    private void addProductPatch(List<ProductPatch> productPatches, String fieldName, Object value) {
        productPatches.add(new ProductPatch(fieldName, value));
    }

    private boolean manufacturerNameChanged() {
        return !Objects.equals(editableManufacturerName.getValue(), product.getManufacturerName());
    }

    private boolean modelNameChanged() {
        return !Objects.equals(editableModelName.getValue(), product.getModelName());
    }

    private boolean priceChanged() {
        return nonNull(editablePriceText.getValue())
            && !Objects.equals(mapToDouble(editablePriceText.getValue()), product.getPrice());
    }

    private boolean expirationDateChanged() {
        return nonNull(editableExpirationDateText.getValue())
            && !Objects.equals(mapToMillis(editableExpirationDateText.getValue()), product.getExpirationDate());
    }

    private void updateInputFieldTexts() {
        editableManufacturerName.setValue(product.getManufacturerName());
        editableModelName.setValue(product.getModelName());
        editablePriceText.setValue(product.getEditablePrice());
        editableExpirationDateText.setValue(product.getEditableExpirationDate());
    }

    private Double mapToDouble(String text) {
        return text != null && !text.isEmpty() ?
            Double.parseDouble(text) :
            null;
    }

    private Long mapToMillis(String text) {
        try {
            return text != null && !text.isEmpty() ?
                new SimpleDateFormat("dd/MM/yyyy").parse(text).getTime() :
                null;
        } catch (ParseException e) {
            throw new RuntimeException("Unexpected error occurred");
        }
    }

    private void executeInBackground(Runnable task) {
        backgroundTaskExecutor.execute(task);
    }

    private void performEditProductCall(String productId, List<ProductPatch> productPatches) {
        try {
            productService.edit(productId, productPatches);
            saveProductSuccess.postValue(true);
        } catch (Exception e) {
            saveProductErrorMessage.postValue(e.getMessage());
        }
    }
}

