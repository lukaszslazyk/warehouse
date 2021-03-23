package studia.ium.warehouseandroidclient.application.ui.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import studia.ium.warehouseandroidclient.business.product.boundary.ProductService;
import studia.ium.warehouseandroidclient.business.product.entity.Product;

public class AddProductViewModel extends ViewModel {
    private ProductService productService;
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

    public AddProductViewModel(ProductService productService) {
        this.productService = productService;
    }

    public void init() {
        setInitialEditableValues();
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
        executeInBackground(() -> performAddProductCall(createProductFromFields()));
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

    public MutableLiveData<String> getExpirationDateValidationErrorMessage() {
        return expirationDateValidationErrorMessage;
    }

    public LiveData<Boolean> getSaveProductSuccess() {
        return saveProductSuccess;
    }

    public MutableLiveData<String> getSaveProductErrorMessage() {
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

    private void setInitialEditableValues() {
        editableManufacturerName.setValue("");
        editableModelName.setValue("");
        editablePriceText.setValue("");
        editableExpirationDateText.setValue("");
    }

    private Product createProductFromFields() {
        Product product = new Product();
        product.setManufacturerName(editableManufacturerName.getValue());
        product.setModelName(editableModelName.getValue());
        product.setPrice(mapToDouble(editablePriceText.getValue()));
        product.setExpirationDate(mapToMillis(editableExpirationDateText.getValue()));

        return product;
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
            saveProductErrorMessage.postValue("Unexpected error occurred");
            return null;
        }
    }

    private void executeInBackground(Runnable task) {
        backgroundTaskExecutor.execute(task);
    }

    private void performAddProductCall(Product product) {
        try {
            productService.add(product);
            saveProductSuccess.postValue(true);
        } catch (Exception e) {
            saveProductErrorMessage.postValue(e.getMessage());
        }
    }
}
