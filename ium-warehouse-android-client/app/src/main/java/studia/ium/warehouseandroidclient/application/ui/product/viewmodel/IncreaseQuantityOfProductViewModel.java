package studia.ium.warehouseandroidclient.application.ui.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import studia.ium.warehouseandroidclient.business.product.boundary.ProductService;

public class IncreaseQuantityOfProductViewModel extends ViewModel {
    private ProductService productService;
    private String productId;
    private MutableLiveData<String> amount = new MutableLiveData<>();
    private MutableLiveData<String> warehouseId = new MutableLiveData<>();
    private MutableLiveData<String> amountValidationErrorMessage = new MutableLiveData<>();
    private MutableLiveData<String> warehouseIdValidationErrorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> increaseQuantitySuccess = new MutableLiveData<>();
    private MutableLiveData<String> increaseQuantityErrorMessage = new MutableLiveData<>();
    private Executor backgroundTaskExecutor = Executors.newSingleThreadExecutor();
    private boolean formValid = false;

    public IncreaseQuantityOfProductViewModel(ProductService productService) {
        this.productService = productService;
    }

    public IncreaseQuantityOfProductViewModel withProductId(String productId) {
        this.productId = productId;

        return this;
    }

    public void init() {
        setInitialValues();
    }

    public void validateForm() {
        prepareForValidation();
        validateAmount();
        validateWarehouseId();
    }

    public boolean formIsValid() {
        return formValid;
    }

    public void save() {
        executeInBackground(this::performIncreaseQuantityOfProductCall);
    }

    public MutableLiveData<String> getAmount() {
        return amount;
    }

    public MutableLiveData<String> getWarehouseId() {
        return warehouseId;
    }

    public LiveData<String> getAmountValidationErrorMessage() {
        return amountValidationErrorMessage;
    }

    public LiveData<String> getWarehouseIdValidationErrorMessage() {
        return warehouseIdValidationErrorMessage;
    }

    public LiveData<Boolean> getIncreaseQuantitySuccess() {
        return increaseQuantitySuccess;
    }

    public LiveData<String> getIncreaseQuantityErrorMessage() {
        return increaseQuantityErrorMessage;
    }

    private void setInitialValues() {
        amount.setValue("");
        warehouseId.setValue("0");
    }

    private void prepareForValidation() {
        formValid = true;
        amountValidationErrorMessage.setValue("");
        warehouseIdValidationErrorMessage.setValue("");
    }

    private void validateAmount() {
        validate(amountIsEmpty(), amountValidationErrorMessage,
            "Amount cannot be empty");
        validate(amountIsEqualToZero(), amountValidationErrorMessage,
            "Amount to decrease cannot be equal to zero");
    }

    private void validateWarehouseId() {
        validate(warehouseIdIsEmpty(), warehouseIdValidationErrorMessage,
            "Warehouse ID cannot be empty");
    }

    private void validate(boolean condition, MutableLiveData<String> errorMessageHolder, String errorMessage) {
        if (condition) {
            errorMessageHolder.setValue(errorMessage);
            formValid = false;
        }
    }

    private boolean amountIsEmpty() {
        return amount.getValue().isEmpty();
    }

    private boolean amountIsEqualToZero() {
        return !amountIsEmpty()
            && mapToInt(amount.getValue()) == 0;
    }

    private boolean warehouseIdIsEmpty() {
        return warehouseId.getValue().isEmpty();
    }

    private Integer mapToInt(String amount) {
        return Integer.parseInt(amount);
    }

    private void executeInBackground(Runnable task) {
        backgroundTaskExecutor.execute(task);
    }

    private void performIncreaseQuantityOfProductCall() {
        try {
            productService.increaseQuantityOfProduct(productId, mapToInt(amount.getValue()), mapToInt(warehouseId.getValue()));
            increaseQuantitySuccess.postValue(true);
        } catch (Exception e) {
            increaseQuantityErrorMessage.postValue(e.getMessage());
        }
    }
}
