package studia.ium.warehouseandroidclient.application.ui.product.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import studia.ium.warehouseandroidclient.application.ui.product.model.PresentableProduct;
import studia.ium.warehouseandroidclient.business.product.boundary.ProductService;

public class DecreaseQuantityOfProductViewModel extends ViewModel {
    private ProductService productService;
    private PresentableProduct product;
    private MutableLiveData<String> amount = new MutableLiveData<>();
    private MutableLiveData<String> warehouseId = new MutableLiveData<>();
    private MutableLiveData<String> amountValidationErrorMessage = new MutableLiveData<>();
    private MutableLiveData<String> warehouseIdValidationErrorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> decreaseQuantitySuccess = new MutableLiveData<>();
    private MutableLiveData<String> decreaseQuantityErrorMessage = new MutableLiveData<>();
    private Executor backgroundTaskExecutor = Executors.newSingleThreadExecutor();
    private boolean formValid = false;

    public DecreaseQuantityOfProductViewModel(ProductService productService) {
        this.productService = productService;
    }

    public DecreaseQuantityOfProductViewModel withProduct(PresentableProduct product) {
        this.product = product;

        return this;
    }

    public void init() {
        setInitialValues();
    }

    public void validateForm() {
        prepareForValidation();
        validateWarehouseId();
        validateAmount();
    }

    public boolean formIsValid() {
        return formValid;
    }

    public void save() {
        executeInBackground(this::performDecreaseQuantityOfProductCall);
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

    public LiveData<Boolean> getDecreaseQuantitySuccess() {
        return decreaseQuantitySuccess;
    }

    public LiveData<String> getDecreaseQuantityErrorMessage() {
        return decreaseQuantityErrorMessage;
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

    private void validateWarehouseId() {
        validate(warehouseIdIsEmpty(), warehouseIdValidationErrorMessage,
            "Warehouse ID cannot be empty");
    }

    private void validateAmount() {
        validate(amountIsEmpty(), amountValidationErrorMessage,
            "Amount cannot be empty");
        validate(amountIsEqualToZero(), amountValidationErrorMessage,
            "Amount to decrease cannot be equal to zero");
        validate(amountIsGreaterThanCurrentProductQuantityInWarehouse(), amountValidationErrorMessage,
            String.format("Amount to decrease cannot be greater than current product quantity: %d in warehouse: %s",
                getProductQuantityInGivenWarehouse(), warehouseId.getValue()));
    }

    private void validate(boolean condition, MutableLiveData<String> errorMessageHolder, String errorMessage) {
        if (condition) {
            errorMessageHolder.setValue(errorMessage);
            formValid = false;
        }
    }

    private boolean warehouseIdIsEmpty() {
        return warehouseId.getValue().isEmpty();
    }

    private boolean amountIsEmpty() {
        return amount.getValue().isEmpty();
    }

    private boolean amountIsEqualToZero() {
        return !amountIsEmpty()
            && mapToInt(amount.getValue()) == 0;
    }

    private boolean amountIsGreaterThanCurrentProductQuantityInWarehouse() {
        return !amountIsEmpty() && !amountIsEqualToZero()
            && mapToInt(amount.getValue()) > getProductQuantityInGivenWarehouse();
    }

    private Integer getProductQuantityInGivenWarehouse() {
        return product
            .getQuantityInWarehouse(mapToInt(warehouseId.getValue()))
            .orElse(0);
    }

    private Integer mapToInt(String amount) {
        return Integer.parseInt(amount);
    }

    private void executeInBackground(Runnable task) {
        backgroundTaskExecutor.execute(task);
    }

    private void performDecreaseQuantityOfProductCall() {
        try {
            productService.decreaseQuantityOfProduct(product.getId(), mapToInt(amount.getValue()), mapToInt(warehouseId.getValue()));
            decreaseQuantitySuccess.postValue(true);
        } catch (Exception e) {
            decreaseQuantityErrorMessage.postValue(e.getMessage());
        }
    }
}
